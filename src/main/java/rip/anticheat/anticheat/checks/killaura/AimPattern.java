/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;

public class AimPattern
extends Check {
    private Map<UUID, Long> lastAttack = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastAngle;

    public AimPattern(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "AimPattern", "Aim (Pattern)", 110, 50, 3, 0);
    }

    @EventHandler
    public void onFight(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        double d = 0.0;
        Location location = player2.getLocation().add(0.0, player2.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = AimPattern.getRotation(location2, location);
        double d2 = AimPattern.clamp180(vector.getX() - vector2.getX());
        double d3 = AimPattern.clamp180(vector.getY() - vector2.getY());
        double d4 = AimPattern.getHorizontalDistance(location2, location);
        double d5 = AimPattern.getDistance3D(location2, location);
        double d6 = d2 * d4 * d5;
        boolean bl = false;
        double d7 = d3 * Math.abs(location.getY() - location2.getY()) * d5;
        d += Math.abs(d6);
        if ((d += Math.abs(d7)) == 180.0) {
            bl = true;
            if (!this.lastAttack.containsKey(player.getUniqueId())) {
                this.lastAttack.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
        if (d == 180.0 && this.lastAttack.containsKey(player.getUniqueId())) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Even Hits: " + Common.FORMAT_0x00.format(d)));
        }
    }

    @EventHandler
    public void evenAngle(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        double d = 0.0;
        Location location = player2.getLocation().add(0.0, player2.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = AimPattern.getRotation(location2, location);
        double d2 = AimPattern.clamp180(vector.getX() - vector2.getX());
        double d3 = AimPattern.clamp180(vector.getY() - vector2.getY());
        double d4 = AimPattern.getHorizontalDistance(location2, location);
        double d5 = AimPattern.getDistance3D(location2, location);
        double d6 = d2 * d4 * d5;
        double d7 = d3 * Math.abs(location.getY() - location2.getY()) * d5;
        d += Math.abs(d6);
        d += Math.abs(d7);
        double d8 = 0.0;
        double d9 = 0.0;
        int n = 0;
        if (!this.lastAngle.containsKey(player.getUniqueId())) {
            this.lastAngle.put(player.getUniqueId(), System.currentTimeMillis());
            d8 = d;
        }
        if (this.lastAngle.containsKey(player.getUniqueId())) {
            d9 = d;
            this.lastAngle.remove(player.getUniqueId());
        }
        if (d8 == d9) {
            ++n;
        }
        if (n == 2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "AimPattern: " + Common.FORMAT_0x00.format(d)));
        }
    }

    public static Vector getRotation(Location location, Location location2) {
        double d = location2.getX() - location.getX();
        double d2 = location2.getY() - location.getY();
        double d3 = location2.getZ() - location.getZ();
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(Math.atan2(d3, d) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)(- Math.atan2(d2, d4) * 180.0 / 3.141592653589793);
        return new Vector(f, f2, 0.0f);
    }

    public static double clamp180(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }

    public static double getHorizontalDistance(Location location, Location location2) {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d4 = Math.sqrt(d2 + d3);
        d = Math.abs(d4);
        return d;
    }

    public static double getDistance3D(Location location, Location location2) {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d4 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d5 = Math.sqrt(d2 + d3 + d4);
        d = Math.abs(d5);
        return d;
    }
}


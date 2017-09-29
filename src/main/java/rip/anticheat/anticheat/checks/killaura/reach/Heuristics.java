/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura.reach;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class Heuristics
extends Check {
    private Map<UUID, Double> velocityH = new WeakHashMap<UUID, Double>();
    private Map<UUID, Double> velocityV = new WeakHashMap<UUID, Double>();
    private Map<UUID, Float> threshold = new WeakHashMap<UUID, Float>();

    public Heuristics(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "Heuristics", "Heuristics (Analyze)", 110, 50, 3, 0);
    }

    @EventHandler
    public void onRegister(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        float f = ServerUtil.getPing(player2);
        float f2 = ServerUtil.getPing(player);
        float f3 = f + f2 / 2.0f;
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        PlayerStats playerStats2 = this.getCore().getPlayerStats(player2);
        if (!player.isOnGround()) {
            return;
        }
        if (!player2.isOnGround()) {
            return;
        }
        if (!playerStats.isOnGround()) {
            return;
        }
        float f4 = f3 * 0.0017f;
        double d = player2.getLocation().getX() - player.getLocation().getX();
        double d2 = player2.getLocation().getZ() - player.getLocation().getZ();
        double d3 = Math.sqrt(d * d + d2 * d2 - player.getVelocity().lengthSquared() * 2.5 - player2.getVelocity().lengthSquared() * 2.5);
        float f5 = 4.3f;
        double d4 = d / 8000.0;
        double d5 = d2 / 8000.0;
        double d6 = (int)(((d4 + d5) / 2.0 + 2.0) * 15.0);
        this.velocityH.put(player2.getUniqueId(), d6);
        f5 += f4;
        if ((this.velocityH.containsKey(player.getUniqueId()) || this.velocityH.containsKey(player2.getUniqueId())) && (this.velocityH.get(player2.getUniqueId()) > 0.0 || this.velocityH.get(player.getUniqueId()) > 0.0)) {
            f5 = (float)((double)f5 + 0.3);
        }
        if (d3 >= (double)f5) {
            this.threshold.put(player.getUniqueId(), Float.valueOf(this.threshold.getOrDefault(player.getUniqueId(), Float.valueOf(0.0f)).floatValue() + 0.1f));
        } else if (this.threshold.containsKey(player.getUniqueId())) {
            float f6 = this.threshold.get(player.getUniqueId()).floatValue();
            this.threshold.put(player.getUniqueId(), Float.valueOf(f6 *= 0.88f));
        }
        if (this.threshold.containsKey(player.getUniqueId()) && d3 >= (double)f5) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "First Hit: " + Common.FORMAT_0x00.format(d3)));
            this.threshold.remove(player.getUniqueId());
        }
    }
}


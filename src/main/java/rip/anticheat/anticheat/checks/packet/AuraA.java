/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package rip.anticheat.anticheat.checks.packet;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;

public class AuraA
extends Check {
    Map<UUID, Integer> hits = new HashMap<UUID, Integer>();

    public AuraA(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "AuraA", "KillAura [0x01]", 110, 50, 3, 0);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!((Player)entityDamageByEntityEvent.getDamager()).hasLineOfSight(entityDamageByEntityEvent.getEntity()) && !this.isPlayerInCorner((Player)entityDamageByEntityEvent.getDamager())) {
            int n = 0;
            Player player = (Player)entityDamageByEntityEvent.getDamager();
            this.hits.putIfAbsent(entityDamageByEntityEvent.getDamager().getUniqueId(), 1);
            if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) >= 5) {
                n = 1;
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(n)));
            }
            if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) >= 10) {
                n = 2;
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(n)));
            }
            if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) > 19) {
                n = 3;
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, Common.FORMAT_0x00.format(n)));
                this.hits.remove(entityDamageByEntityEvent.getDamager().getUniqueId());
                n = 0;
            }
        }
    }

    public boolean isPlayerInCorner(Player player) {
        int n;
        float f = player.getLocation().getYaw();
        if (f < 0.0f) {
            f += 360.0f;
        }
        if ((n = (int)((double)((f %= 360.0f) + 8.0f) / 22.5)) != 0 && n != 4 && n != 8 && n != 12) {
            return true;
        }
        return false;
    }
}


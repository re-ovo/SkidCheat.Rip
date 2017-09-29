/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package rip.anticheat.anticheat.checks.killaura.simple;

import java.text.DecimalFormat;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;

public class HitMissRatio
extends Check {
    private Map<Player, Integer> swings;
    private Map<Player, Integer> hits;

    public HitMissRatio(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "HitMissRatio", "KillAura (HitMissRatio)", 15, 4, 3, 0);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void onUseEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        this.hits.put(player, this.hits.get((Object)player) + 1);
        if (this.hits.containsKey((Object)player) && this.swings.containsKey((Object)player) && this.hits.get((Object)player) > 20 && this.swings.get((Object)player) > 20) {
            double d;
            try {
                d = Math.min(this.hits.get((Object)player), this.swings.get((Object)player)) / Math.max(this.hits.get((Object)player), this.swings.get((Object)player));
            }
            catch (Exception exception) {
                return;
            }
            if ((d *= 100.0) > 80.0) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(d)));
            }
        }
    }

    public Map<Player, Integer> getSwings() {
        return this.swings;
    }

    public Map<Player, Integer> getHits() {
        return this.hits;
    }
}


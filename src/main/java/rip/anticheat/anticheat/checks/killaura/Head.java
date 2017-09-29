/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package rip.anticheat.anticheat.checks.killaura;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import rip.anticheat.anticheat.ActiveDistance;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.learn.LearningProccess;

public class Head
extends Check {
    public Head(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "Heurastics", "Heurastic [Analyze]", 110, 7, 3, 0);
    }

    @EventHandler
    public void onHeadHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager() instanceof Player) {
            Player player = (Player)entityDamageByEntityEvent.getDamager();
            if (!player.hasLineOfSight(entityDamageByEntityEvent.getEntity())) {
                Player[] arrplayer = Bukkit.getServer().getOnlinePlayers();
                int n = arrplayer.length;
                int n2 = 0;
                while (n2 < n) {
                    Player player2 = arrplayer[n2];
                    ++n2;
                }
                LearningProccess.cheatingpre.put(player.getName(), 10.0);
                LearningProccess.startLearning(this, player);
                entityDamageByEntityEvent.setCancelled(true);
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Invalid HeadSpean " + player.getEyeHeight()));
            }
            if (!player.isDead()) {
                this.runCheck(player, entityDamageByEntityEvent.getEntity());
            }
        }
    }

    public void runCheck(Player player, Entity entity) {
        ActiveDistance activeDistance = new ActiveDistance(player.getLocation(), entity.getLocation());
        double d = activeDistance.getxDiff();
        double d2 = activeDistance.getzDiff();
        Player player2 = player;
        if (d == 0.0 || d2 == 0.0) {
            return;
        }
        if (activeDistance.getyDiff() >= 0.6) {
            return;
        }
        Location location = null;
        if (d <= 0.5 && d2 >= 1.0) {
            location = player2.getLocation().getZ() > entity.getLocation().getZ() ? player2.getLocation().add(0.0, 0.0, -1.0) : player2.getLocation().add(0.0, 0.0, 1.0);
        } else if (d2 <= 0.5 && d >= 1.0) {
            location = player2.getLocation().getX() > entity.getLocation().getX() ? player2.getLocation().add(-1.0, 0.0, 0.0) : player2.getLocation().add(1.0, 0.0, 0.0);
        }
        if (location != null && location.getBlock().getType().isSolid()) {
            location.add(0.0, 1.0, 0.0).getBlock().getType().isSolid();
        }
    }
}


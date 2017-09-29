/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.checks.killaura.reach;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.CheatUtil;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class HeuristicFlows
extends Check {
    private double allowedDistance = 3.9;
    private int hitCount = 0;

    public HeuristicFlows(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "HeuristicFlows", "Heuristic (Flows)", 110, 50, 3, 0);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        int n;
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        ++this.hitCount;
        Bukkit.getScheduler().runTaskLater((Plugin)AntiCheat.Instance, () -> {
            int n = this.hitCount = 0;
        }
        , 300);
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        double d = CheatUtil.getHorizontalDistance(player.getLocation(), player2.getLocation());
        double d2 = this.allowedDistance;
        int n2 = ServerUtil.getPing(player);
        int n3 = ServerUtil.getPing(player2);
        int n4 = n2 + n3 / 2;
        int n5 = (int)((double)n4 * 0.0017);
        d2 += (double)n5;
        if (!player2.isSprinting()) {
            d2 += 0.2;
        }
        if (!player2.isOnGround()) {
            return;
        }
        for (PotionEffect potionEffect : player2.getActivePotionEffects()) {
            if (potionEffect.getType().getId() != PotionEffectType.SPEED.getId()) continue;
            n = potionEffect.getAmplifier() + 1;
            d2 += 0.15 * (double)n;
            break;
        }
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getId() != PotionEffectType.SPEED.getId()) continue;
            n = potionEffect.getAmplifier() + 1;
            d2 += 0.15 * (double)n;
            break;
        }
        if (d > d2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(player.getName()) + "'s distance (" + d + ") is greater than " + d2));
        }
    }
}


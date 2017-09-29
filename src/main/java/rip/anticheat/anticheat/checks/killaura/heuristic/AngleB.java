/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import java.text.DecimalFormat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.AngleUtil;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class AngleB
extends Check {
    public AngleB(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "AngleB", "KillAura (Type Angle B)", 19, 2, 1, 0);
    }

    @EventHandler
    public void onAngleHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        double d = ServerUtil.getPing(player);
        double d2 = ServerUtil.getPing(player2);
        double d3 = AngleUtil.getOffsets(player, (LivingEntity)player2)[0];
        if (d2 > 450.0) {
            return;
        }
        if (d >= 100.0 && d < 200.0) {
            d3 -= 50.0;
        } else if (d >= 200.0 && d < 250.0) {
            d3 -= 75.0;
        } else if (d >= 250.0 && d < 300.0) {
            d3 -= 150.0;
        } else if (d >= 300.0 && d < 350.0) {
            d3 -= 300.0;
        } else if (d >= 350.0 && d < 400.0) {
            d3 -= 350.0;
        } else if (d > 400.0) {
            return;
        }
        if (d3 >= 300.0) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "AngleHit B " + Common.FORMAT_0x00.format(new StringBuilder(String.valueOf(d3)).append(" > ").append("400").toString())));
        }
    }
}


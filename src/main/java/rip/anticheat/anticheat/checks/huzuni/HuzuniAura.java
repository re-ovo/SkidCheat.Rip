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
package rip.anticheat.anticheat.checks.huzuni;

import java.text.DecimalFormat;
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

public class HuzuniAura
extends Check {
    private float lastYaw;
    private float lastBad;

    public HuzuniAura(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "Huzuni", "Huzuni [V5X01]", 110, 7, 3, 0);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        float f;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        this.lastYaw = f = player.getLocation().getYaw();
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
    }

    public boolean onAim(Player player, float f) {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        this.lastBad = (float)Math.round(f2 * 10.0f) * 0.1f;
        if ((double)f < 0.1) {
            return true;
        }
        if (f2 > 1.0f && (float)Math.round(f2 * 10.0f) * 0.1f == f2 && (float)Math.round(f2) != f2) {
            if (f2 == this.lastBad) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Aim: " + Common.FORMAT_0x00.format(f)));
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}


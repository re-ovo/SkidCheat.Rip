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
package rip.anticheat.anticheat.checks.killaura;

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

public class YawRate
extends Check {
    private float lastYaw;
    private float lastBad;
    private float lastYawRate;
    private float lastDiff;
    private float lastYaw2;
    private float lastPitch;
    private int streak;
    private int min;

    public YawRate(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "YawRate", "AimAssist [YAW] [LITE / SPOOK]", 110, 7, 3, 0);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        float f = player.getLocation().getYaw();
        float f2 = player.getLocation().getPitch();
        this.onAim(player, f);
        this.onAim3(player, f);
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

    public int onAim2(Player player, float f, float f2) {
        float f3 = f - this.lastYaw2;
        float f4 = f2 - this.lastPitch;
        if (Math.abs(f4) >= 2.0f && f3 == 0.0f) {
            ++this.streak;
        } else {
            return 0;
        }
        this.lastYaw2 = f;
        this.lastPitch = f2;
        if (this.streak >= this.min) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Aim1: " + Common.FORMAT_0x00.format(this.streak)));
            return this.streak;
        }
        return 0;
    }

    public float onAim3(Player player, float f) {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        if (f2 > 0.1f && (float)Math.round(f2) == f2) {
            if (f2 == this.lastBad) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Aim2: " + Common.FORMAT_0x00.format(f)));
                return f2;
            }
            this.lastBad = Math.round(f2);
        } else {
            this.lastBad = 0.0f;
        }
        return 0.0f;
    }
}


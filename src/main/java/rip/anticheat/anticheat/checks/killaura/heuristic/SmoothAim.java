/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import java.text.DecimalFormat;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;

public class SmoothAim
extends Check {
    private int smoothAim = 0;

    public SmoothAim(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "SmoothAim", "Smooth Aim (expirimental)", 110, 50, 3, 0);
    }

    public static double getFrac(double d) {
        return d % 1.0;
    }

    public void setSmoothAim(int n) {
        this.smoothAim = n;
        if (this.smoothAim < 0) {
            this.smoothAim = 0;
        }
    }

    public int getSmoothAim() {
        return this.smoothAim;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Location location = playerMoveEvent.getFrom().clone();
        Location location2 = playerMoveEvent.getTo().clone();
        Player player = playerMoveEvent.getPlayer();
        double d = Math.abs(location.getYaw() - location2.getYaw());
        if (d > 0.0 && d < 360.0) {
            if (SmoothAim.getFrac(d) == 0.0) {
                this.setSmoothAim(this.getSmoothAim() + 100);
                if (this.getSmoothAim() > 2000) {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(d)));
                    this.setSmoothAim(0);
                }
            } else {
                this.setSmoothAim(this.getSmoothAim() - 21);
            }
        }
    }
}


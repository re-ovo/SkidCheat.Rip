/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.movement.jesus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.MathUtil;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class JesusA
extends Check {
    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();

    public JesusA(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "JesusA", "Jesus (Type A)", 5, 50, 2, 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (player.getAllowFlight()) {
            return;
        }
        if (player.isInsideVehicle()) {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        double d = Math.abs(playerMoveEvent.getTo().getX() - playerMoveEvent.getFrom().getX()) + Math.abs(playerMoveEvent.getTo().getZ() - playerMoveEvent.getFrom().getZ());
        double d2 = Math.abs(playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY());
        double d3 = 0.0;
        if (d == 0.12 || d == 0.9) {
            return;
        }
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            d3 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d2);
        if (PlayerUtil.isHoveringOverWater(player, 0) || PlayerUtil.isHoveringOverWater(player, 1) && !PlayerUtil.isOnGround(player, -2)) {
            if (!(PlayerUtil.isOnGround(player, 0) || PlayerUtil.isHoveringOverWater(player, -1) || d2 == 0.0 || d2 > 0.15 || d3 > 0.15 || d < 0.01 || MathUtil.getFraction(playerMoveEvent.getTo().getY()) <= 0.75 && MathUtil.getFraction(playerMoveEvent.getTo().getY()) != 0.0)) {
                ++n;
                if (d > 1.0) {
                    n += 2;
                    if (d > 2.0) {
                        n += 2;
                    }
                }
            } else {
                n -= 2;
            }
            if (n > n2) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "+" + Common.FORMAT_0x00.format(d)));
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            this.lastOffsetY.remove(player.getUniqueId());
        }
    }
}


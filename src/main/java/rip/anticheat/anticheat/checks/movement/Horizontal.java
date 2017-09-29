/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.movement;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
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
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class Horizontal
extends Check {
    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();

    public Horizontal(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "Horizontal", "Horizontal", 6, 50, 2, 0);
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
        if (!player.getNearbyEntities(1.0, 1.0, 1.0).isEmpty()) {
            return;
        }
        if (playerStats.getLastMountDiff() < 500) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.SNOW}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.SNOW})) {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        double d = Math.abs(playerMoveEvent.getTo().getX() - playerMoveEvent.getFrom().getX()) + Math.abs(playerMoveEvent.getTo().getZ() - playerMoveEvent.getFrom().getZ());
        double d2 = Math.abs(playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY());
        double d3 = 0.0;
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            d3 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d2);
        if (!(d2 >= 0.03 || d3 >= 0.03 || d <= 0.01 || playerStats.isOnGround() || PlayerUtil.isOnBlock(player, 0, new Material[]{Material.CARPET}) || PlayerUtil.isHoveringOverWater(player, 0))) {
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
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(Common.FORMAT_0x00.format(d)) + " " + d2));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            this.lastOffsetY.remove(player.getUniqueId());
        }
    }
}


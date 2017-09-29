/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.movement;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

public class Glide
extends Check {
    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();

    public Glide(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "Glide", "Glide", 6, 50, 2, 0);
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
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.WEB}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WEB})) {
            return;
        }
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals((Object)Material.WEB)) {
            return;
        }
        if (PlayerUtil.isHoveringOverWater(player, 1) || PlayerUtil.isHoveringOverWater(player, 0)) {
            return;
        }
        if (playerStats.getVelocityY() > 0.0) {
            return;
        }
        if (playerStats.getLastWorldChangeDiff() < 3000) {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        double d = playerMoveEvent.getFrom().getY() - playerMoveEvent.getTo().getY();
        double d2 = 0.0;
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            d2 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d);
        n = d > 0.0 && d < 0.17 && d2 > 0.0 && d2 < 0.17 && !playerStats.isOnGround() ? ++n : (n -= 2);
        if (n > n2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(d)));
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


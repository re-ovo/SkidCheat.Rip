/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.checks.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class Change
extends Check {
    private List<UUID> built = new ArrayList<UUID>();
    private List<UUID> falling = new ArrayList<UUID>();

    public Change(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "Change", "Change", 6, 50, 2, 0);
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
        if (this.built.contains(player.getUniqueId())) {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        if (!(playerStats.isOnGround() || PlayerUtil.isOnBlock(player, 0, new Material[]{Material.CARPET}) || PlayerUtil.isHoveringOverWater(player, 0) || player.getLocation().getBlock().getType() != Material.AIR)) {
            if (playerMoveEvent.getFrom().getY() > playerMoveEvent.getTo().getY()) {
                if (!this.falling.contains(player.getUniqueId())) {
                    this.falling.add(player.getUniqueId());
                }
            } else {
                n = playerMoveEvent.getTo().getY() > playerMoveEvent.getFrom().getY() ? (this.falling.contains(player.getUniqueId()) ? ++n : --n) : --n;
            }
        } else {
            this.falling.remove(player.getUniqueId());
        }
        if (playerStats.getVelocityY() > 0.0) {
            this.falling.remove(player.getUniqueId());
            n = 0;
        }
        if (n > n2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Went upwards unexpectedly"));
            n = 0;
            this.falling.remove(player.getUniqueId());
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.falling.contains(player.getUniqueId())) {
            this.falling.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onAttack(BlockPlaceEvent blockPlaceEvent) {
        if (blockPlaceEvent.getPlayer() instanceof Player) {
            Player player = blockPlaceEvent.getPlayer();
            this.built.add(player.getUniqueId());
            Bukkit.getScheduler().runTaskLater((Plugin)AntiCheat.Instance, () -> {
                boolean bl = this.built.remove(player.getUniqueId());
            }
            , 60);
        }
    }
}


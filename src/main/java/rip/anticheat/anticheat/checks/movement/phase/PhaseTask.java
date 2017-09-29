/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.material.Directional
 *  org.bukkit.material.Door
 *  org.bukkit.material.Gate
 *  org.bukkit.material.MaterialData
 *  org.bukkit.material.TrapDoor
 *  org.bukkit.scheduler.BukkitRunnable
 */
package rip.anticheat.anticheat.checks.movement.phase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TrapDoor;
import org.bukkit.scheduler.BukkitRunnable;
import rip.anticheat.anticheat.checks.movement.phase.PhaseA;
import rip.anticheat.anticheat.checks.movement.phase.PhaseUtil;

public class PhaseTask
extends BukkitRunnable {
    private PhaseUtil phaseUtil = new PhaseUtil();
    private PhaseA phase;
    private final Map<UUID, Location> validLocations = new HashMap<UUID, Location>();
    private Set<UUID> teleported = new HashSet<UUID>();

    public void addTeleportedPlayer(UUID uUID) {
        this.teleported.add(uUID);
    }

    public PhaseTask(PhaseA phaseA) {
        this.phase = phaseA;
        this.phaseUtil.load(phaseA.config);
    }

    public void run() {
        Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player player = arrplayer[n2];
            UUID uUID = player.getUniqueId();
            Location location = this.validLocations.containsKey(uUID) ? this.validLocations.get(uUID) : player.getLocation();
            Location location2 = player.getLocation();
            if (location.getWorld() == location2.getWorld() && !this.teleported.contains(uUID) && location.distance(location2) > 10.0) {
                if (!(player.getAllowFlight() || this.phase.horse != null && this.phase.horse.containsKey(player.getUniqueId().toString()) && this.phase.horse.get(player.getUniqueId().toString()) < System.currentTimeMillis())) {
                    player.teleport(this.validLocations.get(uUID), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
            } else if (this.isValidMove(uUID, location, location2)) {
                this.validLocations.put(uUID, location2);
            } else if (!(!this.validLocations.containsKey(uUID) || player.getAllowFlight() || this.phase.horse != null && this.phase.horse.containsKey(player.getUniqueId().toString()) && this.phase.horse.get(player.getUniqueId().toString()) < System.currentTimeMillis())) {
                player.teleport(this.validLocations.get(uUID), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            }
            ++n2;
        }
    }

    private boolean isValidMove(UUID uUID, Location location, Location location2) {
        if (location.getWorld() != location2.getWorld()) {
            return true;
        }
        if (this.teleported.remove(uUID)) {
            return true;
        }
        int n = Math.max(location.getBlockX(), location2.getBlockX());
        int n2 = Math.min(location.getBlockX(), location2.getBlockX());
        int n3 = Math.max(location.getBlockY(), location2.getBlockY()) + 1;
        int n4 = Math.min(location.getBlockY(), location2.getBlockY());
        int n5 = Math.max(location.getBlockZ(), location2.getBlockZ());
        int n6 = Math.min(location.getBlockZ(), location2.getBlockZ());
        if (n3 > 256) {
            n = 256;
        }
        if (n4 > 256) {
            n4 = 256;
        }
        int n7 = n2;
        while (n7 <= n) {
            int n8 = n6;
            while (n8 <= n5) {
                int n9 = n4;
                while (n9 <= n3) {
                    Block block = location.getWorld().getBlockAt(n7, n9, n8);
                    if ((n9 != n4 || location.getBlockY() == location2.getBlockY()) && this.hasPhased(block, location, location2)) {
                        return false;
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        return true;
    }

    private boolean hasPhased(Block block, Location location, Location location2) {
        Door door;
        boolean bl;
        if (this.phaseUtil.getPassable().contains((Object)block.getType())) {
            return false;
        }
        double d = Math.max(location.getX(), location2.getX());
        double d2 = Math.min(location.getX(), location2.getX());
        double d3 = Math.max(location.getY(), location2.getY()) + 1.8;
        double d4 = Math.min(location.getY(), location2.getY());
        double d5 = Math.max(location.getZ(), location2.getZ());
        double d6 = Math.min(location.getZ(), location2.getZ());
        double d7 = block.getLocation().getBlockX() + 1;
        double d8 = block.getLocation().getBlockX();
        double d9 = block.getLocation().getBlockY() + 2;
        double d10 = block.getLocation().getBlockY();
        double d11 = block.getLocation().getBlockZ() + 1;
        double d12 = block.getLocation().getBlockZ();
        if (d10 > d4) {
            d9 -= 1.0;
        }
        if (this.phaseUtil.getDoors().contains((Object)block.getType())) {
            door = (Door)block.getType().getNewData(block.getData());
            if (door.isTopHalf()) {
                return false;
            }
            BlockFace blockFace = door.getFacing();
            if (door.isOpen()) {
                boolean bl2;
                Block block2 = block.getRelative(BlockFace.UP);
                if (!this.phaseUtil.getDoors().contains((Object)block2.getType())) {
                    return false;
                }
                boolean bl3 = bl2 = (block2.getData() & 1) == 1;
                if (blockFace == BlockFace.NORTH) {
                    blockFace = bl2 ? BlockFace.WEST : BlockFace.EAST;
                } else if (blockFace == BlockFace.EAST) {
                    blockFace = bl2 ? BlockFace.NORTH : BlockFace.SOUTH;
                } else if (blockFace == BlockFace.SOUTH) {
                    blockFace = bl2 ? BlockFace.EAST : BlockFace.WEST;
                } else {
                    BlockFace blockFace2 = blockFace = bl2 ? BlockFace.SOUTH : BlockFace.NORTH;
                }
            }
            if (blockFace == BlockFace.WEST) {
                d7 -= 0.8;
            }
            if (blockFace == BlockFace.EAST) {
                d8 += 0.8;
            }
            if (blockFace == BlockFace.NORTH) {
                d11 -= 0.8;
            }
            if (blockFace == BlockFace.SOUTH) {
                d12 += 0.8;
            }
        } else if (this.phaseUtil.getGates().contains((Object)block.getType())) {
            if (((Gate)block.getType().getNewData(block.getData())).isOpen()) {
                return false;
            }
            door = ((Directional)block.getType().getNewData(block.getData())).getFacing();
            if (door == BlockFace.NORTH || door == BlockFace.SOUTH) {
                d7 -= 0.2;
                d8 += 0.2;
            } else {
                d11 -= 0.2;
                d12 += 0.2;
            }
        } else if (this.phaseUtil.getTrapdoors().contains((Object)block.getType())) {
            door = (TrapDoor)block.getType().getNewData(block.getData());
            if (door.isOpen()) {
                return false;
            }
            if (door.isInverted()) {
                d10 += 0.85;
            } else {
                d9 -= d10 > d4 ? 0.85 : 1.85;
            }
        } else if (this.phaseUtil.getFences().contains((Object)block.getType())) {
            d11 -= 0.2;
            if (d > (d7 -= 0.2) && d2 > d7 && d5 > d11 && d6 > d11 || d < d8 && d2 < d8 && d5 > d11 && d6 > d11 || d > d7 && d2 > d7 && d5 < d12 && d6 < d12 || d < d8 && d2 < (d8 += 0.2) && d5 < d12 && d6 < (d12 += 0.2)) {
                return false;
            }
            if (block.getRelative(BlockFace.EAST).getType() == block.getType()) {
                d7 += 0.2;
            }
            if (block.getRelative(BlockFace.WEST).getType() == block.getType()) {
                d8 -= 0.2;
            }
            if (block.getRelative(BlockFace.SOUTH).getType() == block.getType()) {
                d11 += 0.2;
            }
            if (block.getRelative(BlockFace.NORTH).getType() == block.getType()) {
                d12 -= 0.2;
            }
        }
        boolean bl4 = location.getX() < location2.getX();
        boolean bl5 = location.getY() < location2.getY();
        boolean bl6 = bl = location.getZ() < location2.getZ();
        if (!(d2 != d && d4 <= d9 && d3 >= d10 && d6 <= d11 && d5 >= d12 && (bl4 && d2 <= d8 && d >= d8 || !bl4 && d2 <= d7 && d >= d7) || d4 != d3 && d2 <= d7 && d >= d8 && d6 <= d11 && d5 >= d12 && (bl5 && d4 <= d10 && d3 >= d10 || !bl5 && d4 <= d9 && d3 >= d9) || d6 != d5 && d2 <= d7 && d >= d8 && d4 <= d9 && d3 >= d10 && (bl && d6 <= d12 && d5 >= d12 || !bl && d6 <= d11 && d5 >= d11))) {
            return false;
        }
        return true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.validLocations.containsKey(player.getUniqueId())) {
            this.validLocations.remove(player.getUniqueId());
        }
        if (this.teleported.contains(player.getUniqueId())) {
            this.teleported.remove(player.getUniqueId());
        }
    }
}


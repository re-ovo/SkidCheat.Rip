/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.event.vehicle.VehicleExitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.checks.movement.phase;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.checks.movement.phase.PhaseTask;

public class PhaseA
extends Check {
    public HashMap<String, Long> horse = new HashMap();
    private PhaseTask task;
    public YamlConfiguration config;

    public PhaseA(AntiCheat antiCheat, YamlConfiguration yamlConfiguration) {
        super(antiCheat, CheckType.MOVEMENT, "PhaseA", "Phase (Type A)", 110, 50, -1, 0);
        this.config = yamlConfiguration;
        this.task = new PhaseTask(this);
        this.task.runTaskTimer((Plugin)antiCheat, 0, 1);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void setTeleported(PlayerTeleportEvent playerTeleportEvent) {
        if (playerTeleportEvent.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            this.task.addTeleportedPlayer(playerTeleportEvent.getPlayer().getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void setTeleported(PlayerRespawnEvent playerRespawnEvent) {
        this.task.addTeleportedPlayer(playerRespawnEvent.getPlayer().getUniqueId());
    }

    @EventHandler
    public void vehExit(VehicleExitEvent vehicleExitEvent) {
        if (vehicleExitEvent.getExited() instanceof Player && vehicleExitEvent.getVehicle().getType().equals((Object)EntityType.HORSE)) {
            Player player = (Player)vehicleExitEvent.getExited();
            this.horse.put(player.getUniqueId().toString(), System.currentTimeMillis() + 600);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.horse.containsKey(player.getUniqueId().toString())) {
            this.horse.remove(player.getUniqueId().toString());
        }
    }
}


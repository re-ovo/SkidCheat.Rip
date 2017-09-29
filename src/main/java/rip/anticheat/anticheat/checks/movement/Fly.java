/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.checks.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.CheatUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class Fly
extends Check {
    private int verticalDownThreshold = 6;
    private int verticalUpThreshold = 3;
    private Map<UUID, Double> lastOffsetYUp = new HashMap<UUID, Double>();
    private Map<UUID, Double> lastOffsetYDown = new HashMap<UUID, Double>();
    ArrayList<Player> attacked = new ArrayList();
    private Map<UUID, Long> flyTicks = new HashMap<UUID, Long>();

    public Fly(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "Fly", "Fly", 3, 50, 2, 0);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new NetworkFlightPacketAdapter());
    }

    @EventHandler
    public void CheckFly(PlayerMoveEvent playerMoveEvent) {
        long l;
        Player player = playerMoveEvent.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (CheatUtil.isInWater(player)) {
            return;
        }
        if (CheatUtil.isInWeb(player)) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) {
            return;
        }
        if (CheatUtil.blocksNear(player)) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        if (playerMoveEvent.getTo().getX() == playerMoveEvent.getFrom().getX() && playerMoveEvent.getTo().getZ() == playerMoveEvent.getFrom().getZ()) {
            return;
        }
        if (playerMoveEvent.getTo().getY() != playerMoveEvent.getFrom().getY()) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        long l2 = System.currentTimeMillis();
        if (this.flyTicks.containsKey(player.getUniqueId())) {
            l2 = this.flyTicks.get(player.getUniqueId());
        }
        if ((l = System.currentTimeMillis() - l2) > 500) {
            this.flyTicks.remove(player.getUniqueId());
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(l)));
            playerMoveEvent.setCancelled(true);
            player.teleport(playerMoveEvent.getFrom());
            return;
        }
        this.flyTicks.put(player.getUniqueId(), l2);
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".vertical-down-threshold", (Object)this.verticalDownThreshold);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".vertical-up-threshold", (Object)this.verticalUpThreshold);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".vertical-down-threshold")) {
            this.verticalDownThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".vertical-down-threshold");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".vertical-up-threshold")) {
            this.verticalUpThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".vertical-up-threshold");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastOffsetYDown.containsKey(player.getUniqueId())) {
            this.lastOffsetYDown.remove(player.getUniqueId());
        }
        if (this.lastOffsetYUp.containsKey(player.getUniqueId())) {
            this.lastOffsetYUp.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onDmg(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getEntity() instanceof Player) {
            final Player player = (Player)entityDamageEvent.getEntity();
            this.attacked.add(player);
            Bukkit.getScheduler().runTaskLater((Plugin)AntiCheat.Instance, new Runnable(){

                @Override
                public void run() {
                    Fly.this.attacked.remove((Object)player);
                }
            }, 20);
        }
    }

    private class NetworkFlightPacketAdapter
    extends PacketAdapter {
        NetworkFlightPacketAdapter() {
            super((Plugin)Fly.this.getCore(), new PacketType[]{PacketType.Play.Client.ABILITIES});
        }

        public void onPacketReceiving(PacketEvent packetEvent) {
            Player player = packetEvent.getPlayer();
            if (!player.getAllowFlight()) {
                Fly.this.getCore().addViolation(player, Fly.this, new Violation(Fly.this, ViolationPriority.HIGHEST, "Illegal Fly Packets"));
            }
        }
    }

}


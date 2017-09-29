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
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.plugin.Plugin
 */
package rip.anticheat.anticheat.checks.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.CheatUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class FlyB
extends Check {
    public int verticalDownThreshold = 6;
    public int verticalUpThreshold = 3;
    private Map<Player, Double> lastOffsetYUp = new HashMap<Player, Double>();
    private Map<Player, Double> lastOffsetYDown = new HashMap<Player, Double>();

    public FlyB(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "FlyB", "Fly (B)", 3, 50, 2, 0);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new NetworkFlightPacketAdapter());
    }

    @EventHandler
    public void Move(PlayerMoveEvent playerMoveEvent) {
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
        if (PlayerUtil.isHoveringOverWater(player, 1) || PlayerUtil.isHoveringOverWater(player, 0)) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.WEB}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WEB})) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 1, new Material[]{Material.SOUL_SAND}) || PlayerUtil.isOnBlock(player, 2, new Material[]{Material.SOUL_SAND})) {
            return;
        }
        if (playerStats.getLastGround() == null || playerStats.isOnGround()) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) {
            return;
        }
        if (playerStats.getVelocityY() > 0.0) {
            return;
        }
        if (player.getLocation().getY() < 0.0) {
            return;
        }
        if (playerStats.getLastMountDiff() < 500) {
            return;
        }
        if (CheatUtil.isInWeb(player)) {
            return;
        }
        double d = playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY();
        if (d < 0.0 && playerStats.getLastWorldChangeDiff() > 3000) {
            int n = playerStats.getCheck(this, 0);
            int n2 = this.verticalDownThreshold;
            d = Math.abs(d);
            double d2 = 0.0;
            if (this.lastOffsetYDown.containsKey((Object)player)) {
                d2 = this.lastOffsetYDown.get((Object)player);
            }
            this.lastOffsetYDown.put(player, d);
            double d3 = Math.abs(d - d2);
            n = d3 < 0.02 ? ++n : (n -= 2);
            if (PlayerUtil.isOnGround(player, -1) || PlayerUtil.isOnGround(player, -2)) {
                --n;
            }
            if (n > n2) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "*"));
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
            return;
        }
        if (d > 0.0) {
            int n = playerStats.getCheck(this, 1);
            int n3 = this.verticalUpThreshold;
            double d4 = 0.0;
            if (this.lastOffsetYUp.containsKey((Object)player)) {
                d4 = this.lastOffsetYUp.get((Object)player);
            }
            this.lastOffsetYUp.put(player, d);
            double d5 = Math.abs(d - d4);
            n = d5 < 0.02 ? ++n : (n -= 2);
            if (PlayerUtil.isOnGround(player, -1) || PlayerUtil.isOnGround(player, -2) || PlayerUtil.isOnGround(player, -3)) {
                --n;
            }
            if (n > n3) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "*"));
                n = 0;
            }
            playerStats.setCheck(this, 1, n);
        }
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

    private class NetworkFlightPacketAdapter
    extends PacketAdapter {
        public NetworkFlightPacketAdapter() {
            super((Plugin)FlyB.this.getCore(), new PacketType[]{PacketType.Play.Client.ABILITIES});
        }

        public void onPacketReceiving(PacketEvent packetEvent) {
            Player player = packetEvent.getPlayer();
            if (!player.getAllowFlight()) {
                FlyB.this.getCore().addViolation(player, FlyB.this, new Violation(FlyB.this, ViolationPriority.HIGHEST, "Illegal Fly Packets"));
            }
        }
    }

}


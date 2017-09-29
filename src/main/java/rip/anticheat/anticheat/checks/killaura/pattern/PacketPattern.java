/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.Location
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.killaura.pattern;

import com.comphenix.protocol.wrappers.EnumWrappers;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;
import rip.anticheat.anticheat.learn.LearningProccess;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class PacketPattern
extends Check {
    private Map<UUID, Long> lastPacketSend = new HashMap<UUID, Long>();
    private double maxPacketRange = 3.4;

    public PacketPattern(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "PacketPattern", "KillAura (Packet-Pattern)", 110, 50, 3, 0);
    }

    public void onPacketSend(PacketUseEntityEvent packetUseEntityEvent) {
        if (packetUseEntityEvent.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(packetUseEntityEvent.getAttacker() instanceof Player)) {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player)) {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        Player player2 = (Player)packetUseEntityEvent.getAttacked();
        double d = player.getLocation().distance(player.getLocation());
        long l = 701;
        if (this.lastPacketSend.containsKey(player.getUniqueId())) {
            l = System.currentTimeMillis() - this.lastPacketSend.get(player.getUniqueId());
        }
        if (l > 700) {
            int n = ServerUtil.getPing(player);
            int n2 = 0;
            if (n > 400) {
                return;
            }
            if (n > 300) {
                this.maxPacketRange *= 1.6;
            } else if (n > 250) {
                this.maxPacketRange *= 2.0;
            } else if (n > 200) {
                this.maxPacketRange *= 1.4;
            }
            if (d >= this.maxPacketRange) {
                ++n2;
            } else if (d >= this.maxPacketRange * 1.2) {
                n2 += 4;
            } else if (d >= this.maxPacketRange * 1.4) {
                n2 += 5;
            }
            if (n2 == 3) {
                LearningProccess.cheatingpre.put(player.getName(), 10.0);
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Invalid Hit: " + Common.FORMAT_0x00.format(d)));
            } else if (n2 >= 4) {
                LearningProccess.cheatingpre.put(player.getName(), 10.0);
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Invalid Hit: " + Common.FORMAT_0x00.format(d)));
            } else if (n2 >= 5) {
                LearningProccess.cheatingpre.put(player.getName(), 10.0);
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, "Invalid Hit: " + Common.FORMAT_0x00.format(d)));
            }
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(string) + ".maxPacketRage", (Object)this.maxPacketRange);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(string) + ".maxPacketRange")) {
            this.maxPacketRange = config.getConfig().getDouble(String.valueOf(string) + ".maxPacketRange");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastPacketSend.containsKey(player.getUniqueId())) {
            this.lastPacketSend.remove(player.getUniqueId());
        }
    }
}


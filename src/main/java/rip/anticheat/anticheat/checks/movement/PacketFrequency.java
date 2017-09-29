/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.movement;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.ServerUtil;
import rip.anticheat.anticheat.util.misc.TimeUtil;

public class PacketFrequency
extends Check {
    private Map<UUID, Map.Entry<Long, Integer>> MS = new HashMap<UUID, Map.Entry<Long, Integer>>();
    private int maxPackets = 22;
    private long time = 1000;

    public PacketFrequency(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "PacketFrequency", "Packet Frequency", 30, 50, 2, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        if (this.MS.containsKey(uUID)) {
            this.MS.remove(uUID);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=1)
    public void Move(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (!this.isEnabled()) {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        int n3 = 0;
        long l = System.currentTimeMillis();
        if (this.MS.containsKey(player.getUniqueId())) {
            n3 = this.MS.get(player.getUniqueId()).getValue();
            l = this.MS.get(player.getUniqueId()).getKey();
        }
        ++n3;
        if (this.MS.containsKey(player.getUniqueId()) && TimeUtil.elapsed(l, this.time)) {
            int n4 = this.maxPackets;
            if (ServerUtil.getPing(player) > 400) {
                n4 *= 1;
            }
            if (n3 > n4) {
                n += 5;
                if ((double)n3 > (double)n4 * 1.3) {
                    n += 3;
                }
            } else {
                n -= 4;
            }
            if (n > n2) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "experimental"));
                n = 0;
            }
            n3 = 0;
            l = TimeUtil.nowlong();
        }
        playerStats.setCheck(this, 0, n);
        this.MS.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Integer>(l, n3));
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".maxpackets", (Object)this.maxPackets);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".time", (Object)this.time);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".maxpackets")) {
            this.maxPackets = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".maxpackets");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".time")) {
            this.time = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".time");
        }
    }
}


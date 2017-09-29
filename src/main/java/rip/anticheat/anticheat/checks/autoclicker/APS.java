/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package rip.anticheat.anticheat.checks.autoclicker;

import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;
import rip.anticheat.anticheat.learn.LearningProccess;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.TimeUtil;

public class APS
extends Check {
    private int maxCPS = 23;
    private Map<UUID, Map.Entry<Integer, Long>> clickTimes = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public APS(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "APS", "AutoClicker (B) [ATTACK]", 1, 50, 2, 0);
    }

    @EventHandler
    public void useEntity(PacketUseEntityEvent packetUseEntityEvent) {
        if (!this.isEnabled()) {
            return;
        }
        if (packetUseEntityEvent.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player)) {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        int n3 = 0;
        long l = System.currentTimeMillis();
        if (this.clickTimes.containsKey(player.getUniqueId())) {
            n3 = this.clickTimes.get(player.getUniqueId()).getKey();
            l = this.clickTimes.get(player.getUniqueId()).getValue();
        }
        ++n3;
        if (this.clickTimes.containsKey(player.getUniqueId()) && TimeUtil.elapsed(l, 1000)) {
            if (n3 > this.maxCPS) {
                ++n;
                if (n3 > this.maxCPS * 5) {
                    ++n;
                }
            } else {
                --n;
            }
            if (n > n2) {
                if (n3 > this.maxCPS * 6) {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, String.valueOf(n3) + " APS"));
                    LearningProccess.cheatingpre.put(player.getName(), 5.0);
                } else if (n3 > this.maxCPS * 3) {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(n3) + " APS"));
                    LearningProccess.cheatingpre.put(player.getName(), 10.0);
                } else {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(n3) + " APS"));
                    LearningProccess.cheatingpre.put(player.getName(), 10.0);
                }
                n = 0;
            }
            n3 = 0;
            l = TimeUtil.nowlong();
        }
        playerStats.setCheck(this, 0, n);
        this.clickTimes.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n3, l));
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".maxcps", (Object)this.maxCPS);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".maxcps")) {
            this.maxCPS = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".maxcps");
        }
    }
}


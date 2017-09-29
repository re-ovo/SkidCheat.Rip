/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.killaura.reach;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.AngleUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class ReachA
extends Check {
    private int firstHitThreshold = 50;
    private double maxRange = 5.1;
    private double velocity = 1.175;
    private Map<UUID, Long> lastAttack = new HashMap<UUID, Long>();

    public ReachA(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "Reach", "Reach (Type A)", 110, 50, 3, 0);
    }

    @EventHandler
    public void onDamage(PacketUseEntityEvent packetUseEntityEvent) {
        if (!this.isEnabled()) {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player)) {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        Player player2 = (Player)packetUseEntityEvent.getAttacked();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        PlayerStats playerStats2 = this.getCore().getPlayerStats(player2);
        double d = player.getLocation().distance(player2.getLocation());
        long l = 701;
        double d2 = this.maxRange;
        if (this.lastAttack.containsKey(player.getUniqueId())) {
            l = System.currentTimeMillis() - this.lastAttack.get(player.getUniqueId());
        }
        this.lastAttack.put(player.getUniqueId(), System.currentTimeMillis());
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (player.isFlying() || player2.isFlying()) {
            return;
        }
        if (d > 6.0) {
            return;
        }
        if (!player2.hasLineOfSight((Entity)player)) {
            return;
        }
        if (player.isSprinting()) {
            d2 += 0.2;
        }
        if (player2.isSprinting()) {
            d2 += 0.3;
        }
        double d3 = AngleUtil.getOffsets(player2, (LivingEntity)player)[0];
        if (l > 700) {
            int n = playerStats.getCheck(this, 0);
            int n2 = this.firstHitThreshold;
            int n3 = ServerUtil.getPing(player);
            if (n3 > 400) {
                return;
            }
            if (n3 > 300) {
                d2 *= 1.6;
            } else if (n3 > 250) {
                d2 *= 2.0;
            } else if (n3 > 200) {
                d2 *= 1.4;
            }
            if (d >= d2) {
                if (d >= d2 * 1.2) {
                    n += 5;
                    if (d >= d2 * 1.4) {
                        n += 10;
                    }
                }
                n += 15;
            } else {
                n -= 5;
            }
            if (n > n2) {
                if (d >= d2 * 1.4) {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "First Hit: " + Common.FORMAT_0x00.format(d)));
                } else {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "First Hit: " + Common.FORMAT_0x00.format(d)));
                }
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
        } else {
            int n;
            double d4 = this.maxRange;
            int n4 = playerStats.getCheck(this, 1);
            int n5 = this.getThreshold();
            if (playerStats2.getVelocityXZ() > 12.0) {
                d4 *= this.velocity;
            }
            if (playerStats2.getVelocityY() > 12.0) {
                d4 *= this.velocity;
            }
            if ((n = ServerUtil.getPing(player)) > 400) {
                d4 *= 1.3;
            } else if (n > 300) {
                d4 *= 1.2;
            } else if (n > 200) {
                d4 *= 1.05;
            }
            if (d >= d4) {
                if (d >= d4 * 1.5) {
                    n4 += 35;
                } else if (d >= d4 * 1.4) {
                    n4 += 30;
                } else if (d >= d4 * 1.3) {
                    n4 += 25;
                } else if (d >= d4 * 1.2) {
                    n4 += 15;
                } else if (d >= d4 * 1.1) {
                    n4 += 10;
                }
                n4 += 15;
            } else {
                n4 -= 8;
            }
            if (n4 > n5) {
                if (d >= d4 * 1.3) {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, Common.FORMAT_0x00.format(d)));
                } else {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(d)));
                }
                n4 = 0;
            }
            playerStats.setCheck(this, 1, n4);
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(string) + ".range", (Object)this.maxRange);
        config.getConfig().set(String.valueOf(string) + ".velocity", (Object)this.velocity);
        config.getConfig().set(String.valueOf(string) + ".firsthit-threshold", (Object)this.firstHitThreshold);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(string) + ".range")) {
            this.maxRange = config.getConfig().getDouble(String.valueOf(string) + ".range");
        }
        if (config.getConfig().contains(String.valueOf(string) + ".velocity")) {
            this.velocity = config.getConfig().getDouble(String.valueOf(string) + ".velocity");
        }
        if (config.getConfig().contains(String.valueOf(string) + ".firsthit-threshold")) {
            this.firstHitThreshold = config.getConfig().getInt(String.valueOf(string) + ".firsthit-threshold");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastAttack.containsKey(player.getUniqueId())) {
            this.lastAttack.remove(player.getUniqueId());
        }
    }
}


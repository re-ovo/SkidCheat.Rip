/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.movement.jesus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
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
import rip.anticheat.anticheat.util.math.MathUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class JesusB
extends Check {
    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();
    private Map<UUID, Double> lastFraction = new HashMap<UUID, Double>();
    private Map<UUID, Double> lastFractionOffset = new HashMap<UUID, Double>();
    private int patternThreshold = 25;
    private int upThreshold = 30;
    private int topThreshold = 30;

    public JesusB(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "JesusB", "Jesus (Type B)", 0, 50, 2, 0);
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
        double d = Math.abs(playerMoveEvent.getTo().getX() - playerMoveEvent.getFrom().getX()) + Math.abs(playerMoveEvent.getTo().getZ() - playerMoveEvent.getFrom().getZ());
        double d2 = Math.abs(playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY());
        double d3 = 0.0;
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            d3 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d2);
        double d4 = MathUtil.getFraction(player.getLocation().getY());
        if (!(!PlayerUtil.isHoveringOverWater(player, 1) && !PlayerUtil.isHoveringOverWater(player, 0) || PlayerUtil.isOnGround(player, 0) || PlayerUtil.isOnGround(player, -2) || PlayerUtil.isOnGround(player, -1) || PlayerUtil.isHoveringOverWater(player, -1) || d2 == 0.0 || d2 > 0.15 || d3 > 0.15 || d <= 0.0)) {
            double d5 = this.lastFraction.getOrDefault(player.getUniqueId(), 0.0);
            this.lastFraction.put(player.getUniqueId(), d4);
            if (d5 != 0.0) {
                double d6 = Math.abs(d4 - d5);
                double d7 = this.lastFractionOffset.getOrDefault(player.getUniqueId(), 0.0);
                this.lastFractionOffset.put(player.getUniqueId(), d6);
                int n = playerStats.getCheck(this, 0);
                int n2 = this.patternThreshold;
                n = d4 > 0.8 && d5 < 0.3 ? (n += 6) : (d4 < 0.3 && d5 > 0.8 ? (n += 6) : (n -= 12));
                if (n > n2) {
                    if (d7 != 0.0 && d7 == d6) {
                        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "(experimental) (2)"));
                    } else {
                        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "(experimental) (1)"));
                    }
                    n = 0;
                }
                playerStats.setCheck(this, 0, n);
            }
            int n = playerStats.getCheck(this, 1);
            int n3 = this.upThreshold;
            if (d4 > 0.8) {
                n += 3;
                if (d4 > 0.9) {
                    n += 6;
                    if (d5 > 0.9) {
                        n += 3;
                    }
                }
            } else {
                n -= 3;
            }
            if (playerStats.getVelocityY() > 0.0) {
                n -= 2;
            }
            if (n > n3) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "(3) " + d2 + " " + d4));
                n = 0;
            }
            playerStats.setCheck(this, 1, n);
            int n4 = playerStats.getCheck(this, 2);
            int n5 = this.topThreshold;
            n4 = !PlayerUtil.isHoveringOverWater(player, 0) && d4 < 0.1 ? (n4 += 6) : (n4 -= 8);
            if (playerStats.getVelocityY() > 0.0) {
                n4 -= 5;
            }
            if (n4 > n5) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.TEST, "(experimental) (4) " + d2 + " " + d4));
                n4 = 0;
            }
            playerStats.setCheck(this, 2, n4);
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".pattern-threshold", (Object)this.patternThreshold);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".up-threshold", (Object)this.upThreshold);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".top-threshold", (Object)this.topThreshold);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".pattern-threshold")) {
            this.patternThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".pattern-threshold");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".up-threshold")) {
            this.upThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".up-threshold");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".top-threshold")) {
            this.topThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".top-threshold");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastOffsetY.containsKey(player.getUniqueId())) {
            this.lastOffsetY.remove(player.getUniqueId());
        }
        if (this.lastFraction.containsKey(player.getUniqueId())) {
            this.lastFraction.remove(player.getUniqueId());
        }
        if (this.lastFractionOffset.containsKey(player.getUniqueId())) {
            this.lastFractionOffset.remove(player.getUniqueId());
        }
    }
}


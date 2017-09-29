/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.movement.speed;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.VelocityEvent;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.MathUtil;
import rip.anticheat.anticheat.util.math.VelocityUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.PlayerUtil;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class SpeedA
extends Check {
    public int groundThreshold = 12;
    public int bunnyThreshold = 12;
    private Map<UUID, Vector> playerVelocity = new ConcurrentHashMap<UUID, Vector>();
    private Map<UUID, Long> velocityUpdate = new ConcurrentHashMap<UUID, Long>();
    private long velocityDeacyTime = 2500;
    private double maxVelocityBeforeDeacy = 0.03;

    public SpeedA(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "SpeedA", "Speed (Type A)", 0, 50, 2, 0);
        new BukkitRunnable(){

            public void run() {
                Player[] arrplayer = Bukkit.getOnlinePlayers();
                int n = arrplayer.length;
                int n2 = 0;
                while (n2 < n) {
                    long l;
                    Player player = arrplayer[n2];
                    Vector vector = player.getVelocity();
                    UUID uUID = player.getUniqueId();
                    if (SpeedA.this.velocityUpdate.containsKey(player.getUniqueId()) && (l = System.currentTimeMillis() - (Long)SpeedA.this.velocityUpdate.get(player.getUniqueId())) > SpeedA.this.velocityDeacyTime && (vector.getX() < SpeedA.this.maxVelocityBeforeDeacy && vector.getY() < SpeedA.this.maxVelocityBeforeDeacy && vector.getZ() < SpeedA.this.maxVelocityBeforeDeacy || vector.getX() > - SpeedA.this.maxVelocityBeforeDeacy && vector.getY() > - SpeedA.this.maxVelocityBeforeDeacy && vector.getZ() > - SpeedA.this.maxVelocityBeforeDeacy)) {
                        SpeedA.this.playerVelocity.remove(player.getUniqueId());
                    }
                    ++n2;
                }
            }
        }.runTaskTimer((Plugin)antiCheat, 0, 1);
    }

    @EventHandler
    public void Move(PlayerMoveEvent playerMoveEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        double d = ServerUtil.getPing(player);
        if (player.getAllowFlight()) {
            return;
        }
        if (playerStats.getVelocityXZ() > 0.0) {
            return;
        }
        if (player.isInsideVehicle()) {
            return;
        }
        if (playerStats.getLastMountDiff() < 500) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.SPONGE}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) {
            return;
        }
        double d2 = MathUtil.offset(MathUtil.getHorizontalVector(playerMoveEvent.getFrom().toVector()), MathUtil.getHorizontalVector(playerMoveEvent.getTo().toVector()));
        double d3 = MathUtil.offset(MathUtil.getHorizontalVector(playerMoveEvent.getFrom().toVector()), MathUtil.getHorizontalVector(playerMoveEvent.getTo().toVector()));
        int n = 0;
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (!potionEffect.getType().equals((Object)PotionEffectType.SPEED)) continue;
            n = potionEffect.getAmplifier() + 1;
            break;
        }
        if (playerStats.isOnGround()) {
            double d4 = d2 == 0.5 ? 0.37 : 0.34;
            int n2 = playerStats.getCheck(this, 0);
            if (PlayerUtil.isOnStairs(player, 0) || PlayerUtil.isOnStairs(player, 1)) {
                d4 = 0.45;
            }
            if (playerStats.getLastGroundTimeDiff() > 150 || playerStats.getLastBunnyTimeDiff() < 150) {
                d4 = 0.65;
            }
            if (PlayerUtil.isOnGround(player, -2)) {
                d4 = 0.75;
            }
            if (d3 == 0.45) {
                return;
            }
            if (d > 600.0) {
                return;
            }
            if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.SKULL}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 2, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 3, new Material[]{Material.SKULL})) {
                return;
            }
            if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 2, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 3, new Material[]{Material.ICE, Material.PACKED_ICE})) {
                d4 = PlayerUtil.isOnGround(player, -2) ? 1.0 : 0.65;
            }
            d4 += (double)(player.getWalkSpeed() > 0.2f ? player.getWalkSpeed() * 10.0f * 0.33f : 0.0f);
            d4 += 0.06 * (double)n;
            if (this.playerVelocity.containsKey(player.getUniqueId())) {
                d4 += VelocityUtil.getVelocityHorizontalAsDistance(this.playerVelocity.get(player.getUniqueId()));
                d4 += 0.9;
            }
            if (d3 == 0.34) {
                return;
            }
            n2 = d3 > d4 ? (n2 += 2) : --n2;
            if (n2 > this.groundThreshold) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Ground " + Common.FORMAT_0x00.format(d3) + " > " + Common.FORMAT_0x00.format(d4)));
                n2 = 0;
            }
            playerStats.setCheck(this, 0, n2);
        } else {
            double d5 = 0.4;
            int n3 = playerStats.getCheck(this, 0);
            if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 2, new Material[]{Material.ICE, Material.PACKED_ICE}) || PlayerUtil.isOnBlock(player, 3, new Material[]{Material.ICE, Material.PACKED_ICE})) {
                d5 = PlayerUtil.isOnGround(player, -2) ? 1.0 : 0.65;
            }
            d5 += (double)(player.getWalkSpeed() > 0.2f ? player.getWalkSpeed() * 10.0f * 0.33f : 0.0f);
            d5 += 0.02 * (double)n;
            if (this.playerVelocity.containsKey(player.getUniqueId())) {
                d5 += VelocityUtil.getVelocityHorizontalAsDistance(this.playerVelocity.get(player.getUniqueId()));
                d5 += 0.9;
            }
            n3 = d3 > d5 ? (n3 += 2) : --n3;
            if (n3 > this.bunnyThreshold) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "Bunny " + Common.FORMAT_0x00.format(d3) + " > " + Common.FORMAT_0x00.format(d5)));
                n3 = 0;
            }
            playerStats.setCheck(this, 0, n3);
        }
    }

    @EventHandler
    public void onVelocity(VelocityEvent velocityEvent) {
        this.playerVelocity.put(velocityEvent.getPlayer().getUniqueId(), velocityEvent.getVec());
        if (!this.velocityUpdate.containsKey(velocityEvent.getPlayer().getUniqueId())) {
            this.velocityUpdate.put(velocityEvent.getPlayer().getUniqueId(), System.currentTimeMillis());
        } else {
            this.velocityUpdate.put(velocityEvent.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".ground-threshold", (Object)this.groundThreshold);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".bunny-threshold", (Object)this.bunnyThreshold);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".ground-threshold")) {
            this.groundThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".ground-threshold");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".bunny-threshold")) {
            this.bunnyThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".bunny-threshold");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.playerVelocity.containsKey(player.getUniqueId())) {
            this.playerVelocity.remove(player.getUniqueId());
        }
        if (this.velocityUpdate.containsKey(player.getUniqueId())) {
            this.velocityUpdate.remove(player.getUniqueId());
        }
    }

}


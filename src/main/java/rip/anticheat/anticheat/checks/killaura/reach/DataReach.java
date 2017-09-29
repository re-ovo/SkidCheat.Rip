/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura.reach;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
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
import rip.anticheat.anticheat.util.math.AngleUtil;
import rip.anticheat.anticheat.util.math.VelocityUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class DataReach
extends Check {
    private Map<Player, Long> lastAttack = new WeakHashMap<Player, Long>();
    private double firstHitReach = 3.6;
    private double maxDist = 3.3;
    private double pingScalar = 0.005;
    private double velocityDivisor = 0.7;
    private double hitMultiplier = 0.08;
    private Map<UUID, Vector> playerVelocity = new ConcurrentHashMap<UUID, Vector>();
    private Map<UUID, Long> velocityUpdate = new ConcurrentHashMap<UUID, Long>();
    private long velocityDeacyTime = 2500;
    private double maxVelocityBeforeDeacy = 0.001;

    public DataReach(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "DataReach", "Reach (Data)", 110, 50, 3, 0);
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
                    if (DataReach.this.velocityUpdate.containsKey(player.getUniqueId()) && (l = System.currentTimeMillis() - (Long)DataReach.this.velocityUpdate.get(player.getUniqueId())) > DataReach.this.velocityDeacyTime && (vector.getX() < DataReach.this.maxVelocityBeforeDeacy && vector.getY() < DataReach.this.maxVelocityBeforeDeacy && vector.getZ() < DataReach.this.maxVelocityBeforeDeacy || vector.getX() > - DataReach.this.maxVelocityBeforeDeacy && vector.getY() > - DataReach.this.maxVelocityBeforeDeacy && vector.getZ() > - DataReach.this.maxVelocityBeforeDeacy)) {
                        DataReach.this.playerVelocity.remove(player.getUniqueId());
                    }
                    ++n2;
                }
            }
        }.runTaskTimer((Plugin)antiCheat, 0, 1);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        if (this.playerVelocity.containsKey(uUID)) {
            this.playerVelocity.remove(uUID);
        }
        if (this.velocityUpdate.containsKey(uUID)) {
            this.velocityUpdate.remove(uUID);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!this.isEnabled()) {
            return;
        }
        if (entityDamageByEntityEvent.getDamager() instanceof Player && entityDamageByEntityEvent.getEntity() instanceof Player) {
            Player player = (Player)entityDamageByEntityEvent.getDamager();
            UUID uUID = player.getUniqueId();
            int n = ServerUtil.getPing(player);
            if (n > 400) {
                return;
            }
            if (player.getGameMode() != GameMode.CREATIVE) {
                Player player2 = (Player)entityDamageByEntityEvent.getEntity();
                double d = AngleUtil.getOffsets(player2, (LivingEntity)player)[0];
                if (d > 1200.0) {
                    return;
                }
                if (!player2.hasLineOfSight((Entity)player)) {
                    return;
                }
                if (!player2.isOnGround()) {
                    return;
                }
                Location location = entityDamageByEntityEvent.getEntity().getLocation().clone();
                location.setY(0.0);
                PlayerStats playerStats = this.getCore().getPlayerStats(player);
                int n2 = playerStats.getCheck(this, 0) + 1;
                if (n2 > 5) {
                    n2 = 5;
                }
                long l = 701;
                if (this.lastAttack.containsKey((Object)player)) {
                    l = System.currentTimeMillis() - this.lastAttack.get((Object)player);
                }
                double d2 = -1.0;
                if (l < 700) {
                    d2 = this.maxDist + this.pingScalar * (double)ServerUtil.getPing(player);
                    playerStats.setCheck(this, 0, n2);
                } else {
                    d2 = this.firstHitReach + this.pingScalar * (double)ServerUtil.getPing(player);
                    n2 = 0;
                    playerStats.setCheck(this, 0, 0);
                }
                double d3 = this.hitMultiplier * (double)n2;
                this.lastAttack.put(player, System.currentTimeMillis());
                if ((d2 += d3) == -1.0) {
                    return;
                }
                Location location2 = player.getLocation().clone();
                location2.setY(0.0);
                double d4 = location.distanceSquared(location2);
                if (this.playerVelocity.containsKey(player2.getUniqueId())) {
                    d2 += (VelocityUtil.getVelocityHorizontalAsDistance(this.playerVelocity.get(player2.getUniqueId())) + 0.3) / this.velocityDivisor;
                }
                if (d4 > (d2 + 2.5E-5) * (d2 + 2.5E-5)) {
                    if (l < 700) {
                        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Last Hit: " + Common.FORMAT_0x00.format(Math.sqrt(d4)) + " >= " + Common.FORMAT_0x00.format(d2 + 2.5E-5) + " combo" + n2));
                    } else {
                        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "First Hit: " + Common.FORMAT_0x00.format(Math.sqrt(d4)) + " >= " + Common.FORMAT_0x00.format(d2 + 2.5E-5) + " combo" + n2));
                    }
                }
            }
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
        config.getConfig().set(String.valueOf(string) + ".range", (Object)this.maxDist);
        config.getConfig().set(String.valueOf(string) + ".ping-scalar", (Object)this.pingScalar);
        config.getConfig().set(String.valueOf(string) + ".velocity", (Object)this.velocityDivisor);
        config.getConfig().set(String.valueOf(string) + ".firsthit-threshold", (Object)this.firstHitReach);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(string) + ".range")) {
            this.maxDist = config.getConfig().getDouble(String.valueOf(string) + ".range");
        }
        if (config.getConfig().contains(String.valueOf(string) + ".ping-scalar")) {
            this.pingScalar = config.getConfig().getDouble(String.valueOf(string) + ".ping-scalar");
        }
        if (config.getConfig().contains(String.valueOf(string) + ".velocity")) {
            this.velocityDivisor = config.getConfig().getDouble(String.valueOf(string) + ".velocity");
        }
        if (config.getConfig().contains(String.valueOf(string) + ".firsthit-threshold")) {
            this.firstHitReach = config.getConfig().getDouble(String.valueOf(string) + ".firsthit-threshold");
        }
    }

}


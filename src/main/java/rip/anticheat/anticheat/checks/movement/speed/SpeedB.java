/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
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
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.VelocityEvent;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.MathUtil;
import rip.anticheat.anticheat.util.math.VelocityUtil;
import rip.anticheat.anticheat.util.misc.PlayerUtil;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class SpeedB
extends Check {
    private Map<UUID, Long> onIce = new WeakHashMap<UUID, Long>();
    private Map<UUID, Long> blockAbove = new WeakHashMap<UUID, Long>();
    private Map<UUID, Long> inTrapdoor = new WeakHashMap<UUID, Long>();
    private Map<UUID, Long> lastVL = new WeakHashMap<UUID, Long>();
    private Map<UUID, Long> flightToggle = new WeakHashMap<UUID, Long>();
    private Map<UUID, Boolean> wasFlying = new WeakHashMap<UUID, Boolean>();
    private Map<UUID, Long> onSoulsand = new WeakHashMap<UUID, Long>();
    private Map<UUID, Long> speedTicks = new WeakHashMap<UUID, Long>();
    private Map<UUID, Integer> speedLevel = new WeakHashMap<UUID, Integer>();
    private Map<UUID, Long> inWeb = new WeakHashMap<UUID, Long>();
    private Map<UUID, Vector> playerVelocity = new WeakHashMap<UUID, Vector>();
    private Map<UUID, Long> velocityUpdate = new WeakHashMap<UUID, Long>();
    private double baseSpeed = 0.71;
    private double maxOverMove = 0.005;
    private double iceScalar = 0.49;
    private double trapdoorScalar = 0.15;
    private double speedScalar = 0.05;
    private long velocityDeacyTime = 2500;
    private double maxVelocityBeforeDeacy = 0.03;

    public SpeedB(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "SpeedB", "Speed (Type B)", 0, 50, 2, 0);
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
                    if (!SpeedB.this.lastVL.containsKey(uUID)) {
                        SpeedB.this.lastVL.put(uUID, 0);
                    }
                    if (!SpeedB.this.wasFlying.containsKey(uUID)) {
                        SpeedB.this.wasFlying.put(uUID, player.isFlying());
                    } else {
                        boolean bl = (Boolean)SpeedB.this.wasFlying.get(uUID);
                        if (bl != player.isFlying()) {
                            SpeedB.this.wasFlying.put(uUID, player.isFlying());
                            SpeedB.this.flightToggle.put(uUID, System.currentTimeMillis());
                        }
                    }
                    if (SpeedB.this.velocityUpdate.containsKey(player.getUniqueId()) && (l = System.currentTimeMillis() - (Long)SpeedB.this.velocityUpdate.get(player.getUniqueId())) > SpeedB.this.velocityDeacyTime && (vector.getX() < SpeedB.this.maxVelocityBeforeDeacy && vector.getY() < SpeedB.this.maxVelocityBeforeDeacy && vector.getZ() < SpeedB.this.maxVelocityBeforeDeacy || vector.getX() > - SpeedB.this.maxVelocityBeforeDeacy && vector.getY() > - SpeedB.this.maxVelocityBeforeDeacy && vector.getZ() > - SpeedB.this.maxVelocityBeforeDeacy)) {
                        SpeedB.this.playerVelocity.remove(player.getUniqueId());
                    }
                    ++n2;
                }
            }
        }.runTaskTimer((Plugin)antiCheat, 0, 1);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        if (this.isEnabled()) {
            long l;
            double d;
            Player player = playerMoveEvent.getPlayer();
            UUID uUID = player.getUniqueId();
            double d2 = ServerUtil.getPing(player);
            if ((double)player.getWalkSpeed() > 0.21) {
                return;
            }
            if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) {
                return;
            }
            if ((PlayerUtil.isOnBlock(player, 0, new Material[]{Material.SPONGE}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.SPONGE})) && !player.isOnGround()) {
                return;
            }
            if (player.getAllowFlight()) {
                return;
            }
            if (player.isInsideVehicle()) {
                return;
            }
            if (d2 > 600.0) {
                return;
            }
            if (this.flightToggle.containsKey(player.getUniqueId()) && (l = System.currentTimeMillis() - this.flightToggle.get(player.getUniqueId())) < 20000) {
                return;
            }
            double d3 = this.baseSpeed;
            double d4 = MathUtil.distanceXZSquared(playerMoveEvent.getFrom(), playerMoveEvent.getTo());
            if (this.playerVelocity.containsKey(player.getUniqueId())) {
                d3 += VelocityUtil.getVelocityHorizontalAsDistance(this.playerVelocity.get(player.getUniqueId()));
                d3 += 0.9;
            }
            this.addSpeedOffsets(player);
            long l2 = System.currentTimeMillis() - this.onIce.get(uUID);
            long l3 = System.currentTimeMillis() - this.blockAbove.get(uUID);
            long l4 = System.currentTimeMillis() - this.inTrapdoor.get(uUID);
            if (l2 < 1000 && l3 < 1000) {
                d3 += this.iceScalar;
                if (l4 < 1000) {
                    d3 += this.trapdoorScalar;
                }
            }
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                if (!potionEffect.getType().equals((Object)PotionEffectType.SPEED)) continue;
                this.speedTicks.put(uUID, System.currentTimeMillis());
                this.speedLevel.put(uUID, potionEffect.getAmplifier());
            }
            long l5 = System.currentTimeMillis() - this.speedTicks.get(uUID);
            if (l5 < 1000) {
                d3 += this.speedScalar * (double)this.speedLevel.get(uUID).intValue();
            }
            if ((d = (d3 + this.maxOverMove) * (d3 + this.maxOverMove)) < d4) {
                long l6;
                if (!this.lastVL.containsKey(uUID)) {
                    this.lastVL.put(uUID, System.currentTimeMillis());
                }
                if ((l6 = System.currentTimeMillis() - this.lastVL.get(uUID)) < 2500) {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, String.valueOf(Common.FORMAT_0x00.format(Math.sqrt(d4))) + " > " + Common.FORMAT_0x00.format(Math.sqrt(d))));
                    this.lastVL.put(uUID, System.currentTimeMillis());
                } else {
                    this.lastVL.put(uUID, System.currentTimeMillis());
                }
            }
        }
    }

    private void addSpeedOffsets(Player player) {
        UUID uUID = player.getUniqueId();
        Block block = player.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
        Block block2 = player.getLocation().add(0.0, 1.0, 0.0).getBlock();
        Block block3 = player.getLocation().add(0.0, 2.0, 0.0).getBlock();
        if (!this.onIce.containsKey(uUID)) {
            this.onIce.put(uUID, 0);
            this.blockAbove.put(uUID, 0);
            this.inTrapdoor.put(uUID, 0);
            this.speedTicks.put(uUID, 0);
            this.speedLevel.put(uUID, 0);
        }
        if (block.getType() != Material.AIR) {
            this.onIce.put(uUID, System.currentTimeMillis());
            if (block3.getType() != Material.AIR) {
                this.blockAbove.put(uUID, System.currentTimeMillis());
            }
            if (block2.getType() == Material.TRAP_DOOR || block2.getType().name().contains("STAIRS")) {
                this.inTrapdoor.put(uUID, System.currentTimeMillis());
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

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.blockAbove.containsKey(player.getUniqueId())) {
            this.blockAbove.remove(player.getUniqueId());
        }
        if (this.flightToggle.containsKey(player.getUniqueId())) {
            this.flightToggle.remove(player.getUniqueId());
        }
        if (this.inTrapdoor.containsKey(player.getUniqueId())) {
            this.inTrapdoor.remove(player.getUniqueId());
        }
        if (this.inWeb.containsKey(player.getUniqueId())) {
            this.inWeb.remove(player.getUniqueId());
        }
        if (this.lastVL.containsKey(player.getUniqueId())) {
            this.lastVL.remove(player.getUniqueId());
        }
        if (this.onIce.containsKey(player.getUniqueId())) {
            this.onIce.remove(player.getUniqueId());
        }
        if (this.onSoulsand.containsKey(player.getUniqueId())) {
            this.onSoulsand.remove(player.getUniqueId());
        }
        if (this.playerVelocity.containsKey(player.getUniqueId())) {
            this.playerVelocity.remove(player.getUniqueId());
        }
        if (this.speedLevel.containsKey(player.getUniqueId())) {
            this.speedLevel.remove(player.getUniqueId());
        }
        if (this.speedTicks.containsKey(player.getUniqueId())) {
            this.speedTicks.remove(player.getUniqueId());
        }
        if (this.velocityUpdate.containsKey(player.getUniqueId())) {
            this.velocityUpdate.remove(player.getUniqueId());
        }
        if (this.wasFlying.containsKey(player.getUniqueId())) {
            this.wasFlying.remove(player.getUniqueId());
        }
    }

}


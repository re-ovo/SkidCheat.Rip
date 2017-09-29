/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.movement;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.MathUtil;
import rip.anticheat.anticheat.util.misc.CheatUtil;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class Ascension
extends Check {
    private double maxHeight = 1.5;
    private Map<UUID, Long> lastDamage = new HashMap<UUID, Long>();
    private Map<UUID, Map.Entry<Long, Double>> AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();

    public Ascension(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "Ascension", "Ascension", 3, 50, 2, 0);
    }

    @EventHandler
    public void CheckAscension(PlayerMoveEvent playerMoveEvent) {
        Location location;
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (playerMoveEvent.getFrom().getY() >= playerMoveEvent.getTo().getY()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (playerStats.getVelocityY() > 0.0) {
            return;
        }
        if (playerStats.getVelocityXZ() > 0.0) {
            return;
        }
        if (PlayerUtil.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK}) || PlayerUtil.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) {
            return;
        }
        long l = System.currentTimeMillis();
        double d = 0.0;
        if (this.AscensionTicks.containsKey(player.getUniqueId())) {
            l = this.AscensionTicks.get(player.getUniqueId()).getKey();
            d = this.AscensionTicks.get(player.getUniqueId()).getValue();
        }
        long l2 = System.currentTimeMillis() - l;
        double d2 = MathUtil.offset(MathUtil.getVerticalVector(playerMoveEvent.getFrom().toVector()), MathUtil.getVerticalVector(playerMoveEvent.getTo().toVector()));
        if (d2 > 0.0) {
            d += d2;
        }
        if (CheatUtil.blocksNear(player)) {
            d = 0.0;
        }
        if (CheatUtil.blocksNear(location = player.getLocation().subtract(0.0, 1.0, 0.0))) {
            d = 0.0;
        }
        double d3 = 0.5;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                if (!potionEffect.getType().equals((Object)PotionEffectType.JUMP)) continue;
                int n = potionEffect.getAmplifier() + 1;
                d3 += Math.pow((double)n + 4.2, 2.0) / 16.0;
                break;
            }
        }
        if (d > d3) {
            if (l2 > 500) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(d2)));
                playerMoveEvent.setCancelled(true);
                l = System.currentTimeMillis();
            }
        } else {
            l = System.currentTimeMillis();
        }
        this.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(l, d));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL && entityDamageEvent.getEntity() instanceof Player) {
            Player player = (Player)entityDamageEvent.getEntity();
            this.lastDamage.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".height", (Object)this.maxHeight);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".height")) {
            this.maxHeight = config.getConfig().getDouble(String.valueOf(String.valueOf(string)) + ".height");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastDamage.containsKey(player.getUniqueId())) {
            this.lastDamage.remove(player.getUniqueId());
        }
    }
}


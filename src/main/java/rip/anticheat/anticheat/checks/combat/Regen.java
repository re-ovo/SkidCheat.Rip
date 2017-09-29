/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent$RegainReason
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package rip.anticheat.anticheat.checks.combat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.Config;

public class Regen
extends Check {
    private long speedSatiated = 2000;
    private long speedPeaceful = 400;
    private Map<UUID, Long> lastRegen = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastPeacefulRegen = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastPotionRegen = new HashMap<UUID, Long>();
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$event$entity$EntityRegainHealthEvent$RegainReason;

    public Regen(AntiCheat antiCheat) {
        super(antiCheat, CheckType.COMBAT, "Regen", "Regen", 3, 50, 2, 0);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent entityRegainHealthEvent) {
        if (!this.isEnabled()) {
            return;
        }
        if (entityRegainHealthEvent.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player)entityRegainHealthEvent.getEntity();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        long l = -1000;
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        long l2 = 0;
        switch (Regen.$SWITCH_TABLE$org$bukkit$event$entity$EntityRegainHealthEvent$RegainReason()[entityRegainHealthEvent.getRegainReason().ordinal()]) {
            case 2: {
                l2 = this.speedSatiated;
                if (this.lastRegen.containsKey(player.getUniqueId())) {
                    l = System.currentTimeMillis() - this.lastRegen.get(player.getUniqueId());
                }
                this.lastRegen.put(player.getUniqueId(), System.currentTimeMillis());
                break;
            }
            case 1: {
                l2 = this.speedPeaceful;
                if (this.lastPeacefulRegen.containsKey(player.getUniqueId())) {
                    l = System.currentTimeMillis() - this.lastPeacefulRegen.get(player.getUniqueId());
                }
                this.lastPeacefulRegen.put(player.getUniqueId(), System.currentTimeMillis());
                break;
            }
            case 6: {
                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    if (!potionEffect.getType().equals((Object)PotionEffectType.REGENERATION)) continue;
                    switch (potionEffect.getAmplifier()) {
                        case 0: {
                            l2 = 1500;
                            break;
                        }
                        case 1: {
                            l2 = 750;
                            break;
                        }
                        case 2: {
                            l2 = 250;
                            break;
                        }
                        default: {
                            return;
                        }
                    }
                }
                if (this.lastPotionRegen.containsKey(player.getUniqueId())) {
                    l = System.currentTimeMillis() - this.lastPotionRegen.get(player.getUniqueId());
                }
                this.lastPotionRegen.put(player.getUniqueId(), System.currentTimeMillis());
                break;
            }
            default: {
                return;
            }
        }
        if (l == -1000) {
            return;
        }
        if (l2 > l) {
            ++n;
            if (l2 / 2 > l) {
                ++n;
            }
            if (l2 / 3 > l) {
                ++n;
            }
        } else {
            --n;
        }
        if ((double)n > (double)n2 * 1.5) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(l) + "ms"));
            n = 0;
        } else if (n > n2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(l) + "ms"));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".speed_satiated", (Object)this.speedSatiated);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".speed_peaceful", (Object)this.speedPeaceful);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".speed_satiated")) {
            this.speedSatiated = config.getConfig().getLong(String.valueOf(String.valueOf(string)) + ".speed_satiated");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".speed_peaceful")) {
            this.speedPeaceful = config.getConfig().getLong(String.valueOf(String.valueOf(string)) + ".speed_peaceful");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastPeacefulRegen.containsKey(player.getUniqueId())) {
            this.lastPeacefulRegen.remove(player.getUniqueId());
        }
        if (this.lastPotionRegen.containsKey(player.getUniqueId())) {
            this.lastPotionRegen.remove(player.getUniqueId());
        }
        if (this.lastRegen.containsKey(player.getUniqueId())) {
            this.lastRegen.remove(player.getUniqueId());
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$event$entity$EntityRegainHealthEvent$RegainReason() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$event$entity$EntityRegainHealthEvent$RegainReason;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[EntityRegainHealthEvent.RegainReason.values().length];
        try {
            arrn[EntityRegainHealthEvent.RegainReason.CUSTOM.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.EATING.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.MAGIC.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.MAGIC_REGEN.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.REGEN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.SATIATED.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.WITHER.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityRegainHealthEvent.RegainReason.WITHER_SPAWN.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$org$bukkit$event$entity$EntityRegainHealthEvent$RegainReason = arrn;
        return $SWITCH_TABLE$org$bukkit$event$entity$EntityRegainHealthEvent$RegainReason;
    }
}


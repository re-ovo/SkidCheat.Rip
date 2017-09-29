/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.event.player.PlayerToggleSprintEvent
 *  org.bukkit.potion.PotionEffectType
 */
package rip.anticheat.anticheat.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffectType;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class NoSlow
extends Check {
    private Map<UUID, Long> lastSneak = new HashMap<UUID, Long>();
    private int sneakThreshold = 15;

    public NoSlow(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "NoSlow", "NoSlow", 1, 50, 2, 0);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onItemConsume(PlayerItemConsumeEvent playerItemConsumeEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerItemConsumeEvent.getPlayer();
        if (player.isInsideVehicle()) {
            return;
        }
        int n = ServerUtil.getPing(player);
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n2 = playerStats.getCheck(this, 0);
        int n3 = this.getThreshold();
        if (n > 250) {
            return;
        }
        n2 = player.isSprinting() ? ++n2 : --n2;
        if (n2 > n3) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Consumed items while sprinting"));
            n2 = 0;
        }
        playerStats.setCheck(this, 0, n2);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void BowShoot(EntityShootBowEvent entityShootBowEvent) {
        if (!this.isEnabled()) {
            return;
        }
        if (!(entityShootBowEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityShootBowEvent.getEntity();
        if (player.isInsideVehicle()) {
            return;
        }
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        n = player.isSprinting() ? ++n : --n;
        if (n > n2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Shot a bow while sprinting"));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onEntityAction(PlayerToggleSneakEvent playerToggleSneakEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerToggleSneakEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        long l = 100000;
        if (this.lastSneak.containsKey(player.getUniqueId())) {
            l = System.currentTimeMillis() - this.lastSneak.get(player.getUniqueId());
        }
        int n = playerStats.getCheck(this, 1);
        int n2 = this.sneakThreshold;
        n = l < 100 ? ++n : (n -= 5);
        if (n > n2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Sneak Bypass"));
            n = 0;
        }
        playerStats.setCheck(this, 1, n);
        this.lastSneak.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onEntityAction2(PlayerToggleSprintEvent playerToggleSprintEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerToggleSprintEvent.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Sprint while blind"));
            return;
        }
        if (player.getFoodLevel() <= 3 && !player.getAllowFlight() && !player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Sprint while hungry"));
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".sneak-threshold", (Object)this.sneakThreshold);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".sneak-threshold")) {
            this.sneakThreshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".sneak-threshold");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastSneak.containsKey(player.getUniqueId())) {
            this.lastSneak.remove(player.getUniqueId());
        }
    }
}


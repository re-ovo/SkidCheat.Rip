/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.Config;

public class FastBow
extends Check {
    private Map<UUID, Long> bowPull = new HashMap<UUID, Long>();
    private Long bowSpeed = 300;

    public FastBow(AntiCheat antiCheat) {
        super(antiCheat, CheckType.COMBAT, "FastBow", "FastBow", 2, 50, 1, 0);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getType().equals((Object)Material.BOW)) {
            this.bowPull.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent projectileLaunchEvent) {
        if (!this.isEnabled()) {
            return;
        }
        if (!(projectileLaunchEvent.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow)projectileLaunchEvent.getEntity();
        if (arrow.getShooter() == null) {
            return;
        }
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)arrow.getShooter();
        if (this.bowPull.containsKey(player.getUniqueId())) {
            Long l = System.currentTimeMillis() - this.bowPull.get(player.getUniqueId());
            double d = arrow.getVelocity().length();
            Long l2 = this.bowSpeed;
            PlayerStats playerStats = this.getCore().getPlayerStats(player);
            int n = playerStats.getCheck(this, 0);
            int n2 = this.getThreshold();
            n = d > 2.5 && l < l2 ? ++n : --n;
            if (n > n2) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, l + "ms"));
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".speed", (Object)this.bowSpeed);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".speed")) {
            this.bowSpeed = config.getConfig().getLong(String.valueOf(String.valueOf(string)) + ".speed");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.bowPull.containsKey(player.getUniqueId())) {
            this.bowPull.remove(player.getUniqueId());
        }
    }
}


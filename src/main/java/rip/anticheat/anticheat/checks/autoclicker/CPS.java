/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package rip.anticheat.anticheat.checks.autoclicker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.learn.LearningProccess;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.TimeUtil;

public class CPS
extends Check {
    private int maxCPS = 23;
    private Map<UUID, Long> clickTimes = new HashMap<UUID, Long>();
    public static CPS cps;

    public CPS(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "CPS", "AutoClicker (A) [CON]", 2, 50, 2, 0);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        long l;
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        double d = playerStats.getCPS();
        int n = playerStats.getCheck(this, 0);
        if (playerInteractEvent.getAction() == Action.LEFT_CLICK_AIR) {
            playerStats.setCPS(d += 1.0);
        } else if (playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK) {
            playerStats.setCPS(d += 0.5);
        }
        if (this.clickTimes.containsKey(player.getUniqueId()) && TimeUtil.elapsed(l = this.clickTimes.get(player.getUniqueId()).longValue(), 1000)) {
            if (d >= (double)this.maxCPS) {
                ++n;
                LearningProccess.cheatingpre.put(player.getName(), 5.0);
                LearningProccess.startLearning(this, player);
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(d) + " Cps"));
            } else {
                --n;
            }
            playerStats.setLatestCPS((int)d);
            if (playerStats.getLatestCPS() > playerStats.getHighestCPS()) {
                playerStats.setHighestCPS(playerStats.getLatestCPS());
            }
            playerStats.setCPS(0.0);
            this.clickTimes.remove(player.getUniqueId());
        }
        playerStats.setCheck(this, 0, n);
        if (!this.clickTimes.containsKey(player.getUniqueId())) {
            this.clickTimes.put(player.getUniqueId(), TimeUtil.nowlong());
        }
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(string) + ".maxcps", (Object)this.maxCPS);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(string) + ".maxcps")) {
            this.maxCPS = config.getConfig().getInt(String.valueOf(string) + ".maxcps");
        }
    }

    public static CPS cpsInstance() {
        return cps;
    }
}


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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
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
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.TimeUtil;

public class ClickPattern
extends Check {
    private int maxCPS = 16;
    private Map<Player, Long> clickTimes = new HashMap<Player, Long>();

    public ClickPattern(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "ClickPattern", "ClickPattern (Type A)", 2, 50, 2, 0);
    }

    @EventHandler
    public void useEntity(PlayerInteractEvent playerInteractEvent) {
        long l;
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        double d = playerStats.getCPS();
        int n = playerStats.getCheck(this, 0);
        double d2 = 0.0;
        if (playerInteractEvent.getAction() == Action.LEFT_CLICK_AIR) {
            playerStats.setCPS(d += 1.0);
        } else if (playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK) {
            playerStats.setCPS(d += 0.5);
        }
        if (d == 12.0) {
            d2 += 1.0;
        } else if (d == 13.0) {
            d2 -= 1.0;
        }
        if (d == 14.0) {
            d2 += 1.0;
        } else if (d == 15.0) {
            d2 -= 2.0;
        } else if (d == 16.0) {
            d2 += 2.0;
        }
        if (d2 >= 5.0) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(Common.FORMAT_0x00.format(d)) + " Click-Pattern (Expirimental)"));
        }
        if (this.clickTimes.containsKey((Object)player) && TimeUtil.elapsed(l = this.clickTimes.get((Object)player).longValue(), 1000)) {
            if (d >= (double)this.maxCPS) {
                ++n;
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(Common.FORMAT_0x00.format(d)) + " CPS"));
            } else {
                --n;
            }
            playerStats.setLatestCPS((int)d);
            if (playerStats.getLatestCPS() > playerStats.getHighestCPS()) {
                playerStats.setHighestCPS(playerStats.getLatestCPS());
            }
            playerStats.setCPS(0.0);
            this.clickTimes.remove((Object)player);
        }
        playerStats.setCheck(this, 0, n);
        if (!this.clickTimes.containsKey((Object)player)) {
            this.clickTimes.put(player, TimeUtil.nowlong());
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
}


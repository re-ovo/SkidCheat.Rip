/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AlertType;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.commands.old.AntiCheatCommand;
import rip.anticheat.anticheat.commands.old.AutobanCommand;
import rip.anticheat.anticheat.jday.JudgementDay;
import rip.anticheat.anticheat.jday.JudgementDays;
import rip.anticheat.anticheat.update.UpdateType;
import rip.anticheat.anticheat.update.events.UpdateEvent;
import rip.anticheat.anticheat.util.formatting.ActionMessageUtil;
import rip.anticheat.anticheat.util.formatting.ChatUtil;
import rip.anticheat.anticheat.util.math.MathUtil;
import rip.anticheat.anticheat.util.misc.MessagesYml;

public class Autoban
implements Listener {
    public AntiCheat Core;
    public Map<Player, Long> AutoBanQueue = new HashMap<Player, Long>();
    public Map<Player, Check> AutoBanChecks = new HashMap<Player, Check>();

    public Autoban(AntiCheat antiCheat) {
        this.Core = antiCheat;
        this.Core.getCommand("autoban").setExecutor((CommandExecutor)new AutobanCommand(this));
        this.Core.RegisterListener(this);
    }

    @EventHandler
    public void Update(UpdateEvent updateEvent) {
        if (!updateEvent.getType().equals((Object)UpdateType.SEC_02)) {
            return;
        }
        for (Player player : new HashMap<Player, Long>(this.AutoBanQueue).keySet()) {
            if (player == null) {
                this.AutoBanQueue.remove((Object)player);
                continue;
            }
            Long l = this.AutoBanQueue.get((Object)player);
            if (System.currentTimeMillis() <= l) continue;
            this.forceban(player);
        }
    }

    public Map<Player, Long> getAutobanQueue() {
        return new HashMap<Player, Long>(this.AutoBanQueue);
    }

    public void setBanCheck(Player player, Check check) {
        if (this.AutoBanChecks.containsKey((Object)player)) {
            this.AutoBanChecks.remove((Object)player);
        }
        if (check != null) {
            this.AutoBanChecks.put(player, check);
        }
    }

    public void cancelban(Player player) {
        this.AutoBanQueue.remove((Object)player);
        this.AutoBanChecks.remove((Object)player);
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        playerStats.removeViolations();
        playerStats.removeBans();
    }

    public void forceban(Player player) {
        Object object2;
        if (!AntiCheatCommand.autoban) {
            this.AutoBanQueue.clear();
            this.AutoBanChecks.clear();
            return;
        }
        Check check = this.AutoBanChecks.get((Object)player);
        if (check != null && check.isTempDisabled()) {
            return;
        }
        if (this.AutoBanQueue.containsKey((Object)player)) {
            this.AutoBanQueue.remove((Object)player);
        }
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        playerStats.removeBans();
        if (playerStats.getViolations() != null) {
            for (List<Violation> object22 : playerStats.getViolations().values()) {
                for (Object object2 : object22) {
                    object2.setUnused(true);
                }
            }
        }
        String string = check == null ? "no valid check id" : check.getId();
        final String string2 = this.Core.banCommand.replaceAll("%player%", player.getName()).replaceAll("%id%", string).trim();
        this.Core.getServer().getScheduler().runTask((Plugin)this.Core, new Runnable(){

            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), string2);
                ++rip.anticheat.anticheat.Tracker.bansToday;
                ++rip.anticheat.anticheat.Tracker.bansOverall;
            }
        });
        object2 = new ArrayList<String>(this.Core.banMessages);
        if (object2.size() > 0) {
            String string3 = ChatUtil.c(((String)object2.get(MathUtil.r(object2.size()))).replaceAll("%player%", player.getName()).replace("\\n", "\n").replaceAll("%id%", string).trim());
            Bukkit.getServer().broadcastMessage(string3);
        }
        if (this.AutoBanChecks.containsKey((Object)player)) {
            this.AutoBanChecks.remove((Object)player);
        }
    }

    public void scheduleban(Check check, Player player) {
        if (!AntiCheatCommand.autoban) {
            this.AutoBanQueue.clear();
            this.AutoBanChecks.clear();
            return;
        }
        if (this.AutoBanQueue.containsKey((Object)player)) {
            return;
        }
        if (check == null) {
            return;
        }
        if (check.isTempDisabled()) {
            return;
        }
        int n = check.getAutobanTimer();
        String string = check.getDisplayName();
        this.AutoBanQueue.put(player, System.currentTimeMillis() + (long)n * 1000);
        this.setBanCheck(player, check);
        List<Player> list = this.Core.getPlayers(AlertType.NORMAL);
        String string2 = this.Core.getMessages().getMessage("alerts.bantimer.bantimer").replaceAll("%player%", player.getName()).replaceAll("%time%", String.valueOf(n)).replaceAll("%check%", string);
        for (Player player2 : list) {
            ActionMessageUtil.sendToPlayer(player2, string2);
        }
    }

    public void ban(Check check, Player player) {
        if (check.isTempDisabled()) {
            return;
        }
        JudgementDay judgementDay = this.Core.getJudgementDays().getJudgementDay(check);
        if (judgementDay != null) {
            if (!judgementDay.getQueued().contains(player.getUniqueId())) {
                judgementDay.queuePlayer(player);
                List<Player> list = this.Core.getPlayers(AlertType.JDAY);
                String string = this.Core.getMessages().getMessage("alerts.jday.admins").replaceAll("%player%", player.getName()).replaceAll("%jday%", judgementDay.getName()).replaceAll("%total%", String.valueOf(judgementDay.getQueued().size()));
                for (Player player2 : list) {
                    player2.sendMessage(string);
                }
            }
            return;
        }
        if (check.getAutobanTimer() > 0) {
            this.scheduleban(check, player);
        } else {
            this.setBanCheck(player, check);
            this.forceban(player);
        }
    }

}


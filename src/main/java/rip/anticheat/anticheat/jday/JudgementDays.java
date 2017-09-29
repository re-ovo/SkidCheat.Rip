/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.jday;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.jday.JudgementDay;
import rip.anticheat.anticheat.update.UpdateType;
import rip.anticheat.anticheat.update.events.UpdateEvent;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.MessagesYml;

public class JudgementDays
implements Listener {
    public List<JudgementDay> JDays = new ArrayList<JudgementDay>();
    private JudgementDay Executed = null;
    private Long LastBan = -1;
    private Long Interval = 1000;
    private String Command = "ban %player% %reason%";
    private AntiCheat Core;
    public Config Config;

    public JudgementDays(AntiCheat antiCheat) {
        this.Core = antiCheat;
        this.Core.RegisterListener(this);
        this.Config = new Config(this.Core, "", "jdays");
        this.Config.setDefault("interval", 1000);
        this.Config.setDefault("command", "ban %player% %reason%");
        this.Config.save();
        this.loadConfig();
    }

    public void loadConfig() {
        this.Interval = this.Config.getConfig().getLong("interval");
        this.Command = this.Config.getConfig().getString("command");
        this.JDays.clear();
        ConfigurationSection configurationSection = this.Config.getConfig().getConfigurationSection("jdays");
        if (configurationSection == null) {
            configurationSection = this.Config.getConfig().createSection("jdays");
        }
        for (String string : configurationSection.getKeys(false)) {
            String string2 = "jdays." + string;
            long l = this.Config.getConfig().getLong(String.valueOf(String.valueOf(string2)) + ".TimeCreated");
            String string3 = this.Config.getConfig().getString(String.valueOf(String.valueOf(string2)) + ".Creator");
            String string4 = this.Config.getConfig().getString(String.valueOf(String.valueOf(string2)) + ".Reason");
            boolean bl = this.Config.getConfig().getBoolean(String.valueOf(String.valueOf(string2)) + ".Finished");
            int n = this.Config.getConfig().getInt(String.valueOf(String.valueOf(string2)) + ".TotalQueued");
            List list = this.Config.getConfig().getStringList(String.valueOf(String.valueOf(string2)) + ".Checks");
            List list2 = this.Config.getConfig().getStringList(String.valueOf(String.valueOf(string2)) + ".Queued");
            JudgementDay judgementDay = new JudgementDay(string, string4, string3, l, bl, n);
            for (String string5 : list) {
                Check check = this.Core.getCheck(string5);
                if (check == null) continue;
                judgementDay.addCheck(check);
            }
            for (String string5 : list2) {
                judgementDay.addQueue(UUID.fromString(string5));
            }
            this.addJudgementDay(judgementDay);
        }
    }

    public void saveConfig() {
        this.Config.getConfig().set("jdays", (Object)null);
        for (JudgementDay judgementDay : this.getJudgementDays()) {
            String string = "jdays." + judgementDay.getName();
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".Reason", (Object)judgementDay.getReason());
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".TimeCreated", (Object)judgementDay.getTimeCreated());
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".Creator", (Object)judgementDay.getCreator());
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".Finished", (Object)judgementDay.isFinished());
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".TotalQueued", (Object)judgementDay.getTotalQueued());
            ArrayList<String> arrayList = new ArrayList<String>();
            for (Check check : judgementDay.getChecks()) {
                arrayList.add(check.getName());
            }
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".Checks", arrayList);
            ArrayList<String> object2 = new ArrayList<String>();
            for (UUID uUID : judgementDay.getQueued()) {
                object2.add(uUID.toString());
            }
            this.Config.getConfig().set(String.valueOf(String.valueOf(string)) + ".Queued", object2);
        }
        this.Config.save();
    }

    public void execute(JudgementDay judgementDay) {
        this.Executed = judgementDay;
        this.LastBan = System.currentTimeMillis();
    }

    public List<JudgementDay> getJudgementDays() {
        return new ArrayList<JudgementDay>(this.JDays);
    }

    public JudgementDay getJudgementDay(String string) {
        for (JudgementDay judgementDay : this.getJudgementDays()) {
            if (!judgementDay.getName().equalsIgnoreCase(string)) continue;
            return judgementDay;
        }
        return null;
    }

    public JudgementDay getJudgementDay(Check check) {
        for (JudgementDay judgementDay : this.getJudgementDays()) {
            if (!judgementDay.getChecks().contains(check) || judgementDay.isFinished()) continue;
            return judgementDay;
        }
        return null;
    }

    public void addJudgementDay(JudgementDay judgementDay) {
        this.JDays.add(judgementDay);
    }

    public JudgementDay removeJudgementDay(String string) {
        for (JudgementDay judgementDay : this.getJudgementDays()) {
            if (!judgementDay.getName().equalsIgnoreCase(string)) continue;
            this.JDays.remove(judgementDay);
            return judgementDay;
        }
        return null;
    }

    @EventHandler
    public void Update(UpdateEvent updateEvent) {
        if (!updateEvent.getType().equals((Object)UpdateType.TICK)) {
            return;
        }
        if (this.Executed == null) {
            return;
        }
        long l = System.currentTimeMillis() - this.LastBan;
        if (l > this.Interval) {
            if (this.Executed.getQueued().size() > 0) {
                OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(this.Executed.getQueued().get(0));
                final String string = this.Command.replaceAll("%player%", offlinePlayer.getName()).replaceAll("%reason%", this.Executed.getReason());
                final String string2 = this.Core.getMessages().getMessage("jdays.ban.banned").replaceAll("%player%", offlinePlayer.getName()).replaceAll("%reason%", this.Executed.getReason());
                this.Core.getServer().getScheduler().runTask((Plugin)this.Core, new Runnable(){

                    @Override
                    public void run() {
                        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), string);
                        Bukkit.getServer().broadcastMessage(string2);
                    }
                });
                this.Executed.removeQueued(offlinePlayer.getUniqueId());
            } else {
                this.Executed.setFinished(true);
                this.Executed = null;
            }
            this.LastBan = System.currentTimeMillis();
        }
    }

}


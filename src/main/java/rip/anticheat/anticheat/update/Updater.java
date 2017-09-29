/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 */
package rip.anticheat.anticheat.update;

import java.text.DecimalFormat;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.update.UpdateType;
import rip.anticheat.anticheat.update.events.UpdateEvent;
import rip.anticheat.anticheat.util.misc.MessagesYml;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class Updater
implements Runnable {
    private AntiCheat Core;
    private int updater;
    private DecimalFormat format = new DecimalFormat("0.00");

    public Updater(AntiCheat antiCheat) {
        this.Core = antiCheat;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.Core, (Runnable)this, 0, 1);
    }

    public void Disable() {
        Bukkit.getScheduler().cancelTask(this.updater);
    }

    @Override
    public void run() {
        double d = ServerUtil.getTps()[0];
        String string = "";
        String string2 = "";
        for (Check arrupdateType2 : AntiCheat.Instance.getChecks()) {
            if (!arrupdateType2.isActuallyEnabled()) continue;
            if (arrupdateType2.getTpsDisable() >= d) {
                if (arrupdateType2.isTempDisabled()) continue;
                string2 = String.valueOf(string2) + arrupdateType2.getDisplayName() + ", ";
                arrupdateType2.setTempDisabled(true);
                continue;
            }
            if (!arrupdateType2.isTempDisabled()) continue;
            string = String.valueOf(string) + arrupdateType2.getDisplayName() + ", ";
            arrupdateType2.setTempDisabled(false);
        }
        if (!string.isEmpty()) {
            string = string.substring(0, string.length() - 2);
            Bukkit.broadcast((String)this.Core.getMessages().getMessage("autobans.tps.enabled").replaceAll("%checks%", string).replaceAll("%tps%", this.format.format(d)), (String)"anticheat.staff");
        }
        if (!string2.isEmpty()) {
            string2 = string2.substring(0, string2.length() - 2);
            Bukkit.broadcast((String)this.Core.getMessages().getMessage("autobans.tps.disabled").replaceAll("%checks%", string2).replaceAll("%tps%", this.format.format(d)), (String)"anticheat.staff");
        }
        UpdateType[] arrupdateType = UpdateType.values();
        int n = arrupdateType.length;
        int n2 = 0;
        while (n2 < n) {
            UpdateType updateType = arrupdateType[n2];
            if (updateType != null && updateType.Elapsed()) {
                try {
                    UpdateEvent exception = new UpdateEvent(updateType);
                    Bukkit.getPluginManager().callEvent((Event)exception);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            ++n2;
        }
    }
}


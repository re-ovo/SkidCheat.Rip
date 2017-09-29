/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 */
package rip.anticheat.anticheat;

import org.bukkit.configuration.file.FileConfiguration;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.util.misc.Config;

public class Tracker {
    public static int bansToday;
    public static int bansOverall;

    public static void load() {
        bansToday = 0;
        bansOverall = AntiCheat.Instance.Config.getConfig().getInt("bans-overall", 0);
    }

    public static void save() {
        AntiCheat.Instance.Config.getConfig().set("bans-overall", (Object)bansOverall);
    }
}


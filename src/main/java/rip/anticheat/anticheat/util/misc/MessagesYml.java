/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.file.FileConfiguration
 */
package rip.anticheat.anticheat.util.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.util.formatting.ChatUtil;
import rip.anticheat.anticheat.util.misc.Config;

public class MessagesYml {
    Map<String, String> Messages;
    private AntiCheat Core;
    private Config MessagesYml;

    public MessagesYml(AntiCheat antiCheat) {
        this.Messages = new HashMap<String, String>(){
            private static final long serialVersionUID = -3715093512616717385L;
        };
        this.Core = antiCheat;
        this.load();
    }

    public void load() {
        this.MessagesYml = this.Core.getMainConfig();
        FileConfiguration fileConfiguration = this.MessagesYml.getConfig();
        for (String string : this.Messages.keySet()) {
            if (!fileConfiguration.contains("messages." + string)) {
                fileConfiguration.set("messages." + string, (Object)this.Messages.get(string));
            }
            this.Messages.put(string, fileConfiguration.getString("messages." + string));
        }
        this.MessagesYml.save();
    }

    public void save() {
        FileConfiguration fileConfiguration = this.MessagesYml.getConfig();
        for (String string : this.Messages.keySet()) {
            fileConfiguration.set("messages." + string, (Object)this.Messages.get(string));
        }
        this.MessagesYml.save();
    }

    public String getMessage(String string) {
        if (!this.Messages.containsKey(string)) {
            return String.valueOf(String.valueOf(ChatUtil.Red)) + "Invalid message! Report this to staff: " + string;
        }
        String string2 = ChatColor.translateAlternateColorCodes((char)'&', (String)this.Messages.get(string));
        string2 = string2.replace("\\n", "\n");
        return string2;
    }

}


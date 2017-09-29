/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.commands.old;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.util.formatting.ChatUtil;

public class LogsCommand
implements CommandExecutor {
    private AntiCheat Core;

    public LogsCommand(AntiCheat antiCheat) {
        this.Core = antiCheat;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (command.getName().equalsIgnoreCase("logs")) {
            if (!commandSender.hasPermission("anticheat.staff")) {
                commandSender.sendMessage(String.valueOf(ChatUtil.Red) + "No Permission dude");
                return true;
            }
            if (arrstring.length == 0) {
                commandSender.sendMessage(String.valueOf(ChatUtil.Red) + "Enter a valid name");
                return true;
            }
            if (arrstring[0].length() > 2) {
                Player player = Bukkit.getPlayer((String)arrstring[0]);
                AntiCheat.Instance.attemptToRead(player, arrstring[1]);
            }
        }
        return false;
    }
}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.commands.simple;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.UUIDFetcher;
import rip.anticheat.anticheat.util.formatting.ChatUtil;

public class CheatLogsCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Console cannot execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        if (!player.hasPermission("anticheat.cheatlogs")) {
            player.sendMessage(ChatUtil.colorize("&cNo permission."));
            return true;
        }
        if (arrstring.length == 0) {
            player.sendMessage(ChatUtil.colorize("&cUsage: /cheatlogs <uuid>"));
        }
        if (arrstring.length == 1) {
            try {
                String string2 = String.valueOf(UUIDFetcher.getUUIDOf(arrstring[0]));
                AntiCheat.Instance.attemptToRead(player, string2);
            }
            catch (Exception exception) {
                player.sendMessage(ChatUtil.colorize("&7[&cAnticheat&7] &7An error has occurred while attempting to grab an UUID from Mojang's API."));
            }
        } else {
            return true;
        }
        return false;
    }
}


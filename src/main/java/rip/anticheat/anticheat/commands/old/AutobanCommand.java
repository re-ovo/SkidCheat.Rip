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

import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.AlertType;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Autoban;
import rip.anticheat.anticheat.commands.old.AntiCheatCommand;
import rip.anticheat.anticheat.util.formatting.ChatUtil;
import rip.anticheat.anticheat.util.misc.MessagesYml;

public class AutobanCommand
implements CommandExecutor {
    private Autoban autoban;

    public AutobanCommand(Autoban autoban) {
        this.autoban = autoban;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!commandSender.hasPermission("anticheat.staff")) {
            commandSender.sendMessage(String.valueOf(String.valueOf(ChatUtil.Red)) + "No permission.");
            return true;
        }
        if (!AntiCheatCommand.autoban) {
            commandSender.sendMessage(ChatUtil.c("&cAutobans are currently disabled. To re-enable them, type /anticheat autobans enable."));
            return true;
        }
        if (arrstring.length < 2 || arrstring.length > 2) {
            commandSender.sendMessage(ChatUtil.c("&cUsage: /autoban <cancel,ban> <player>"));
            return true;
        }
        String string2 = arrstring[0];
        String string3 = arrstring[1];
        Player player = Bukkit.getServer().getPlayer(string3);
        if (player == null || !player.isOnline()) {
            commandSender.sendMessage(String.valueOf(String.valueOf(ChatUtil.Red)) + "This player does not exist.");
            return true;
        }
        if (this.autoban.getAutobanQueue().containsKey((Object)player)) {
            List<Player> list = this.autoban.Core.getPlayers(AlertType.NORMAL);
            String string4 = this.autoban.Core.getMessages().getMessage("alerts.bantimer.cancelled").replaceAll("%sender%", commandSender.getName()).replaceAll("%player%", player.getName());
            String string5 = this.autoban.Core.getMessages().getMessage("alerts.bantimer.forced").replaceAll("%sender%", commandSender.getName()).replaceAll("%player%", player.getName());
            if (arrstring[0].equalsIgnoreCase("cancel")) {
                this.autoban.cancelban(player);
                for (Player player2 : list) {
                    player2.sendMessage(string4);
                }
                return true;
            }
            if (arrstring[0].equalsIgnoreCase("ban")) {
                this.autoban.forceban(player);
                for (Player player3 : list) {
                    player3.sendMessage(string5);
                }
                return true;
            }
        }
        commandSender.sendMessage(String.valueOf(String.valueOf(ChatUtil.Red)) + "This player is not in the autoban queue!");
        return true;
    }
}


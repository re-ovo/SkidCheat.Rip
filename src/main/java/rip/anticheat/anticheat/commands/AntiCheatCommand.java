/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.commands;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.commands.CommandInterface;
import rip.anticheat.anticheat.commands.Permission;
import rip.anticheat.anticheat.commands.exception.CommandAlreadyRegisteredException;
import rip.anticheat.anticheat.util.formatting.ChatUtil;

public class AntiCheatCommand
implements CommandExecutor {
    private Map<String, CommandInterface> registeredSubCommands = new HashMap<String, CommandInterface>();

    public void registerSubCommand(String string, CommandInterface commandInterface) {
        if (!string.equals("") && commandInterface != null) {
            if (!this.registeredSubCommands.containsKey(string = string.toLowerCase())) {
                this.registeredSubCommands.put(string, commandInterface);
            } else {
                throw new CommandAlreadyRegisteredException();
            }
        }
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (commandSender instanceof Player) {
            CommandInterface commandInterface;
            Player player = (Player)commandSender;
            if (arrstring.length >= 1 && (commandInterface = this.registeredSubCommands.get(arrstring[0].toLowerCase())) != null) {
                Permission permission = commandInterface.getClass().getAnnotation(Permission.class);
                if (permission != null && !player.hasPermission(permission.permission())) {
                    player.sendMessage(ChatUtil.colorize("&cSorry, but you don't have permission for that command!"));
                    return false;
                }
                String[] arrstring2 = new String[arrstring.length - 1];
                System.arraycopy(arrstring, 1, arrstring2, 0, arrstring2.length);
                return commandInterface.onCommand(player, arrstring2);
            }
            return false;
        }
        commandSender.sendMessage(ChatUtil.colorize("&cSorry, only players can execute this command!"));
        return false;
    }
}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package rip.anticheat.anticheat.commands.old;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TrainCommand
implements CommandExecutor {
    public static Map<UUID, Integer> checking2;
    public static Map<UUID, Long> startchecking;

    public TrainCommand() {
        checking2 = new WeakHashMap<UUID, Integer>();
        startchecking = new WeakHashMap<UUID, Long>();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (command.getName().equalsIgnoreCase("train")) {
            if (!commandSender.hasPermission("anticheat.admin")) {
                commandSender.sendMessage((Object)ChatColor.RED + "No Permission");
                return true;
            }
            if (arrstring[0].length() < 2) {
                commandSender.sendMessage((Object)ChatColor.RED + "/train <player> <flux / pandora>");
                return true;
            }
            if (!arrstring[1].equalsIgnoreCase("flux") || !arrstring[1].equalsIgnoreCase("pandora")) {
                commandSender.sendMessage("/train <player> <flux / pandora>");
                return true;
            }
        }
        return false;
    }

    public void checkPlayer(Player player) {
        startchecking.put(player.getUniqueId(), 1);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        int n = 0;
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        if (startchecking.containsKey(player.getUniqueId())) {
            if (n == 0) {
                d = player.getLocation().getYaw();
                d2 = player.getLocation().getPitch();
                d3 = d;
                d4 = d2;
                n = 1;
            } else if (n == 1) {
                double d5 = player.getLocation().getYaw();
                double d6 = player.getLocation().getPitch();
                double d7 = Math.abs(d5 - d3);
                double d8 = Math.abs(d6 - d4);
                if (d7 < 0.3) {
                    checking2.put(player.getUniqueId(), 10);
                    n = 2;
                }
                if (d8 == d4) {
                    checking2.put(player.getUniqueId(), 10);
                    n = 2;
                }
            }
        }
    }
}


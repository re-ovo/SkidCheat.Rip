/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package rip.anticheat.anticheat.commands.simple;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import rip.anticheat.anticheat.ItemBuilder;
import rip.anticheat.anticheat.util.formatting.ChatUtil;

public class AnticheatGuiCommand
implements CommandExecutor,
Listener {
    public AnticheatGuiCommand(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    public void onAutoBanGUI(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)9, (String)((Object)ChatColor.RED + "Manage AutoBans"));
        inventory.setItem(0, new ItemBuilder(Material.BOOK, (Object)ChatColor.RED + "Enable AutoBans", 1, new String[0]).getItem());
        inventory.setItem(8, new ItemBuilder(Material.BOOK, (Object)ChatColor.RED + "Disable AutoBans", 1, new String[0]).getItem());
        player.openInventory(inventory);
    }

    public void onLicenseCheck(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)9, (String)((Object)ChatColor.RED + "Manage AutoBans"));
        inventory.setItem(4, new ItemBuilder(Material.BOOK, (Object)ChatColor.GREEN + "License Statues: Enabled", 1, new String[0]).getItem());
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Console cannot execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        if (!player.hasPermission("anticheat.staff")) {
            player.sendMessage(ChatUtil.colorize("&cNo permission."));
            return true;
        }
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)9, (String)((Object)ChatColor.RED + "Anticheat GUI"));
        inventory.setItem(0, new ItemBuilder(Material.BOOK, (Object)ChatColor.RED + "Manage Autobans", 1, new String[0]).getItem());
        inventory.setItem(4, new ItemBuilder(Material.BOOK, (Object)ChatColor.RED + "Manage Checks", 1, new String[0]).getItem());
        inventory.setItem(8, new ItemBuilder(Material.BOOK, (Object)ChatColor.RED + "Manage License", 1, new String[0]).getItem());
        player.openInventory(inventory);
        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (!inventoryClickEvent.getInventory().getName().equalsIgnoreCase((Object)ChatColor.RED + "Anticheat GUI")) {
            return;
        }
        if (!player.hasPermission("anticheat.staff")) {
            return;
        }
        switch (inventoryClickEvent.getSlot()) {
            case 0: {
                inventoryClickEvent.setCancelled(true);
                this.onAutoBanGUI(player);
                break;
            }
            case 4: {
                inventoryClickEvent.setCancelled(true);
                player.sendMessage((Object)ChatColor.RED + "Work in Progress");
                break;
            }
            case 8: {
                inventoryClickEvent.setCancelled(true);
                break;
            }
            default: {
                inventoryClickEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvBan(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (!inventoryClickEvent.getInventory().getName().equalsIgnoreCase((Object)ChatColor.RED + "Manage AutoBans")) {
            return;
        }
        if (!player.hasPermission("anticheat.staff")) {
            return;
        }
        switch (inventoryClickEvent.getSlot()) {
            case 0: {
                inventoryClickEvent.setCancelled(true);
                player.performCommand("/anticheat autobans enable");
                break;
            }
            case 8: {
                inventoryClickEvent.setCancelled(true);
                player.performCommand("/anticheat autobans disable");
                break;
            }
            default: {
                inventoryClickEvent.setCancelled(true);
            }
        }
    }
}


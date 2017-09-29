/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.ItemMeta$Spigot
 */
package rip.anticheat.anticheat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    private Material type;
    private String name;
    private String[] lore;
    private int amount;
    private short data;
    private boolean unbreakable;

    public /* varargs */ ItemBuilder(Material material, String string, String ... arrstring) {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = 1;
        this.data = 0;
    }

    public /* varargs */ ItemBuilder(Material material, String string, boolean bl, String ... arrstring) {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = 1;
        this.unbreakable = bl;
        this.data = 0;
    }

    public /* varargs */ ItemBuilder(Material material, String string, int n, String ... arrstring) {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = n;
        this.data = 0;
    }

    public /* varargs */ ItemBuilder(Material material, String string, int n, short s, String ... arrstring) {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = n;
        this.data = s;
    }

    public ItemBuilder(Material material, String string, int n, short s, List<String> list) {
        this.type = material;
        this.name = string;
        this.lore = list.toArray(new String[list.size()]);
        this.amount = n;
        this.data = s;
    }

    public /* varargs */ ItemBuilder(ItemStack itemStack, String ... arrstring) {
        this.type = itemStack.getType();
        this.name = itemStack.getItemMeta().getDisplayName();
        this.lore = arrstring;
        this.amount = itemStack.getAmount();
        this.data = itemStack.getDurability();
    }

    public ItemStack getItem() {
        String string;
        ArrayList<String> arrayList = new ArrayList<String>();
        String[] arrstring = this.lore;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            string = arrstring[n2];
            arrayList.add(ChatColor.translateAlternateColorCodes((char)'&', (String)string));
            ++n2;
        }
        string = new ItemStack(this.type, this.amount, this.data);
        ItemMeta itemMeta = string.getItemMeta();
        itemMeta.setDisplayName(this.name);
        itemMeta.setLore(arrayList);
        itemMeta.spigot().setUnbreakable(this.unbreakable);
        string.setItemMeta(itemMeta);
        return string;
    }
}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.material.MaterialData
 *  org.bukkit.material.Openable
 */
package rip.anticheat.anticheat.checks.movement;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.Config;

public class HClip
extends Check {
    public static List<Material> blocked = new ArrayList<Material>();
    public boolean cancel = false;

    static {
        blocked.add(Material.ACTIVATOR_RAIL);
        blocked.add(Material.AIR);
        blocked.add(Material.ANVIL);
        blocked.add(Material.BED_BLOCK);
        blocked.add(Material.POTATO);
        blocked.add(Material.POTATO_ITEM);
        blocked.add(Material.CARROT);
        blocked.add(Material.CARROT_ITEM);
        blocked.add(Material.BIRCH_WOOD_STAIRS);
        blocked.add(Material.BREWING_STAND);
        blocked.add(Material.BOAT);
        blocked.add(Material.BRICK_STAIRS);
        blocked.add(Material.BROWN_MUSHROOM);
        blocked.add(Material.CAKE_BLOCK);
        blocked.add(Material.CARPET);
        blocked.add(Material.CAULDRON);
        blocked.add(Material.COBBLESTONE_STAIRS);
        blocked.add(Material.COBBLE_WALL);
        blocked.add(Material.DARK_OAK_STAIRS);
        blocked.add(Material.DIODE);
        blocked.add(Material.DIODE_BLOCK_ON);
        blocked.add(Material.DIODE_BLOCK_OFF);
        blocked.add(Material.DEAD_BUSH);
        blocked.add(Material.DETECTOR_RAIL);
        blocked.add(Material.DOUBLE_PLANT);
        blocked.add(Material.DOUBLE_STEP);
        blocked.add(Material.DRAGON_EGG);
        blocked.add(Material.PAINTING);
        blocked.add(Material.FLOWER_POT);
        blocked.add(Material.GOLD_PLATE);
        blocked.add(Material.HOPPER);
        blocked.add(Material.STONE_PLATE);
        blocked.add(Material.IRON_PLATE);
        blocked.add(Material.HUGE_MUSHROOM_1);
        blocked.add(Material.HUGE_MUSHROOM_2);
        blocked.add(Material.IRON_DOOR_BLOCK);
        blocked.add(Material.IRON_DOOR);
        blocked.add(Material.FENCE);
        blocked.add(Material.IRON_FENCE);
        blocked.add(Material.IRON_PLATE);
        blocked.add(Material.ITEM_FRAME);
        blocked.add(Material.JUKEBOX);
        blocked.add(Material.JUNGLE_WOOD_STAIRS);
        blocked.add(Material.LADDER);
        blocked.add(Material.LEVER);
        blocked.add(Material.LONG_GRASS);
        blocked.add(Material.NETHER_FENCE);
        blocked.add(Material.NETHER_STALK);
        blocked.add(Material.NETHER_WARTS);
        blocked.add(Material.MELON_STEM);
        blocked.add(Material.PUMPKIN_STEM);
        blocked.add(Material.QUARTZ_STAIRS);
        blocked.add(Material.RAILS);
        blocked.add(Material.RED_MUSHROOM);
        blocked.add(Material.RED_ROSE);
        blocked.add(Material.SAPLING);
        blocked.add(Material.SEEDS);
        blocked.add(Material.SIGN);
        blocked.add(Material.SIGN_POST);
        blocked.add(Material.SKULL);
        blocked.add(Material.SMOOTH_STAIRS);
        blocked.add(Material.NETHER_BRICK_STAIRS);
        blocked.add(Material.SPRUCE_WOOD_STAIRS);
        blocked.add(Material.STAINED_GLASS_PANE);
        blocked.add(Material.REDSTONE_COMPARATOR);
        blocked.add(Material.REDSTONE_COMPARATOR_OFF);
        blocked.add(Material.REDSTONE_COMPARATOR_ON);
        blocked.add(Material.REDSTONE_LAMP_OFF);
        blocked.add(Material.REDSTONE_LAMP_ON);
        blocked.add(Material.REDSTONE_TORCH_OFF);
        blocked.add(Material.REDSTONE_TORCH_ON);
        blocked.add(Material.REDSTONE_WIRE);
        blocked.add(Material.SANDSTONE_STAIRS);
        blocked.add(Material.STEP);
        blocked.add(Material.ACACIA_STAIRS);
        blocked.add(Material.SUGAR_CANE);
        blocked.add(Material.SUGAR_CANE_BLOCK);
        blocked.add(Material.ENCHANTMENT_TABLE);
        blocked.add(Material.SOUL_SAND);
        blocked.add(Material.TORCH);
        blocked.add(Material.TRAP_DOOR);
        blocked.add(Material.TRIPWIRE);
        blocked.add(Material.TRIPWIRE_HOOK);
        blocked.add(Material.WALL_SIGN);
        blocked.add(Material.VINE);
        blocked.add(Material.WATER_LILY);
        blocked.add(Material.WEB);
        blocked.add(Material.WOOD_DOOR);
        blocked.add(Material.WOOD_DOUBLE_STEP);
        blocked.add(Material.WOOD_PLATE);
        blocked.add(Material.WOOD_STAIRS);
        blocked.add(Material.WOOD_STEP);
        blocked.add(Material.HOPPER);
        blocked.add(Material.WOODEN_DOOR);
        blocked.add(Material.YELLOW_FLOWER);
        blocked.add(Material.LAVA);
        blocked.add(Material.WATER);
        blocked.add(Material.STATIONARY_WATER);
        blocked.add(Material.STATIONARY_LAVA);
        blocked.add(Material.CACTUS);
        blocked.add(Material.CHEST);
        blocked.add(Material.PISTON_BASE);
        blocked.add(Material.PISTON_MOVING_PIECE);
        blocked.add(Material.PISTON_EXTENSION);
        blocked.add(Material.PISTON_STICKY_BASE);
        blocked.add(Material.TRAPPED_CHEST);
        blocked.add(Material.SNOW);
        blocked.add(Material.ENDER_CHEST);
        blocked.add(Material.THIN_GLASS);
        blocked.add(Material.ENDER_PORTAL_FRAME);
    }

    public HClip(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "HClip", "HClip", 0, 50, 2, 0);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=1)
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        double d;
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (!this.isEnabled()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.isInsideVehicle()) {
            return;
        }
        if (player.getLocation().getY() < 0.0 || player.getLocation().getY() > (double)player.getWorld().getMaxHeight()) {
            return;
        }
        if (playerStats.getLastMountDiff() < 500) {
            return;
        }
        Location location = playerMoveEvent.getTo().clone();
        Location location2 = playerMoveEvent.getFrom().clone();
        double d2 = location.getX() - location2.getX();
        if (d2 < -100.0 || d2 > 100.0) {
            return;
        }
        int n = 0;
        if (d2 < -1.0 || d2 > 1.0) {
            int n2 = (int)Math.ceil(Math.abs(d2));
            int n3 = 0;
            while (n3 < n2) {
                Location location3;
                Openable openable;
                Location location4 = location3 = d2 < -0.0 ? location2.clone().add((double)(- n3), 0.0, 0.0) : location2.clone().add((double)n3, 0.0, 0.0);
                if (!(location3.getBlock() == null || !location3.getBlock().getType().isSolid() || !location3.getBlock().getType().isBlock() || location3.getBlock().getType() == Material.AIR || blocked.contains((Object)location3.getBlock().getType()) || location3.getBlock().getState().getData() instanceof Openable && (openable = (Openable)location3.getBlock().getState().getData()).isOpen())) {
                    ++n;
                }
                ++n3;
            }
        }
        if ((d = location.getZ() - location2.getZ()) < -100.0 || d > 100.0) {
            return;
        }
        if (d < -1.0 || d > 1.0) {
            int n4 = (int)Math.ceil(Math.abs(d));
            int n5 = 0;
            while (n5 < n4) {
                Openable openable;
                Location location5;
                Location location6 = location5 = d < -0.0 ? location2.clone().add(0.0, 0.0, (double)(- n5)) : location2.clone().add(0.0, 0.0, (double)n5);
                if (!(location5.getBlock() == null || !location5.getBlock().getType().isSolid() || !location5.getBlock().getType().isBlock() || location5.getBlock().getType() == Material.AIR || blocked.contains((Object)location5.getBlock().getType()) || location5.getBlock().getState().getData() instanceof Openable && (openable = (Openable)location5.getBlock().getState().getData()).isOpen())) {
                    ++n;
                }
                ++n5;
            }
        }
        int n6 = playerStats.getCheck(this, 0);
        int n7 = this.getThreshold();
        if (n > 0) {
            ++n6;
            if (this.cancel) {
                playerMoveEvent.setTo(playerMoveEvent.getFrom());
            }
        } else {
            --n6;
        }
        if (n6 > n7) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(n)));
            n6 = 0;
        }
        playerStats.setCheck(this, 0, n6);
    }

    @Override
    public void save(Config config) {
        super.save(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".cancel", (Object)this.cancel);
        config.save();
    }

    @Override
    public void load(Config config) {
        super.load(config);
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".cancel")) {
            this.cancel = config.getConfig().getBoolean(String.valueOf(String.valueOf(string)) + ".cancel");
        }
    }
}


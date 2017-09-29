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

import java.text.DecimalFormat;
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
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.Config;

public class VClip
extends Check {
    private static List<Material> blocked = new ArrayList<Material>();
    private boolean cancel = false;

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
        blocked.add(Material.FENCE_GATE);
        blocked.add(Material.FENCE);
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

    public VClip(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "VClip", "VClip", 0, 50, 2, 0);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=1)
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        int n;
        int n2;
        Player player = playerMoveEvent.getPlayer();
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
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (playerStats.getLastMountDiff() < 500) {
            return;
        }
        Location location = playerMoveEvent.getTo().clone();
        Location location2 = playerMoveEvent.getFrom().clone();
        double d = location.getY() - location2.getY();
        if (d < -100.0 || d > 100.0) {
            return;
        }
        int n3 = 0;
        if (d < -1.0 || d > 1.0) {
            n2 = (int)Math.ceil(Math.abs(d));
            n = 0;
            while (n < n2) {
                Openable openable;
                Location location3;
                Location location4 = location3 = d < -0.0 ? location2.clone().add(0.0, (double)(- n), 0.0) : location2.clone().add(0.0, (double)n, 0.0);
                if (!(location3.getBlock() == null || !location3.getBlock().getType().isSolid() || !location3.getBlock().getType().isBlock() || location3.getBlock().getType() == Material.AIR || blocked.contains((Object)location3.getBlock().getType()) || location3.getBlock().getState().getData() instanceof Openable && (openable = (Openable)location3.getBlock().getState().getData()).isOpen())) {
                    ++n3;
                }
                ++n;
            }
        }
        n2 = playerStats.getCheck(this, 0);
        n = this.getThreshold();
        if (n3 > 0) {
            ++n2;
            if (this.cancel) {
                playerMoveEvent.setTo(playerMoveEvent.getFrom());
            }
        } else {
            --n2;
        }
        if (n2 > n) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(n3)));
            n2 = 0;
        }
        playerStats.setCheck(this, 0, n2);
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


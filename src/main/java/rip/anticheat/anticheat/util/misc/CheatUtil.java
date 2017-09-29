/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.util.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.util.math.BlockUtil;

public final class CheatUtil {
    private static final List<Material> INSTANT_BREAK;
    private static final List<Material> FOOD;
    private static final List<Material> INTERACTABLE;
    private static final Map<Material, Material> COMBO;
    public static final String SPY_METADATA;
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$Material;

    static {
        SPY_METADATA = "ac-spydata";
        INSTANT_BREAK = new ArrayList<Material>();
        FOOD = new ArrayList<Material>();
        INTERACTABLE = new ArrayList<Material>();
        COMBO = new HashMap<Material, Material>();
        INSTANT_BREAK.add(Material.RED_MUSHROOM);
        INSTANT_BREAK.add(Material.RED_ROSE);
        INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        INSTANT_BREAK.add(Material.REDSTONE);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        INSTANT_BREAK.add(Material.LONG_GRASS);
        INSTANT_BREAK.add(Material.PAINTING);
        INSTANT_BREAK.add(Material.WHEAT);
        INSTANT_BREAK.add(Material.SUGAR_CANE);
        INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        INSTANT_BREAK.add(Material.DIODE);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        INSTANT_BREAK.add(Material.SAPLING);
        INSTANT_BREAK.add(Material.TORCH);
        INSTANT_BREAK.add(Material.CROPS);
        INSTANT_BREAK.add(Material.SNOW);
        INSTANT_BREAK.add(Material.TNT);
        INSTANT_BREAK.add(Material.POTATO);
        INSTANT_BREAK.add(Material.CARROT);
        INTERACTABLE.add(Material.STONE_BUTTON);
        INTERACTABLE.add(Material.LEVER);
        INTERACTABLE.add(Material.CHEST);
        FOOD.add(Material.COOKED_BEEF);
        FOOD.add(Material.COOKED_CHICKEN);
        FOOD.add(Material.COOKED_FISH);
        FOOD.add(Material.GRILLED_PORK);
        FOOD.add(Material.PORK);
        FOOD.add(Material.MUSHROOM_SOUP);
        FOOD.add(Material.RAW_BEEF);
        FOOD.add(Material.RAW_CHICKEN);
        FOOD.add(Material.RAW_FISH);
        FOOD.add(Material.APPLE);
        FOOD.add(Material.GOLDEN_APPLE);
        FOOD.add(Material.MELON);
        FOOD.add(Material.COOKIE);
        FOOD.add(Material.BREAD);
        FOOD.add(Material.SPIDER_EYE);
        FOOD.add(Material.ROTTEN_FLESH);
        FOOD.add(Material.POTATO_ITEM);
        COMBO.put(Material.SHEARS, Material.WOOL);
        COMBO.put(Material.IRON_SWORD, Material.WEB);
        COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        COMBO.put(Material.STONE_SWORD, Material.WEB);
        COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }

    public static boolean isSafeSetbackLocation(Player player) {
        if ((CheatUtil.isInWeb(player) || CheatUtil.isInWater(player) || !CheatUtil.cantStandAtSingle(player.getLocation().getBlock())) && !player.getEyeLocation().getBlock().getType().isSolid()) {
            return true;
        }
        return false;
    }

    public static double getXDelta(Location location, Location location2) {
        return Math.abs(location.getX() - location2.getX());
    }

    public static double getZDelta(Location location, Location location2) {
        return Math.abs(location.getZ() - location2.getZ());
    }

    public static double getDistance3D(Location location, Location location2) {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d4 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d5 = Math.sqrt(d2 + d3 + d4);
        d = Math.abs(d5);
        return d;
    }

    public static double getVerticalDistance(Location location, Location location2) {
        double d = 0.0;
        double d2 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d3 = Math.sqrt(d2);
        d = Math.abs(d3);
        return d;
    }

    public static double getHorizontalDistance(Location location, Location location2) {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d4 = Math.sqrt(d2 + d3);
        d = Math.abs(d4);
        return d;
    }

    public static boolean cantStandAtBetter(Block block) {
        boolean bl;
        Block block2 = block.getRelative(BlockFace.DOWN);
        boolean bl2 = block2.getType() == Material.AIR;
        boolean bl3 = block2.getRelative(BlockFace.NORTH).getType() == Material.AIR;
        boolean bl4 = block2.getRelative(BlockFace.EAST).getType() == Material.AIR;
        boolean bl5 = block2.getRelative(BlockFace.SOUTH).getType() == Material.AIR;
        boolean bl6 = block2.getRelative(BlockFace.WEST).getType() == Material.AIR;
        boolean bl7 = block2.getRelative(BlockFace.NORTH_EAST).getType() == Material.AIR;
        boolean bl8 = block2.getRelative(BlockFace.NORTH_WEST).getType() == Material.AIR;
        boolean bl9 = block2.getRelative(BlockFace.SOUTH_EAST).getType() == Material.AIR;
        boolean bl10 = block2.getRelative(BlockFace.SOUTH_WEST).getType() == Material.AIR;
        boolean bl11 = bl = block2.getRelative(BlockFace.DOWN).getType() == Material.AIR || block2.getRelative(BlockFace.DOWN).getType() == Material.WATER || block2.getRelative(BlockFace.DOWN).getType() == Material.LAVA;
        if (bl2 && bl3 && bl4 && bl5 && bl6 && bl7 && bl9 && bl8 && bl10 && bl) {
            return true;
        }
        return false;
    }

    public static boolean cantStandAtSingle(Block block) {
        Block block2 = block.getRelative(BlockFace.DOWN);
        boolean bl = block2.getType() == Material.AIR;
        return bl;
    }

    public static boolean cantStandAtWater(Block block) {
        boolean bl;
        Block block2 = block.getRelative(BlockFace.DOWN);
        boolean bl2 = block.getType() == Material.AIR;
        boolean bl3 = block2.getRelative(BlockFace.NORTH).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean bl4 = block2.getRelative(BlockFace.SOUTH).getType() == Material.WATER || block2.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        boolean bl5 = block2.getRelative(BlockFace.EAST).getType() == Material.WATER || block2.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        boolean bl6 = block2.getRelative(BlockFace.WEST).getType() == Material.WATER || block2.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        boolean bl7 = block2.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        boolean bl8 = block2.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        boolean bl9 = block2.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean bl10 = bl = block2.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || block2.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;
        if (bl3 && bl4 && bl5 && bl6 && bl7 && bl8 && bl9 && bl && bl2) {
            return true;
        }
        return false;
    }

    public static boolean canStandWithin(Block block) {
        boolean bl;
        boolean bl2 = block.getType() == Material.SAND;
        boolean bl3 = block.getType() == Material.GRAVEL;
        boolean bl4 = bl = block.getType().isSolid() && !block.getType().name().toLowerCase().contains("door") && !block.getType().name().toLowerCase().contains("fence") && !block.getType().name().toLowerCase().contains("bars") && !block.getType().name().toLowerCase().contains("sign");
        if (!(bl2 || bl3 || bl)) {
            return true;
        }
        return false;
    }

    public static Vector getRotation(Location location, Location location2) {
        double d = location2.getX() - location.getX();
        double d2 = location2.getY() - location.getY();
        double d3 = location2.getZ() - location.getZ();
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(Math.atan2(d3, d) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)(- Math.atan2(d2, d4) * 180.0 / 3.141592653589793);
        return new Vector(f, f2, 0.0f);
    }

    public static double clamp180(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }

    public static int getLevelForEnchantment(Player player, String string) {
        try {
            Enchantment enchantment = Enchantment.getByName((String)string);
            ItemStack[] arritemStack = player.getInventory().getArmorContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack itemStack = arritemStack[n2];
                if (itemStack.containsEnchantment(enchantment)) {
                    return itemStack.getEnchantmentLevel(enchantment);
                }
                ++n2;
            }
        }
        catch (Exception exception) {
            return -1;
        }
        return -1;
    }

    public static boolean cantStandAt(Block block) {
        if (!CheatUtil.canStand(block) && CheatUtil.cantStandClose(block) && CheatUtil.cantStandFar(block)) {
            return true;
        }
        return false;
    }

    public static boolean cantStandAtExp(Location location) {
        return CheatUtil.cantStandAt(new Location(location.getWorld(), CheatUtil.fixXAxis(location.getX()), location.getY() - 0.01, (double)location.getBlockZ()).getBlock());
    }

    public static boolean cantStandClose(Block block) {
        if (!(CheatUtil.canStand(block.getRelative(BlockFace.NORTH)) || CheatUtil.canStand(block.getRelative(BlockFace.EAST)) || CheatUtil.canStand(block.getRelative(BlockFace.SOUTH)) || CheatUtil.canStand(block.getRelative(BlockFace.WEST)))) {
            return true;
        }
        return false;
    }

    public static boolean cantStandFar(Block block) {
        if (!(CheatUtil.canStand(block.getRelative(BlockFace.NORTH_WEST)) || CheatUtil.canStand(block.getRelative(BlockFace.NORTH_EAST)) || CheatUtil.canStand(block.getRelative(BlockFace.SOUTH_WEST)) || CheatUtil.canStand(block.getRelative(BlockFace.SOUTH_EAST)))) {
            return true;
        }
        return false;
    }

    public static boolean canStand(Block block) {
        if (!block.isLiquid() && block.getType() != Material.AIR) {
            return true;
        }
        return false;
    }

    public static boolean isFullyInWater(Location location) {
        double d = CheatUtil.fixXAxis(location.getX());
        if (new Location(location.getWorld(), d, location.getY(), (double)location.getBlockZ()).getBlock().isLiquid() && new Location(location.getWorld(), d, (double)Math.round(location.getY()), (double)location.getBlockZ()).getBlock().isLiquid()) {
            return true;
        }
        return false;
    }

    public static double fixXAxis(double d) {
        double d2 = d;
        double d3 = d2 - (double)Math.round(d2) + 0.01;
        if (d3 < 0.3) {
            d2 = NumberConversions.floor((double)d) - 1;
        }
        return d2;
    }

    public static boolean isHoveringOverWater(Location location, int n) {
        int n2 = location.getBlockY();
        while (n2 > location.getBlockY() - n) {
            Block block = new Location(location.getWorld(), (double)location.getBlockX(), (double)n2, (double)location.getBlockZ()).getBlock();
            if (block.getType() != Material.AIR) {
                return block.isLiquid();
            }
            --n2;
        }
        return false;
    }

    public static boolean isHoveringOverWater(Location location) {
        return CheatUtil.isHoveringOverWater(location, 25);
    }

    public static boolean isInstantBreak(Material material) {
        return INSTANT_BREAK.contains((Object)material);
    }

    public static boolean isFood(Material material) {
        return FOOD.contains((Object)material);
    }

    public static boolean isSlab(Block block) {
        Material material = block.getType();
        switch (CheatUtil.$SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 24: 
            case 221: 
            case 248: 
            case 278: {
                return true;
            }
        }
        return false;
    }

    public static boolean isStair(Block block) {
        Material material = block.getType();
        switch (CheatUtil.$SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
            case 28: 
            case 65: 
            case 77: 
            case 99: 
            case 101: 
            case 127: 
            case 166: 
            case 201: 
            case 334: {
                return true;
            }
        }
        return false;
    }

    public static boolean isInteractable(Material material) {
        return INTERACTABLE.contains((Object)material);
    }

    public static boolean sprintFly(Player player) {
        if (!player.isSprinting() && !player.isFlying()) {
            return false;
        }
        return true;
    }

    public static boolean isOnLilyPad(Player player) {
        Block block = player.getLocation().getBlock();
        Material material = Material.WATER_LILY;
        if (block.getType() != material && block.getRelative(BlockFace.NORTH).getType() != material && block.getRelative(BlockFace.SOUTH).getType() != material && block.getRelative(BlockFace.EAST).getType() != material && block.getRelative(BlockFace.WEST).getType() != material) {
            return false;
        }
        return true;
    }

    public static boolean isSubmersed(Player player) {
        if (player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) {
            return true;
        }
        return false;
    }

    public static boolean isInWater(Player player) {
        if (!(player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid())) {
            return false;
        }
        return true;
    }

    public static boolean isInWeb(Player player) {
        if (player.getLocation().getBlock().getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.UP).getType() != Material.WEB) {
            return false;
        }
        return true;
    }

    public static boolean isClimbableBlock(Block block) {
        if (block.getType() != Material.VINE && block.getType() != Material.LADDER && block.getType() != Material.WATER && block.getType() != Material.STATIONARY_WATER) {
            return false;
        }
        return true;
    }

    public static boolean isOnVine(Player player) {
        if (player.getLocation().getBlock().getType() == Material.VINE) {
            return true;
        }
        return false;
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean blocksNear(Player player) {
        return CheatUtil.blocksNear(player.getLocation());
    }

    public static boolean blocksNear(Location location) {
        boolean bl = false;
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), true)) {
            if (block.getType() == Material.AIR) continue;
            bl = true;
            break;
        }
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), false)) {
            if (block.getType() == Material.AIR) continue;
            bl = true;
            break;
        }
        location.setY(location.getY() - 0.5);
        if (location.getBlock().getType() != Material.AIR) {
            bl = true;
        }
        if (CheatUtil.isBlock(location.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER})) {
            bl = true;
        }
        return bl;
    }

    public static boolean slabsNear(Location location) {
        boolean bl = false;
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), true)) {
            if (!block.getType().equals((Object)Material.STEP) && !block.getType().equals((Object)Material.DOUBLE_STEP) && !block.getType().equals((Object)Material.WOOD_DOUBLE_STEP) && !block.getType().equals((Object)Material.WOOD_STEP)) continue;
            bl = true;
            break;
        }
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), false)) {
            if (!block.getType().equals((Object)Material.STEP) && !block.getType().equals((Object)Material.DOUBLE_STEP) && !block.getType().equals((Object)Material.WOOD_DOUBLE_STEP) && !block.getType().equals((Object)Material.WOOD_STEP)) continue;
            bl = true;
            break;
        }
        if (CheatUtil.isBlock(location.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP})) {
            bl = true;
        }
        return bl;
    }

    public static boolean isBlock(Block block, Material[] arrmaterial) {
        Material material = block.getType();
        Material[] arrmaterial2 = arrmaterial;
        int n = arrmaterial2.length;
        int n2 = 0;
        while (n2 < n) {
            Material material2 = arrmaterial2[n2];
            if (material2 == material) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static String[] getCommands(String string) {
        return string.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }

    public static String removeWhitespace(String string) {
        return string.replaceAll(" ", "");
    }

    public static boolean hasArmorEnchantment(Player player, Enchantment enchantment) {
        ItemStack[] arritemStack = player.getInventory().getArmorContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemStack = arritemStack[n2];
            if (itemStack != null && itemStack.containsEnchantment(enchantment)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static String listToCommaString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < list.size()) {
            stringBuilder.append(list.get(n));
            if (n < list.size() - 1) {
                stringBuilder.append(",");
            }
            ++n;
        }
        return stringBuilder.toString();
    }

    public static long lifeToSeconds(String string) {
        if (string.equals("0") || string.equals("")) {
            return 0;
        }
        String[] arrstring = new String[]{"d", "h", "m", "s"};
        int[] arrn = new int[]{86400, 3600, 60, 1};
        long l = 0;
        int n = 0;
        while (n < arrstring.length) {
            Matcher matcher = Pattern.compile("([0-9]*)" + arrstring[n]).matcher(string);
            while (matcher.find()) {
                l += (long)(Integer.parseInt(matcher.group(1)) * arrn[n]);
            }
            ++n;
        }
        return l;
    }

    public static double[] cursor(Player player, LivingEntity livingEntity) {
        Location location = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location2, location);
        double d = CheatUtil.clamp180(vector.getX() - vector2.getX());
        double d2 = CheatUtil.clamp180(vector.getY() - vector2.getY());
        double d3 = CheatUtil.getHorizontalDistance(location2, location);
        double d4 = CheatUtil.getDistance3D(location2, location);
        double d5 = d * d3 * d4;
        double d6 = d2 * Math.abs(Math.sqrt(location.getY() - location2.getY())) * d4;
        return new double[]{Math.abs(d5), Math.abs(d6)};
    }

    public static double getAimbotoffset(Location location, double d, LivingEntity livingEntity) {
        Location location2 = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location3 = location.add(0.0, d, 0.0);
        Vector vector = new Vector(location3.getYaw(), location3.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location3, location2);
        double d2 = CheatUtil.clamp180(vector.getX() - vector2.getX());
        double d3 = CheatUtil.getHorizontalDistance(location3, location2);
        double d4 = CheatUtil.getDistance3D(location3, location2);
        double d5 = d2 * d3 * d4;
        return d5;
    }

    public static double getAimbotoffset2(Location location, double d, LivingEntity livingEntity) {
        Location location2 = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location3 = location.add(0.0, d, 0.0);
        Vector vector = new Vector(location3.getYaw(), location3.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location3, location2);
        double d2 = CheatUtil.clamp180(vector.getY() - vector2.getY());
        double d3 = CheatUtil.getDistance3D(location3, location2);
        double d4 = d2 * Math.abs(Math.sqrt(location2.getY() - location3.getY())) * d3;
        return d4;
    }

    public static double[] getOffsetsOffCursor(Player player, LivingEntity livingEntity) {
        Location location = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location2, location);
        double d = CheatUtil.clamp180(vector.getX() - vector2.getX());
        double d2 = CheatUtil.clamp180(vector.getY() - vector2.getY());
        double d3 = CheatUtil.getHorizontalDistance(location2, location);
        double d4 = CheatUtil.getDistance3D(location2, location);
        double d5 = d * d3 * d4;
        double d6 = d2 * Math.abs(Math.sqrt(location.getY() - location2.getY())) * d4;
        return new double[]{Math.abs(d5), Math.abs(d6)};
    }

    public static double getOffsetOffCursor(Player player, LivingEntity livingEntity) {
        double d = 0.0;
        double[] arrd = CheatUtil.getOffsetsOffCursor(player, livingEntity);
        d += arrd[0];
        return d += arrd[1];
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$Material() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$Material;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Material.values().length];
        try {
            arrn[Material.ACACIA_STAIRS.ordinal()] = 165;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ACTIVATOR_RAIL.ordinal()] = 159;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.AIR.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ANVIL.ordinal()] = 147;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.APPLE.ordinal()] = 177;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ARROW.ordinal()] = 179;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BAKED_POTATO.ordinal()] = 310;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BEACON.ordinal()] = 140;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BED.ordinal()] = 272;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BEDROCK.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BED_BLOCK.ordinal()] = 27;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BIRCH_WOOD_STAIRS.ordinal()] = 137;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BLAZE_POWDER.ordinal()] = 294;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BLAZE_ROD.ordinal()] = 286;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BOAT.ordinal()] = 250;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BONE.ordinal()] = 269;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BOOK.ordinal()] = 257;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BOOKSHELF.ordinal()] = 48;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BOOK_AND_QUILL.ordinal()] = 303;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BOW.ordinal()] = 178;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BOWL.ordinal()] = 198;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BREAD.ordinal()] = 214;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BREWING_STAND.ordinal()] = 119;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BREWING_STAND_ITEM.ordinal()] = 296;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BRICK.ordinal()] = 46;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BRICK_STAIRS.ordinal()] = 110;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BROWN_MUSHROOM.ordinal()] = 40;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BUCKET.ordinal()] = 242;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.BURNING_FURNACE.ordinal()] = 63;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CACTUS.ordinal()] = 82;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CAKE.ordinal()] = 271;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CAKE_BLOCK.ordinal()] = 93;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CARPET.ordinal()] = 168;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CARROT.ordinal()] = 143;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CARROT_ITEM.ordinal()] = 308;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CARROT_STICK.ordinal()] = 315;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CAULDRON.ordinal()] = 120;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CAULDRON_ITEM.ordinal()] = 297;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CHAINMAIL_BOOTS.ordinal()] = 222;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CHAINMAIL_CHESTPLATE.ordinal()] = 220;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CHAINMAIL_HELMET.ordinal()] = 219;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CHAINMAIL_LEGGINGS.ordinal()] = 221;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CHEST.ordinal()] = 55;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CLAY.ordinal()] = 83;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CLAY_BALL.ordinal()] = 254;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CLAY_BRICK.ordinal()] = 253;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COAL.ordinal()] = 180;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COAL_BLOCK.ordinal()] = 170;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COAL_ORE.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COBBLESTONE.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COBBLESTONE_STAIRS.ordinal()] = 68;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COBBLE_WALL.ordinal()] = 141;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COCOA.ordinal()] = 129;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COMMAND.ordinal()] = 139;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COMMAND_MINECART.ordinal()] = 331;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COMPASS.ordinal()] = 262;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COOKED_BEEF.ordinal()] = 281;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COOKED_CHICKEN.ordinal()] = 283;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COOKED_FISH.ordinal()] = 267;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.COOKIE.ordinal()] = 274;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.CROPS.ordinal()] = 60;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DARK_OAK_STAIRS.ordinal()] = 166;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DAYLIGHT_DETECTOR.ordinal()] = 153;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DEAD_BUSH.ordinal()] = 33;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DETECTOR_RAIL.ordinal()] = 29;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND.ordinal()] = 181;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_AXE.ordinal()] = 196;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_BARDING.ordinal()] = 328;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_BLOCK.ordinal()] = 58;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_BOOTS.ordinal()] = 230;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_CHESTPLATE.ordinal()] = 228;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_HELMET.ordinal()] = 227;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_HOE.ordinal()] = 210;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_LEGGINGS.ordinal()] = 229;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_ORE.ordinal()] = 57;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_PICKAXE.ordinal()] = 195;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_SPADE.ordinal()] = 194;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIAMOND_SWORD.ordinal()] = 193;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIODE.ordinal()] = 273;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIODE_BLOCK_OFF.ordinal()] = 94;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIODE_BLOCK_ON.ordinal()] = 95;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DIRT.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DISPENSER.ordinal()] = 24;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DOUBLE_PLANT.ordinal()] = 172;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DOUBLE_STEP.ordinal()] = 44;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DRAGON_EGG.ordinal()] = 124;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.DROPPER.ordinal()] = 160;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EGG.ordinal()] = 261;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EMERALD.ordinal()] = 305;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EMERALD_BLOCK.ordinal()] = 135;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EMERALD_ORE.ordinal()] = 131;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EMPTY_MAP.ordinal()] = 312;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENCHANTED_BOOK.ordinal()] = 320;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENCHANTMENT_TABLE.ordinal()] = 118;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENDER_CHEST.ordinal()] = 132;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENDER_PEARL.ordinal()] = 285;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENDER_PORTAL.ordinal()] = 121;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENDER_PORTAL_FRAME.ordinal()] = 122;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ENDER_STONE.ordinal()] = 123;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EXPLOSIVE_MINECART.ordinal()] = 324;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EXP_BOTTLE.ordinal()] = 301;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.EYE_OF_ENDER.ordinal()] = 298;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FEATHER.ordinal()] = 205;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FENCE.ordinal()] = 86;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FENCE_GATE.ordinal()] = 109;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FERMENTED_SPIDER_EYE.ordinal()] = 293;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FIRE.ordinal()] = 52;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FIREBALL.ordinal()] = 302;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FIREWORK.ordinal()] = 318;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FIREWORK_CHARGE.ordinal()] = 319;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FISHING_ROD.ordinal()] = 263;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FLINT.ordinal()] = 235;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FLINT_AND_STEEL.ordinal()] = 176;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FLOWER_POT.ordinal()] = 142;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FLOWER_POT_ITEM.ordinal()] = 307;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.FURNACE.ordinal()] = 62;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GHAST_TEAR.ordinal()] = 287;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GLASS.ordinal()] = 21;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GLASS_BOTTLE.ordinal()] = 291;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GLOWING_REDSTONE_ORE.ordinal()] = 75;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GLOWSTONE.ordinal()] = 90;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GLOWSTONE_DUST.ordinal()] = 265;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLDEN_APPLE.ordinal()] = 239;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLDEN_CARROT.ordinal()] = 313;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_AXE.ordinal()] = 203;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_BARDING.ordinal()] = 327;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_BLOCK.ordinal()] = 42;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_BOOTS.ordinal()] = 234;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_CHESTPLATE.ordinal()] = 232;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_HELMET.ordinal()] = 231;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_HOE.ordinal()] = 211;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_INGOT.ordinal()] = 183;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_LEGGINGS.ordinal()] = 233;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_NUGGET.ordinal()] = 288;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_ORE.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_PICKAXE.ordinal()] = 202;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_PLATE.ordinal()] = 149;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_RECORD.ordinal()] = 332;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_SPADE.ordinal()] = 201;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GOLD_SWORD.ordinal()] = 200;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GRASS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GRAVEL.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GREEN_RECORD.ordinal()] = 333;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.GRILLED_PORK.ordinal()] = 237;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.HARD_CLAY.ordinal()] = 169;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.HAY_BLOCK.ordinal()] = 167;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.HOPPER.ordinal()] = 156;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.HOPPER_MINECART.ordinal()] = 325;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.HUGE_MUSHROOM_1.ordinal()] = 101;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.HUGE_MUSHROOM_2.ordinal()] = 102;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ICE.ordinal()] = 80;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.INK_SACK.ordinal()] = 268;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_AXE.ordinal()] = 175;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_BARDING.ordinal()] = 326;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_BLOCK.ordinal()] = 43;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_BOOTS.ordinal()] = 226;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_CHESTPLATE.ordinal()] = 224;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_DOOR.ordinal()] = 247;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_DOOR_BLOCK.ordinal()] = 72;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_FENCE.ordinal()] = 103;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_HELMET.ordinal()] = 223;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_HOE.ordinal()] = 209;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_INGOT.ordinal()] = 182;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_LEGGINGS.ordinal()] = 225;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_ORE.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_PICKAXE.ordinal()] = 174;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_PLATE.ordinal()] = 150;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_SPADE.ordinal()] = 173;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.IRON_SWORD.ordinal()] = 184;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ITEM_FRAME.ordinal()] = 306;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.JACK_O_LANTERN.ordinal()] = 92;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.JUKEBOX.ordinal()] = 85;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.JUNGLE_WOOD_STAIRS.ordinal()] = 138;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LADDER.ordinal()] = 66;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LAPIS_BLOCK.ordinal()] = 23;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LAPIS_ORE.ordinal()] = 22;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LAVA.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LAVA_BUCKET.ordinal()] = 244;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEASH.ordinal()] = 329;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEATHER.ordinal()] = 251;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEATHER_BOOTS.ordinal()] = 218;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEATHER_CHESTPLATE.ordinal()] = 216;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEATHER_HELMET.ordinal()] = 215;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEATHER_LEGGINGS.ordinal()] = 217;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEAVES.ordinal()] = 19;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEAVES_2.ordinal()] = 163;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LEVER.ordinal()] = 70;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LOCKED_CHEST.ordinal()] = 96;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LOG.ordinal()] = 18;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LOG_2.ordinal()] = 164;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.LONG_GRASS.ordinal()] = 32;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MAGMA_CREAM.ordinal()] = 295;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MAP.ordinal()] = 275;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MELON.ordinal()] = 277;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MELON_BLOCK.ordinal()] = 105;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MELON_SEEDS.ordinal()] = 279;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MELON_STEM.ordinal()] = 107;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MILK_BUCKET.ordinal()] = 252;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MINECART.ordinal()] = 245;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MOB_SPAWNER.ordinal()] = 53;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MONSTER_EGG.ordinal()] = 300;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MONSTER_EGGS.ordinal()] = 99;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MOSSY_COBBLESTONE.ordinal()] = 49;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MUSHROOM_SOUP.ordinal()] = 199;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.MYCEL.ordinal()] = 112;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NAME_TAG.ordinal()] = 330;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHERRACK.ordinal()] = 88;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_BRICK.ordinal()] = 114;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_BRICK_ITEM.ordinal()] = 322;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_BRICK_STAIRS.ordinal()] = 116;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_FENCE.ordinal()] = 115;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_STALK.ordinal()] = 289;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_STAR.ordinal()] = 316;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NETHER_WARTS.ordinal()] = 117;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.NOTE_BLOCK.ordinal()] = 26;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.OBSIDIAN.ordinal()] = 50;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PACKED_ICE.ordinal()] = 171;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PAINTING.ordinal()] = 238;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PAPER.ordinal()] = 256;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PISTON_BASE.ordinal()] = 34;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PISTON_EXTENSION.ordinal()] = 35;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PISTON_MOVING_PIECE.ordinal()] = 37;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PISTON_STICKY_BASE.ordinal()] = 30;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.POISONOUS_POTATO.ordinal()] = 311;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PORK.ordinal()] = 236;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PORTAL.ordinal()] = 91;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.POTATO.ordinal()] = 144;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.POTATO_ITEM.ordinal()] = 309;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.POTION.ordinal()] = 290;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.POWERED_MINECART.ordinal()] = 260;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.POWERED_RAIL.ordinal()] = 28;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PUMPKIN.ordinal()] = 87;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PUMPKIN_PIE.ordinal()] = 317;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PUMPKIN_SEEDS.ordinal()] = 278;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.PUMPKIN_STEM.ordinal()] = 106;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.QUARTZ.ordinal()] = 323;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.QUARTZ_BLOCK.ordinal()] = 157;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.QUARTZ_ORE.ordinal()] = 155;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.QUARTZ_STAIRS.ordinal()] = 158;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RAILS.ordinal()] = 67;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RAW_BEEF.ordinal()] = 280;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RAW_CHICKEN.ordinal()] = 282;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RAW_FISH.ordinal()] = 266;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_10.ordinal()] = 341;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_11.ordinal()] = 342;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_12.ordinal()] = 343;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_3.ordinal()] = 334;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_4.ordinal()] = 335;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_5.ordinal()] = 336;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_6.ordinal()] = 337;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_7.ordinal()] = 338;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_8.ordinal()] = 339;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RECORD_9.ordinal()] = 340;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE.ordinal()] = 248;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_BLOCK.ordinal()] = 154;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_COMPARATOR.ordinal()] = 321;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 151;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 152;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_LAMP_OFF.ordinal()] = 125;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_LAMP_ON.ordinal()] = 126;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_ORE.ordinal()] = 74;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_TORCH_OFF.ordinal()] = 76;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_TORCH_ON.ordinal()] = 77;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.REDSTONE_WIRE.ordinal()] = 56;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RED_MUSHROOM.ordinal()] = 41;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.RED_ROSE.ordinal()] = 39;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.ROTTEN_FLESH.ordinal()] = 284;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SADDLE.ordinal()] = 246;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SAND.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SANDSTONE.ordinal()] = 25;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SANDSTONE_STAIRS.ordinal()] = 130;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SAPLING.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SEEDS.ordinal()] = 212;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SHEARS.ordinal()] = 276;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SIGN.ordinal()] = 240;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SIGN_POST.ordinal()] = 64;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SKULL.ordinal()] = 146;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SKULL_ITEM.ordinal()] = 314;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SLIME_BALL.ordinal()] = 258;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SMOOTH_BRICK.ordinal()] = 100;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SMOOTH_STAIRS.ordinal()] = 111;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SNOW.ordinal()] = 79;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SNOW_BALL.ordinal()] = 249;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SNOW_BLOCK.ordinal()] = 81;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SOIL.ordinal()] = 61;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SOUL_SAND.ordinal()] = 89;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SPECKLED_MELON.ordinal()] = 299;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SPIDER_EYE.ordinal()] = 292;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SPONGE.ordinal()] = 20;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SPRUCE_WOOD_STAIRS.ordinal()] = 136;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STAINED_CLAY.ordinal()] = 161;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STAINED_GLASS.ordinal()] = 97;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STAINED_GLASS_PANE.ordinal()] = 162;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STATIONARY_LAVA.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STATIONARY_WATER.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STEP.ordinal()] = 45;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STICK.ordinal()] = 197;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_AXE.ordinal()] = 192;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_BUTTON.ordinal()] = 78;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_HOE.ordinal()] = 208;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_PICKAXE.ordinal()] = 191;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_PLATE.ordinal()] = 71;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_SPADE.ordinal()] = 190;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STONE_SWORD.ordinal()] = 189;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STORAGE_MINECART.ordinal()] = 259;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.STRING.ordinal()] = 204;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SUGAR.ordinal()] = 270;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SUGAR_CANE.ordinal()] = 255;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SUGAR_CANE_BLOCK.ordinal()] = 84;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.SULPHUR.ordinal()] = 206;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.THIN_GLASS.ordinal()] = 104;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.TNT.ordinal()] = 47;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.TORCH.ordinal()] = 51;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.TRAPPED_CHEST.ordinal()] = 148;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.TRAP_DOOR.ordinal()] = 98;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.TRIPWIRE.ordinal()] = 134;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.TRIPWIRE_HOOK.ordinal()] = 133;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.VINE.ordinal()] = 108;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WALL_SIGN.ordinal()] = 69;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WATCH.ordinal()] = 264;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WATER.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WATER_BUCKET.ordinal()] = 243;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WATER_LILY.ordinal()] = 113;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WEB.ordinal()] = 31;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WHEAT.ordinal()] = 213;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOODEN_DOOR.ordinal()] = 65;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_AXE.ordinal()] = 188;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_BUTTON.ordinal()] = 145;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_DOOR.ordinal()] = 241;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_DOUBLE_STEP.ordinal()] = 127;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_HOE.ordinal()] = 207;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_PICKAXE.ordinal()] = 187;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_PLATE.ordinal()] = 73;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_SPADE.ordinal()] = 186;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_STAIRS.ordinal()] = 54;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_STEP.ordinal()] = 128;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOD_SWORD.ordinal()] = 185;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WOOL.ordinal()] = 36;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WORKBENCH.ordinal()] = 59;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.WRITTEN_BOOK.ordinal()] = 304;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Material.YELLOW_FLOWER.ordinal()] = 38;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$org$bukkit$Material = arrn;
        return $SWITCH_TABLE$org$bukkit$Material;
    }
}


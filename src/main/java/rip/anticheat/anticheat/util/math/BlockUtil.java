/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package rip.anticheat.anticheat.util.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.anticheat.anticheat.util.math.MathUtil;

public class BlockUtil {
    public static HashSet<Byte> blockPassSet = new HashSet();
    public static HashSet<Byte> blockAirFoliageSet = new HashSet();
    public static HashSet<Byte> fullSolid = new HashSet();
    public static HashSet<Byte> blockUseSet = new HashSet();

    public static Block getLowestBlockAt(Location location) {
        Block block = location.getWorld().getBlockAt((int)location.getX(), 0, (int)location.getZ());
        if (block == null || block.getType().equals((Object)Material.AIR)) {
            block = location.getBlock();
            int n = (int)location.getY();
            while (n > 0) {
                Block block2 = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
                Block block3 = block2.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (block3 == null || block3.getType().equals((Object)Material.AIR)) {
                    block = block2;
                }
                --n;
            }
        }
        return block;
    }

    public static boolean containsBlock(Location location, Material material) {
        int n = 0;
        while (n < 256) {
            Block block = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
            if (block != null && block.getType().equals((Object)material)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public static boolean containsBlock(Location location) {
        int n = 0;
        while (n < 256) {
            Block block = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
            if (block != null && !block.getType().equals((Object)Material.AIR)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public static boolean containsBlockBelow(Location location) {
        int n = 0;
        while (n < (int)location.getY()) {
            Block block = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
            if (block != null && !block.getType().equals((Object)Material.AIR)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public static ArrayList<Block> getBlocksAroundCenter(Location location, int n) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        int n2 = location.getBlockX() - n;
        while (n2 <= location.getBlockX() + n) {
            int n3 = location.getBlockY() - n;
            while (n3 <= location.getBlockY() + n) {
                int n4 = location.getBlockZ() - n;
                while (n4 <= location.getBlockZ() + n) {
                    Location location2 = new Location(location.getWorld(), (double)n2, (double)n3, (double)n4);
                    if (location2.distance(location) <= (double)n) {
                        arrayList.add(location2.getBlock());
                    }
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        return arrayList;
    }

    public static Location stringToLocation(String string) {
        String[] arrstring = string.split(",");
        World world = Bukkit.getWorld((String)arrstring[0]);
        double d = Double.parseDouble(arrstring[1]);
        double d2 = Double.parseDouble(arrstring[2]);
        double d3 = Double.parseDouble(arrstring[3]);
        float f = Float.parseFloat(arrstring[4]);
        float f2 = Float.parseFloat(arrstring[5]);
        return new Location(world, d, d2, d3, f, f2);
    }

    public static String LocationToString(Location location) {
        return String.valueOf(String.valueOf(location.getWorld().getName())) + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }

    public static boolean isStair(Block block) {
        String string = block.getType().name().toLowerCase();
        if (!(string.contains("stair") || string.contains("_step") || string.equals("step"))) {
            return false;
        }
        return true;
    }

    public static boolean isWeb(Block block) {
        if (block.getType() == Material.WEB) {
            return true;
        }
        return false;
    }

    public static boolean containsBlockType(Material[] arrmaterial, Block block) {
        Material[] arrmaterial2 = arrmaterial;
        int n = arrmaterial2.length;
        int n2 = 0;
        while (n2 < n) {
            Material material = arrmaterial2[n2];
            if (material == block.getType()) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static boolean isLiquid(Block block) {
        if (block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA)) {
            return true;
        }
        return false;
    }

    public static boolean isSolid(Block block) {
        if (block != null && BlockUtil.isSolid(block.getTypeId())) {
            return true;
        }
        return false;
    }

    public static boolean isIce(Block block) {
        if (block != null && (block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE)) {
            return true;
        }
        return false;
    }

    public static boolean isAny(Block block, Material[] arrmaterial) {
        Material[] arrmaterial2 = arrmaterial;
        int n = arrmaterial2.length;
        int n2 = 0;
        while (n2 < n) {
            Material material = arrmaterial2[n2];
            if (block.getType().equals((Object)material)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static boolean isSolid(int n) {
        return BlockUtil.isSolid((byte)n);
    }

    public static boolean isSolid(byte by) {
        if (blockPassSet.isEmpty()) {
            blockPassSet.add(Byte.valueOf(0));
            blockPassSet.add(Byte.valueOf(6));
            blockPassSet.add(Byte.valueOf(8));
            blockPassSet.add(Byte.valueOf(9));
            blockPassSet.add(Byte.valueOf(10));
            blockPassSet.add(Byte.valueOf(11));
            blockPassSet.add(Byte.valueOf(27));
            blockPassSet.add(Byte.valueOf(28));
            blockPassSet.add(Byte.valueOf(30));
            blockPassSet.add(Byte.valueOf(31));
            blockPassSet.add(Byte.valueOf(32));
            blockPassSet.add(Byte.valueOf(37));
            blockPassSet.add(Byte.valueOf(38));
            blockPassSet.add(Byte.valueOf(39));
            blockPassSet.add(Byte.valueOf(40));
            blockPassSet.add(Byte.valueOf(50));
            blockPassSet.add(Byte.valueOf(51));
            blockPassSet.add(Byte.valueOf(55));
            blockPassSet.add(Byte.valueOf(59));
            blockPassSet.add(Byte.valueOf(63));
            blockPassSet.add(Byte.valueOf(66));
            blockPassSet.add(Byte.valueOf(68));
            blockPassSet.add(Byte.valueOf(69));
            blockPassSet.add(Byte.valueOf(70));
            blockPassSet.add(Byte.valueOf(72));
            blockPassSet.add(Byte.valueOf(75));
            blockPassSet.add(Byte.valueOf(76));
            blockPassSet.add(Byte.valueOf(77));
            blockPassSet.add(Byte.valueOf(78));
            blockPassSet.add(Byte.valueOf(83));
            blockPassSet.add(Byte.valueOf(90));
            blockPassSet.add(Byte.valueOf(104));
            blockPassSet.add(Byte.valueOf(105));
            blockPassSet.add(Byte.valueOf(115));
            blockPassSet.add(Byte.valueOf(119));
            blockPassSet.add(Byte.valueOf(-124));
            blockPassSet.add(Byte.valueOf(-113));
            blockPassSet.add(Byte.valueOf(-81));
            blockPassSet.add(Byte.valueOf(-85));
        }
        return !blockPassSet.contains(Byte.valueOf(by));
    }

    public static boolean airFoliage(Block block) {
        if (block != null && BlockUtil.airFoliage(block.getTypeId())) {
            return true;
        }
        return false;
    }

    public static boolean airFoliage(int n) {
        return BlockUtil.airFoliage((byte)n);
    }

    public static boolean airFoliage(byte by) {
        if (blockAirFoliageSet.isEmpty()) {
            blockAirFoliageSet.add(Byte.valueOf(0));
            blockAirFoliageSet.add(Byte.valueOf(6));
            blockAirFoliageSet.add(Byte.valueOf(31));
            blockAirFoliageSet.add(Byte.valueOf(32));
            blockAirFoliageSet.add(Byte.valueOf(37));
            blockAirFoliageSet.add(Byte.valueOf(38));
            blockAirFoliageSet.add(Byte.valueOf(39));
            blockAirFoliageSet.add(Byte.valueOf(40));
            blockAirFoliageSet.add(Byte.valueOf(51));
            blockAirFoliageSet.add(Byte.valueOf(59));
            blockAirFoliageSet.add(Byte.valueOf(104));
            blockAirFoliageSet.add(Byte.valueOf(105));
            blockAirFoliageSet.add(Byte.valueOf(115));
            blockAirFoliageSet.add(Byte.valueOf(-115));
            blockAirFoliageSet.add(Byte.valueOf(-114));
        }
        return blockAirFoliageSet.contains(Byte.valueOf(by));
    }

    public static boolean fullSolid(Block block) {
        if (block != null && BlockUtil.fullSolid(block.getTypeId())) {
            return true;
        }
        return false;
    }

    public static boolean fullSolid(int n) {
        return BlockUtil.fullSolid((byte)n);
    }

    public static boolean fullSolid(byte by) {
        if (fullSolid.isEmpty()) {
            fullSolid.add(Byte.valueOf(1));
            fullSolid.add(Byte.valueOf(2));
            fullSolid.add(Byte.valueOf(3));
            fullSolid.add(Byte.valueOf(4));
            fullSolid.add(Byte.valueOf(5));
            fullSolid.add(Byte.valueOf(7));
            fullSolid.add(Byte.valueOf(12));
            fullSolid.add(Byte.valueOf(13));
            fullSolid.add(Byte.valueOf(14));
            fullSolid.add(Byte.valueOf(15));
            fullSolid.add(Byte.valueOf(16));
            fullSolid.add(Byte.valueOf(17));
            fullSolid.add(Byte.valueOf(19));
            fullSolid.add(Byte.valueOf(20));
            fullSolid.add(Byte.valueOf(21));
            fullSolid.add(Byte.valueOf(22));
            fullSolid.add(Byte.valueOf(23));
            fullSolid.add(Byte.valueOf(24));
            fullSolid.add(Byte.valueOf(25));
            fullSolid.add(Byte.valueOf(29));
            fullSolid.add(Byte.valueOf(33));
            fullSolid.add(Byte.valueOf(35));
            fullSolid.add(Byte.valueOf(41));
            fullSolid.add(Byte.valueOf(42));
            fullSolid.add(Byte.valueOf(43));
            fullSolid.add(Byte.valueOf(44));
            fullSolid.add(Byte.valueOf(45));
            fullSolid.add(Byte.valueOf(46));
            fullSolid.add(Byte.valueOf(47));
            fullSolid.add(Byte.valueOf(48));
            fullSolid.add(Byte.valueOf(49));
            fullSolid.add(Byte.valueOf(56));
            fullSolid.add(Byte.valueOf(57));
            fullSolid.add(Byte.valueOf(58));
            fullSolid.add(Byte.valueOf(60));
            fullSolid.add(Byte.valueOf(61));
            fullSolid.add(Byte.valueOf(62));
            fullSolid.add(Byte.valueOf(73));
            fullSolid.add(Byte.valueOf(74));
            fullSolid.add(Byte.valueOf(79));
            fullSolid.add(Byte.valueOf(80));
            fullSolid.add(Byte.valueOf(82));
            fullSolid.add(Byte.valueOf(84));
            fullSolid.add(Byte.valueOf(86));
            fullSolid.add(Byte.valueOf(87));
            fullSolid.add(Byte.valueOf(88));
            fullSolid.add(Byte.valueOf(89));
            fullSolid.add(Byte.valueOf(91));
            fullSolid.add(Byte.valueOf(95));
            fullSolid.add(Byte.valueOf(97));
            fullSolid.add(Byte.valueOf(98));
            fullSolid.add(Byte.valueOf(99));
            fullSolid.add(Byte.valueOf(100));
            fullSolid.add(Byte.valueOf(103));
            fullSolid.add(Byte.valueOf(110));
            fullSolid.add(Byte.valueOf(112));
            fullSolid.add(Byte.valueOf(121));
            fullSolid.add(Byte.valueOf(123));
            fullSolid.add(Byte.valueOf(124));
            fullSolid.add(Byte.valueOf(125));
            fullSolid.add(Byte.valueOf(126));
            fullSolid.add(Byte.valueOf(-127));
            fullSolid.add(Byte.valueOf(-123));
            fullSolid.add(Byte.valueOf(-119));
            fullSolid.add(Byte.valueOf(-118));
            fullSolid.add(Byte.valueOf(-104));
            fullSolid.add(Byte.valueOf(-103));
            fullSolid.add(Byte.valueOf(-101));
            fullSolid.add(Byte.valueOf(-98));
        }
        return fullSolid.contains(Byte.valueOf(by));
    }

    public static boolean usable(Block block) {
        if (block != null && BlockUtil.usable(block.getTypeId())) {
            return true;
        }
        return false;
    }

    public static boolean usable(int n) {
        return BlockUtil.usable((byte)n);
    }

    public static boolean usable(byte by) {
        if (blockUseSet.isEmpty()) {
            blockUseSet.add(Byte.valueOf(23));
            blockUseSet.add(Byte.valueOf(26));
            blockUseSet.add(Byte.valueOf(33));
            blockUseSet.add(Byte.valueOf(47));
            blockUseSet.add(Byte.valueOf(54));
            blockUseSet.add(Byte.valueOf(58));
            blockUseSet.add(Byte.valueOf(61));
            blockUseSet.add(Byte.valueOf(62));
            blockUseSet.add(Byte.valueOf(64));
            blockUseSet.add(Byte.valueOf(69));
            blockUseSet.add(Byte.valueOf(71));
            blockUseSet.add(Byte.valueOf(77));
            blockUseSet.add(Byte.valueOf(93));
            blockUseSet.add(Byte.valueOf(94));
            blockUseSet.add(Byte.valueOf(96));
            blockUseSet.add(Byte.valueOf(107));
            blockUseSet.add(Byte.valueOf(116));
            blockUseSet.add(Byte.valueOf(117));
            blockUseSet.add(Byte.valueOf(-126));
            blockUseSet.add(Byte.valueOf(-111));
            blockUseSet.add(Byte.valueOf(-110));
            blockUseSet.add(Byte.valueOf(-102));
            blockUseSet.add(Byte.valueOf(-98));
        }
        return blockUseSet.contains(Byte.valueOf(by));
    }

    public static HashMap<Block, Double> getInRadius(Location location, double d) {
        return BlockUtil.getInRadius(location, d, 999.0);
    }

    public static HashMap<Block, Double> getInRadius(Location location, double d, double d2) {
        HashMap<Block, Double> hashMap = new HashMap<Block, Double>();
        int n = (int)d + 1;
        int n2 = - n;
        while (n2 <= n) {
            int n3 = - n;
            while (n3 <= n) {
                int n4 = - n;
                while (n4 <= n) {
                    double d3;
                    Block block;
                    if ((double)Math.abs(n4) <= d2 && (d3 = MathUtil.offset(location, (block = location.getWorld().getBlockAt((int)(location.getX() + (double)n2), (int)(location.getY() + (double)n4), (int)(location.getZ() + (double)n3))).getLocation().add(0.5, 0.5, 0.5))) <= d) {
                        hashMap.put(block, 1.0 - d3 / d);
                    }
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        return hashMap;
    }

    public static HashMap<Block, Double> getInRadius(Block block, double d) {
        HashMap<Block, Double> hashMap = new HashMap<Block, Double>();
        int n = (int)d + 1;
        int n2 = - n;
        while (n2 <= n) {
            int n3 = - n;
            while (n3 <= n) {
                int n4 = - n;
                while (n4 <= n) {
                    Block block2 = block.getRelative(n2, n4, n3);
                    double d2 = MathUtil.offset(block.getLocation(), block2.getLocation());
                    if (d2 <= d) {
                        hashMap.put(block2, 1.0 - d2 / d);
                    }
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        return hashMap;
    }

    public static boolean isBlock(ItemStack itemStack) {
        if (itemStack != null && itemStack.getTypeId() > 0 && itemStack.getTypeId() < 256) {
            return true;
        }
        return false;
    }

    public static Block getHighest(Location location) {
        return BlockUtil.getHighest(location, null);
    }

    public static Block getHighest(Location location, HashSet<Material> hashSet) {
        location.setY(0.0);
        int n = 0;
        while (n < 256) {
            location.setY((double)(256 - n));
            if (BlockUtil.isSolid(location.getBlock())) break;
            ++n;
        }
        return location.getBlock().getRelative(BlockFace.UP);
    }

    public static boolean isInAir(Player player) {
        boolean bl = false;
        for (Block block : BlockUtil.getSurrounding(player.getLocation().getBlock(), true)) {
            if (block.getType() == Material.AIR) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public static ArrayList<Block> getSurrounding(Block block, boolean bl) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        if (bl) {
            int n = -1;
            while (n <= 1) {
                int n2 = -1;
                while (n2 <= 1) {
                    int n3 = -1;
                    while (n3 <= 1) {
                        if (n != 0 || n2 != 0 || n3 != 0) {
                            arrayList.add(block.getRelative(n, n2, n3));
                        }
                        ++n3;
                    }
                    ++n2;
                }
                ++n;
            }
        } else {
            arrayList.add(block.getRelative(BlockFace.UP));
            arrayList.add(block.getRelative(BlockFace.DOWN));
            arrayList.add(block.getRelative(BlockFace.NORTH));
            arrayList.add(block.getRelative(BlockFace.SOUTH));
            arrayList.add(block.getRelative(BlockFace.EAST));
            arrayList.add(block.getRelative(BlockFace.WEST));
        }
        return arrayList;
    }

    public static ArrayList<Block> getSurroundingXZ(Block block) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        arrayList.add(block.getRelative(BlockFace.NORTH));
        arrayList.add(block.getRelative(BlockFace.NORTH_EAST));
        arrayList.add(block.getRelative(BlockFace.NORTH_WEST));
        arrayList.add(block.getRelative(BlockFace.SOUTH));
        arrayList.add(block.getRelative(BlockFace.SOUTH_EAST));
        arrayList.add(block.getRelative(BlockFace.SOUTH_WEST));
        arrayList.add(block.getRelative(BlockFace.EAST));
        arrayList.add(block.getRelative(BlockFace.WEST));
        return arrayList;
    }

    public static String serializeLocation(Location location) {
        int n = (int)location.getX();
        int n2 = (int)location.getY();
        int n3 = (int)location.getZ();
        int n4 = (int)location.getPitch();
        int n5 = (int)location.getYaw();
        return new String(String.valueOf(String.valueOf(location.getWorld().getName())) + "," + n + "," + n2 + "," + n3 + "," + n4 + "," + n5);
    }

    public static Location deserializeLocation(String string) {
        if (string == null) {
            return null;
        }
        String[] arrstring = string.split(",");
        World world = Bukkit.getServer().getWorld(arrstring[0]);
        Double d = Double.parseDouble(arrstring[1]);
        Double d2 = Double.parseDouble(arrstring[2]);
        Double d3 = Double.parseDouble(arrstring[3]);
        Float f = Float.valueOf(Float.parseFloat(arrstring[4]));
        Float f2 = Float.valueOf(Float.parseFloat(arrstring[5]));
        Location location = new Location(world, d.doubleValue(), d2.doubleValue(), d3.doubleValue());
        location.setPitch(f.floatValue());
        location.setYaw(f2.floatValue());
        return location;
    }

    public static boolean isVisible(Block block) {
        for (Block block2 : BlockUtil.getSurrounding(block, false)) {
            if (block2.getType().isOccluding()) continue;
            return true;
        }
        return false;
    }
}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package rip.anticheat.anticheat.util.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.anticheat.anticheat.util.math.BlockUtil;
import rip.anticheat.anticheat.util.math.MathUtil;

public class PlayerUtil {
    public static void clear(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.setSprinting(false);
        player.setFoodLevel(20);
        player.setSaturation(3.0f);
        player.setExhaustion(0.0f);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setLevel(0);
        player.setExp(0.0f);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.updateInventory();
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public static Location getEyeLocation(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + player.getEyeHeight());
        return location;
    }

    public static boolean isOnClimbable(Player player) {
        for (Block block : BlockUtil.getSurrounding(player.getLocation().getBlock(), false)) {
            if (block.getType() != Material.LADDER && block.getType() != Material.VINE) continue;
            return true;
        }
        if (player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE) {
            return false;
        }
        return true;
    }

    public static boolean isOnGround(Location location, int n) {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = MathUtil.getFraction(d) > 0.0 ? Math.abs(MathUtil.getFraction(d)) : 1.0 - Math.abs(MathUtil.getFraction(d));
        double d4 = MathUtil.getFraction(d2) > 0.0 ? Math.abs(MathUtil.getFraction(d2)) : 1.0 - Math.abs(MathUtil.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d3 > 0.7) {
            if (BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d4 < 0.3 ? BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 - 1)) : d4 > 0.7 && BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 + 1))) {
            return true;
        }
        return false;
    }

    public static boolean isOnGround(Player player, int n) {
        return PlayerUtil.isOnGround(player.getLocation(), n);
    }

    public static boolean isOnBlock(Location location, int n, Material[] arrmaterial) {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = MathUtil.getFraction(d) > 0.0 ? Math.abs(MathUtil.getFraction(d)) : 1.0 - Math.abs(MathUtil.getFraction(d));
        double d4 = MathUtil.getFraction(d2) > 0.0 ? Math.abs(MathUtil.getFraction(d2)) : 1.0 - Math.abs(MathUtil.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d3 > 0.7) {
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d4 < 0.3 ? BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1)) : d4 > 0.7 && BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1))) {
            return true;
        }
        return false;
    }

    public static boolean isOnBlock(Player player, int n, Material[] arrmaterial) {
        return PlayerUtil.isOnBlock(player.getLocation(), n, arrmaterial);
    }

    public static boolean isHoveringOverWater(Location location, int n) {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = MathUtil.getFraction(d) > 0.0 ? Math.abs(MathUtil.getFraction(d)) : 1.0 - Math.abs(MathUtil.getFraction(d));
        double d4 = MathUtil.getFraction(d2) > 0.0 ? Math.abs(MathUtil.getFraction(d2)) : 1.0 - Math.abs(MathUtil.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d3 > 0.7) {
            if (BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d4 < 0.3 ? BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 - 1)) : d4 > 0.7 && BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 + 1))) {
            return true;
        }
        return false;
    }

    public static boolean isHoveringOverWater(Player player, int n) {
        return PlayerUtil.isHoveringOverWater(player.getLocation(), n);
    }

    public static boolean isOnStairs(Location location, int n) {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = MathUtil.getFraction(d) > 0.0 ? Math.abs(MathUtil.getFraction(d)) : 1.0 - Math.abs(MathUtil.getFraction(d));
        double d4 = MathUtil.getFraction(d2) > 0.0 ? Math.abs(MathUtil.getFraction(d2)) : 1.0 - Math.abs(MathUtil.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d3 > 0.7) {
            if (BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 - 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 - 1))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 + 1))) {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 + 1))) {
                    return true;
                }
            }
        } else if (d4 < 0.3 ? BlockUtil.isStair(world.getBlockAt(n2, n3, n4 - 1)) : d4 > 0.7 && BlockUtil.isStair(world.getBlockAt(n2, n3, n4 + 1))) {
            return true;
        }
        return false;
    }

    public static boolean isOnStairs(Player player, int n) {
        return PlayerUtil.isOnStairs(player.getLocation(), n);
    }

    public static List<Entity> getNearbyRidables(Location location, double d) {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        for (Entity entity : new ArrayList(location.getWorld().getEntities())) {
            if (!entity.getType().equals((Object)EntityType.HORSE) && !entity.getType().equals((Object)EntityType.BOAT) || entity.getLocation().distance(location) > d) continue;
            arrayList.add(entity);
        }
        return arrayList;
    }
}


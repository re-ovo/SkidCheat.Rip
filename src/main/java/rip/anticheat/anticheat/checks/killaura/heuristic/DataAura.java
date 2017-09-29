/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class DataAura
implements Listener {
    public static Map<Player, Integer> hitsDone1 = new HashMap<Player, Integer>();
    public static Map<File, List<String>> results1;
    public static Map<Player, Integer> hitsDone2;
    public static Map<Player, Integer> checking2;
    public static Map<File, List<String>> results2;
    public static Map<Player, Integer> checking1;

    static {
        checking1 = new HashMap<Player, Integer>();
        results1 = new HashMap<File, List<String>>();
        hitsDone2 = new HashMap<Player, Integer>();
        checking2 = new HashMap<Player, Integer>();
        results2 = new HashMap<File, List<String>>();
    }

    @EventHandler
    public final void onEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        if (checking1.containsKey((Object)player) && hitsDone1.containsKey((Object)player)) {
            if (checking1.get((Object)player) >= 20) {
                checking1.remove((Object)player);
                results1.remove(hitsDone1.get((Object)player));
                Map<Player, Integer> map = hitsDone1;
                Player player2 = player;
                hitsDone1.remove((Object)player);
            } else {
                results1.containsKey(hitsDone1.get((Object)player));
                double d = entityDamageByEntityEvent.getEntity().getLocation().getX() - player.getLocation().getX();
                double d2 = entityDamageByEntityEvent.getEntity().getLocation().getZ() - player.getLocation().getZ();
                double d3 = d;
                double d4 = d3 * d3;
                double d5 = d2;
                double d6 = Math.sqrt(d4 + d5 * d5);
                Player player3 = player;
                double d7 = Math.abs(DataAura.getDirection(player3.getLocation(), entityDamageByEntityEvent.getEntity().getLocation()));
                double d8 = Math.abs(DataAura.wrapAngleTo180_float(player3.getLocation().getYaw()));
                double d9 = Math.abs(d7 - d8);
                String string = String.valueOf(String.valueOf(d7)) + d9 + d6;
                results1.get(hitsDone1.get((Object)player)).add(string);
                Map<Player, Integer> map = checking1;
                Map<Player, Integer> map2 = checking1;
                Player player4 = player;
                map.put(player4, map2.get((Object)player4) + 1);
            }
            if (checking1.containsKey((Object)player)) {
                if (checking1.get((Object)player) == 5) {
                    return;
                }
                if (checking1.get((Object)player) == 10) {
                    return;
                }
                if (checking1.get((Object)player) == 15) {
                    return;
                }
                if (checking1.get((Object)player) == 17) {
                    return;
                }
                if (checking1.get((Object)player) == 18) {
                    return;
                }
                if (checking1.get((Object)player) == 19) {
                    return;
                }
                checking1.get((Object)player).intValue();
            }
        }
    }

    @EventHandler
    public final void onEvent(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        if (player.getVelocity().getX() > 0.01) {
            return;
        }
        if (player.getVelocity().getY() > 0.01) {
            return;
        }
        if (player.getVelocity().getZ() > 0.01) {
            return;
        }
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        Location location = player.getLocation();
        double d = -1.0;
        double d2 = 0.0;
        if (location.add(0.0, -1.0, 0.0).getBlock().getType() == Material.SPONGE) {
            return;
        }
        Location location2 = player.getLocation();
        double d3 = -1.0;
        double d4 = 0.0;
        if (location2.add(0.0, -1.0, 0.0).getBlock().getType() == Material.ICE) {
            return;
        }
        Location location3 = player.getLocation();
        double d5 = -1.0;
        double d6 = 0.0;
        if (location3.add(0.0, -1.0, 0.0).getBlock().getType() == Material.SOUL_SAND) {
            return;
        }
        Location location4 = player.getLocation();
        double d7 = 1.0;
        double d8 = 0.0;
        if (location4.add(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) {
            return;
        }
        Location location5 = player.getLocation();
        double d9 = 1.0;
        double d10 = 0.0;
        if (location5.add(0.0, 1.0, 0.0).getBlock().getType() == null) {
            return;
        }
        Location location6 = player.getLocation();
        double d11 = 0.0;
        if (location6.add(0.0, 0.0, 0.0).getBlock().getType() == null) {
            return;
        }
        Location location7 = player.getLocation();
        double d12 = -1.0;
        double d13 = 0.0;
        if (location7.add(0.0, -1.0, 0.0).getBlock().getType() == null) {
            return;
        }
        Location location8 = player.getLocation();
        double d14 = 1.0;
        double d15 = 0.0;
        if (location8.add(0.0, 1.0, 0.0).getBlock().getType() == null) {
            return;
        }
        Location location9 = player.getLocation();
        double d16 = 0.0;
        if (location9.add(0.0, 0.0, 0.0).getBlock().getType() == null) {
            return;
        }
        Location location10 = player.getLocation();
        double d17 = -1.0;
        double d18 = 0.0;
        if (location10.add(0.0, -1.0, 0.0).getBlock().getType() == null) {
            return;
        }
        if (playerMoveEvent.getFrom().getX() <= playerMoveEvent.getTo().getX() && playerMoveEvent.getFrom().getX() >= playerMoveEvent.getTo().getX() && playerMoveEvent.getFrom().getZ() <= playerMoveEvent.getTo().getZ() && playerMoveEvent.getFrom().getZ() >= playerMoveEvent.getTo().getZ()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (checking2.containsKey((Object)player) && hitsDone2.containsKey((Object)player)) {
            if (checking2.get((Object)player) >= 20) {
                checking2.remove((Object)player);
                results2.remove(hitsDone2.get((Object)player));
                Map<Player, Integer> map = hitsDone2;
                Player player2 = player;
                hitsDone2.remove((Object)player);
            } else {
                results2.containsKey(hitsDone2.get((Object)player));
                double d19 = playerMoveEvent.getFrom().getX() - playerMoveEvent.getTo().getX();
                double d20 = playerMoveEvent.getFrom().getZ() - playerMoveEvent.getTo().getZ();
                double d21 = d19;
                double d22 = d21 * d21;
                double d23 = d20;
                double d24 = Math.sqrt(d22 + d23 * d23);
                double d25 = playerMoveEvent.getFrom().getY() - playerMoveEvent.getTo().getY();
                double d26 = Math.sqrt(d25 * d25);
                Player player3 = player;
                double d27 = player3.getVelocity().getX();
                double d28 = player3.getVelocity().getY();
                double d29 = player3.getVelocity().getZ();
                double d30 = d27;
                double d31 = d30 * d30;
                double d32 = d29;
                double d33 = d31 + d32 * d32;
                double d34 = d28;
                double d35 = Math.sqrt(d33 + d34 * d34);
                String string = String.valueOf(String.valueOf(d35)) + d26 + d24;
                results2.get(hitsDone2.get((Object)player)).add(string);
                Map<Player, Integer> map = checking2;
                Map<Player, Integer> map2 = checking2;
                Player player4 = player;
                map.put(player4, map2.get((Object)player4) + 1);
            }
            if (checking2.containsKey((Object)player)) {
                if (checking2.get((Object)player) == 5) {
                    return;
                }
                if (checking2.get((Object)player) == 10) {
                    return;
                }
                if (checking2.get((Object)player) == 15) {
                    return;
                }
                if (checking2.get((Object)player) == 17) {
                    return;
                }
                if (checking2.get((Object)player) == 18) {
                    return;
                }
                if (checking2.get((Object)player) == 19) {
                    return;
                }
                checking2.get((Object)player).intValue();
            }
        }
    }

    public static double getDirection(Location location, Location location2) {
        if (location == null || location2 == null) {
            return 0.0;
        }
        double d = location2.getX() - location.getX();
        return DataAura.wrapAngleTo180_float((float)(Math.atan2(location2.getZ() - location.getZ(), d) * 180.0 / 3.141592653589793) - 90.0f);
    }

    public static float wrapAngleTo180_float(float f) {
        if ((f %= 360.0f) >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }
}


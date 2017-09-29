/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.util.misc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.util.misc.ReflectionUtil;

public class ServerUtil {
    private static Object MC_SERVER_OBJ = null;
    private static Field MC_SERVER_TPS_FIELD = null;

    public static List<Entity> getEntities(World world) {
        return new ArrayList<Entity>(world.getEntities());
    }

    public static void asyncKick(Player player, final String string) {
        AntiCheat.Instance.getServer().getScheduler().runTask((Plugin)AntiCheat.Instance, new Runnable(){

            @Override
            public void run() {
                Player.this.kickPlayer(string);
            }
        });
    }

    public static int getPing(Player player) {
        Object object = ReflectionUtil.getEntityHandle((Entity)player);
        if (object != null) {
            Field field = ReflectionUtil.getField("ping", object.getClass());
            try {
                return field.getInt(object);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return 150;
    }

    public static double[] getTps() {
        if (MC_SERVER_OBJ == null) {
            try {
                MC_SERVER_OBJ = ReflectionUtil.getMethod("getServer", ReflectionUtil.getNMSClass("MinecraftServer")).invoke(null, new Object[0]);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                reflectiveOperationException.printStackTrace();
            }
            MC_SERVER_TPS_FIELD = ReflectionUtil.getField("recentTps", ReflectionUtil.getNMSClass("MinecraftServer"));
        }
        try {
            return (double[])MC_SERVER_TPS_FIELD.get(MC_SERVER_OBJ);
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return null;
        }
    }

    public static double[] getRoundedTps() {
        double[] arrd = ServerUtil.getTps();
        int n = 0;
        while (n < arrd.length) {
            double d = arrd[n];
            arrd[n] = d = (double)Math.round(d * 100.0) / 100.0;
            ++n;
        }
        return arrd;
    }

}


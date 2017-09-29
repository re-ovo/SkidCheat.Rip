/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.util.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ReflectionUtil {
    private static Logger log = Logger.getLogger("BoxUtils");
    private static HashMap<String, Class<?>> classCache = new HashMap(128);
    private static HashMap<String, Field> fieldCache = new HashMap(128);
    private static HashMap<String, Method> methodCache = new HashMap(128);
    private static HashMap<String, Constructor> constructorCache = new HashMap(128);
    private static String obcPrefix = null;
    private static String nmsPrefix = null;

    static {
        try {
            nmsPrefix = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            obcPrefix = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        }
        catch (Exception exception) {
            nmsPrefix = "net.minecraft.server.";
            obcPrefix = "org.bukkit.craftbukkit.";
        }
    }

    private ReflectionUtil() {
    }

    public static Class<?> getCraftBukkitClass(String string) {
        return ReflectionUtil.getClass(String.valueOf(obcPrefix) + string);
    }

    public static Class<?> getNMSClass(String string) {
        return ReflectionUtil.getClass(String.valueOf(nmsPrefix) + string);
    }

    public static Class<?> getClass(String string) {
        Validate.notNull((Object)string);
        if (classCache.containsKey(string)) {
            return classCache.get(string);
        }
        Class class_ = null;
        try {
            class_ = Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the class " + string);
        }
        if (class_ != null) {
            classCache.put(string, class_);
        }
        return class_;
    }

    public static Field getField(String string, Class<?> class_) {
        Validate.notNull((Object)string);
        Validate.notNull(class_);
        String string2 = String.valueOf(class_.getCanonicalName()) + "@" + string;
        if (fieldCache.containsKey(string2)) {
            return fieldCache.get(string2);
        }
        Field field = null;
        try {
            field = class_.getField(string);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the field " + string + " in class " + class_.getSimpleName());
        }
        if (field != null) {
            fieldCache.put(string2, field);
        }
        return field;
    }

    public static Method getMethod(Class<?> class_, String string, Class<?>[] arrclass) {
        Validate.notNull((Object)string);
        Validate.notNull(class_);
        String string2 = String.valueOf(class_.getCanonicalName()) + "@" + string;
        if (methodCache.containsKey(string2)) {
            return methodCache.get(string2);
        }
        Method method = null;
        try {
            method = class_.getMethod(string, arrclass);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " + string + " in class " + class_.getSimpleName());
        }
        if (method != null) {
            methodCache.put(string2, method);
        }
        return method;
    }

    public static Method getMethod(String string, Class<?> class_, Class<?>[] arrclass) {
        Validate.notNull((Object)string);
        Validate.notNull(class_);
        String string2 = String.valueOf(class_.getCanonicalName()) + "@" + string;
        if (methodCache.containsKey(string2)) {
            return methodCache.get(string2);
        }
        Method method = null;
        try {
            method = class_.getMethod(string, arrclass);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " + string + " in class " + class_.getSimpleName());
        }
        if (method != null) {
            methodCache.put(string2, method);
        }
        return method;
    }

    public static Method getMethod(String string, Class<?> class_) {
        Validate.notNull((Object)string);
        Validate.notNull(class_);
        String string2 = String.valueOf(class_.getCanonicalName()) + "@" + string;
        if (methodCache.containsKey(string2)) {
            return methodCache.get(string2);
        }
        Method method = null;
        try {
            method = class_.getMethod(string, new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " + string + " in class " + class_.getSimpleName());
        }
        if (method != null) {
            methodCache.put(string2, method);
        }
        return method;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static Constructor<?> getConstructor(Class<?> var0, Class<?>[] var1_1) {
        block5 : {
            Validate.notNull(var0);
            Validate.notNull(var1_1);
            var2_2 = String.valueOf(var0.getSimpleName()) + "#";
            var6_3 = var1_1;
            var5_4 = var6_3.length;
            var4_6 = 0;
            while (var4_6 < var5_4) {
                var3_8 = var6_3[var4_6];
                var2_2 = String.valueOf(var2_2) + var3_8.getSimpleName() + "_";
                ++var4_6;
            }
            if (ReflectionUtil.constructorCache.containsKey(var2_2 = var2_2.substring(0, var2_2.length() - 1))) {
                return ReflectionUtil.constructorCache.get(var2_2);
            }
            var3_9 = null;
            try {
                var3_10 = var0.getConstructor(var1_1);
                break block5;
            }
            catch (NoSuchMethodException var4_7) {
                var5_5 = new StringBuilder();
                var9_12 = var1_1;
                var8_13 = var9_12.length;
                var7_14 = 0;
                ** while (var7_14 < var8_13)
            }
lbl-1000: // 1 sources:
            {
                var6_3 = var9_12[var7_14];
                var5_5.append(var6_3.getCanonicalName()).append(", ");
                ++var7_14;
                continue;
            }
lbl28: // 1 sources:
            var5_5.setLength(var5_5.length() - 2);
            ReflectionUtil.log.log(Level.SEVERE, "[Reflection] Unable to find the the constructor  with the params [" + var5_5.toString() + "] in class " + var0.getSimpleName());
        }
        if (var3_11 == null) return var3_11;
        ReflectionUtil.constructorCache.put(var2_2, (Constructor)var3_11);
        return var3_11;
    }

    public static Object getEntityHandle(Entity entity) {
        try {
            Method method = ReflectionUtil.getMethod("getHandle", entity.getClass());
            return method.invoke((Object)entity, new Object[0]);
        }
        catch (Exception exception) {
            log.log(Level.SEVERE, "[Reflection] Unable to getHandle of " + (Object)entity.getType());
            return null;
        }
    }

    public static void sendPacket(Player player, Object object) {
        Object object2 = null;
        try {
            object2 = ReflectionUtil.getEntityHandle((Entity)player);
            Object object3 = object2.getClass().getField("playerConnection").get(object2);
            ReflectionUtil.getMethod("sendPacket", object3.getClass()).invoke(object3, object);
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
        catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
    }
}


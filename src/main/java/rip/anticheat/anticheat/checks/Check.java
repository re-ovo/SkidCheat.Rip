/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat.checks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.ID;

public class Check
implements Listener {
    private CheckType checkType;
    private String displayName;
    private String Name;
    private AntiCheat Core;
    private long expiration = 300000;
    private int threshold;
    private int level;
    private int bans;
    private int autobanTimer;
    private boolean enabled = true;
    private String id;
    private double tpsDisable;
    private boolean tempDisabled;

    public Check(AntiCheat antiCheat, CheckType checkType, String string, String string2, int n, int n2, int n3, int n4) {
        this.Core = antiCheat;
        this.checkType = checkType;
        this.Name = string;
        this.displayName = string2;
        this.threshold = n;
        this.level = n2;
        this.bans = n3;
        this.autobanTimer = n4;
        this.id = ID.generate();
        this.tpsDisable = 17.0;
        this.tempDisabled = false;
    }

    public CheckType getCheckType() {
        return this.checkType;
    }

    public AntiCheat getCore() {
        return this.Core;
    }

    public String getName() {
        return this.Name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public int getLevel() {
        return this.level;
    }

    public int getBans() {
        return this.bans;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public int getAutobanTimer() {
        return this.autobanTimer;
    }

    public boolean isEnabled() {
        if (this.enabled && !this.tempDisabled) {
            return true;
        }
        return false;
    }

    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    public void setExpiration(long l) {
        this.expiration = l;
    }

    public String getId() {
        return this.id;
    }

    public boolean isTempDisabled() {
        return this.tempDisabled;
    }

    public void setTempDisabled(boolean bl) {
        this.tempDisabled = bl;
    }

    public double getTpsDisable() {
        return this.tpsDisable;
    }

    public boolean isActuallyEnabled() {
        return this.enabled;
    }

    protected BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask((Plugin)AntiCheat.Instance, runnable);
    }

    protected BukkitTask runTaskLater(Runnable runnable, long l) {
        return Bukkit.getScheduler().runTaskLater((Plugin)AntiCheat.Instance, runnable, l);
    }

    protected BukkitTask runTaskTimer(Runnable runnable, long l, long l2) {
        return Bukkit.getScheduler().runTaskTimer((Plugin)AntiCheat.Instance, runnable, l, l2);
    }

    protected void registerPacketReceiving(PacketType packetType, final Consumer<PacketEvent> consumer) {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)AntiCheat.Instance, new PacketType[]{packetType}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                consumer.accept(packetEvent);
            }
        });
    }

    protected void registerPacketSending(PacketType packetType, final Consumer<PacketEvent> consumer) {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)AntiCheat.Instance, new PacketType[]{packetType}){

            public void onPacketSending(PacketEvent packetEvent) {
                consumer.accept(packetEvent);
            }
        });
    }

    public void save(Config config) {
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".enabled", (Object)this.enabled);
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".name", (Object)this.getDisplayName());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".threshold", (Object)this.getThreshold());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".level", (Object)this.getLevel());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".expiration", (Object)this.getExpiration());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".bans", (Object)this.getBans());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".timer", (Object)this.getAutobanTimer());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".id", (Object)this.getId());
        config.getConfig().set(String.valueOf(String.valueOf(string)) + ".tps", (Object)this.getTpsDisable());
        config.save();
    }

    public void load(Config config) {
        String string = "checks." + this.getCheckType().name().toLowerCase() + "." + this.getName().toLowerCase();
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".enabled")) {
            this.setEnabled(config.getConfig().getBoolean(String.valueOf(String.valueOf(string)) + ".enabled"));
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".name")) {
            this.displayName = config.getConfig().getString(String.valueOf(String.valueOf(string)) + ".name");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".threshold")) {
            this.threshold = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".threshold");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".level")) {
            this.level = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".level");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".expiration")) {
            this.expiration = config.getConfig().getLong(String.valueOf(String.valueOf(string)) + ".expiration");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".bans")) {
            this.bans = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".bans");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".timer")) {
            this.autobanTimer = config.getConfig().getInt(String.valueOf(String.valueOf(string)) + ".timer");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".id")) {
            this.id = config.getConfig().getString(String.valueOf(String.valueOf(string)) + ".id");
        }
        if (config.getConfig().contains(String.valueOf(String.valueOf(string)) + ".tps")) {
            this.tpsDisable = config.getConfig().getDouble(String.valueOf(String.valueOf(string)) + ".tps");
        }
    }

}


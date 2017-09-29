/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.reflect.StructureModifier
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.Messenger
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.bukkit.plugin.messaging.PluginMessageListenerRegistration
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package rip.anticheat.anticheat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import rip.anticheat.anticheat.AlertType;
import rip.anticheat.anticheat.Autoban;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.ServerVersion;
import rip.anticheat.anticheat.Tracker;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.api.events.PlayerViolationEvent;
import rip.anticheat.anticheat.api.events.ViolationBroadcastEvent;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.checks.autoclicker.APS;
import rip.anticheat.anticheat.checks.autoclicker.AutoDetection0x01;
import rip.anticheat.anticheat.checks.autoclicker.AutoDetection0x218;
import rip.anticheat.anticheat.checks.autoclicker.CPS;
import rip.anticheat.anticheat.checks.autoclicker.PatternDetection;
import rip.anticheat.anticheat.checks.combat.FastBow;
import rip.anticheat.anticheat.checks.combat.Regen;
import rip.anticheat.anticheat.checks.exploits.PingSpoof;
import rip.anticheat.anticheat.checks.huzuni.HuzuniAura;
import rip.anticheat.anticheat.checks.killaura.Turn;
import rip.anticheat.anticheat.checks.killaura.YawRate;
import rip.anticheat.anticheat.checks.killaura.heuristic.AimAssist;
import rip.anticheat.anticheat.checks.killaura.heuristic.PacketCount18;
import rip.anticheat.anticheat.checks.killaura.heuristic.SmoothAim;
import rip.anticheat.anticheat.checks.killaura.pattern.PatternA;
import rip.anticheat.anticheat.checks.killaura.reach.DataReach;
import rip.anticheat.anticheat.checks.killaura.reach.HeuristicFlows;
import rip.anticheat.anticheat.checks.killaura.reach.Heuristics;
import rip.anticheat.anticheat.checks.killaura.reach.ReachA;
import rip.anticheat.anticheat.checks.killaura.reach.ReachB;
import rip.anticheat.anticheat.checks.movement.Ascension;
import rip.anticheat.anticheat.checks.movement.Change;
import rip.anticheat.anticheat.checks.movement.Fly;
import rip.anticheat.anticheat.checks.movement.FlyB;
import rip.anticheat.anticheat.checks.movement.Glide;
import rip.anticheat.anticheat.checks.movement.HClip;
import rip.anticheat.anticheat.checks.movement.NoFall;
import rip.anticheat.anticheat.checks.movement.NoSlow;
import rip.anticheat.anticheat.checks.movement.Step;
import rip.anticheat.anticheat.checks.movement.Timer;
import rip.anticheat.anticheat.checks.movement.VClip;
import rip.anticheat.anticheat.checks.movement.jesus.JesusA;
import rip.anticheat.anticheat.checks.movement.jesus.JesusB;
import rip.anticheat.anticheat.checks.movement.phase.PhaseA;
import rip.anticheat.anticheat.checks.movement.speed.SpeedA;
import rip.anticheat.anticheat.checks.movement.speed.SpeedB;
import rip.anticheat.anticheat.checks.packet.AuraA;
import rip.anticheat.anticheat.checks.pme.ClientPME;
import rip.anticheat.anticheat.checks.spook.SpookListener;
import rip.anticheat.anticheat.checks.vape.AutoClickerV5x01;
import rip.anticheat.anticheat.checks.vape.AutoClickerV5x0118;
import rip.anticheat.anticheat.checks.vape.VapeListener;
import rip.anticheat.anticheat.commands.old.AntiCheatCommand;
import rip.anticheat.anticheat.commands.simple.AnticheatGuiCommand;
import rip.anticheat.anticheat.commands.simple.CheatLogsCommand;
import rip.anticheat.anticheat.events.PacketPlayerEvent;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;
import rip.anticheat.anticheat.events.handler.VelocityPacketHandler;
import rip.anticheat.anticheat.jday.JudgementDays;
import rip.anticheat.anticheat.listeners.Listener1;
import rip.anticheat.anticheat.update.Updater;
import rip.anticheat.anticheat.util.formatting.ChatUtil;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.Config;
import rip.anticheat.anticheat.util.misc.MessagesYml;
import rip.anticheat.anticheat.util.misc.ServerUtil;

public class AntiCheat
extends JavaPlugin
implements Listener {
    public static AntiCheat Instance;
    public Config Config;
    public boolean notifyConsole = true;
    public boolean crashBan = true;
    public boolean crashAnimationSpamEnabled = true;
    public int crashAnimationSpam = 3000;
    public boolean crashBlockSpamEnabled = true;
    public int crashBlockSpam = 3000;
    public String banCommand = "ban %player% Cheating";
    public List<String> banMessages = new ArrayList<String>();
    public JudgementDays JDays;
    public Autoban Autoban;
    public MessagesYml Messages;
    public List<Check> Checks = new ArrayList<Check>();
    public Map<UUID, PlayerStats> PlayerStats = new HashMap<UUID, PlayerStats>();
    public static String VERSION;
    public static ServerVersion SERVER_VERSION;
    private Map<AlertType, List<Player>> Alerts = new HashMap<AlertType, List<Player>>();
    private static String UUID;
    private int currentTick;
    private int ticksPassed;
    private static /* synthetic */ int[] $SWITCH_TABLE$rip$anticheat$anticheat$ViolationPriority;

    static {
        VERSION = "skidded release v1337";
        SERVER_VERSION = null;
        UUID = "erouax is a faggot -imrowin";
    }

    public void onEnable() {
        File file = new File(this.getDataFolder() + "/logs");
        file.mkdirs();
        String string = this.getServer().getClass().getName().split("org.bukkit.craftbukkit.")[1];
        Instance = this;
        Instance.RegisterListener(this);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new VelocityPacketHandler((Plugin)this));
        new rip.anticheat.anticheat.update.Updater(this);
        new rip.anticheat.anticheat.listeners.Listener1(this);
        this.getCommand("anticheat").setExecutor((CommandExecutor)new AntiCheatCommand(this));
        this.getCommand("logs").setExecutor((CommandExecutor)new CheatLogsCommand());
        this.Config = new Config(this, "", "config");
        this.Config.setDefault("notify-console", true);
        this.Config.setDefault("bans-overall", 0);
        this.Config.setDefault("ban.command", "ban %player% Cheating");
        this.Config.setDefault("ban.messages", Arrays.asList("&cAntiCheat &7has detected &c%player% &7Cheating!"));
        this.Config.setDefault("crash.ban", true);
        this.Config.setDefault("crash.animation.enabled", true);
        this.Config.setDefault("crash.animation.spam", 3000);
        this.Config.setDefault("crash.blockplacement.enabled", true);
        this.Config.setDefault("crash.blockplacement.spam", 3000);
        this.notifyConsole = this.getMainConfig().getConfig().getBoolean("notify-console");
        this.crashBan = this.Config.getConfig().getBoolean("crash.ban");
        this.crashAnimationSpamEnabled = this.Config.getConfig().getBoolean("crash.animation.enabled");
        this.crashAnimationSpam = this.Config.getConfig().getInt("crash.animation.spam");
        this.crashBlockSpamEnabled = this.Config.getConfig().getBoolean("crash.blockplacement.enabled");
        this.crashBlockSpam = this.Config.getConfig().getInt("crash.blockplacement.spam");
        this.banCommand = this.Config.getConfig().getString("ban.command");
        this.banMessages = this.Config.getConfig().getStringList("ban.messages");
        this.Messages = new MessagesYml(this);
        this.Autoban = new Autoban(this);
        this.runCheckManagment();
        VapeListener vapeListener = new VapeListener(this);
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "LOLIMAHCKER", (PluginMessageListener)vapeListener);
        this.getServer().getPluginManager().registerEvents((Listener)vapeListener, (Plugin)this);
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "BungeeCord", (PluginMessageListener)new ClientPME(this));
        this.getServer().getPluginManager().registerEvents((Listener)new ClientPME(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SpookListener(), (Plugin)this);
        AlertType[] arralertType = AlertType.values();
        int n = arralertType.length;
        int n2 = 0;
        while (n2 < n) {
            AlertType alertType = arralertType[n2];
            this.Alerts.put(alertType, new ArrayList());
            ++n2;
        }
        Player[] arrplayer = Bukkit.getServer().getOnlinePlayers();
        int n3 = arrplayer.length;
        n2 = 0;
        while (n2 < n3) {
            Player player = arrplayer[n2];
            if (player.hasPermission("anticheat.staff")) {
                this.toggle(AlertType.NORMAL, player);
            }
            ++n2;
        }
        new BukkitRunnable(){

            public void run() {
                AntiCheat.access$1(AntiCheat.this, AntiCheat.this.currentTick >= 20 ? 1 : AntiCheat.this.currentTick + 1);
                AntiCheat antiCheat = AntiCheat.this;
                AntiCheat.access$3(antiCheat, antiCheat.ticksPassed + 1);
            }
        }.runTaskTimer((Plugin)this, 1, 1);
        new BukkitRunnable(){

            public void run() {
                Bukkit.broadcast((String)AntiCheat.this.Messages.getMessage("tps").replace("%tps%", Common.FORMAT_0x00.format(ServerUtil.getTps()[0])), (String)"anticheat.staff");
            }
        }.runTaskTimerAsynchronously((Plugin)this, 0, 7200);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.ARM_ANIMATION}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                PlayerStats playerStats = AntiCheat.this.getPlayerStats(player);
                if (AntiCheat.this.crashAnimationSpamEnabled) {
                    if (playerStats.getAnimationSpam() == -1) {
                        packetEvent.setCancelled(true);
                    }
                    int n = AntiCheat.this.crashAnimationSpam;
                    boolean bl = AntiCheat.this.crashBan;
                    long l = System.currentTimeMillis() - playerStats.getLastAnimation();
                    if (l < 5) {
                        playerStats.setAnimationSpam(playerStats.getAnimationSpam() + 1);
                        if (playerStats.getAnimationSpam() > n) {
                            playerStats.setAnimationSpam(-1);
                            packetEvent.setCancelled(true);
                            AntiCheat.this.log(String.valueOf(String.valueOf(player.getName())) + " attempted Crash Exploit (Animation)");
                            if (bl) {
                                AntiCheat.this.getAutoban().forceban(player);
                            } else {
                                ServerUtil.asyncKick(player, "Timed out");
                            }
                        }
                    } else {
                        playerStats.setAnimationSpam(0);
                    }
                }
                playerStats.setLastAnimation(System.currentTimeMillis());
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.BLOCK_PLACE}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                PlayerStats playerStats = AntiCheat.this.getPlayerStats(player);
                if (AntiCheat.this.crashBlockSpamEnabled) {
                    if (playerStats.getBlockPlacementSpam() == -1) {
                        packetEvent.setCancelled(true);
                    }
                    int n = AntiCheat.this.crashBlockSpam;
                    boolean bl = AntiCheat.this.crashBan;
                    long l = System.currentTimeMillis() - playerStats.getLastBlockPlacementPacket();
                    if (l < 5) {
                        playerStats.setBlockPlacementSpam(playerStats.getBlockPlacementSpam() + 1);
                        if (playerStats.getBlockPlacementSpam() > n) {
                            playerStats.setBlockPlacementSpam(-1);
                            packetEvent.setCancelled(true);
                            AntiCheat.this.log(String.valueOf(String.valueOf(player.getName())) + " attempted Crash Exploit (Block Placement)");
                            if (bl) {
                                AntiCheat.this.getAutoban().forceban(player);
                            } else {
                                ServerUtil.asyncKick(player, "Timed out");
                            }
                        }
                    } else {
                        playerStats.setBlockPlacementSpam(0);
                    }
                }
                playerStats.setLastBlockPlacementPacket(System.currentTimeMillis());
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.POSITION_LOOK}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (Double)packetEvent.getPacket().getDoubles().read(0), (Double)packetEvent.getPacket().getDoubles().read(1), (Double)packetEvent.getPacket().getDoubles().read(2), ((Float)packetEvent.getPacket().getFloat().read(0)).floatValue(), ((Float)packetEvent.getPacket().getFloat().read(1)).floatValue()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.LOOK}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), ((Float)packetEvent.getPacket().getFloat().read(0)).floatValue(), ((Float)packetEvent.getPacket().getFloat().read(1)).floatValue()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.POSITION}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (Double)packetEvent.getPacket().getDoubles().read(0), (Double)packetEvent.getPacket().getDoubles().read(1), (Double)packetEvent.getPacket().getDoubles().read(2), player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.FLYING}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.KEEP_ALIVE}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                PlayerStats playerStats = AntiCheat.this.getPlayerStats(player);
                playerStats.setLastReceivedKeepAlive(System.currentTimeMillis());
                playerStats.setLastReceivedKeepAliveID((Integer)packetEvent.getPacket().getIntegers().read(0));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Server.KEEP_ALIVE}){

            public void onPacketSending(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                PlayerStats playerStats = AntiCheat.this.getPlayerStats(player);
                playerStats.setLastSentKeepAlive(System.currentTimeMillis());
                playerStats.setLastSentKeepAliveID((Integer)packetEvent.getPacket().getIntegers().read(0));
            }
        });
        final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        new BukkitRunnable(){

            public void run() {
                for (Player player : concurrentHashMap.keySet()) {
                    if (player.isOnline()) continue;
                    concurrentHashMap.remove((Object)player);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)this, 20, 20);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)this, new PacketType[]{PacketType.Play.Client.USE_ENTITY}){

            public void onPacketReceiving(PacketEvent packetEvent) {
                PacketContainer packetContainer = packetEvent.getPacket();
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                EnumWrappers.EntityUseAction entityUseAction = null;
                try {
                    entityUseAction = (EnumWrappers.EntityUseAction)packetContainer.getEntityUseActions().read(0);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (entityUseAction == null) {
                    return;
                }
                int n = (Integer)packetContainer.getIntegers().read(0);
                Entity entity = null;
                for (Entity entity2 : ServerUtil.getEntities(player.getWorld())) {
                    if (entity2.getEntityId() != n) continue;
                    entity = entity2;
                }
                long l = -1;
                if (concurrentHashMap.containsKey((Object)player)) {
                    l = (Long)concurrentHashMap.get((Object)player);
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketUseEntityEvent(entityUseAction, player, entity, l));
            }
        });
        this.Checks.add(new NoSlow(this));
        this.Checks.add(new JesusA(this));
        this.Checks.add(new JesusB(this));
        this.Checks.add(new Ascension(this));
        this.Checks.add(new Fly(this));
        this.Checks.add(new Glide(this));
        this.Checks.add(new VClip(this));
        this.Checks.add(new HClip(this));
        if (!new File(this.getDataFolder(), "phase.yml").exists()) {
            this.saveResource("phase.yml", true);
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)new File(this.getDataFolder(), "phase.yml"));
        this.Checks.add(new PhaseA(this, yamlConfiguration));
        this.Checks.add(new SpeedA(this));
        this.Checks.add(new SpeedB(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new Change(this));
        this.Checks.add(new Step(this));
        this.Checks.add(new NoFall(this));
        this.Checks.add(new ReachA(this));
        this.Checks.add(new ReachB(this));
        this.Checks.add(new FastBow(this));
        this.Checks.add(new Regen(this));
        this.Checks.add(new CPS(this));
        this.Checks.add(new APS(this));
        this.Checks.add(new PatternA(this));
        this.Checks.add(new VapeListener(this));
        this.Checks.add(new YawRate(this));
        this.Checks.add(new AutoDetection0x01(this));
        this.Checks.add(new AutoClickerV5x01(this));
        this.Checks.add(new AutoDetection0x218(this));
        this.Checks.add(new AutoClickerV5x0118(this));
        this.Checks.add(new PacketCount18(this));
        this.Checks.add(new AuraA(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new PingSpoof(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new AuraA(this));
        this.Checks.add(new DataReach(this));
        this.Checks.add(new FlyB(this));
        this.Checks.add(new HuzuniAura(this));
        this.Checks.add(new ClientPME(this));
        this.Checks.add(new PatternDetection(this));
        this.Checks.add(new Heuristics(this));
        this.Checks.add(new HeuristicFlows(this));
        this.Checks.add(new Turn(this));
        this.Checks.add(new SmoothAim(this));
        this.Checks.add(new AimAssist(this));
        for (Check check : this.Checks) {
            this.RegisterListener(check);
            check.load(this.getMainConfig());
            check.save(this.getMainConfig());
        }
        this.JDays = new JudgementDays(this);
        Tracker.load();
        new BukkitRunnable(){

            public void run() {
                Bukkit.broadcastMessage((String)AntiCheat.this.Messages.getMessage("broadcast").replace("%today%", String.valueOf(Tracker.bansToday)).replace("%total%", String.valueOf(Tracker.bansOverall)));
            }
        }.runTaskTimerAsynchronously((Plugin)this, 36000, 36000);
        this.getCommand("cheatlogs").setExecutor((CommandExecutor)new CheatLogsCommand());
        this.getCommand("anticheatgui").setExecutor((CommandExecutor)new AnticheatGuiCommand((Plugin)this));
    }

    public void onDisable() {
        this.getJudgementDays().saveConfig();
        Tracker.save();
        this.getMainConfig().save();
    }

    public void reloadConfig() {
        this.getMainConfig().reload();
        this.getMessages().load();
        this.getJudgementDays().loadConfig();
        for (Check check : this.getChecks()) {
            check.load(this.getMainConfig());
            check.save(this.getMainConfig());
        }
        this.notifyConsole = this.getMainConfig().getConfig().getBoolean("notify-console");
        this.crashBan = this.Config.getConfig().getBoolean("crash.ban");
        this.crashAnimationSpamEnabled = this.Config.getConfig().getBoolean("crash.animation.enabled");
        this.crashAnimationSpam = this.Config.getConfig().getInt("crash.animation.spam");
        this.crashBlockSpamEnabled = this.Config.getConfig().getBoolean("crash.blockplacement.enabled");
        this.crashBlockSpam = this.Config.getConfig().getInt("crash.blockplacement.spam");
        this.banCommand = this.Config.getConfig().getString("ban.command");
        this.banMessages = this.Config.getConfig().getStringList("ban.messages");
    }

    @EventHandler
    public void Join(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        PlayerStats playerStats = this.getPlayerStats(player);
        playerStats.setAnimationSpam(0);
        playerStats.setBlockPlacementSpam(0);
        playerStats.setLastReceivedKeepAlive(0);
        playerStats.setLastReceivedKeepAliveID(0);
        playerStats.setLastSentKeepAlive(0);
        playerStats.setLastSentKeepAliveID(0);
        if (player.hasPermission("anticheat.staff")) {
            this.toggle(AlertType.NORMAL, player);
        }
    }

    @EventHandler
    public void Quit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        PlayerStats playerStats = this.getPlayerStats(player);
        playerStats.setAnimationSpam(0);
        playerStats.setBlockPlacementSpam(0);
        playerStats.setLastReceivedKeepAlive(0);
        playerStats.setLastReceivedKeepAliveID(0);
        playerStats.setLastSentKeepAlive(0);
        playerStats.setLastSentKeepAliveID(0);
    }

    public void log(String string) {
        if (this.notifyConsole) {
            System.out.println(string);
        }
    }

    public JudgementDays getJudgementDays() {
        return this.JDays;
    }

    public Autoban getAutoban() {
        return this.Autoban;
    }

    public MessagesYml getMessages() {
        return this.Messages;
    }

    public Config getMainConfig() {
        return this.Config;
    }

    public List<Check> getChecks() {
        return new ArrayList<Check>(this.Checks);
    }

    public Check getCheck(String string) {
        for (Check check : this.getChecks()) {
            if (!check.getName().equalsIgnoreCase(string)) continue;
            return check;
        }
        return null;
    }

    public List<Player> getPlayers(AlertType alertType) {
        return new ArrayList<Player>((Collection)this.Alerts.get((Object)alertType));
    }

    private void runCheckManagment() {
    }

    public void toggle(AlertType alertType, Player player) {
        List<Player> list = this.getPlayers(alertType);
        if (list.contains((Object)player)) {
            this.Alerts.get((Object)alertType).remove((Object)player);
        } else {
            this.Alerts.get((Object)alertType).add(player);
        }
    }

    public PlayerStats getPlayerStats(UUID uUID) {
        if (this.PlayerStats.containsKey(uUID)) {
            return this.PlayerStats.get(uUID);
        }
        PlayerStats playerStats = new PlayerStats(uUID);
        this.PlayerStats.put(uUID, playerStats);
        return playerStats;
    }

    public PlayerStats getPlayerStats(Player player) {
        return this.getPlayerStats(player.getUniqueId());
    }

    public void addBan(Player player, Check check, Violation violation) {
        if (check.getBans() <= 0) {
            return;
        }
        PlayerStats playerStats = this.getPlayerStats(player);
        String string = "[BAN] " + player.getName() + " failed " + check.getDisplayName();
        playerStats.addBan(check);
        String string2 = this.getMessages().getMessage("alerts.normal.admins").replaceAll("%message%", violation.getMessage()).replaceAll("%vio%", "" + playerStats.getViolations(check).size()).replaceAll("%player%", player.getName()).replaceAll("%check%", check.getDisplayName()).replaceAll("%ping%", String.valueOf(ServerUtil.getPing(player)));
        String string3 = this.getMessages().getMessage("alerts.normal.staff").replaceAll("%message%", violation.getMessage()).replaceAll("%vio%", "" + playerStats.getViolations(check).size()).replaceAll("%player%", player.getName()).replaceAll("%check%", check.getDisplayName()).replaceAll("%ping%", String.valueOf(ServerUtil.getPing(player)));
        List<Player> list = this.getPlayers(AlertType.NORMAL);
        for (Player player2 : list) {
            player2.sendMessage(player2.hasPermission("anticheat.admin") ? string2 : string3);
        }
        if (playerStats.getBans(check) > check.getBans()) {
            this.getAutoban().ban(check, player);
        }
        this.log(string);
    }

    public void addViolation(Player player, Check check, Violation violation) {
        PlayerStats playerStats = this.getPlayerStats(player);
        playerStats.addViolation(check, violation);
        String string = "[" + violation.getPriority().toString() + "] " + player.getName() + " failed " + check.getDisplayName() + (violation.getMessage().isEmpty() ? "" : new StringBuilder(": ").append(violation.getMessage()).toString()) + " " + ServerUtil.getPing(player) + "ms";
        int n = 0;
        for (Violation object2 : playerStats.getViolations(check)) {
            if (object2.isUnused()) continue;
            if (object2.getDiffWhenCreated() > object2.getCheck().getExpiration()) {
                object2.setUnused(true);
                continue;
            }
            switch (AntiCheat.$SWITCH_TABLE$rip$anticheat$anticheat$ViolationPriority()[object2.getPriority().ordinal()]) {
                default: {
                    break;
                }
                case 3: {
                    n += 10;
                    break;
                }
                case 4: {
                    n += 20;
                    break;
                }
                case 5: {
                    n += 30;
                    break;
                }
                case 6: {
                    n += 50;
                }
            }
        }
        String string2 = this.getMessages().getMessage("alerts.advanced.admins").replaceAll("%player%", player.getName()).replaceAll("%check%", check.getDisplayName()).replaceAll("%vio%", String.valueOf(playerStats.getViolations(check).size())).replaceAll("%message%", violation.getMessage()).replaceAll("%vl%", String.valueOf(n)).replaceAll("%priority%", violation.getPriority().name()).replaceAll("%ping%", String.valueOf(ServerUtil.getPing(player)));
        String string22 = this.getMessages().getMessage("alerts.advanced.staff").replaceAll("%player%", player.getName()).replaceAll("%check%", check.getDisplayName()).replaceAll("%vio%", String.valueOf(playerStats.getViolations(check).size())).replaceAll("%message%", violation.getMessage()).replaceAll("%vl%", String.valueOf(n)).replaceAll("%priority%", violation.getPriority().name()).replaceAll("%ping%", String.valueOf(ServerUtil.getPing(player)));
        List<Player> list = this.getPlayers(AlertType.ADVANCED);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss (a) ");
        this.dumpCheat(player, String.valueOf(simpleDateFormat.format(date)) + "[" + player.getUniqueId() + "] " + player.getName() + " is using " + check.getName() + " [" + String.valueOf(playerStats.getViolations(check).size()) + "]");
        ViolationBroadcastEvent violationBroadcastEvent = new ViolationBroadcastEvent(player, check.getCheckType());
        Bukkit.getPluginManager().callEvent((Event)violationBroadcastEvent);
        if (!violationBroadcastEvent.isCancelled()) {
            for (Player player2 : list) {
                player2.sendMessage(player2.hasPermission("anticheat.admin") ? string2 : string22);
            }
            if (n >= check.getLevel()) {
                if (check.getBans() > 0) {
                    this.addBan(player, check, violation);
                }
                if (playerStats.getViolations(check) != null) {
                    for (Violation violation2 : playerStats.getViolations(check)) {
                        violation2.setUnused(true);
                    }
                }
            }
        }
        Bukkit.getPluginManager().callEvent((Event)new PlayerViolationEvent(player, check.getCheckType(), string2));
        this.log(string);
    }

    public void RegisterListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, (Plugin)this);
    }

    public Map<UUID, PlayerStats> getPlayerStats() {
        return this.PlayerStats;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public int getTicksPassed() {
        return this.ticksPassed;
    }

    private void dumpCheat(Player player, String string) {
        File file = new File(this.getDataFolder() + "/logs/" + player.getUniqueId() + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                System.out.println("[Anticheat] Failed trying to create a logs file.");
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(string);
            printWriter.close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void attemptToRead(Player player, String string) {
        File file = new File(this.getDataFolder() + "/logs/" + string + ".txt");
        if (!file.exists()) {
            player.sendMessage(ChatUtil.colorize("&7[&cAnticheat&7] &7Can't find &c" + string + "&7's logs."));
        }
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            try {
                String string2;
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                bufferedReader = new BufferedReader(new FileReader(file));
                player.sendMessage(ChatUtil.colorize("&7[&cAnticheat&7] &7Violations found on db for &c" + string));
                while ((string2 = bufferedReader.readLine()) != null) {
                    player.sendMessage(string2);
                }
            }
            catch (IOException iOException) {
                player.sendMessage(ChatUtil.colorize("&7[&cAnticheat&7] &7Can't find &c" + string + "&7's logs."));
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                }
                catch (IOException iOException2) {
                    player.sendMessage(ChatUtil.colorize("&7[&cAnticheat&7] &7Can't find &c" + string + "&7's logs."));
                }
            }
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            }
            catch (IOException iOException) {
                player.sendMessage(ChatUtil.colorize("&7[&cAnticheat&7] &7Can't find &c" + string + "&7's logs."));
            }
        }
    }

    static /* synthetic */ void access$1(AntiCheat antiCheat, int n) {
        antiCheat.currentTick = n;
    }

    static /* synthetic */ void access$3(AntiCheat antiCheat, int n) {
        antiCheat.ticksPassed = n;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$rip$anticheat$anticheat$ViolationPriority() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$rip$anticheat$anticheat$ViolationPriority;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[ViolationPriority.values().length];
        try {
            arrn[ViolationPriority.HIGH.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[ViolationPriority.HIGHEST.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[ViolationPriority.LOW.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[ViolationPriority.LOWEST.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[ViolationPriority.MEDIUM.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[ViolationPriority.TEST.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$rip$anticheat$anticheat$ViolationPriority = arrn;
        return $SWITCH_TABLE$rip$anticheat$anticheat$ViolationPriority;
    }

}


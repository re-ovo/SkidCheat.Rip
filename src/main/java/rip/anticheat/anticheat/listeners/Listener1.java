/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerVelocityEvent
 *  org.bukkit.util.Vector
 *  org.spigotmc.event.entity.EntityMountEvent
 */
package rip.anticheat.anticheat.listeners;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityMountEvent;
import rip.anticheat.anticheat.AlertType;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.events.PacketPlayerEvent;
import rip.anticheat.anticheat.util.misc.MessagesYml;
import rip.anticheat.anticheat.util.misc.PlayerUtil;

public class Listener1
implements Listener {
    private AntiCheat Core;
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause;

    public Listener1(AntiCheat antiCheat) {
        this.Core = antiCheat;
        this.Core.RegisterListener(this);
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent packetPlayerEvent) {
        Player player = packetPlayerEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        if (playerStats.getLastPlayerPacketDiff() > 200) {
            playerStats.setLastDelayedPacket(System.currentTimeMillis());
        }
        playerStats.setLastPlayerPacket(System.currentTimeMillis());
    }

    @EventHandler
    public void Mount(EntityMountEvent entityMountEvent) {
        if (!(entityMountEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityMountEvent.getEntity();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        playerStats.setLastMount(System.currentTimeMillis());
    }

    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent playerChangedWorldEvent) {
        Player player = playerChangedWorldEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        playerStats.setLastWorldChange(System.currentTimeMillis());
    }

    @EventHandler
    public void Join(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        File file = new File(AntiCheat.Instance.getDataFolder() + "/logs/" + player.getUniqueId() + ".txt");
        try {
            if (file.createNewFile()) {
                System.out.println("[Anticheat] I created a logs file.");
            }
        }
        catch (IOException iOException) {
            System.out.println("[Anticheat] Failed trying to create a logs file.");
        }
        playerStats.setLastWorldChange(System.currentTimeMillis());
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        if (PlayerUtil.isOnGround(playerMoveEvent.getTo(), 0) || PlayerUtil.isOnGround(playerMoveEvent.getTo(), 1)) {
            if (!playerStats.isOnGround()) {
                playerStats.setLastGround(null);
            }
            playerStats.setOnGround(true);
            playerStats.setLastGroundTime(System.currentTimeMillis());
        } else {
            if (playerStats.isOnGround()) {
                playerStats.setLastGround(player.getLocation());
            }
            playerStats.setOnGround(false);
            playerStats.setLastBunnyTime(System.currentTimeMillis());
        }
        if (playerMoveEvent.getFrom().getY() != playerMoveEvent.getTo().getY() && playerStats.getVelocityY() > 0.0) {
            playerStats.setVelocityY(playerStats.getVelocityY() - 1.0);
        }
        if ((playerMoveEvent.getFrom().getX() != playerMoveEvent.getTo().getX() || playerMoveEvent.getFrom().getZ() != playerMoveEvent.getTo().getZ()) && playerStats.getVelocityXZ() > 0.0) {
            playerStats.setVelocityXZ(playerStats.getVelocityXZ() - 1.0);
        }
    }

    @EventHandler
    public void Velocity(PlayerVelocityEvent playerVelocityEvent) {
        Player player = playerVelocityEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        Vector vector = playerVelocityEvent.getVelocity();
        double d = Math.abs(vector.getX());
        double d2 = Math.abs(vector.getY());
        double d3 = Math.abs(vector.getZ());
        if (player.getLastDamageCause() != null) {
            switch (Listener1.$SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause()[player.getLastDamageCause().getCause().ordinal()]) {
                case 5: {
                    return;
                }
                case 9: {
                    return;
                }
                case 10: {
                    return;
                }
                case 17: {
                    return;
                }
                case 19: {
                    return;
                }
                case 6: {
                    return;
                }
                case 16: 
                case 21: {
                    return;
                }
            }
        }
        if (d2 > 0.0) {
            double d4 = (int)(Math.pow(d2 + 2.0, 2.0) * 5.0);
            playerStats.setVelocityY(d4);
        }
        if (d > 0.0 || d3 > 0.0) {
            int n = (int)(((d + d3) / 2.0 + 2.0) * 10.0);
            playerStats.setVelocityXZ(n);
        }
        playerStats.setVelocityTime(System.currentTimeMillis());
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        if (playerKickEvent.getReason().contains("Flying is not enabled")) {
            List<Player> list = AntiCheat.Instance.getPlayers(AlertType.NORMAL);
            for (Player player : list) {
                player.sendMessage(AntiCheat.Instance.getMessages().getMessage("nofly").replaceAll("%player%", playerKickEvent.getPlayer().getName()));
            }
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[EntityDamageEvent.DamageCause.values().length];
        try {
            arrn[EntityDamageEvent.DamageCause.BLOCK_EXPLOSION.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.CONTACT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.CUSTOM.ordinal()] = 22;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.DROWNING.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.ENTITY_ATTACK.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.ENTITY_EXPLOSION.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.FALL.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.FALLING_BLOCK.ordinal()] = 20;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.FIRE.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.FIRE_TICK.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.LAVA.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.LIGHTNING.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.MAGIC.ordinal()] = 18;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.MELTING.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.POISON.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.PROJECTILE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.STARVATION.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.SUFFOCATION.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.SUICIDE.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.THORNS.ordinal()] = 21;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.VOID.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EntityDamageEvent.DamageCause.WITHER.ordinal()] = 19;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause = arrn;
        return $SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause;
    }
}


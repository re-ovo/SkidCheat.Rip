/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerAnimationEvent
 *  org.bukkit.event.player.PlayerAnimationType
 *  org.bukkit.potion.PotionEffectType
 */
package rip.anticheat.anticheat.listeners;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.potion.PotionEffectType;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;
import rip.anticheat.anticheat.events.PlayerClickEvaluateEvent;
import rip.anticheat.anticheat.update.UpdateType;
import rip.anticheat.anticheat.update.events.UpdateEvent;

public class CombatListener
implements Listener {
    @EventHandler
    public void onPlayerPacket(PlayerAnimationEvent playerAnimationEvent) {
        if (!playerAnimationEvent.getAnimationType().equals((Object)PlayerAnimationType.ARM_SWING)) {
            return;
        }
        Player player = playerAnimationEvent.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            return;
        }
        PlayerStats playerStats = AntiCheat.Instance.getPlayerStats(player);
        playerStats.setHits(playerStats.getClicks() + 1);
    }

    @EventHandler
    public void onPlayerPacket(PacketUseEntityEvent packetUseEntityEvent) {
        EnumWrappers.EntityUseAction entityUseAction;
        Player player = packetUseEntityEvent.getAttacker();
        if (packetUseEntityEvent.getAttacked() instanceof Player && (entityUseAction = packetUseEntityEvent.getAction()) == EnumWrappers.EntityUseAction.ATTACK) {
            PlayerStats playerStats = AntiCheat.Instance.getPlayerStats(player);
            playerStats.setHits(playerStats.getHits() + 1);
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent updateEvent) {
        if (updateEvent.getType() == UpdateType.SEC) {
            Player[] arrplayer = Bukkit.getOnlinePlayers();
            int n = arrplayer.length;
            int n2 = 0;
            while (n2 < n) {
                Player player = arrplayer[n2];
                PlayerStats playerStats = AntiCheat.Instance.getPlayerStats(player);
                int n3 = playerStats.getClicks();
                int n4 = playerStats.getHits();
                Bukkit.getPluginManager().callEvent((Event)new PlayerClickEvaluateEvent(player, playerStats, n3, n4));
                playerStats.setHits(0);
                playerStats.setClicks(0);
                ++n2;
            }
        }
    }
}


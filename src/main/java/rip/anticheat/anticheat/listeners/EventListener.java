/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener
implements Listener {
    @EventHandler(ignoreCancelled=1)
    public void onAttack(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof Player)) {
            return;
        }
        PlayerAttackEvent playerAttackEvent = new PlayerAttackEvent((Player)entity, entityDamageByEntityEvent.getEntity());
        Bukkit.getPluginManager().callEvent((Event)playerAttackEvent);
        if (playerAttackEvent.isCancelled()) {
            entityDamageByEntityEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Bukkit.getPluginManager().callEvent((Event)new PlayerDisconnectEvent(playerQuitEvent.getPlayer()));
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        Bukkit.getPluginManager().callEvent((Event)new PlayerDisconnectEvent(playerKickEvent.getPlayer()));
    }

    public static class PlayerAttackEvent
    extends Event
    implements Cancellable {
        private final Player player;
        private final Entity entity;
        private boolean isCancelled;
        private static final HandlerList HANDLERS = new HandlerList();

        public PlayerAttackEvent(Player player, Entity entity) {
            this.player = player;
            this.entity = entity;
            this.isCancelled = false;
        }

        public boolean isCancelled() {
            return this.isCancelled;
        }

        public void setCancelled(boolean bl) {
            this.isCancelled = bl;
        }

        public HandlerList getHandlers() {
            return HANDLERS;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        public Player getPlayer() {
            return this.player;
        }

        public Entity getEntity() {
            return this.entity;
        }
    }

    public static class PlayerDisconnectEvent
    extends Event {
        private final Player player;
        private static final HandlerList HANDLERS = new HandlerList();

        public PlayerDisconnectEvent(Player player) {
            this.player = player;
        }

        public HandlerList getHandlers() {
            return HANDLERS;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        public Player getPlayer() {
            return this.player;
        }
    }

}


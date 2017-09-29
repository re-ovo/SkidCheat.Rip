/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package rip.anticheat.anticheat.events;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketHandleEvent
extends Event {
    public EnumWrappers.EntityUseAction Action;
    public Player Attacker;
    public Entity Attacked;
    private static final HandlerList handlers = new HandlerList();

    public PacketHandleEvent(EnumWrappers.EntityUseAction entityUseAction, Player player, Entity entity) {
        this.Action = entityUseAction;
        this.Attacker = player;
        this.Attacked = entity;
    }

    public EnumWrappers.EntityUseAction getAction() {
        return this.Action;
    }

    public Player getAttacker() {
        return this.Attacker;
    }

    public Entity getAttacked() {
        return this.Attacked;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


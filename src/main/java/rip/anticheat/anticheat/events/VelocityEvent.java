/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class VelocityEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Vector vec;

    public VelocityEvent(Player player, Vector vector) {
        this.player = player;
        this.vec = vector;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Vector getVec() {
        return this.vec;
    }
}


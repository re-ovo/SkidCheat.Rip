/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package rip.anticheat.anticheat.update.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rip.anticheat.anticheat.update.UpdateType;

public class UpdateEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private UpdateType Type;

    public UpdateEvent(UpdateType updateType) {
        this.Type = updateType;
    }

    public UpdateType getType() {
        return this.Type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


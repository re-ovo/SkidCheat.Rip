/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package rip.anticheat.anticheat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rip.anticheat.anticheat.checks.CheckType;

public class ViolationBroadcastEvent
extends Event
implements Cancellable {
    private boolean isCancelled = false;
    private static final HandlerList handlers = new HandlerList();
    private Player violatingPlayer;
    private CheckType violatingCheckType;

    public ViolationBroadcastEvent(Player player, CheckType checkType) {
        this.violatingPlayer = player;
        this.violatingCheckType = checkType;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean bl) {
        this.isCancelled = bl;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getViolatingPlayer() {
        return this.violatingPlayer;
    }

    public CheckType getViolatingCheckType() {
        return this.violatingCheckType;
    }
}


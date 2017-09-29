/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package rip.anticheat.anticheat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rip.anticheat.anticheat.PlayerStats;

public class PlayerClickEvaluateEvent
extends Event {
    private static HandlerList handlerList = new HandlerList();
    private Player player;
    private PlayerStats profile;
    private int clicks;
    private int hits;

    public PlayerClickEvaluateEvent(Player player, PlayerStats playerStats, int n, int n2) {
        this.player = player;
        this.profile = playerStats;
        this.clicks = n;
        this.hits = n2;
    }

    public PlayerStats getProfile() {
        return this.profile;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getClicks() {
        return this.clicks;
    }

    public int getHits() {
        return this.hits;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}


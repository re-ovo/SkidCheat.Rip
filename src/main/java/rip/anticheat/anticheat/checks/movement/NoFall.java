/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package rip.anticheat.anticheat.checks.movement;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;

public class NoFall
extends Check {
    public NoFall(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "NoFall", "NoFall", 3, 50, 2, 0);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=1)
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (!this.isEnabled()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.isInsideVehicle()) {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        n = playerMoveEvent.getFrom().getY() > playerMoveEvent.getTo().getY() ? (player.isOnGround() && !playerStats.isOnGround() ? ++n : --n) : --n;
        if (n > n2) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Fake Packets"));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }
}


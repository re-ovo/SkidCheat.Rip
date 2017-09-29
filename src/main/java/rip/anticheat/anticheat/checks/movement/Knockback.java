/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerToggleSprintEvent
 */
package rip.anticheat.anticheat.checks.movement;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.PlayerStats;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;

public class Knockback
extends Check {
    private Map<Player, Long> lastSprintStart = new HashMap<Player, Long>();
    private Map<Player, Long> lastSprintStop = new HashMap<Player, Long>();

    public Knockback(AntiCheat antiCheat) {
        super(antiCheat, CheckType.COMBAT, "Knockback", "Knockback", 3, 50, 2, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastSprintStart.containsKey((Object)player)) {
            this.lastSprintStart.remove((Object)player);
        }
        if (this.lastSprintStop.containsKey((Object)player)) {
            this.lastSprintStop.remove((Object)player);
        }
    }

    @EventHandler
    public void Sprint(PlayerToggleSprintEvent playerToggleSprintEvent) {
        Player player = playerToggleSprintEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (playerStats.getLastDelayedPacketDiff() < 500 || playerStats.getLastPlayerPacketDiff() > 200) {
            return;
        }
        if (playerToggleSprintEvent.isSprinting() && this.lastSprintStop.containsKey((Object)player)) {
            int n = playerStats.getCheck(this, 0);
            int n2 = this.getThreshold();
            long l = System.currentTimeMillis() - this.lastSprintStop.get((Object)player);
            n = l < 5 ? ++n : (l > 1000 ? --n : (n -= 2));
            if (n > n2) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "*"));
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
        }
        if (!playerToggleSprintEvent.isSprinting()) {
            this.lastSprintStop.put(player, System.currentTimeMillis());
        } else if (playerToggleSprintEvent.isSprinting()) {
            this.lastSprintStart.put(player, System.currentTimeMillis());
        }
    }
}


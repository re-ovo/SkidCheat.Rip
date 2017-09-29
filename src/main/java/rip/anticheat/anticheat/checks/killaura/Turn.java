/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.listeners.EventListener;
import rip.anticheat.anticheat.util.math.MathUtil;

public class Turn
extends Check {
    private final Map<UUID, Integer> lastViolations = new WeakHashMap<UUID, Integer>();
    private final Map<UUID, Integer> lastHits = new WeakHashMap<UUID, Integer>();

    public Turn(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "Turn", "Head-Snap [Kill-Aura]", 110, 7, 3, 0);
    }

    @EventHandler
    public void onAttack(EventListener.PlayerAttackEvent playerAttackEvent) {
        Player player = playerAttackEvent.getPlayer();
        int n = AntiCheat.Instance.getTicksPassed();
        if (n - this.lastViolations.getOrDefault(player.getUniqueId(), 0) <= 1 && playerAttackEvent.getEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize().dot(player.getLocation().getDirection()) > 0.97) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "Invalid Turn"));
        }
    }

    @EventHandler(ignoreCancelled=1)
    public void onRotate(PlayerMoveEvent playerMoveEvent) {
        Location location = playerMoveEvent.getTo();
        float f = MathUtil.getYawDifference(playerMoveEvent.getFrom(), location);
        if (f < 77.5f) {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        int n = AntiCheat.Instance.getTicksPassed();
        this.lastViolations.put(player.getUniqueId(), n);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent playerQuitEvent) {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        this.lastHits.remove(uUID);
        this.lastViolations.remove(uUID);
    }
}


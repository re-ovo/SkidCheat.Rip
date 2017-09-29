/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.api.PlayerData;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.listeners.EventListener;
import rip.anticheat.anticheat.util.math.MathUtil;

public class AimAssist
extends Check {
    private final Map<UUID, Set<PlayerData.Hit>> hits = new WeakHashMap<UUID, Set<PlayerData.Hit>>();

    public AimAssist(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "AimAssister", "AimAssist (expirimental)", 15, 4, 3, 0);
    }

    @EventHandler
    public void onAttack(EventListener.PlayerAttackEvent playerAttackEvent) {
        Player player = playerAttackEvent.getPlayer();
        Location location = player.getLocation();
        Location location2 = playerAttackEvent.getEntity().getLocation();
        float f = (float)location2.toVector().subtract(location.toVector()).normalize().dot(location.getDirection());
        Set set = this.hits.getOrDefault(player.getUniqueId(), new HashSet());
        set.add(new PlayerData.Hit(f, location));
        this.hits.put(player.getUniqueId(), set);
        if (set.size() > 19) {
            this.analyze(player);
        }
    }

    private void analyze(Player player) {
        UUID uUID = player.getUniqueId();
        Set<PlayerData.Hit> set = this.hits.get(uUID);
        float f = 0.0f;
        float f2 = 0.0f;
        long l = 0;
        Location location = null;
        int n = 0;
        int n2 = 0;
        double d = 0.0;
        Location location2 = null;
        for (PlayerData.Hit hit : set) {
            f = hit.getAccuracy();
            l = hit.getTime();
            location = hit.getLocation();
            if (location2 != null) {
                d += MathUtil.getXZDistance(location, location2);
            }
            if (f > 0.95f) {
                ++n;
                if ((double)(f2 - f) <= 0.005) {
                    ++n2;
                }
            }
            location2 = location;
            f2 = f;
        }
        if (n > 18 && d > 7.5) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "ahits: " + n + " distanceT: " + d));
        }
        this.hits.remove(uUID);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        this.hits.remove(playerDeathEvent.getEntity().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(EventListener.PlayerDisconnectEvent playerDisconnectEvent) {
        this.hits.remove(playerDisconnectEvent.getPlayer().getUniqueId());
    }
}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.autoclicker;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.math.MathUtil;

public class PatternDetection
extends Check {
    private static final Map<UUID, Long> cpsMS = new WeakHashMap<UUID, Long>();
    private static final Map<UUID, Double[]> cps = new WeakHashMap<UUID, Double[]>();
    private static final Map<UUID, Long> lastTickWithPacketSent = new WeakHashMap<UUID, Long>();
    private static final Map<UUID, Boolean> lastPacketTick = new WeakHashMap<UUID, Boolean>();
    private static final Map<UUID, Long> packetHitsSinceLastCheck = new WeakHashMap<UUID, Long>();
    private static final Map<UUID, Long> lastCheckedTick = new WeakHashMap<UUID, Long>();
    private static final Map<UUID, Long> hitsSinceLastCheck = new WeakHashMap<UUID, Long>();

    public PatternDetection(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "PatternClick", "AutoClicker Type X", 10, 2, 20, 0);
    }

    public void onInt(PlayerInteractEvent playerInteractEvent) {
        Double[] arrdouble;
        Player player = playerInteractEvent.getPlayer();
        if (playerInteractEvent.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }
        if (cps.containsKey(player.getUniqueId())) {
            arrdouble = cps.get(player.getUniqueId());
            arrdouble[4] = arrdouble[4] + 1.0;
        } else {
            arrdouble = new Double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        }
        cps.put(player.getUniqueId(), arrdouble);
        for (Map.Entry<UUID, Double[]> entry : cps.entrySet()) {
            Double[] arrdouble2 = entry.getValue();
            this.analyzeDouble(player);
            if (Arrays.stream(arrdouble2).anyMatch(d -> d >= 10.0) && arrdouble2[4].equals(arrdouble2[0]) && arrdouble2[4].equals(arrdouble2[1]) && arrdouble2[4].equals(arrdouble2[2]) && arrdouble2[4].equals(arrdouble2[3])) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Consistency"));
            }
            if (Arrays.stream(arrdouble2).anyMatch(d -> d > 12.0) && MathUtil.close(arrdouble2, 2)) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Type C Randomization"));
            }
            arrdouble2[0] = arrdouble2[1];
            arrdouble2[1] = arrdouble2[2];
            arrdouble2[2] = arrdouble2[3];
            arrdouble2[3] = arrdouble2[4];
            arrdouble2[4] = 0.0;
            cps.put(player.getUniqueId(), arrdouble2);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
    }

    public void analyzeDouble(Player player) {
        UUID uUID = player.getUniqueId();
        if (cpsMS.containsKey(uUID) && System.currentTimeMillis() - cpsMS.get(player.getUniqueId()) <= 1) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Doubles"));
        }
        cpsMS.put(uUID, System.currentTimeMillis());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent playerQuitEvent) {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        packetHitsSinceLastCheck.remove(uUID);
        lastCheckedTick.remove(uUID);
        lastPacketTick.remove(uUID);
        lastTickWithPacketSent.remove(uUID);
        hitsSinceLastCheck.remove(uUID);
        cps.remove(uUID);
        cpsMS.remove(uUID);
    }
}


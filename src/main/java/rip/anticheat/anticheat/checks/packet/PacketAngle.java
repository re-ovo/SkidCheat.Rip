/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.packet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;

public class PacketAngle
extends Check {
    public PacketAngle(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "Angle", "KillAura [Angle]", 110, 50, 3, 0);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        LivingEntity livingEntity = (LivingEntity)entityDamageByEntityEvent.getEntity();
        if (player.getLocation().distanceSquared(livingEntity.getLocation()) < 3.0) {
            return;
        }
        float f = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).angle(livingEntity.getLocation().getDirection());
    }
}


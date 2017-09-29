/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import java.text.DecimalFormat;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;

public class HitBox
extends Check {
    private double HITBOX_LENGTH = 1.05;

    public HitBox(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "HitBox", "HitBox (KA)", 110, 50, 3, 0);
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getDamager();
        if (!this.hasInHitBox((LivingEntity)player2)) {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Hit > " + Common.FORMAT_0x00.format(this.HITBOX_LENGTH)));
        }
    }

    public boolean hasInHitBox(LivingEntity livingEntity) {
        boolean bl = false;
        Vector vector = livingEntity.getLocation().toVector().subtract(livingEntity.getLocation().toVector());
        Vector vector2 = livingEntity.getLocation().toVector().subtract(livingEntity.getLocation().toVector());
        if (!(livingEntity.getLocation().getDirection().normalize().crossProduct(vector).lengthSquared() >= this.HITBOX_LENGTH && livingEntity.getLocation().getDirection().normalize().crossProduct(vector2).lengthSquared() >= this.HITBOX_LENGTH || vector.normalize().dot(livingEntity.getLocation().getDirection().normalize()) < 0.0 && vector2.normalize().dot(livingEntity.getLocation().getDirection().normalize()) < 0.0)) {
            bl = true;
        }
        return bl;
    }
}


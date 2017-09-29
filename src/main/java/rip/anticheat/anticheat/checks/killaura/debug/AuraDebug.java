/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package rip.anticheat.anticheat.checks.killaura.debug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;

public class AuraDebug
extends Check {
    public AuraDebug(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "Debug", "KillAura (Debug)", 1, 50, 2, 0);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
    }
}


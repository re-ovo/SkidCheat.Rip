/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.checks.spook;

import java.text.DecimalFormat;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;

public class SpookA
extends Check {
    private float lastYaw;
    private int lastBad;
    private static SpookA spooka;

    public SpookA(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "SpookA", "Spook (Angle)", 19, 2, 1, 0);
    }

    float onAim(Player player, float f) {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        if (f2 > 1.0f && (float)Math.round(f2) == f2) {
            if (f2 == (float)this.lastBad) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(String.valueOf(this.lastYaw) + " - " + this.lastBad)));
                return f2;
            }
            this.lastBad = Math.round(f2);
        } else {
            this.lastBad = 0;
        }
        return 0.0f;
    }

    static SpookA SpookAInstance() {
        return spooka;
    }
}


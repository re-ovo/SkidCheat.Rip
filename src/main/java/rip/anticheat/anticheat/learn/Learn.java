/*
 * Decompiled with CFR 0_122.
 */
package rip.anticheat.anticheat.learn;

import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.learn.Cheating;

public class Learn {
    private CheckType type;
    private final double value;

    public Learn(CheckType checkType, double d) {
        this.type = checkType;
        this.value = d;
    }

    public void storeCheatData(Cheating cheating) {
    }
}


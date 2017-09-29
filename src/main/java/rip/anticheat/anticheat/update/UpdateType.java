/*
 * Decompiled with CFR 0_122.
 */
package rip.anticheat.anticheat.update;

import java.io.PrintStream;
import rip.anticheat.anticheat.util.misc.TimeUtil;

public enum UpdateType {
    MIN_64("MIN_64", 0, 3840000),
    MIN_32("MIN_32", 1, 1920000),
    MIN_16("MIN_16", 2, 960000),
    MIN_08("MIN_08", 3, 480000),
    MIN_05("MIN_05", 4, 300000),
    MIN_04("MIN_04", 5, 240000),
    MIN_02("MIN_02", 6, 120000),
    MIN_01("MIN_01", 7, 60000),
    SLOWEST("SLOWEST", 8, 32000),
    SLOWER("SLOWER", 9, 16000),
    SLOW("SLOW", 10, 4000),
    SEC_05("SEC_05", 11, 5000),
    SEC_04("SEC_04", 12, 4000),
    SEC_03("SEC_03", 13, 3000),
    SEC_02("SEC_02", 14, 2000),
    SEC("SEC", 15, 1000),
    FAST("FAST", 16, 500),
    FASTER("FASTER", 17, 250),
    FASTEST("FASTEST", 18, 125),
    TICK("TICK", 19, 49);
    
    private long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(String string2, int n2, String string3, int n3, long l) {
        this._time = string3;
        this._last = System.currentTimeMillis();
    }

    public boolean Elapsed() {
        if (TimeUtil.elapsed(this._last, this._time)) {
            this._last = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void StartTime() {
        this._timeCount = System.currentTimeMillis();
    }

    public void StopTime() {
        this._timeSpent += System.currentTimeMillis() - this._timeCount;
    }

    public void PrintAndResetTime() {
        System.out.println(String.valueOf(String.valueOf(this.name())) + " in a second: " + this._timeSpent);
        this._timeSpent = 0;
    }
}


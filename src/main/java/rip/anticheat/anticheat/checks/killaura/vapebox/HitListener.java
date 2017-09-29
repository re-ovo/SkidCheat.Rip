/*
 * Decompiled with CFR 0_122.
 */
package rip.anticheat.anticheat.checks.killaura.vapebox;

public class HitListener {
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    public HitListener(double d, double d2, double d3, double d4, double d5, double d6) {
        this.minX = d;
        this.minY = d2;
        this.minZ = d3;
        this.maxX = d4;
        this.maxY = d5;
        this.maxZ = d6;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMinZ() {
        return this.minZ;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public double getMaxZ() {
        return this.maxZ;
    }
}


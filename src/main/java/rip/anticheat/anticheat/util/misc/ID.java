/*
 * Decompiled with CFR 0_122.
 */
package rip.anticheat.anticheat.util.misc;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class ID {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        return new BigInteger(24, RANDOM).toString(32);
    }
}


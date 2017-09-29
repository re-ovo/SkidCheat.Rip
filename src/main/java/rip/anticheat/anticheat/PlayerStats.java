/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package rip.anticheat.anticheat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.checks.Check;

public class PlayerStats {
    private UUID UUID;
    private Map<Check, Map<Integer, Integer>> threshold = new HashMap<Check, Map<Integer, Integer>>();
    private Map<Check, List<Violation>> violations = new HashMap<Check, List<Violation>>();
    private Map<Check, Integer> bans = new HashMap<Check, Integer>();
    private long lastReceivedKeepAlive;
    private int lastReceivedKeepAliveID;
    private long lastSentKeepAlive;
    private int lastSentKeepAliveID;
    private long lastAnimation;
    private int animationSpam;
    private long lastBlockPlacementPacket;
    private int blockPlacementSpam;
    private double velocityY;
    private double velocityXZ;
    private long velocityTime;
    private boolean onGround;
    private Location lastGround;
    private long lastGroundTime = System.currentTimeMillis();
    private long lastBunnyTime = System.currentTimeMillis();
    private int highestCPS;
    private int latestCPS;
    private long lastMount;
    private long lastWorldChange;
    private long lastPlayerPacket;
    private long lastDelayedPacket;
    private double cps = 0.0;
    private int clicks;
    private int hits;

    public PlayerStats(UUID uUID) {
        this.UUID = uUID;
    }

    public UUID getUUID() {
        return this.UUID;
    }

    public double getCPS() {
        return this.cps;
    }

    public void setCPS(double d) {
        this.cps = d;
    }

    public long getLastDelayedPacket() {
        return this.lastDelayedPacket;
    }

    public long getLastDelayedPacketDiff() {
        return System.currentTimeMillis() - this.getLastDelayedPacket();
    }

    public long getLastPlayerPacket() {
        return this.lastPlayerPacket;
    }

    public long getLastPlayerPacketDiff() {
        return System.currentTimeMillis() - this.getLastPlayerPacket();
    }

    public long getLastMountDiff() {
        return System.currentTimeMillis() - this.getLastMount();
    }

    public long getLastMount() {
        return this.lastMount;
    }

    public long getLastWorldChangeDiff() {
        return System.currentTimeMillis() - this.getLastWorldChange();
    }

    public long getLastWorldChange() {
        return this.lastWorldChange;
    }

    public long getLastReceivedKeepAlive() {
        return this.lastReceivedKeepAlive;
    }

    public long getLastSentKeepAlive() {
        return this.lastSentKeepAlive;
    }

    public int getLastReceivedKeepAliveID() {
        return this.lastReceivedKeepAliveID;
    }

    public int getLastSentKeepAliveID() {
        return this.lastSentKeepAliveID;
    }

    public long getLastAnimation() {
        return this.lastAnimation;
    }

    public int getAnimationSpam() {
        return this.animationSpam;
    }

    public long getLastBlockPlacementPacket() {
        return this.lastBlockPlacementPacket;
    }

    public int getBlockPlacementSpam() {
        return this.blockPlacementSpam;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    public double getVelocityXZ() {
        return this.velocityXZ;
    }

    public long getVelocityTime() {
        return this.velocityTime;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public Location getLastGround() {
        return this.lastGround;
    }

    public long getLastGroundTime() {
        return this.lastGroundTime;
    }

    public long getLastGroundTimeDiff() {
        return System.currentTimeMillis() - this.lastGroundTime;
    }

    public long getLastBunnyTime() {
        return this.lastBunnyTime;
    }

    public long getLastBunnyTimeDiff() {
        return System.currentTimeMillis() - this.lastBunnyTime;
    }

    public int getHighestCPS() {
        return this.highestCPS;
    }

    public int getLatestCPS() {
        return this.latestCPS;
    }

    public int getCheck(Check check, int n) {
        Map<Integer, Integer> map;
        if (this.threshold.containsKey(check) && (map = this.threshold.get(check)).containsKey(n)) {
            return map.get(n);
        }
        return 0;
    }

    public int getBans(Check check) {
        return this.bans.getOrDefault(check, 0);
    }

    public List<Violation> getViolations(Check check) {
        return this.violations.get(check);
    }

    public Map<Check, List<Violation>> getViolations() {
        return new HashMap<Check, List<Violation>>(this.violations);
    }

    public void setLastDelayedPacket(long l) {
        this.lastDelayedPacket = l;
    }

    public void setLastPlayerPacket(long l) {
        this.lastPlayerPacket = l;
    }

    public void setLastMount(long l) {
        this.lastMount = l;
    }

    public void setLastWorldChange(long l) {
        this.lastWorldChange = l;
    }

    public void setLastReceivedKeepAlive(long l) {
        this.lastReceivedKeepAlive = l;
    }

    public void setLastSentKeepAlive(long l) {
        this.lastSentKeepAlive = l;
    }

    public void setLastReceivedKeepAliveID(int n) {
        this.lastReceivedKeepAliveID = n;
    }

    public void setLastSentKeepAliveID(int n) {
        this.lastSentKeepAliveID = n;
    }

    public void setLastAnimation(long l) {
        this.lastAnimation = l;
    }

    public void setAnimationSpam(int n) {
        this.animationSpam = n;
    }

    public void setLastBlockPlacementPacket(long l) {
        this.lastBlockPlacementPacket = l;
    }

    public void setBlockPlacementSpam(int n) {
        this.blockPlacementSpam = n;
    }

    public void setVelocityY(double d) {
        this.velocityY = d;
    }

    public void setVelocityXZ(double d) {
        this.velocityXZ = d;
    }

    public void setVelocityTime(long l) {
        this.velocityTime = l;
    }

    public void setOnGround(boolean bl) {
        this.onGround = bl;
    }

    public void setLastGround(Location location) {
        this.lastGround = location;
    }

    public void setLastGroundTime(long l) {
        this.lastGroundTime = l;
    }

    public void setLastBunnyTime(long l) {
        this.lastBunnyTime = l;
    }

    public void setHighestCPS(int n) {
        this.highestCPS = n;
    }

    public void setLatestCPS(int n) {
        this.latestCPS = n;
    }

    public void setCheck(Check check, int n, int n2) {
        if (n2 < 0) {
            n2 = 0;
        }
        if (this.threshold.containsKey(check)) {
            Map<Integer, Integer> map = this.threshold.get(check);
            map.put(n, n2);
            this.threshold.put(check, map);
        } else {
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            hashMap.put(n, n2);
            this.threshold.put(check, hashMap);
        }
    }

    public void addViolation(Check check, Violation violation) {
        if (this.violations.containsKey(check)) {
            this.violations.get(check).add(violation);
        } else {
            ArrayList<Violation> arrayList = new ArrayList<Violation>();
            arrayList.add(violation);
            this.violations.put(check, arrayList);
        }
    }

    public void removeViolations() {
        this.violations.clear();
    }

    public void removeBans() {
        this.bans.clear();
    }

    public void removeViolations(Check check) {
        this.violations.remove(check);
    }

    public void removeBans(Check check) {
        this.bans.put(check, 0);
    }

    public void addBan(Check check) {
        if (this.bans.containsKey(check)) {
            this.bans.put(check, this.bans.get(check) + 1);
        } else {
            this.bans.put(check, 1);
        }
    }

    public int getClicks() {
        return this.clicks;
    }

    public void setClicks(int n) {
        this.clicks = n;
    }

    public int getHits() {
        return this.hits;
    }

    public void setHits(int n) {
        this.hits = n;
    }
}


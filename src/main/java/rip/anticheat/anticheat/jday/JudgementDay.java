/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.jday;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.checks.Check;

public class JudgementDay {
    private String Name;
    private String Reason;
    private String Creator;
    private Long TimeCreated;
    private boolean Finished;
    private int TotalQueued = 0;
    private List<Check> Checks = new ArrayList<Check>();
    private List<UUID> Queued = new ArrayList<UUID>();

    public JudgementDay(String string, String string2, String string3, Long l, boolean bl, int n) {
        this.Name = string;
        this.Reason = string2;
        this.Creator = string3;
        this.TimeCreated = l;
        this.Finished = bl;
        this.TotalQueued = n;
    }

    public String getName() {
        return this.Name;
    }

    public String getReason() {
        return this.Reason;
    }

    public int getTotalQueued() {
        return this.TotalQueued;
    }

    public String getCreator() {
        return this.Creator;
    }

    public Long getTimeCreated() {
        return this.TimeCreated;
    }

    public boolean isFinished() {
        return this.Finished;
    }

    public void setName(String string) {
        this.Name = string;
    }

    public void setReason(String string) {
        this.Reason = string;
    }

    public void setFinished(boolean bl) {
        this.Finished = bl;
    }

    public List<Check> getChecks() {
        return new ArrayList<Check>(this.Checks);
    }

    public List<UUID> getQueued() {
        return new ArrayList<UUID>(this.Queued);
    }

    public void removeQueued(UUID uUID) {
        this.Queued.remove(uUID);
    }

    public void addCheck(Check check) {
        this.Checks.add(check);
    }

    public void removeCheck(Check check) {
        this.Checks.remove(check);
    }

    public void addQueue(UUID uUID) {
        this.Queued.add(uUID);
    }

    public void queuePlayer(Player player) {
        ++this.TotalQueued;
        this.addQueue(player.getUniqueId());
    }
}


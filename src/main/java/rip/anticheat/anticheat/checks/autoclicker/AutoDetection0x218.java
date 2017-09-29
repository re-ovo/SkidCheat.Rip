/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package rip.anticheat.anticheat.checks.autoclicker;

import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;

public class AutoDetection0x218
extends Check {
    private static final double MIN_CPS_FOR_CONSTANTS_CHECK = 14.0;
    private static final double INVALID_CLICK_SPEED = 19.0;
    private WeakHashMap<Player, ClickProfile> profiles = new WeakHashMap();

    public AutoDetection0x218(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "AutoClicker", "AutoClicker [0x01]", 110, 7, 3, 0);
    }

    @EventHandler
    public void onClick(PacketUseEntityEvent packetUseEntityEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        ClickProfile clickProfile = null;
        if (!this.profiles.containsKey((Object)player)) {
            clickProfile = new ClickProfile();
            this.profiles.put(player, clickProfile);
        } else {
            clickProfile = this.profiles.get((Object)player);
        }
        clickProfile.issueClick(player, this);
    }

    public class ClickProfile {
        public double clicks;
        private long clickSprint;
        private double lastCPS;
        private double twoSecondsAgoCPS;
        private double threeSecondsAgoCPS;
        private int violations;

        public ClickProfile() {
            this.clicks = 0.0;
            this.clickSprint = 0;
            this.lastCPS = 0.0;
            this.twoSecondsAgoCPS = 0.0;
            this.threeSecondsAgoCPS = 0.0;
            this.violations = 0;
        }

        public void issueClick(Player player, Check check) {
            long l = System.currentTimeMillis();
            if (l - this.clickSprint >= 1000) {
                this.shuffleDown();
                this.clickSprint = l;
                this.clicks = 0.0;
                if (this.isConstant() || this.hasInvalidClickSpeed() || this.hasVapePattern()) {
                    ++this.violations;
                    if (this.violations >= 4) {
                        AutoDetection0x218.this.getCore().addViolation(player, check, new Violation(check, ViolationPriority.LOW, String.format("CPS[1s ago]: %.2f, CPS[2s ago]: %.2f, CPS[3s ago]: %.2f", this.lastCPS, this.twoSecondsAgoCPS, this.threeSecondsAgoCPS)));
                    }
                }
            }
            this.clicks += 1.0;
        }

        private void shuffleDown() {
            this.threeSecondsAgoCPS = this.twoSecondsAgoCPS;
            this.twoSecondsAgoCPS = this.lastCPS;
            this.lastCPS = this.clicks;
        }

        private boolean hasInvalidClickSpeed() {
            if (this.lastCPS >= 19.0) {
                return true;
            }
            return false;
        }

        private boolean hasVapePattern() {
            if (this.lastCPS == 9.0 && this.twoSecondsAgoCPS == 11.0 && this.threeSecondsAgoCPS == 10.0) {
                return true;
            }
            return false;
        }

        private boolean isConstant() {
            if (this.threeSecondsAgoCPS >= 14.0) {
                if (this.lastCPS == this.twoSecondsAgoCPS && this.twoSecondsAgoCPS == this.threeSecondsAgoCPS) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }

}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package rip.anticheat.anticheat.checks.vape;

import java.io.PrintStream;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;

public class AutoClickerV5x01
extends Check {
    private WeakHashMap<Player, ClickProfile> profiles = new WeakHashMap();

    public AutoClickerV5x01(AntiCheat antiCheat) {
        super(antiCheat, CheckType.AUTOCLICKER, "VClicker", "AutoClicker [V5X01]", 110, 7, 3, 0);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent playerInteractEvent) {
        if (!this.isEnabled()) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
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
                double d = this.lastCPS;
                double d2 = this.twoSecondsAgoCPS;
                double d3 = this.threeSecondsAgoCPS;
                if (d == 9.0 && d2 == 11.0 && d3 == 10.0 || d == 9.0 && d2 == 8.0 && d3 == 10.0) {
                    System.out.println("[0x01]: " + player.getName() + "logged for a Pattern of VIDJDEI");
                    ++this.violations;
                    if (this.violations >= 1) {
                        AutoClickerV5x01.this.getCore().addViolation(player, check, new Violation(check, ViolationPriority.LOW, String.format("CPS[1s ago]: %.2f, CPS[2s ago]: %.2f, CPS[3s ago]: %.2f", this.lastCPS, this.twoSecondsAgoCPS, this.threeSecondsAgoCPS)));
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
    }

}


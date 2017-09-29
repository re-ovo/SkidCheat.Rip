/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.PacketUseEntityEvent;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.UtilTime;

public class PacketCount18
extends Check {
    private Map<UUID, Long> LastMS = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> Clicks = new HashMap<UUID, List<Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public PacketCount18(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "PatternCount", "KillAura (Count)", 110, 50, 3, 0);
    }

    @EventHandler
    public void onUseEntity(PacketUseEntityEvent packetUseEntityEvent) {
        Player player = packetUseEntityEvent.getAttacker();
        int n = 0;
        long l = System.currentTimeMillis();
        if (this.ClickTicks.containsKey(player.getUniqueId())) {
            n = this.ClickTicks.get(player.getUniqueId()).getKey();
            l = this.ClickTicks.get(player.getUniqueId()).getValue();
        }
        if (this.LastMS.containsKey(player.getUniqueId())) {
            long l2 = UtilTime.nowlong() - this.LastMS.get(player.getUniqueId());
            if (l2 > 500 || l2 < 5) {
                this.LastMS.put(player.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (this.Clicks.containsKey(player.getUniqueId())) {
                List<Long> list = this.Clicks.get(player.getUniqueId());
                if (list.size() == 10) {
                    this.Clicks.remove(player.getUniqueId());
                    Collections.sort(list);
                    long l3 = list.get(list.size() - 1) - list.get(0);
                    if (l3 < 30) {
                        ++n;
                        l = System.currentTimeMillis();
                    }
                } else {
                    list.add(l2);
                    this.Clicks.put(player.getUniqueId(), list);
                }
            } else {
                ArrayList<Long> arrayList = new ArrayList<Long>();
                arrayList.add(l2);
                this.Clicks.put(player.getUniqueId(), arrayList);
            }
        }
        if (this.ClickTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(l, 5000)) {
            n = 0;
            l = UtilTime.nowlong();
        }
        if (n > 0) {
            n = 0;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(n)));
        }
        this.LastMS.put(player.getUniqueId(), UtilTime.nowlong());
        this.ClickTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n, l));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (this.LastMS.containsKey(player.getUniqueId())) {
            this.LastMS.remove(player.getUniqueId());
        }
        if (this.ClickTicks.containsKey(player.getUniqueId())) {
            this.ClickTicks.remove(player.getUniqueId());
        }
        if (this.Clicks.containsKey(player.getUniqueId())) {
            this.Clicks.remove(player.getUniqueId());
        }
    }
}


/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package rip.anticheat.anticheat.checks.movement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.events.PacketPlayerEvent;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.math.MathUtil;

public class Timer
extends Check {
    private Map<UUID, Long> lastTimer = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> MS = new HashMap<UUID, List<Long>>();
    private Map<UUID, Integer> timerTicks = new HashMap<UUID, Integer>();

    public Timer(AntiCheat antiCheat) {
        super(antiCheat, CheckType.MOVEMENT, "Timer", "Timer", 6, 50, 2, 0);
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent packetPlayerEvent) {
        Player player = packetPlayerEvent.getPlayer();
        int n = 0;
        if (this.timerTicks.containsKey(player.getUniqueId())) {
            n = this.timerTicks.get(player.getUniqueId());
        }
        if (this.lastTimer.containsKey(player.getUniqueId())) {
            long l = System.currentTimeMillis() - this.lastTimer.get(player.getUniqueId());
            List list = new ArrayList<Long>();
            if (this.MS.containsKey(player.getUniqueId())) {
                list = this.MS.get(player.getUniqueId());
            }
            list.add(l);
            if (list.size() == 20) {
                Long l22;
                boolean bl = true;
                for (Long l22 : list) {
                    if (l22 >= 1) continue;
                    bl = false;
                }
                l22 = MathUtil.averageLong(list);
                n = l22 < 48 && bl ? ++n : 0;
                this.MS.remove(player.getUniqueId());
            } else {
                this.MS.put(player.getUniqueId(), list);
            }
        }
        if (n > 4) {
            n = 0;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, Common.FORMAT_0x00.format(n)));
        }
        this.lastTimer.put(player.getUniqueId(), System.currentTimeMillis());
        this.timerTicks.put(player.getUniqueId(), n);
    }
}


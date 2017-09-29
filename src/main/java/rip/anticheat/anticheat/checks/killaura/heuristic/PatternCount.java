/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.reflect.StructureModifier
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.World
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.checks.killaura.heuristic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;
import rip.anticheat.anticheat.util.formatting.Common;
import rip.anticheat.anticheat.util.misc.TimeUtil;

public class PatternCount
extends Check {
    private Map<UUID, Long> LastMS = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> Clicks = new HashMap<UUID, List<Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public PatternCount(AntiCheat antiCheat) {
        super(antiCheat, CheckType.KILLAURA, "PatternCount", "KillAura (Count)", 110, 50, 3, 0);
        this.onRegister();
    }

    public void onRegister() {
        this.registerPacketReceiving(PacketType.Play.Client.USE_ENTITY, packetEvent -> {
            if (packetEvent.getPacket().getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.ATTACK) {
                return;
            }
            Player player = packetEvent.getPlayer();
            if (!(packetEvent.getPacket().getEntityModifier(player.getWorld()).read(0) instanceof LivingEntity)) {
                return;
            }
            Player player2 = packetEvent.getPlayer();
            int n = 0;
            long l = System.currentTimeMillis();
            if (this.ClickTicks.containsKey(player2.getUniqueId())) {
                n = this.ClickTicks.get(player2.getUniqueId()).getKey();
                l = this.ClickTicks.get(player2.getUniqueId()).getValue();
            }
            if (this.LastMS.containsKey(player2.getUniqueId())) {
                long l2 = TimeUtil.nowlong() - this.LastMS.get(player2.getUniqueId());
                if (l2 > 500 || l2 < 5) {
                    this.LastMS.put(player2.getUniqueId(), TimeUtil.nowlong());
                    return;
                }
                if (this.Clicks.containsKey(player2.getUniqueId())) {
                    List<Long> list = this.Clicks.get(player2.getUniqueId());
                    if (list.size() == 10) {
                        this.Clicks.remove(player2.getUniqueId());
                        Collections.sort(list);
                        long l3 = list.get(list.size() - 1) - list.get(0);
                        if (l3 < 30) {
                            ++n;
                            l = System.currentTimeMillis();
                        }
                    } else {
                        list.add(l2);
                        this.Clicks.put(player2.getUniqueId(), list);
                    }
                } else {
                    ArrayList<Long> arrayList = new ArrayList<Long>();
                    arrayList.add(l2);
                    this.Clicks.put(player2.getUniqueId(), arrayList);
                }
            }
            if (this.ClickTicks.containsKey(player2.getUniqueId()) && TimeUtil.elapsed(l, 5000)) {
                n = 0;
                l = TimeUtil.nowlong();
            }
            if (n > 0) {
                this.getCore().addViolation(player2, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(n)));
                n = 0;
            }
            this.LastMS.put(player2.getUniqueId(), TimeUtil.nowlong());
            this.ClickTicks.put(player2.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n, l));
        }
        );
    }
}


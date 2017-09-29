/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.messaging.PluginMessageListener
 */
package rip.anticheat.anticheat.checks.vape;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.Violation;
import rip.anticheat.anticheat.ViolationPriority;
import rip.anticheat.anticheat.checks.Check;
import rip.anticheat.anticheat.checks.CheckType;

public class VapeListener
extends Check
implements Listener,
PluginMessageListener {
    public static Set<UUID> vapers = new HashSet<UUID>();

    public VapeListener(AntiCheat antiCheat) {
        super(antiCheat, CheckType.OTHER, "VapeA", "Vape (Packets) [Cracked]", 2, 2, 2, 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        playerJoinEvent.getPlayer().sendMessage("\u00a78 \u00a78 \u00a71 \u00a73 \u00a73 \u00a77 \u00a78 ");
    }

    public void onPluginMessageReceived(String string, Player player, byte[] arrby) {
        try {
            String string2 = new String(arrby);
        }
        catch (Exception exception) {
            String string3 = "";
        }
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "Packets on-join [VAPE]"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        vapers.remove(playerQuitEvent.getPlayer().getUniqueId());
    }
}


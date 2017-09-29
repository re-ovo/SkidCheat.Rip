/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package rip.anticheat.anticheat.events.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import rip.anticheat.anticheat.events.VelocityEvent;
import rip.anticheat.anticheat.events.handler.WrapperPlayServerEntityVelocity;

public class VelocityPacketHandler
extends PacketAdapter {
    private Map<UUID, Long> lastVecUpdate = new HashMap<UUID, Long>();

    public VelocityPacketHandler(Plugin plugin) {
        super(plugin, new PacketType[]{PacketType.Play.Server.ENTITY_VELOCITY});
    }

    public void onPacketSending(PacketEvent packetEvent) {
        WrapperPlayServerEntityVelocity wrapperPlayServerEntityVelocity = new WrapperPlayServerEntityVelocity(packetEvent.getPacket());
        Player player = null;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Player) || entity.getEntityId() != wrapperPlayServerEntityVelocity.getEntityID()) continue;
                player = (Player)entity;
            }
        }
        if (player != null) {
            if (!this.lastVecUpdate.containsKey(player.getUniqueId())) {
                this.lastVecUpdate.put(player.getUniqueId(), 0);
            }
            long l = System.currentTimeMillis() - this.lastVecUpdate.get(player.getUniqueId());
            this.lastVecUpdate.put(player.getUniqueId(), System.currentTimeMillis());
            if (l > 5) {
                Entity entity;
                entity = new Vector(wrapperPlayServerEntityVelocity.getVelocityX(), wrapperPlayServerEntityVelocity.getVelocityY(), wrapperPlayServerEntityVelocity.getVelocityZ());
                VelocityEvent velocityEvent = new VelocityEvent(player, (Vector)entity);
                Bukkit.getPluginManager().callEvent((Event)velocityEvent);
            }
        }
    }
}


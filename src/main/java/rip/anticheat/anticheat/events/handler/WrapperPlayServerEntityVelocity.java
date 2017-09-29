/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.reflect.StructureModifier
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.events.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.events.handler.AbstractPacket;

public class WrapperPlayServerEntityVelocity
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_VELOCITY;

    public WrapperPlayServerEntityVelocity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityVelocity(PacketContainer packetContainer) {
        super(packetContainer, TYPE);
    }

    public int getEntityID() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setEntityID(int n) {
        this.handle.getIntegers().write(0, (Object)n);
    }

    public Entity getEntity(World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent packetEvent) {
        return this.getEntity(packetEvent.getPlayer().getWorld());
    }

    public double getVelocityX() {
        return (double)((Integer)this.handle.getIntegers().read(1)).intValue() / 8000.0;
    }

    public void setVelocityX(double d) {
        this.handle.getIntegers().write(1, (Object)((int)(d * 8000.0)));
    }

    public double getVelocityY() {
        return (double)((Integer)this.handle.getIntegers().read(2)).intValue() / 8000.0;
    }

    public void setVelocityY(double d) {
        this.handle.getIntegers().write(2, (Object)((int)(d * 8000.0)));
    }

    public double getVelocityZ() {
        return (double)((Integer)this.handle.getIntegers().read(3)).intValue() / 8000.0;
    }

    public void setVelocityZ(double d) {
        this.handle.getIntegers().write(3, (Object)((int)(d * 8000.0)));
    }
}


package com.github.sib_energy_craft.energy_container.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Getter
@AllArgsConstructor
public class ScreenHandlerIntPropertyUpdateS2CPacket implements Packet<ClientPlayPacketListener> {
    private final int syncId;
    private final int propertyId;
    private final int value;

    public ScreenHandlerIntPropertyUpdateS2CPacket(PacketByteBuf buf) {
        this(buf.readUnsignedByte(), buf.readShort(), buf.readInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(syncId);
        buf.writeShort(propertyId);
        buf.writeInt(value);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }
}

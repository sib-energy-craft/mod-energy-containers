package com.github.sib_energy_craft.energy_container.load.client;

import com.github.sib_energy_craft.energy_container.network.IntPropertyListener;
import com.github.sib_energy_craft.energy_container.network.ScreenHandlerIntPropertyUpdateS2CPacket;
import com.github.sib_energy_craft.sec_utils.load.DefaultClientModInitializer;
import lombok.extern.slf4j.Slf4j;

import static com.github.sib_energy_craft.energy_container.network.NetworkPackets.UPDATE_INT_PROPERTY;
import static net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Slf4j
public final class Network implements DefaultClientModInitializer {
    static {
        registerGlobalReceiver(UPDATE_INT_PROPERTY, (client, handler, buf, responseSender) -> {
            try {
                var packet = new ScreenHandlerIntPropertyUpdateS2CPacket(buf);
                var playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }
                var currentScreenHandler = playerEntity.currentScreenHandler;

                if (!(currentScreenHandler instanceof IntPropertyListener propertyListener)) {
                    return;
                }
                if (currentScreenHandler.syncId == packet.getSyncId()) {
                    propertyListener.setIntProperty(packet.getPropertyId(), packet.getValue());
                }
            } catch (Exception e) {
                log.error("Int property process error", e);
            }
        });
    }
}

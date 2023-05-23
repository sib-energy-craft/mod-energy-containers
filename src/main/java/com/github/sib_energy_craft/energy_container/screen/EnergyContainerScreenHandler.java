package com.github.sib_energy_craft.energy_container.screen;

import com.github.sib_energy_craft.energy_container.block.AbstractEnergyContainerBlock;
import com.github.sib_energy_craft.energy_container.load.ScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class EnergyContainerScreenHandler extends AbstractEnergyContainerScreenHandler {

    public EnergyContainerScreenHandler(int syncId,
                                        @NotNull PlayerInventory playerInventory,
                                        @NotNull Inventory inventory,
                                        int charge,
                                        @NotNull AbstractEnergyContainerBlock block) {
        super(ScreenHandlers.ENERGY_CONTAINER, syncId, playerInventory, inventory, charge,
                block.getMaxCharge(), block.getMaxCharge());
    }

    public EnergyContainerScreenHandler(int syncId,
                                        @NotNull PlayerInventory playerInventory,
                                        @NotNull PacketByteBuf packetByteBuf) {
        super(ScreenHandlers.ENERGY_CONTAINER, syncId, playerInventory, packetByteBuf.readInt(),
                packetByteBuf.readInt(), packetByteBuf.readInt());
    }
}

package com.github.sib_energy_craft.energy_container.screen;

import com.github.sib_energy_craft.energy_container.load.client.Screens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class EnergyContainerScreenHandler extends AbstractEnergyContainerScreenHandler {

    public EnergyContainerScreenHandler(int syncId,
                                        @NotNull PlayerInventory playerInventory,
                                        @NotNull Inventory inventory,
                                        @NotNull PropertyDelegate propertyDelegate) {
        super(Screens.ENERGY_CONTAINER, syncId, playerInventory, inventory, propertyDelegate);
    }

    public EnergyContainerScreenHandler(int syncId,
                                        @NotNull PlayerInventory playerInventory,
                                        @NotNull PacketByteBuf packetByteBuf) {
        super(Screens.ENERGY_CONTAINER, syncId, playerInventory);

    }
}

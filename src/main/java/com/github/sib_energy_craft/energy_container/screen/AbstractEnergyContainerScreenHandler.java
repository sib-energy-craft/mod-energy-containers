package com.github.sib_energy_craft.energy_container.screen;

import com.github.sib_energy_craft.energy_api.screen.ChargeSlot;
import com.github.sib_energy_craft.energy_api.tags.CoreTags;
import com.github.sib_energy_craft.energy_container.block.entity.EnergyContainerProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

import static com.github.sib_energy_craft.energy_container.block.entity.AbstractEnergyContainerBlockEntity.CHARGE_SLOT;
import static com.github.sib_energy_craft.energy_container.block.entity.AbstractEnergyContainerBlockEntity.DISCHARGE_SLOT;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractEnergyContainerScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    protected AbstractEnergyContainerScreenHandler(@NotNull ScreenHandlerType<?> type,
                                                   int syncId,
                                                   @NotNull PlayerInventory playerInventory) {
        this(type, syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(3));
    }

    protected AbstractEnergyContainerScreenHandler(@NotNull ScreenHandlerType<?> type,
                                                   int syncId,
                                                   @NotNull PlayerInventory playerInventory,
                                                   @NotNull Inventory inventory,
                                                   @NotNull PropertyDelegate propertyDelegate) {
        super(type, syncId);
        AbstractEnergyContainerScreenHandler.checkSize(inventory, 2);
        AbstractEnergyContainerScreenHandler.checkDataCount(propertyDelegate, 3);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addSlot(new ChargeSlot(inventory, CHARGE_SLOT, 56, 17, true));
        this.addSlot(new ChargeSlot(inventory, DISCHARGE_SLOT, 56, 53, false));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean canUse(@NotNull PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @NotNull
    @Override
    public ItemStack quickMove(@NotNull PlayerEntity player, int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if(index == DISCHARGE_SLOT || index == CHARGE_SLOT) {
                if(!this.insertItem(slotStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(CoreTags.isChargeable(slotStack)) {
                if(!this.insertItem(slotStack, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 2 && index < 29) {
                if(!this.insertItem(slotStack, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 29 && index < 38) {
                if(!this.insertItem(slotStack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, slotStack);
        }
        return itemStack;
    }

    /**
     * Get charge progress status
     *
     * @return charge progress
     */
    public int getChargeProgress() {
        int i = getCharge();
        int j = getMaxCharge();
        if (j == 0 || i == 0) {
            return 0;
        }
        return i * 24 / j;
    }

    /**
     * Get container charge
     *
     * @return charge
     */
    public int getCharge() {
        return this.propertyDelegate.get(EnergyContainerProperties.CHARGE.ordinal());
    }

    /**
     * Get container max charge
     *
     * @return max charge
     */
    public int getMaxCharge() {
        return this.propertyDelegate.get(EnergyContainerProperties.MAX_CHARGE.ordinal());
    }

    /**
     * Get container energy packet size
     *
     * @return packet size
     */
    public int getEnergyPacketSize() {
        return this.propertyDelegate.get(EnergyContainerProperties.ENERGY_PACKET_SIZE.ordinal());
    }
}


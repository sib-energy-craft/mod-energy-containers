package com.github.sib_energy_craft.charge_pad.block.entity;

import com.github.sib_energy_craft.charge_pad.block.AbstractChargePadBlock;
import com.github.sib_energy_craft.energy_container.block.entity.AbstractEnergyContainerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public abstract class AbstractChargePadBlockEntity extends AbstractEnergyContainerBlockEntity {
    protected boolean chargeStateOn;

    protected AbstractChargePadBlockEntity(@NotNull BlockEntityType<?> blockEntityType,
                                           @NotNull BlockPos pos,
                                           @NotNull BlockState state,
                                           @NotNull String containerNameCode,
                                           @NotNull AbstractChargePadBlock block) {
        super(blockEntityType, pos,state, containerNameCode, block);
    }

    public static void tick(@NotNull World world,
                            @NotNull BlockPos pos,
                            @NotNull BlockState state,
                            @NotNull AbstractChargePadBlockEntity blockEntity) {
        if(world.isClient) {
            return;
        }

        AbstractEnergyContainerBlockEntity.tick(world, pos, state, blockEntity);

        if(blockEntity.chargeStateOn) {
            blockEntity.chargeStateOn = false;
        } else if(state.get(AbstractChargePadBlock.CHARGE)) {
            world.setBlockState(pos, state.with(AbstractChargePadBlock.CHARGE, Boolean.FALSE), Block.NOTIFY_ALL);
        }
    }

    /**
     * Charge item stack
     *
     * @param chargingStack stack to charge
     */
    public boolean charge(@NotNull ItemStack chargingStack) {
        if(energyContainer.hasEnergy()) {
            var packetSize = block.getEnergyLevel().toBig;
            energyContainer.charge(chargingStack, packetSize);
            return true;
        }
        return false;
    }

    /**
     * Set block entity state to charging
     */
    public void chargeStateOn() {
        this.chargeStateOn = true;
    }
}


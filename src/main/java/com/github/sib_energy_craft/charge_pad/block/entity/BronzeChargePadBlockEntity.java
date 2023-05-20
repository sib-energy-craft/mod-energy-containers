package com.github.sib_energy_craft.charge_pad.block.entity;

import com.github.sib_energy_craft.charge_pad.block.BronzeChargePadBlock;
import com.github.sib_energy_craft.charge_pad.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public class BronzeChargePadBlockEntity extends AbstractChargePadBlockEntity {
    public BronzeChargePadBlockEntity(@NotNull BlockPos pos,
                                      @NotNull BlockState state,
                                      @NotNull BronzeChargePadBlock block) {
        super(Entities.BRONZE_CHARGE_PAD, pos, state, block.getContainerNameCode(), block);
    }
}


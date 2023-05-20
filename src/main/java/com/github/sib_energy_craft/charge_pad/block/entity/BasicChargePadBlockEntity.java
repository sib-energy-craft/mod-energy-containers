package com.github.sib_energy_craft.charge_pad.block.entity;

import com.github.sib_energy_craft.charge_pad.block.BasicChargePadBlock;
import com.github.sib_energy_craft.charge_pad.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public class BasicChargePadBlockEntity extends AbstractChargePadBlockEntity {
    public BasicChargePadBlockEntity(@NotNull BlockPos pos,
                                     @NotNull BlockState state,
                                     @NotNull BasicChargePadBlock block) {
        super(Entities.BASIC_CHARGE_PAD, pos, state, block.getContainerNameCode(), block);
    }
}


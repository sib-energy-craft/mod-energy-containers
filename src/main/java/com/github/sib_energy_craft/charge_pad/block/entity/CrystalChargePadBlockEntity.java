package com.github.sib_energy_craft.charge_pad.block.entity;

import com.github.sib_energy_craft.charge_pad.block.CrystalChargePadBlock;
import com.github.sib_energy_craft.charge_pad.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public class CrystalChargePadBlockEntity extends AbstractChargePadBlockEntity {
    public CrystalChargePadBlockEntity(@NotNull BlockPos pos,
                                       @NotNull BlockState state,
                                       @NotNull CrystalChargePadBlock block) {
        super(Entities.CRYSTAL_CHARGE_PAD, pos, state, block.getContainerNameCode(), block);
    }
}


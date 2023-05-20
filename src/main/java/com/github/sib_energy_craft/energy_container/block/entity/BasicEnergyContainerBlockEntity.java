package com.github.sib_energy_craft.energy_container.block.entity;

import com.github.sib_energy_craft.energy_container.block.BasicEnergyContainerBlock;
import com.github.sib_energy_craft.energy_container.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class BasicEnergyContainerBlockEntity extends AbstractEnergyContainerBlockEntity {
    public BasicEnergyContainerBlockEntity(@NotNull BlockPos pos,
                                           @NotNull BlockState state,
                                           @NotNull BasicEnergyContainerBlock block) {
        super(Entities.BASIC_ENERGY_CONTAINER, pos, state, block.getContainerNameCode(), block);
    }
}


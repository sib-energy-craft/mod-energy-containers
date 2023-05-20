package com.github.sib_energy_craft.charge_pad.block;

import com.github.sib_energy_craft.charge_pad.block.entity.CrystalChargePadBlockEntity;
import com.github.sib_energy_craft.charge_pad.load.Entities;
import com.github.sib_energy_craft.energy_api.EnergyLevel;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public class CrystalChargePadBlock extends AbstractChargePadBlock {
    @Getter
    private final String containerNameCode;

    public CrystalChargePadBlock(@NotNull Settings settings,
                                 @NotNull String containerNameCode,
                                 @NotNull EnergyLevel energyLevel,
                                 int maxCharge) {
        super(settings, energyLevel, maxCharge);
        this.containerNameCode = containerNameCode;
    }

    @NotNull
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos,
                                         @NotNull BlockState state) {
        return new CrystalChargePadBlockEntity(pos, state, this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world,
                                                                  @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return checkPadType(world, type, Entities.CRYSTAL_CHARGE_PAD);
    }
}

package com.github.sib_energy_craft.charge_pad.block;

import com.github.sib_energy_craft.charge_pad.block.entity.AbstractChargePadBlockEntity;
import com.github.sib_energy_craft.energy_api.EnergyLevel;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.energy_api.tags.CoreTags;
import com.github.sib_energy_craft.energy_container.block.AbstractEnergyContainerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public abstract class AbstractChargePadBlock extends AbstractEnergyContainerBlock {
    public static final BooleanProperty CHARGE = BooleanProperty.of("charge");

    protected AbstractChargePadBlock(@NotNull Settings settings,
                                     @NotNull EnergyLevel energyLevel,
                                     int maxCharge) {
        super(settings, energyLevel, maxCharge);
        this.setDefaultState(getDefaultState()
                .with(CHARGE, Boolean.FALSE));
    }


    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CHARGE);
    }

    @Override
    public void onSteppedOn(@NotNull World world,
                            @NotNull BlockPos pos,
                            @NotNull BlockState state,
                            @NotNull Entity entity) {
        var blockEntity = world.getBlockEntity(pos);
        if(!(blockEntity instanceof AbstractChargePadBlockEntity chargepadBlockEntity)) {
            return;
        }
        if(!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        List<ItemStack> itemsToCharge = new ArrayList<>();

        getItemsToCharge(livingEntity.getItemsEquipped(), itemsToCharge);
        getItemToCharge(livingEntity.getActiveItem(), itemsToCharge);

        var charged = false;
        for (var itemStack : itemsToCharge) {
            charged |= chargepadBlockEntity.charge(itemStack);
        }
        if(charged) {
            world.setBlockState(pos, state.with(CHARGE, Boolean.TRUE), Block.NOTIFY_ALL);
            chargepadBlockEntity.chargeStateOn();
        }
    }

    private static void getItemsToCharge(@NotNull Iterable<ItemStack> itemStacks,
                                         @NotNull List<ItemStack> itemsToCharge) {
        for (var itemStack : itemStacks) {
            getItemToCharge(itemStack, itemsToCharge);
        }
    }

    private static void getItemToCharge(@NotNull ItemStack itemStack,
                                        @NotNull List<ItemStack> itemsToCharge) {
        if (itemStack.isEmpty() || !CoreTags.isChargeable(itemStack)) {
            return;
        }
        var item = itemStack.getItem();
        if (!(item instanceof ChargeableItem chargeableItem)) {
            return;
        }
        if(chargeableItem.hasFreeSpace(itemStack)) {
            itemsToCharge.add(itemStack);
        }
    }

    @Nullable
    protected static <T extends BlockEntity, E extends AbstractChargePadBlockEntity> BlockEntityTicker<T> checkPadType(
            @NotNull World world,
            @NotNull BlockEntityType<T> givenType,
            @NotNull BlockEntityType<E> expectedType) {
        return world.isClient ? null : BlockWithEntity.checkType(givenType, expectedType,
                AbstractChargePadBlockEntity::tick);
    }
}

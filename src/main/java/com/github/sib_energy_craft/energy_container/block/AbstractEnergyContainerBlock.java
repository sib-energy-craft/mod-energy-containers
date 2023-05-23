package com.github.sib_energy_craft.energy_container.block;

import com.github.sib_energy_craft.energy_api.EnergyLevel;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.energy_container.block.entity.AbstractEnergyContainerBlockEntity;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractEnergyContainerBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.FACING;
    @Getter
    private final int maxCharge;
    @Getter
    private final EnergyLevel energyLevel;

    protected AbstractEnergyContainerBlock(@NotNull Settings settings,
                                           @NotNull EnergyLevel energyLevel,
                                           int maxCharge) {
        super(settings);
        this.energyLevel = energyLevel;
        this.maxCharge = maxCharge;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.UP));
    }

    @NotNull
    @Override
    public ActionResult onUse(@NotNull BlockState state,
                              @NotNull World world,
                              @NotNull BlockPos pos,
                              @NotNull PlayerEntity player,
                              @NotNull Hand hand,
                              @NotNull BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        this.openScreen(world, pos, player);
        return ActionResult.CONSUME;
    }

    @NotNull
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        var playerLookDirection = ctx.getPlayerLookDirection();
        return this.getDefaultState().with(FACING, playerLookDirection.getOpposite());
    }

    @Override
    public void onPlaced(@NotNull World world,
                         @NotNull BlockPos pos,
                         @NotNull BlockState state,
                         @Nullable LivingEntity placer,
                         @NotNull ItemStack itemStack) {
        var blockEntity = world.getBlockEntity(pos);
        if(!(blockEntity instanceof AbstractEnergyContainerBlockEntity energyContainerBlock)) {
            return;
        }
        var item = itemStack.getItem();
        if(item instanceof ChargeableItem chargeableItem) {
            energyContainerBlock.onPlaced(chargeableItem.getCharge(itemStack));
        }
    }

    @Override
    public void onStateReplaced(@NotNull BlockState state,
                                @NotNull World world,
                                @NotNull BlockPos pos,
                                @NotNull BlockState newState,
                                boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AbstractEnergyContainerBlockEntity energyContainerBlock) {
            if (world instanceof ServerWorld) {
                ItemScatterer.spawn(world, pos, energyContainerBlock);
            }
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorOutput(@NotNull BlockState state) {
        return false;
    }

    @Override
    public int getComparatorOutput(@NotNull BlockState state,
                                   @NotNull World world,
                                   @NotNull BlockPos pos) {
        return 0;
    }

    @NotNull
    @Override
    public BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState state,
                             @NotNull BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state,
                             @NotNull BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    protected static <T extends BlockEntity, E extends AbstractEnergyContainerBlockEntity> BlockEntityTicker<T> checkType(
            @NotNull World world,
            @NotNull BlockEntityType<T> givenType,
            @NotNull BlockEntityType<E> expectedType) {
        return world.isClient ? null : AbstractEnergyContainerBlock.checkType(givenType, expectedType,
                AbstractEnergyContainerBlockEntity::tick);
    }

    @Override
    public void afterBreak(@NotNull World world,
                           @NotNull PlayerEntity player,
                           @NotNull BlockPos pos,
                           @NotNull BlockState state,
                           @Nullable BlockEntity blockEntity,
                           @NotNull ItemStack hand) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        getDroppedStacks(state, serverWorld, pos, blockEntity, player, hand).forEach((stackx) -> {
            dropStack(world, pos, stackx);
            if (!(blockEntity instanceof AbstractEnergyContainerBlockEntity energyContainerBlock)) {
                return;
            }
            var item = stackx.getItem();
            var charge = energyContainerBlock.getCharge();
            if (item instanceof ChargeableItem chargeableItem) {
                chargeableItem.charge(stackx, charge);
            }
        });
        state.onStacksDropped(serverWorld, pos, hand, true);
    }

    protected void openScreen(@NotNull World world,
                              @NotNull BlockPos blockPos,
                              @NotNull PlayerEntity playerEntity) {
        var blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof AbstractEnergyContainerBlockEntity containerBlockEntity) {
            playerEntity.openHandledScreen(containerBlockEntity);
        }
    }
}

package com.github.sib_energy_craft.energy_container.block.entity;

import com.github.sib_energy_craft.containers.CleanEnergyContainer;
import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.EnergyOffer;
import com.github.sib_energy_craft.energy_api.consumer.EnergyConsumer;
import com.github.sib_energy_craft.energy_api.supplier.EnergySupplier;
import com.github.sib_energy_craft.energy_api.tags.CoreTags;
import com.github.sib_energy_craft.energy_container.block.AbstractEnergyContainerBlock;
import com.github.sib_energy_craft.screen.property.ScreenPropertyTypes;
import com.github.sib_energy_craft.energy_container.screen.EnergyContainerScreenHandler;
import com.github.sib_energy_craft.network.PropertyUpdateSyncer;
import com.github.sib_energy_craft.screen.property.TypedScreenProperty;
import com.github.sib_energy_craft.sec_utils.utils.BlockEntityUtils;
import lombok.Getter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractEnergyContainerBlockEntity extends BlockEntity
        implements SidedInventory, ExtendedScreenHandlerFactory, EnergyConsumer, EnergySupplier {

    public static final int CHARGE_SLOT = 0;
    public static final int DISCHARGE_SLOT = 1;

    public static final int[] ACTIVE_SLOTS = {
            CHARGE_SLOT, DISCHARGE_SLOT
    };

    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    @Getter
    protected final Text containerName;
    protected final AbstractEnergyContainerBlock block;
    protected final List<TypedScreenProperty<?>> typedScreenProperties;
    protected CleanEnergyContainer energyContainer;

    private Direction supplyingDirection;

    protected AbstractEnergyContainerBlockEntity(@NotNull BlockEntityType<?> blockEntityType,
                                                 @NotNull BlockPos pos,
                                                 @NotNull BlockState state,
                                                 @NotNull String containerNameCode,
                                                 @NotNull AbstractEnergyContainerBlock block) {
        super(blockEntityType, pos,state);
        this.containerName = Text.translatable(containerNameCode);
        this.energyContainer = new CleanEnergyContainer(Energy.ZERO, block.getMaxCharge());
        this.block = block;
        this.typedScreenProperties = List.of(
                new TypedScreenProperty<>(
                        EnergyContainerProperties.CHARGE.ordinal(),
                        ScreenPropertyTypes.INT,
                        this::getCharge
                )
        );
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.energyContainer = CleanEnergyContainer.readNbt(nbt);
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        this.energyContainer.writeNbt(nbt);
    }

    public static void tick(@NotNull World world,
                            @NotNull BlockPos pos,
                            @NotNull BlockState state,
                            @NotNull AbstractEnergyContainerBlockEntity blockEntity) {
        if(world.isClient) {
            return;
        }

        var supplierSide = state.get(AbstractEnergyContainerBlock.FACING);
        if(blockEntity.supplyingDirection != supplierSide) {
            blockEntity.supplyingDirection = supplierSide;
        }

        var packetSize = blockEntity.block.getEnergyLevel().toBig;
        var dischargeStack = blockEntity.inventory.get(DISCHARGE_SLOT);
        blockEntity.energyContainer.discharge(dischargeStack, packetSize);

        if(blockEntity.energyContainer.hasEnergy()) {
            var chargingStack = blockEntity.inventory.get(CHARGE_SLOT);
            blockEntity.energyContainer.charge(chargingStack, packetSize);
            if(blockEntity.energyContainer.hasEnergy()) {
                blockEntity.tick(blockEntity);
            }
        }

        AbstractEnergyContainerBlockEntity.markDirty(world, pos, state);
    }

    @Override
    public int[] getAvailableSlots(@NotNull Direction side) {
        return ACTIVE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot,
                             @NotNull ItemStack stack,
                             @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot,
                              @NotNull ItemStack stack,
                              @NotNull Direction dir) {
        if (dir == Direction.DOWN && slot == DISCHARGE_SLOT) {
            return stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET);
        }
        return true;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @NotNull
    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @NotNull
    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @NotNull
    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot,
                         @NotNull ItemStack stack) {
        var itemStack = this.inventory.get(slot);
        var bl = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areNbtEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        if ((slot == CHARGE_SLOT || slot == DISCHARGE_SLOT) && !bl) {
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(@NotNull PlayerEntity player) {
        return BlockEntityUtils.canPlayerUse(this, player);
    }

    @Override
    public boolean isValid(int slot,
                           @NotNull ItemStack stack) {
        if (slot == CHARGE_SLOT || slot == DISCHARGE_SLOT) {
            return CoreTags.isChargeable(stack);
        }
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    /**
     * Called when item placed in world
     * @param charge item charge
     */
    public void onPlaced(int charge) {
        this.energyContainer.add(charge);
    }

    @Override
    public void receiveOffer(@NotNull EnergyOffer energyOffer) {
        var energyLevel = block.getEnergyLevel();
        if(energyOffer.getEnergyAmount().compareTo(energyLevel.toBig) > 0) {
            if(energyOffer.acceptOffer()) {
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1f, 1f);
                    serverWorld.breakBlock(pos, false);
                    return;
                }
            }
        }
        energyContainer.receiveOffer(energyOffer);
    }

    @Override
    public @NotNull EnergyOffer createOffer() {
        var energyLevel = block.getEnergyLevel();
        return new EnergyOffer(this, energyLevel.toBig);
    }

    @Override
    public boolean supplyEnergy(@NotNull Energy energyAmount) {
        return energyContainer.subtract(energyAmount);
    }

    @Override
    public @NotNull Set<Direction> getSupplyingDirections() {
        return Set.of(supplyingDirection);
    }

    @Override
    public boolean isConsumeFrom(@NotNull Direction direction) {
        return supplyingDirection != direction;
    }

    /**
     * Get current container charge
     *
     * @return container charge
     */
    public int getCharge() {
        return energyContainer.getCharge().intValue();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId,
                                    @NotNull PlayerInventory playerInventory,
                                    @NotNull PlayerEntity player) {
        var screenHandler = new EnergyContainerScreenHandler(syncId, playerInventory, this, getCharge(), block);
        var world = player.world;
        if(!world.isClient && player instanceof ServerPlayerEntity serverPlayerEntity) {
            var syncer = new PropertyUpdateSyncer(syncId, serverPlayerEntity, typedScreenProperties);
            screenHandler.setSyncer(syncer);
        }
        return screenHandler;
    }

    @Override
    public Text getDisplayName() {
        return containerName;
    }

    @Override
    public void writeScreenOpeningData(@NotNull ServerPlayerEntity player,
                                       @NotNull PacketByteBuf buf) {
        buf.writeInt(getCharge());
        buf.writeInt(block.getMaxCharge());
        buf.writeInt(block.getEnergyLevel().to);
    }
}


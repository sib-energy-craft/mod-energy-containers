package com.github.sib_energy_craft.energy_container.item;

import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.energy_container.block.AbstractEnergyContainerBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class EnergyContainerBlockItem extends BlockItem implements ChargeableItem {

    public EnergyContainerBlockItem(@NotNull AbstractEnergyContainerBlock block,
                                    @NotNull Settings settings) {
        super(block, settings);
    }

    @Override
    public void onCraft(@NotNull ItemStack itemStack) {
        ChargeableItem.super.onCraft(itemStack);
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack,
                              @Nullable World world,
                              @NotNull List<Text> tooltip,
                              @NotNull TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        var block = getBlock();
        var energyLevel = block.getEnergyLevel();
        var textColor = Color.GRAY.getRGB();
        var textStyle = Style.EMPTY.withColor(textColor);
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.max_input_eu", energyLevel.to)
                .setStyle(textStyle));
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.output_eu", energyLevel.to)
                .setStyle(textStyle));
        this.appendTooltip(stack, tooltip);
    }

    @Override
    public AbstractEnergyContainerBlock getBlock() {
        return (AbstractEnergyContainerBlock) super.getBlock();
    }

    @Override
    public int getMaxCharge() {
        var block = getBlock();
        return block.getMaxCharge();
    }
}

package com.github.sib_energy_craft.charge_pad.load;

import com.github.sib_energy_craft.energy_container.item.EnergyContainerBlockItem;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import com.github.sib_energy_craft.sec_utils.utils.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Items implements DefaultModInitializer {
    public static final EnergyContainerBlockItem BASIC_CHARGE_PAD;
    public static final EnergyContainerBlockItem BRONZE_CHARGE_PAD;
    public static final EnergyContainerBlockItem CRYSTAL_CHARGE_PAD;

    static {
        var commonSettings = new Item.Settings();

        BASIC_CHARGE_PAD = ItemUtils.register(ItemGroups.FUNCTIONAL,
                Blocks.BASIC_CHARGE_PAD,
                it -> new EnergyContainerBlockItem(it, commonSettings));

        BRONZE_CHARGE_PAD = ItemUtils.register(ItemGroups.FUNCTIONAL,
                Blocks.BRONZE_CHARGE_PAD,
                it -> new EnergyContainerBlockItem(it, commonSettings));

        CRYSTAL_CHARGE_PAD = ItemUtils.register(ItemGroups.FUNCTIONAL,
                Blocks.CRYSTAL_CHARGE_PAD,
                it -> new EnergyContainerBlockItem(it, commonSettings));
    }
}

package com.github.sib_energy_craft.charge_pad.load;

import com.github.sib_energy_craft.charge_pad.block.BasicChargePadBlock;
import com.github.sib_energy_craft.charge_pad.block.BronzeChargePadBlock;
import com.github.sib_energy_craft.charge_pad.block.CrystalChargePadBlock;
import com.github.sib_energy_craft.energy_api.EnergyLevel;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.common.Identified;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.sib_energy_craft.sec_utils.utils.BlockUtils.register;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Blocks implements DefaultModInitializer {
    public static final Identified<BasicChargePadBlock> BASIC_CHARGE_PAD;
    public static final Identified<BronzeChargePadBlock> BRONZE_CHARGE_PAD;
    public static final Identified<CrystalChargePadBlock> CRYSTAL_CHARGE_PAD;

    static {
        var metalBlockSettings = AbstractBlock.Settings.create()
                .mapColor(MapColor.IRON_GRAY)
                .sounds(BlockSoundGroup.METAL)
                .strength(5, 6)
                .requiresTool();

        var basicChargePad = new BasicChargePadBlock(
                metalBlockSettings,
                "container.basic_charge_pad",
                EnergyLevel.L1,
                40_000
        );
        BASIC_CHARGE_PAD = register(Identifiers.of("basic_charge_pad"), basicChargePad);

        var bronzeChargePad = new BronzeChargePadBlock(
                metalBlockSettings,
                "container.bronze_charge_pad",
                EnergyLevel.L2,
                300_000
        );
        BRONZE_CHARGE_PAD = register(Identifiers.of("bronze_charge_pad"), bronzeChargePad);

        var crystalChargePad = new CrystalChargePadBlock(
                metalBlockSettings,
                "container.crystal_charge_pad",
                EnergyLevel.L3,
                4_000_000
        );
        CRYSTAL_CHARGE_PAD = register(Identifiers.of("crystal_charge_pad"), crystalChargePad);
    }
}

package com.github.sib_energy_craft.charge_pad.load;

import com.github.sib_energy_craft.charge_pad.block.entity.BasicChargePadBlockEntity;
import com.github.sib_energy_craft.charge_pad.block.entity.BronzeChargePadBlockEntity;
import com.github.sib_energy_craft.charge_pad.block.entity.CrystalChargePadBlockEntity;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.block.entity.BlockEntityType;

import static com.github.sib_energy_craft.sec_utils.utils.EntityUtils.register;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Entities implements DefaultModInitializer {
    public static final BlockEntityType<BasicChargePadBlockEntity> BASIC_CHARGE_PAD;
    public static final BlockEntityType<BronzeChargePadBlockEntity> BRONZE_CHARGE_PAD;
    public static final BlockEntityType<CrystalChargePadBlockEntity> CRYSTAL_CHARGE_PAD;

    static {
        BASIC_CHARGE_PAD = register(Blocks.BASIC_CHARGE_PAD, BasicChargePadBlockEntity::new);
        BRONZE_CHARGE_PAD = register(Blocks.BRONZE_CHARGE_PAD, BronzeChargePadBlockEntity::new);
        CRYSTAL_CHARGE_PAD = register(Blocks.CRYSTAL_CHARGE_PAD, CrystalChargePadBlockEntity::new);
    }
}

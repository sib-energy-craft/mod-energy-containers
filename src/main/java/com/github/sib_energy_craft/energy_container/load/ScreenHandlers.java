package com.github.sib_energy_craft.energy_container.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.energy_container.screen.EnergyContainerScreenHandler;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.screen.ScreenHandlerType;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerHandler;

/**
 * @since 0.0.3
 * @author sibmaks
 */
public final class ScreenHandlers implements DefaultModInitializer {
    public static final ScreenHandlerType<EnergyContainerScreenHandler> ENERGY_CONTAINER;

    static {
        ENERGY_CONTAINER = registerHandler(Identifiers.of("energy_container"), EnergyContainerScreenHandler::new);
    }
}

package com.github.sib_energy_craft.energy_container.load.client;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.energy_container.screen.EnergyContainerScreen;
import com.github.sib_energy_craft.energy_container.screen.EnergyContainerScreenHandler;
import com.github.sib_energy_craft.sec_utils.load.DefaultClientModInitializer;
import net.minecraft.screen.ScreenHandlerType;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.register;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Screens implements DefaultClientModInitializer {
    public static final ScreenHandlerType<EnergyContainerScreenHandler> ENERGY_CONTAINER;

    static {
        ENERGY_CONTAINER = register(Identifiers.of("energy_container"), EnergyContainerScreenHandler::new,
                EnergyContainerScreen::new);
    }
}

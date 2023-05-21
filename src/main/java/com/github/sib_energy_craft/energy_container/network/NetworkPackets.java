package com.github.sib_energy_craft.energy_container.network;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.Identifier;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkPackets {
    public static final Identifier UPDATE_INT_PROPERTY = Identifiers.of("update_int_property");
}

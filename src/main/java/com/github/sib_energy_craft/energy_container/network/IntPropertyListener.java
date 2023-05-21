package com.github.sib_energy_craft.energy_container.network;

/**
 * @author sibmaks
 * @since 0.0.3
 */
public interface IntPropertyListener {
    /**
     * Called then int property is changed
     *
     * @param index index of property
     * @param value property value
     */
    void setIntProperty(int index, int value);
}

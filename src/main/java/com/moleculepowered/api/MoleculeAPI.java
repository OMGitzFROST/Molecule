package com.moleculepowered.api;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class MoleculeAPI {

    public static Plugin plugin;

    /**
     * Return's the plugin was registered with {@link MoleculeAPI}, please note that
     * if a plugin has not been registered, this method could return null.
     *
     * @return The registered plugin
     * @see #setPlugin(Plugin)
     */
    public static @Nullable Plugin getPlugin() {
        return plugin;
    }

    /**
     * Registers a plugin to associate with {@link MoleculeAPI}
     *
     * @param plugin Target plugin
     */
    public static void setPlugin(Plugin plugin) {
        MoleculeAPI.plugin = plugin;
    }
}

package com.moleculepowered.api;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Objects;

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

    /**
     * Returns a resource from within MoleculeAPI, please note that this should not be used to
     * return a resource from within your own plugin's resource folder.
     *
     * @param name Resource name
     * @return A resource from MoleculeAPI
     */
    public static @NotNull InputStream getResource(@NotNull String name) {
        return Objects.requireNonNull(MoleculeAPI.class.getClassLoader().getResourceAsStream(name));
    }
}

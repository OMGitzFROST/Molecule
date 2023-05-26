package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.config.ConfigManager;
import com.moleculepowered.api.user.UserManager;
import com.moleculepowered.api.util.ComparableVersion;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class used to create a {@link MoleculePlugin} using the bukkit framework
 *
 * @author OMGitzFROST
 */
public abstract class BukkitPlugin extends JavaPlugin implements MoleculePlugin {

    /**
     * Returns a {@link ComparableVersion} number for this plugin. This method allows
     * you to compare two version numbers to identify if they are greater than, less than
     * or equal to each other.
     *
     * @return A comparable version
     */
    @Override
    public @NotNull ComparableVersion getVersion() {
        return new ComparableVersion(getDescription().getVersion());
    }

    /*
    MANAGER GETTERS
     */

    /**
     * Return's an instance of the {@link UserManager} class.
     *
     * @return {@link UserManager} class
     */
    @Override
    public abstract @NotNull UserManager getUserManager();

    /**
     * Returns an instance of the {@link ConfigManager} class
     *
     * @return {@link ConfigManager} class
     */
    @Override
    public abstract @NotNull ConfigManager getConfigManager();
}

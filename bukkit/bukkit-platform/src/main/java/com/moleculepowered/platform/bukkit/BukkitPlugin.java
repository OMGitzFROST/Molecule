package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.config.ConfigManager;
import com.moleculepowered.api.platform.Platform;
import com.moleculepowered.api.user.UserManager;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.platform.bukkit.user.BukkitUserManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class used to create a {@link MoleculePlugin} using the bukkit framework
 *
 * @author OMGitzFROST
 */
public abstract class BukkitPlugin extends JavaPlugin implements MoleculePlugin
{
    private final UserManager userManager;
    private final BukkitConsole console;

    /*
    CONSTRUCTOR
     */

    /**
     * The main constructor used to initialize the required objects assigned to this platform.
     */
    public BukkitPlugin() {
        console = new BukkitConsole(this);
        userManager = new BukkitUserManager(this);
    }

    /**
     * Returns the platform that this implementation is on
     *
     * @return Implementation platform
     */
    @Override
    public @NotNull Platform.Type getPlatform() {
        return Type.BUKKIT;
    }

    /**
     * Returns an enhanced console logger, it features color coded messages (differs between
     * server environments), as well as customized logging methods (debug, etc)
     *
     * @return Platform console
     */
    @Override
    public @NotNull BukkitConsole getConsole() {
        return console;
    }

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
    public @NotNull UserManager getUserManager() {
        return userManager;
    }

    /**
     * Returns an instance of the {@link ConfigManager} class
     *
     * @return {@link ConfigManager} class
     */
    @Override
    public @NotNull ConfigManager getConfigManager() {
        throw new UnsupportedOperationException("A config manager has not been configured for the bungee platform");
    }
}

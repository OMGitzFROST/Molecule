package com.moleculepowered.platform.bungee;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.config.ConfigManager;
import com.moleculepowered.api.platform.Platform;
import com.moleculepowered.api.platform.PlatformConsole;
import com.moleculepowered.api.user.UserManager;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.platform.bungee.user.BungeeUserManager;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

// TODO: 5/27/23 JAVADOC
public class BungeePlugin extends Plugin implements MoleculePlugin
{
    private final UserManager userManager;
    private final PlatformConsole console;

    // TODO: 5/27/23 JAVADOC
    public BungeePlugin() {
        this.userManager = new BungeeUserManager(this);
        this.console     = new BungeeConsole(this);
    }

    /**
     * Returns an enhanced console logger, it features color coded messages (differs between
     * server environments), as well as customized logging methods (debug, etc)
     *
     * @return Platform console
     */
    @Override
    public @NotNull PlatformConsole getConsole() {
        return console;
    }

    /**
     * Returns the platform that this implementation is built on
     *
     * @return Implementation platform
     */
    @Override
    public Platform.@NotNull Type getPlatform() {
        return Type.BUNGEE;
    }

    /**
     * Returns an internal resource within your jars resource file
     *
     * @param resourceName Path to internal resource
     * @return A resource input stream
     */
    @Override
    public InputStream getResource(String resourceName) {
        // TODO: 5/27/23 TEST, UNSURE IF THIS WORKS
        return getClass().getClassLoader().getResourceAsStream(resourceName);
    }

    /**
     * Returns the name assigned to this plugin
     *
     * @return Plugin name
     */
    @Override
    public String getName() {
        return getDescription().getName();
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
        // TODO: 5/27/23 ADD FUNCTIONALITY
        throw new UnsupportedOperationException("A config manager has not been configured for the bungee platform");
    }
}

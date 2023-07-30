package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.Platform;
import com.moleculepowered.api.user.UserManager;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.platform.bukkit.updater.BukkitUpdater;
import com.moleculepowered.platform.bukkit.user.BukkitUserManager;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The BukkitPlugin class is an abstract class used to create a Bukkit-based plugin using the Bukkit framework.
 * It extends the JavaPlugin class and implements the MoleculePlugin interface, providing additional functionality
 * and features specific to the Molecule platform.
 *
 * <p>
 * To create a Bukkit plugin, extend the BukkitPlugin class and implement the necessary methods and event listeners.
 * The BukkitPlugin class provides access to the BukkitUpdater, console, user manager, and other essential components
 * for plugin development and management.
 * </p>
 *
 * @author OMGitzFROST
 * @see JavaPlugin
 * @see MoleculePlugin
 */
public abstract class BukkitPlugin extends JavaPlugin implements MoleculePlugin
{
    private final UserManager userManager;
    private final BukkitConsole console;
    private final BukkitUpdater updater;

    /*
    CONSTRUCTOR
     */

    /**
     * The main constructor used to initialize the required objects assigned to this platform.
     */
    public BukkitPlugin() {

        // INITIALIZE UPDATER
        updater = new BukkitUpdater(this);

        // INITIALIZE CONSOLE
        console = new BukkitConsole(this);

        // INITIALIZE USER-MANAGER
        userManager = new BukkitUserManager(this);
        userManager.onEnable();
    }

    /**
     * Registers all the events in the given listener class.
     *
     * @param listener The listener to register
     */
    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Calls an event with the given details.
     *
     * @param event The event details
     * @throws IllegalStateException Thrown when an asynchronous event is fired from synchronous code.
     *                               <p>
     *                               <i>Note: This is a best-effort basis and should not be used to test synchronized state.
     *                               This is an indicator of flawed flow logic.</i>
     */
    public void callEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
    }

    /**
     * Returns the platform that this implementation is on.
     *
     * @return The implementation platform
     */
    @Override
    public @NotNull Platform.Type getPlatform() {
        return Type.BUKKIT;
    }

    /**
     * Returns an enhanced console logger. It features color-coded messages (differs between
     * server environments), as well as customized logging methods (debug, etc).
     *
     * @return The platform console
     */
    @Override
    public @NotNull BukkitConsole getConsole() {
        return console;
    }

    /**
     * Returns the BukkitUpdater associated with this platform.
     *
     * @return The BukkitUpdater object
     */
    @Override
    public @NotNull BukkitUpdater getUpdater() {
        return updater;
    }

    /**
     * Returns a {@link ComparableVersion} number for this plugin. This method allows
     * you to compare two version numbers to identify if they are greater than, less than,
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
     * Returns an instance of the {@link UserManager} class.
     *
     * @return The {@link UserManager} class
     */
    @Override
    public @NotNull UserManager getUserManager() {
        return userManager;
    }
}

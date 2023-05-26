package com.moleculepowered.api;

import com.moleculepowered.api.config.ConfigManager;
import com.moleculepowered.api.model.Manager;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.api.user.User;
import com.moleculepowered.api.user.UserManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * An interface that provides the framework needed for our cross-platform plugin's
 *
 * @author OMGitzFROST
 */
public interface MoleculePlugin extends Manager {

    /*
    FILE METHODS
     */

    /**
     * Used to return this plugin's data folder
     *
     * @return Plugins data folder
     */
    File getDataFolder();

    /**
     * Returns the folder where the user data files are stored.
     *
     * @return User data folder
     */
    default @NotNull File getUserDataFolder() {
        return new File(getDataFolder(), "user-data");
    }

    /**
     * Returns an internal resource within your jars resource file
     *
     * @param resourceName Path to internal resource
     * @return A resource input stream
     */
    InputStream getResource(String resourceName);

    /*
    DESCRIPTION GETTERS
     */

    /**
     * Returns the name assigned to this plugin
     *
     * @return Plugin name
     */
    String getName();

    /**
     * Returns a {@link ComparableVersion} number for this plugin. This method allows
     * you to compare two version numbers to identify if they are greater than, less than
     * or equal to each other.
     *
     * @return A comparable version
     */
    @NotNull ComparableVersion getVersion();

    /*
    USER GETTER METHODS
     */

    /**
     * <p>Returns a user based on the name provided, using a filter system; this method will attempt to locate a
     * user with the matching name. Otherwise, if a user is not found, this will throw an exception.</p>
     *
     * <p>This method is case-insensitive so any input could return a user</p>
     *
     * @param name Target name
     * @return A user based on the name provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    default @NotNull User getUser(String name) {
        return getUserManager().getUser(name);
    }

    /**
     * Returns a user based on the uuid provided, using a filter system; this method will attempt to locate a
     * user with the matching uuid. Otherwise, if a user is not found, this will throw an exception.
     *
     * @param uuid Target {@link UUID}
     * @return A user based on the uuid provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    default @NotNull User getUser(UUID uuid) {
        return getUserManager().getUser(uuid);
    }

    /**
     * Return's a user based on a specific filter, please note that this method will not return a null user,
     * but if it cannot find a user using the provided filter, this method will throw an exception.
     *
     * @param filter User filter
     * @return A user based on the filter provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    default @NotNull User getUser(Predicate<User> filter) {
        return getUserManager().getUser(filter);
    }

    /*
    SERVICE MANAGER GETTERS
     */

    /**
     * Return's an instance of the {@link UserManager} class.
     *
     * @return {@link UserManager} class
     */
    @NotNull UserManager getUserManager();

    /**
     * Returns an instance of the {@link ConfigManager} class
     *
     * @return {@link ConfigManager} class
     */
    @NotNull ConfigManager getConfigManager();
}

package com.moleculepowered.api;

import com.moleculepowered.api.model.Manager;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.user.User;
import com.moleculepowered.api.user.UserManager;
import com.moleculepowered.api.util.ComparableVersion;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * An interface that provides the framework needed for our cross-platform plugins.
 *
 * @author OMGitzFROST
 */
public interface MoleculePlugin extends Manager, Platform
{
    /*
    PLATFORM METHODS
     */

    /**
     * Returns an instance of the updater assigned to this platform.
     * This updater handles update checks.
     *
     * <p>Note that calling this method alone will not schedule or determine which marketplaces
     * the updater will fetch information from. You need to configure this behavior separately
     * for the updater to work effectively.
     *
     * @return The updater instance.
     */
    @NotNull Updater getUpdater();

    /**
     * Returns an enhanced console logger that features color-coded messages (differs between
     * server environments) and customized logging methods (debug, etc).
     *
     * @return Platform console
     */
    @NotNull
    Console getConsole();

    /**
     * Returns the platform that this implementation is built on.
     *
     * @return Implementation platform
     */
    @NotNull
    Platform.Type getPlatform();

    /*
    FILE METHODS
     */

    /**
     * Returns the data folder assigned to your plugin.
     *
     * @return Plugin data folder
     */
    File getDataFolder();

    /**
     * Returns an internal resource within your jar's resource file.
     *
     * @param resourceName Path to the internal resource
     * @return A resource input stream
     */
    InputStream getResource(String resourceName);

    /*
    DESCRIPTION GETTERS
     */

    /**
     * Returns the name assigned to this plugin.
     *
     * @return Plugin name
     */
    String getName();

    /**
     * Returns a {@link ComparableVersion} number for this plugin. This method allows
     * you to compare two version numbers to identify if they are greater than, less than,
     * or equal to each other.
     *
     * @return A comparable version
     */
    @NotNull ComparableVersion getVersion();

    /*
    USER GETTER METHODS
     */

    /**
     * Returns a user based on the provided name, using a filter system. This method will attempt to locate a
     * user with the matching name. If a user is not found, this method will throw an exception.
     *
     * <p>This method is case-insensitive, so any input could return a user.</p>
     *
     * @param name Target name
     * @return A user based on the provided name
     * @throws NullPointerException when a user cannot be found with the provided filter
     */
    default @NotNull User getUser(String name) {
        return getUserManager().getUser(name);
    }

    /**
     * Returns a user based on the provided UUID, using a filter system. This method will attempt to locate a
     * user with the matching UUID. If a user is not found, this method will throw an exception.
     *
     * @param uuid Target {@link UUID}
     * @return A user based on the provided UUID
     * @throws NullPointerException when a user cannot be found with the provided filter
     */
    default @NotNull User getUser(UUID uuid) {
        return getUserManager().getUser(uuid);
    }

    /**
     * Returns a user based on a specific filter. Please note that this method will not return a null user,
     * but if it cannot find a user using the provided filter, this method will throw an exception.
     *
     * @param filter User filter
     * @return A user based on the provided filter
     * @throws NullPointerException when a user cannot be found with the provided filter
     */
    default @NotNull User getUser(Predicate<User> filter) {
        return getUserManager().getUser(filter);
    }

    /**
     * Returns a collection of users handled by this manager. Note that users are NOT
     * loaded into this collection by default but should typically be loaded using the
     * {@link #onEnable()} method.
     *
     * @return A collection of users
     */
    default @NotNull Collection<User> getUsers() {
        return getUserManager().getUsers();
    }

    /*
    SERVICE MANAGER GETTERS
     */

    /**
     * Returns an instance of the {@link UserManager} class.
     *
     * @return {@link UserManager} class
     */
    @NotNull UserManager getUserManager();
}
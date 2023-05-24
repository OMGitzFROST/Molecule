package com.moleculepowered.api.user;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * <p>The user interface is designed to simply provide methods that allow developers
 * to update, retrieve and handle user's effectively. The intended use for a user
 * is to be handled in an offline setting, in instances where the user information
 * cannot be accessed dynamically, such as using a player object. User information is handled
 * using the storage system delegated by the {@link UserData} interface.</p>
 *
 * @author OMGitzFROST
 */
public interface User {

    /**
     * Retrieve the name of assigned to this user object.
     *
     * @return the name of the user
     */
    @NotNull String getName();

    /**
     * Sets the name of the user.
     *
     * @param name the new name for this user
     */
    void setName(@NotNull String name);

    /**
     * Retrieve the locale assigned to this user object
     *
     * @return the user's locale
     */
    @NotNull String getLocale();

    /**
     * Sets the new locale that should be assigned to this user
     *
     * @param locale target locale
     */
    void setLocale(@NotNull String locale);

    /**
     * Retrieve the unique id assigned to this user.
     *
     * @return the unique id of this user
     */
    @NotNull UUID getUniqueId();
}

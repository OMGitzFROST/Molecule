package com.moleculepowered.api.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The user interface is designed to simply provide methods that allow developers
 * to update, retrieve, and handle users effectively. The intended use for a user
 * is to be handled in an offline setting, in instances where the user information
 * cannot be accessed dynamically, such as using a player object. User information is handled
 * using the storage system delegated by the {@link com.moleculepowered.api.user.UserData} interface.
 */
public interface User extends UserData {

    /**
     * Retrieve the unique ID assigned to this user.
     *
     * @return the unique ID of this user
     */
    @NotNull
    UUID getUniqueId();

    /**
     * Retrieve the name assigned to this user object.
     *
     * @return the name of the user
     */
    @NotNull
    String getName();

    /**
     * Retrieve the display name assigned to this user object.
     * Please note that this value may be different from the name listed.
     *
     * @return the display name of the user
     */
    @Nullable
    String getDisplayName();

    /**
     * Retrieve the custom name assigned to this user object.
     * Please note that this value may be different from the name listed.
     *
     * @return the custom name of the user
     */
    @Nullable
    String getCustomName();

    /**
     * Retrieve the locale assigned to this user object.
     *
     * @return the user's locale
     */
    @Nullable
    String getLocale();

    /**
     * Sets the new display name that should be assigned to this user.
     *
     * @param input the target display name
     */
    void setDisplayName(@Nullable String input);

    /**
     * Sets the new custom name that should be assigned to this user.
     *
     * @param input the target custom name
     */
    void setCustomName(@Nullable String input);

    /**
     * Sets the new locale that should be assigned to this user.
     *
     * @param input the target locale
     */
    void setLocale(@NotNull String input);
}
package com.moleculepowered.api.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The core storage system for our {@link User} objects. This interface is designed
 * to provide the required methods needed to handle user data across different platforms.
 */
public interface UserData
{

    /**
     * Attempts to create a new user data file.
     * <p>
     * By default, this method will not create a user file if one already exists in the
     * {@link #getDataFolder()}. Otherwise, this method will create one as usual.
     */
    void create();

    /**
     * Deletes a user's data file if one exists; otherwise, this method will do nothing.
     */
    void delete();

    /**
     * Attempts to update a user's data file. Usually, this method adds new keys to the configuration
     * and attempts to re-add removed comments if applicable.
     *
     * @param player the originating player
     */
    <T> void update(T player);

    /**
     * Used to add or update existing data with a new value.
     * <p>
     * NOTE: The value you provide must be one of the following data types:
     * {@link String}, {@link Number}, {@link Boolean}, {@link Character},
     * or {@link JsonElement}, otherwise this method will throw an exception.
     *
     * @param key   the target key
     * @param value the target value
     * @throws IllegalArgumentException when the value provided is not supported
     */
    void setData(@NotNull String key, Object value);

    /**
     * Returns data from a user's data file. If the data key does not exist inside the file,
     * the provided default value will be returned.
     *
     * @param key the data key
     * @param def the default value
     * @return the data assigned to the provided key
     */
    @Nullable String getData(@NotNull String key, @Nullable String def);

    /**
     * Returns data from a user's data file. If the data key does not exist inside the file,
     * null will be returned.
     *
     * @param key the data key
     * @return the data assigned to the provided key
     */
    default @Nullable String getData(@NotNull String key) {
        return getData(key, null);
    }

    /**
     * Returns the configuration assigned to this user. The behavior of this method, including autoload
     * features, may vary between platforms.
     *
     * @return The user's configuration as a {@link JsonObject}.
     */
    @NotNull JsonObject getData();

    /**
     * Returns the data file associated with this user.
     *
     * @return the user data file
     */
    @NotNull File getFile();

    /**
     * Returns the data folder where this user's data is stored.
     *
     * @return the user data folder
     */
    default @NotNull File getDataFolder() {
        return getFile().getParentFile();
    }
}
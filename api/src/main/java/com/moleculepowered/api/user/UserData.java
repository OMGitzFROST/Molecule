package com.moleculepowered.api.user;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * The core storage system for our {@link User} objects, this interface is designed
 * to provide the required methods needed to handle user data across different platforms.
 *
 * @author OMGitzFROST
 */
public interface UserData {

    /**
     * Creates a new user data file if one does not already exist
     *
     * @throws IOException when this method fails to create user data file
     */
    void create() throws IOException;

    /**
     * Deletes a user's data file if one exists, otherwise this method will do nothing.
     */
    void delete();

    /**
     * Attempts to update a user's data file, usually this method adds new keys to the configuration
     * and attempts to re-add removed comments if applicable.
     *
     * @throws IOException when this method fails to update a user's data file
     */
    void update() throws IOException;

    /**
     * Return the data file associated with this user
     *
     * @return User data file
     */
    @NotNull File getFile();
}

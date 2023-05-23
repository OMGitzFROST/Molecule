package com.moleculepowered.api.user;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * The core storage system for our {@link User} objects, this interface is designed
 * to provide the required methods needed to handle user data across different platforms.
 *
 * @author OMGitzFROST
 */
public interface UserData {

    /**
     * Return the data file associated with this user
     *
     * @return User data file
     */
    @NotNull File getFile();
}

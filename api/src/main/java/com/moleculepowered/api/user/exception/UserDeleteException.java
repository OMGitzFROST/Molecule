package com.moleculepowered.api.user.exception;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.moleculepowered.api.localization.i18n.tl;
import static com.moleculepowered.api.util.StringUtil.format;

// TODO: 6/3/23 ADD JAVADOC
public final class UserDeleteException extends RuntimeException
{
    /**
     * Constructs a {@link UserDeleteException} with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UserDeleteException() {
        super();
    }

    /**
     * Constructs a {@link UserDeleteException} with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UserDeleteException(@NotNull File userFile) {
        super(tl("Failed to delete user data file: ", userFile.getPath()));
    }

    /**
     * Constructs a {@link UserDeleteException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param param   Optional parameters that will be included inside the exception message
     */
    public UserDeleteException(String message, Object... param) {
        super(tl(format(message, param)));
    }
}

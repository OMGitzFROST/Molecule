package com.moleculepowered.api.exception.user;

import com.moleculepowered.api.localization.i18n;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.moleculepowered.api.localization.i18n.tl;
import static com.moleculepowered.api.util.StringUtil.format;

/**
 * Exception thrown when there is an error creating a user.
 *
 * <p>Please note that if the {@link i18n} class is properly configured to translate messages,
 * this class will use it to automatically translate messages. If not configured, all messages
 * will be output as they are provided.</p>
 *
 * @author OMGitzFROST
 */
public final class UserCreateException extends RuntimeException
{
    /**
     * Constructs a {@link UserCreateException} with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public UserCreateException() {
        super();
    }

    /**
     * Constructs a {@link UserCreateException} with the specified user file.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param userFile the user data file that failed to be deleted
     */
    public UserCreateException(@NotNull File userFile) {
        super(tl("Failed to delete user data file: ", userFile.getPath()));
    }

    /**
     * Constructs a {@link UserCreateException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     * @param param   optional parameters that will be included inside the exception message
     */
    public UserCreateException(String message, Object... param) {
        super(tl(format(message, param)));
    }
}
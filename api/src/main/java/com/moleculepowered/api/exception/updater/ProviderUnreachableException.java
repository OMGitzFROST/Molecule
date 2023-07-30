package com.moleculepowered.api.exception.updater;

import com.moleculepowered.api.localization.i18n;

import static com.moleculepowered.api.localization.i18n.tl;

/**
 * This exception is thrown when an update provider fails to reach the external server
 * for any reason.
 *
 * <p>
 * Please note that if the {@link i18n} class is properly configured to translate messages,
 * this class will use it to automatically translate messages. If not configured, all messages
 * will be output as they are provided.
 * </p>
 *
 * @author OMGitzFROST
 */
public final class ProviderUnreachableException extends RuntimeException
{
    /**
     * Constructs a new ProviderUnreachableException with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause(Throwable)}.
     */
    public ProviderUnreachableException() {
        super();
    }

    /**
     * Constructs a new ProviderUnreachableException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause(Throwable)}.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the {@link #getMessage()} method.
     * @param param   optional parameters that will be included inside the exception message
     */
    public ProviderUnreachableException(String message, Object... param) {
        super(tl(message, param));
    }

    /**
     * Constructs a new ProviderUnreachableException with the specified cause and a detail message
     * of (cause==null ? null : cause.toString()) (which typically contains the class and detail message
     * of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public ProviderUnreachableException(Throwable cause) {
        super(cause);
    }
}

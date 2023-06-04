package com.moleculepowered.api.updater.exception;

import static com.moleculepowered.api.localization.i18n.tl;

// TODO: 6/3/23 ADD JAVADOC
public final class ProviderUnreachableException extends RuntimeException
{
    /**
     * Constructs a provider fetch exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ProviderUnreachableException() {
        super();
    }

    /**
     * Constructs a provider fetch exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param param   Optional parameters that will be included inside the exception message
     */
    public ProviderUnreachableException(String message, Object... param) {
        super(tl(message, param));
    }

    // TODO: 6/3/23 ADD JAVADOC
    public ProviderUnreachableException(Throwable thrown, String message, Object... param) {
        super(tl(message, param), thrown);
    }

    // TODO: 6/3/23 ADD JAVADOC
    public ProviderUnreachableException(Throwable thrown) {
        super(thrown);
    }
}

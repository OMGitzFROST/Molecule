package com.moleculepowered.api.exception;

import static com.moleculepowered.api.util.StringUtil.format;

public class ProviderFetchException extends RuntimeException {

    /**
     * Constructs a provider fetch exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ProviderFetchException() {
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
    public ProviderFetchException(String message, Object... param) {
        super(format(message, param));
    }

    /**
     * Constructs a provider fetch exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @param param   Optional parameters that will be included in the exception message
     */
    public ProviderFetchException(String message, Throwable cause, Object... param) {
        super(format(message, param), cause);
    }

    /**
     * Constructs a provider fetch exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwable.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public ProviderFetchException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a provider fetch exception with the specified detail
     * message, cause, suppression enabled or disabled, and writable
     * stack trace enabled or disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether the stack trace should
     *                           be writable
     * @param param              Optional params that will be included in the exception message
     */
    protected ProviderFetchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... param) {
        super(format(message, param), cause, enableSuppression, writableStackTrace);
    }
}

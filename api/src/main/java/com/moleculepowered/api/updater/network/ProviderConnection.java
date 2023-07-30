package com.moleculepowered.api.updater.network;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static com.moleculepowered.api.util.StringUtil.format;

/**
 * Represents the connection used by our providers.
 * <p>
 * It provides methods that are commonly used by our update providers. Please note that
 * this class is {@link AutoCloseable}, therefore it is recommended that you create new
 * objects using a try-catch block.
 * </p>
 *
 * @author OMGitzFROST
 * @see AutoCloseable
 */
public class ProviderConnection implements AutoCloseable
{
    private final HttpURLConnection connection;

    /**
     * The main constructor used to create a new {@link ProviderConnection}. Please note
     * that this constructor will automatically configure the provider URL and will automatically
     * open a connection to that URL.
     *
     * @param url   the URL representing the connection
     * @param param optional parameters to include inside the URL
     * @throws IOException if an issue occurs while creating/connecting to the URL
     */
    public ProviderConnection(String url, Object... param) throws IOException {
        URL targetURL = new URL(format(url, Arrays.stream(param).map(String::valueOf).toArray()));
        HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
        conn.addRequestProperty("User-Agent", "MoleculeAPI/ProviderConnection");
        conn.setInstanceFollowRedirects(true);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.connect();
        this.connection = conn;
    }

    /**
     * Returns a buffered reader that reads the content returned after the provider connects.
     *
     * @return the buffered reader containing the connection content
     * @throws IOException when the connection's input stream is null
     */
    public @NotNull BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    /**
     * Gets the status code from an HTTP response message.
     * For example, in the case of the following status lines:
     * <pre>
     * HTTP/1.0 200 OK
     * HTTP/1.0 401 Unauthorized
     * </pre>
     * It will return 200 and 401 respectively.
     * Returns -1 if no code can be discerned
     * from the response (i.e., the response is not valid HTTP).
     *
     * @return the HTTP status code, or -1
     * @throws IOException if an error occurs while connecting to the server
     */
    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    /**
     * Get a {@link URL} representation of the provider connection.
     *
     * @return the URL connection
     */
    public @NotNull URL getURL() {
        return connection.getURL();
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * try-with-resources statement.
     * <p>
     * While this interface method is declared to throw {@code Exception}, implementers are strongly encouraged to
     * declare concrete implementations of the {@code close} method to throw more specific exceptions, or to throw no
     * exception at all if the close operation cannot fail.
     * </p>
     * <p>
     * Cases where the close operation may fail require careful attention by implementers. It is strongly advised to
     * relinquish the underlying resources and to internally mark the resource as closed, prior to throwing the
     * exception. The {@code close} method is unlikely to be invoked more than once, and so this ensures that the
     * resources are released in a timely manner. Furthermore, it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     * </p>
     * <p>
     * Implementers of this interface are also strongly advised to not have the {@code close} method throw
     * {@link InterruptedException}.
     * </p>
     * <p>
     * This exception interacts with a thread's interrupted status, and runtime misbehavior is likely to occur if an
     * {@code InterruptedException} is suppressed.
     * </p>
     * <p>
     * More generally, if it causes problems for an exception to be suppressed, the {@code AutoCloseable.close} method
     * should not throw it.
     * </p>
     * <p>
     * Note that unlike the {@link Closeable#close close} method of {@link Closeable}, this {@code close} method is
     * not required to be idempotent. In other words, calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is required to have no effect if called more than
     * once.
     * </p>
     */
    @Override
    public void close() {
        connection.disconnect();
    }
}

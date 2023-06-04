package com.moleculepowered.api.updater.network;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static com.moleculepowered.api.util.StringUtil.format;

public class ProviderConnection implements AutoCloseable
{
    private final HttpURLConnection connection;

    public ProviderConnection(String url, Object... param) throws IOException {
        URL targetURL = new URL(format(url, Arrays.stream(param).map(String::valueOf).toArray()));
        HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
        conn.addRequestProperty("User-Agent", "MoleculeAPI/Spigot");
        conn.setInstanceFollowRedirects(true);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.connect();
        this.connection = conn;
    }

    public BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    public URL getURL() {
        return connection.getURL();
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * <p>While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to
     * declare concrete implementations of the {@code close} method to
     * throw more specific exceptions, or to throw no exception at all
     * if the close operation cannot fail.
     *
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once, and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore, it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     *
     * <p><em>Implementers of this interface are also strongly advised
     * to not have the {@code close} method throw {@link
     * InterruptedException}.</em>
     * <p>
     * This exception interacts with a thread's interrupted status,
     * and runtime misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed
     * suppressed}.
     * <p>
     * More generally, if it causes problems for an
     * exception to be suppressed, the {@code AutoCloseable.close}
     * method should not throw it.
     *
     * <p>Note that unlike the {@link Closeable#close close}
     * method of {@link Closeable}, this {@code close} method
     * is <em>not</em> required to be idempotent.  In other words,
     * calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is
     * required to have no effect if called more than once.
     * <p>
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     */
    @Override
    public void close() {
        connection.disconnect();
    }
}

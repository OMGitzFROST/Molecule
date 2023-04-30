package com.moleculepowered.api.updater.provider;

import com.moleculepowered.api.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;

import static com.moleculepowered.api.util.StringUtil.format;

public abstract class AbstractProvider {

    /**
     * A shorthand method for creating usable https connections, please note that this method
     * opens the connection for you beforehand, it's your job though to disconnect it once you no
     * longer need the connection
     *
     * @param url   The target url
     * @param param Optional parameters to be included in the url
     * @return A usable connection.
     * @throws IOException when the connection failed to create
     */
    public HttpURLConnection connection(String url, Object... param) throws IOException {
        URL targetURL = new URL(format(url, Arrays.stream(param).map(String::valueOf).toArray()));
        HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
        conn.addRequestProperty("User-Agent", "MoleculeAPI/Spigot");
        conn.setInstanceFollowRedirects(true);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.connect();
        return conn;
    }

    /*
    ABSTRACT METHODS
     */

    /**
     * A method used to fetch the latest information for this provider, it handles
     * the main logic and is recommended to only assign values to the other methods
     * within this provider, such as changelog link, download link, etc.
     */
    public abstract void fetch();

    /**
     * Returns the name assigned to this provider, this name will be used for logging messages
     *
     * @return Provider name
     */
    public abstract @NotNull String getName();

    /**
     * Returns the url pointing to the location the latest update can be downloaded from.
     *
     * @return Download link
     */
    public abstract @Nullable String getDownloadLink();

    /**
     * Returns the url pointing to the changelog for the latest update
     *
     * @return Update changelog
     */
    public abstract @Nullable String getChangelogLink();

    /**
     * Return's the author's donation link
     *
     * @return Donation link
     */
    public abstract @Nullable String getDonationLink();

    /**
     * Returns the latest version number returned by this provider
     *
     * @return Latest version number
     */
    public abstract @NotNull Version getRelease();

    /**
     * Return's the price of the latest update
     *
     * @return Update price
     */
    public abstract @Nullable String getPrice();

    /**
     * Returns a set of contributors for the latest update
     *
     * @return Set of contributors
     */
    public abstract @Nullable Set<String> getContributors();

    /**
     * Used to return the total number of downloads for this release
     *
     * @return Total downloads
     */
    public abstract int getDownloads();

    /**
     * Return's true if the latest release is a premium resource
     *
     * @return true if premium
     */
    public abstract boolean isPremium();
}
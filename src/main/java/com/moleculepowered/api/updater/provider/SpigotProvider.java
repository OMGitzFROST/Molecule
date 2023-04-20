package com.moleculepowered.api.updater.provider;

import com.moleculepowered.api.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Set;

public class SpigotProvider extends AbstractProvider {

    private final int resourceID;
    private String downloadLink, latestVersion, changelogLink;

    public SpigotProvider(int resourceID) {
        this.resourceID = resourceID;
    }

    public SpigotProvider(String resourceID) {
        this(Integer.parseInt(resourceID));
    }

    /**
     * A method used to fetch the latest information for this provider, it handles
     * the main logic and is recommended to only assign values to the other methods
     * within this provider, such as changelog link, download link, etc.
     */
    @Override
    public void fetch() {
        try {
            HttpURLConnection conn = connection("https://api.spigotmc.org/legacy/update.php?resource={0}", resourceID);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            latestVersion = reader.readLine();
            downloadLink = "https://www.spigotmc.org/resources/" + resourceID;
            changelogLink = downloadLink + "/updates";

            // CLOSE CONNECTION
            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the name assigned to this provider, this name will be used for logging messages
     *
     * @return Provider name
     */
    @Override
    public @NotNull String getName() {
        return "Spigot";
    }

    /**
     * Returns the url pointing to the location the latest update can be downloaded from.
     *
     * @return Download link
     */
    @Override
    public @Nullable String getDownloadLink() {
        return downloadLink;
    }

    /**
     * Returns the url pointing to the changelog for the latest update
     *
     * @return Update changelog
     */
    @Override
    public @Nullable String getChangelogLink() {
        return changelogLink;
    }

    /**
     * Return's the author's donation link
     *
     * @return Donation link
     */
    @Override
    public String getDonationLink() {
        return null;
    }

    /**
     * Returns the latest version number returned by this provider
     *
     * @return Latest version number
     */
    @Override
    public @NotNull Version getLatestVersion() {
        return new Version(latestVersion);
    }

    /**
     * Return's the price of the latest update
     *
     * @return Update price
     */
    @Override
    public @Nullable String getPrice() {
        return null;
    }

    /**
     * Returns a set of contributors for the latest update
     *
     * @return Set of contributors
     */
    @Override
    public @Nullable Set<String> getContributors() {
        return null;
    }

    /**
     * Used to return the total number of downloads for this release
     *
     * @return Total downloads
     */
    @Override
    public int getDownloads() {
        return 0;
    }

    /**
     * Return's true if the latest release is a premium resource
     *
     * @return true if premium
     */
    @Override
    public boolean isPremium() {
        return false;
    }
}

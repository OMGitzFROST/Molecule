package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.ProviderFetchException;
import com.moleculepowered.api.util.StringUtil;
import com.moleculepowered.api.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Set;

import static com.moleculepowered.api.util.StringUtil.format;

public class BukkitProvider extends AbstractProvider {

    private final String TITLE_VALUE = "name";
    private final String DOWNLOAD_URL = "downloadUrl";
    private final String FILE_URL = "fileUrl";
    private final String API_KEY;
    private final int resourceID;
    private String downloadLink, changeLogLink, latestVersion;

    public BukkitProvider(int resourceID, @Nullable String apiKey) {
        this.resourceID = resourceID;
        this.API_KEY = apiKey;
    }

    public BukkitProvider(int resourceID) {
        this(resourceID, null);
    }

    /**
     * A method used to fetch the latest information for this provider, it handles
     * the main logic and is recommended to only assign values to the other methods
     * within this provider, such as changelog link, download link, etc.
     */
    @Override
    public void fetch() {
        try {
            HttpURLConnection conn = connection("https://api.curseforge.com/servermods/files?projectIds={0}", resourceID);

            // ADD API KEY IF PRESENT
            if (API_KEY != null && !API_KEY.isEmpty()) conn.addRequestProperty("X-API-Key", API_KEY);
            conn.connect();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonArray array = new Gson().fromJson(reader, JsonArray.class);
            JsonObject resource = array.get(array.size() - 1).getAsJsonObject();

            // GET VERSION INFORMATION
            latestVersion = resource.get(TITLE_VALUE).getAsString();
            downloadLink = resource.get(DOWNLOAD_URL).getAsString();

            HttpURLConnection changeLogConn = connection("https://dev.bukkit.org/projects/{0}", resourceID);
            String fileName = StringUtil.lastIndex(changeLogConn.getURL().toString(), "/", +1);
            String fileID = StringUtil.lastIndex(resource.get(FILE_URL).getAsString(), "/", +1);

            // GET UPDATE INFORMATION
            changeLogLink = format("https://www.curseforge.com/minecraft/bukkit-plugins/{0}/files/{1}", fileName, fileID);

            // CLOSE CONNECTION
            changeLogConn.disconnect();
            conn.disconnect();
        } catch (IOException ex) {
            throw new ProviderFetchException("The provider failed to fetch the latest update", ex);
        }
    }

    /**
     * Returns the name assigned to this provider, this name will be used for logging messages
     *
     * @return Provider name
     */
    @Override
    public @NotNull String getName() {
        return "Bukkit";
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
        return changeLogLink;
    }

    /**
     * Return's the author's donation link
     *
     * @return Donation link
     */
    @Override
    public String getDonationLink() {
        throw new IllegalArgumentException("This operation is not supported by this provider");
    }

    /**
     * Returns the latest version number returned by this provider
     *
     * @return Latest version number
     */
    @Override
    public @NotNull Version getRelease() {
        return new Version(latestVersion);
    }

    /**
     * Return's the price of the latest update
     *
     * @return Update price
     */
    @Override
    public @Nullable String getPrice() {
        throw new IllegalArgumentException("This operation is not supported by this provider");
    }

    /**
     * Returns a set of contributors for the latest update
     *
     * @return Set of contributors
     */
    @Override
    public @Nullable Set<String> getContributors() {
        throw new IllegalArgumentException("This operation is not supported by this provider");
    }

    /**
     * Used to return the total number of downloads for this release
     *
     * @return Total downloads
     */
    @Override
    public int getDownloads() {
        throw new IllegalArgumentException("This operation is not supported by this provider");
    }

    /**
     * Return's true if the latest release is a premium resource
     *
     * @return true if premium
     */
    @Override
    public boolean isPremium() {
        throw new IllegalArgumentException("This operation is not supported by this provider");
    }
}

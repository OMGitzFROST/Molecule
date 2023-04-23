package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.ProviderFetchException;
import com.moleculepowered.api.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

public class PolymartProvider extends AbstractProvider {

    private final Set<String> contributors = new HashSet<>();
    private String downloadLink, latestVersion, changelogLink;
    private final int resourceID;
    private String price;
    private int totalDownloads;

    public PolymartProvider(int resourceID) {
        this.resourceID = resourceID;
    }

    public PolymartProvider(String resourceID) {
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
            HttpURLConnection conn = connection("https://api.polymart.org/v1/getResourceInfo/resource_id={0}", resourceID);

            // CREATE A READER FOR JSON PARSING
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // CREATE JSON OBJECTS FOR REQUESTED VALUES
            JsonObject response = new Gson().fromJson(reader, JsonObject.class).getAsJsonObject("response");
            JsonObject resource = response.getAsJsonObject("resource");
            JsonObject owner = resource.getAsJsonObject("owner");
            JsonObject latest = resource.getAsJsonObject("updates").getAsJsonObject("latest");

            // SET INFORMATION VALUES
            latestVersion = latest.get("version").getAsString();
            price = resource.get("price").getAsString() + " " + resource.get("currency").getAsString();
            totalDownloads = resource.get("downloads").getAsInt();

            // SETTING LINK VALUES
            String rawDownloadLink = resource.get("url").getAsString();
            int hangingIndex = rawDownloadLink.indexOf("?");
            downloadLink = rawDownloadLink.substring(0, hangingIndex);
            changelogLink = downloadLink + "/updates";

            // ADD CONTRIBUTORS
            contributors.add(owner.get("name").getAsString());

            // DISCONNECT
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
        return "Polymart";
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
     * Returns the latest version number returned by this provider
     *
     * @return Latest version number
     */
    @Override
    public @NotNull Version getRelease() {
        return new Version(latestVersion);
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
     * Return's the price of the latest update
     *
     * @return Update price
     */
    @Override
    public @Nullable String getPrice() {
        return price;
    }

    /**
     * Returns a set of contributors for the latest update
     *
     * @return Set of contributors
     */
    @Override
    public @Nullable Set<String> getContributors() {
        return contributors;
    }

    /**
     * Used to return the total number of downloads for this release
     *
     * @return Total downloads
     */
    @Override
    public int getDownloads() {
        return totalDownloads;
    }

    /**
     * Return's true if the latest release is a premium resource
     *
     * @return true if premium
     */
    @Override
    public boolean isPremium() {
        return price != null && price.contains("0.00");
    }
}

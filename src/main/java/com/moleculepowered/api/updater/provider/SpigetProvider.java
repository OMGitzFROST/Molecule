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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.moleculepowered.api.util.StringUtil.format;

public class SpigetProvider extends AbstractProvider {

    private final Set<String> contributors = new HashSet<>();
    private final int resourceID;
    private String downloadLink, latestVersion, changelogLink, donationLink;
    private String price;
    private int totalDownloads;
    private boolean premium;

    public SpigetProvider(int resourceID) {
        this.resourceID = resourceID;
    }

    public SpigetProvider(String resourceID) {
        this.resourceID = Integer.parseInt(resourceID);
    }

    /**
     * A method used to fetch the latest information for this provider, it handles
     * the main logic and is recommended to only assign values to the other methods
     * within this provider, such as changelog link, download link, etc.
     */
    @Override
    public void fetch() {
        try {
            HttpURLConnection conn1 = connection("https://api.spiget.org/v2/resources/{0}", resourceID);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

            // CREATE JSON OBJECTS FOR REQUESTED VALUES
            JsonObject response = new Gson().fromJson(reader, JsonObject.class);
            JsonObject file = response.get("file").getAsJsonObject();
            JsonArray updates = response.getAsJsonArray("updates");

            // SET VALUES
            downloadLink = format("https://www.spigotmc.org/{0}", file.get("url").getAsString());
            changelogLink = format("https://www.spigotmc.org/resources/{0}/update?update={1}", resourceID, updates.get(0).getAsJsonObject().get("id").getAsString());
            price = format("{0} {1}", response.get("price").getAsString(), StringUtil.nonNull(response.get("currency")));
            totalDownloads = response.get("downloads").getAsInt();
            premium = response.get("premium").getAsBoolean();

            if (response.get("donationLink") != null) donationLink = response.get("donationLink").getAsString();
            if (response.get("contributors") != null)
                contributors.addAll(Arrays.asList(response.get("contributors").getAsString().split(",")));

            conn1.disconnect();

            // GET LATEST VERSION NUMBER
            HttpURLConnection conn2 = connection("https://api.spiget.org/v2/resources/{0}/versions/latest", resourceID);
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
            JsonObject response2 = new Gson().fromJson(reader2, JsonObject.class);

            // SET LATEST VERSION
            latestVersion = response2.get("name").getAsString();
            conn2.disconnect();
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
        return "Spiget";
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
        return donationLink;
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
        return premium;
    }
}

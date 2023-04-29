package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.ProviderFetchException;
import com.moleculepowered.api.exception.RateLimitReachedException;
import com.moleculepowered.api.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

import static com.moleculepowered.api.util.StringUtil.format;

public class GithubProvider extends AbstractProvider {

    private final String REPO;
    private final Set<String> contributors = new HashSet<>();
    private String downloadLink, latestVersion, changelogLink;
    private int totalDownloads;

    public GithubProvider(@NotNull String repo) {
        this.REPO = repo;
    }

    /**
     * A method used to fetch the latest information for this provider, it handles
     * the main logic and is recommended to only assign values to the other methods
     * within this provider, such as changelog link, download link, etc.
     */
    @Override
    public void fetch() {
        try {
            HttpURLConnection contributorConn = connection("https://api.github.com/repos/{0}/contributors", REPO);
            if (contributorConn.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN)
                throw new RateLimitReachedException();

            if (contributorConn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
                final BufferedReader contributorReader = new BufferedReader(new InputStreamReader(contributorConn.getInputStream()));
                // SET CONTRIBUTORS
                JsonArray contributors = new Gson().fromJson(contributorReader, JsonArray.class);
                for (JsonElement contributor : contributors) {
                    this.contributors.add(contributor.getAsJsonObject().get("login").getAsString());
                }
            }

            // DISCONNECT FROM CONTRIBUTOR CONNECTION
            contributorConn.disconnect();

            HttpURLConnection releaseConn = connection("https://api.github.com/repos/{0}/releases/latest", REPO);
            if (releaseConn.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN)
                throw new RateLimitReachedException();

            BufferedReader releaseReader = new BufferedReader(new InputStreamReader(releaseConn.getInputStream()));
            JsonObject resource = new Gson().fromJson(releaseReader, JsonObject.class);
            JsonArray assets = resource.get("assets").getAsJsonArray();

            // SET REMOTE VERSION AND CHANGELOG LINK
            latestVersion = resource.get("tag_name").getAsString();
            changelogLink = resource.get("html_url").getAsString();

            // ACCESS THE REPOSITORIES ASSETS AND SET THE DOWNLOAD URL IF ONE EXISTS

            if (assets.size() > 0) {
                JsonObject latestResource = assets.get(0).getAsJsonObject();
                downloadLink = latestResource.get("browser_download_url").getAsString();
                totalDownloads = latestResource.get("download_count").getAsInt();
            }

            // DISCONNECT FROM RELEASE
            releaseConn.disconnect();
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(format("Invalid repository path: {0}", REPO), ex);
        } catch (RateLimitReachedException ignored) {
            // IGNORED SO OTHER PROVIDERS CAN TAKE OVER
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
        return "Github";
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
    public @Nullable String getDonationLink() {
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
        throw new IllegalArgumentException("This operation is not supported by this provider");
    }
}

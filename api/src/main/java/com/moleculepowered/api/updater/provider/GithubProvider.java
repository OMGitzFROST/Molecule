package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.updater.ProviderUnreachableException;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.network.ProviderConnection;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.moleculepowered.api.util.StringUtil.format;

/**
 * This implementation of the {@link AbstractProvider} class was created to strictly
 * handle update checking from a GitHub repository. Please note that in order for updates
 * to be viewable by this provider, you must create a Release within its repository.
 * <p>
 * Note that this class itself is not meant to be used on its own or to create an object,
 * but instead intended to be used within the {@link Updater#addProvider(AbstractProvider)} method.
 * </p>
 *
 * @see AbstractProvider
 * @see Updater#addProvider(AbstractProvider)
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public class GithubProvider extends AbstractProvider {

    private final String REPO;
    private final String HOST;

    /**
     * The main constructor for this provider, it initializes the repository where the updates
     * will be located. Please note the parameter you provide cannot be null.
     *
     * @param repo Target repository
     */
    public GithubProvider(@NotNull String repo) {
        this.REPO = repo;
        this.HOST = format("https://api.github.com/repos/{0}", REPO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetch() {
        try (ProviderConnection release = new ProviderConnection(HOST + "/releases/latest");
             ProviderConnection contributors = new ProviderConnection(HOST + "/contributors")) {

            // IF RATE LIMIT IS REACHED, DO NOT ATTEMPT TO FETCH
            if (release.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) return;

            // READ INCOMING INFORMATION
            JsonObject resource = new Gson().fromJson(release.getBufferedReader(), JsonObject.class);
            JsonArray assets = resource.get("assets").getAsJsonArray();

            // SET REMOTE VERSION AND CHANGELOG LINK
            setLatestVersion(resource.get("tag_name").getAsString());
            setChangelogLink(resource.get("html_url").getAsString());

            // ACCESS THE REPOSITORIES ASSETS AND SET THE DOWNLOAD URL IF ONE EXISTS
            if (assets.size() > 0) {
                JsonObject latestResource = assets.get(0).getAsJsonObject();
                setDownloadLink(latestResource.get("browser_download_url").getAsString());
            }

            // SET CONTRIBUTORS LIST
            JsonArray contributorArray = new Gson().fromJson(contributors.getBufferedReader(), JsonArray.class);
            if (!contributorArray.isJsonNull()) {
                contributorArray.iterator().forEachRemaining(c -> addContributor(c.getAsJsonObject().get("login").getAsString()));
            }
        } catch (SocketException | UnknownHostException ex) {
            throw new ProviderUnreachableException("An internet connection could not be established, please try again later.");
        } catch (FileNotFoundException ex) {
            throw new ProviderUnreachableException("An error occurred contacting the ({0}) repo.", REPO);
        } catch (IOException ex) {
            throw new ProviderUnreachableException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName() {
        return "Github";
    }
}
package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.updater.ProviderUnreachableException;
import com.moleculepowered.api.Platform;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.network.ProviderConnection;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This implementation of the {@link AbstractProvider} class was created to strictly
 * handle update checking from the Hanger (PaperMC) marketplace.
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
public class HangarProvider extends AbstractProvider
{
    private final String SLUG;
    private final String HOST;
    private final Platform.Type platform;

    /**
     * The main constructor used to initialize this provider. The only platforms
     * accepted currently are "Paper", "Velocity", and "Waterfall".
     * <p>
     * To show an example, let's use EssentialsX. For Essentials, their slug is
     * "EssentialsX/Essentials", which can be found by looking at their project URL
     * (e.g., <a href="https://hangar.papermc.io/EssentialsX/Essentials">...</a>).
     * </p>
     *
     * @param slug     the slug pointing to the project
     * @param platform Platform type (e.g., {@link Platform.Type#PAPER})
     */
    public HangarProvider(@NotNull String slug, @NotNull Platform.Type platform) {
        this.SLUG = slug;
        this.HOST = "https://hangar.papermc.io/api/v1/projects/{0}/versions?limit=1&offset=0&platform={1}";
        this.platform = platform;
    }

    /**
     * The main constructor used to initialize this provider. By default, the platform
     * used to retrieve update information will be {@link Platform.Type#PAPER}.
     * <p>
     * To show an example, let's use EssentialsX. For Essentials, their slug is
     * "EssentialsX/Essentials", which can be found by looking at their project URL
     * (e.g., <a href="https://hangar.papermc.io/EssentialsX/Essentials">...</a>).
     * </p>
     *
     * @param slug the slug pointing to the project
     */
    public HangarProvider(@NotNull String slug) {
        this(slug, Platform.Type.PAPER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetch() {
        try (ProviderConnection conn = new ProviderConnection(HOST, SLUG, platform.name())) {

            JsonObject release = new Gson().fromJson(conn.getBufferedReader(), JsonObject.class);
            JsonArray resultArray = release.get("result").getAsJsonArray();

            if (resultArray.size() != 0) {
                JsonObject result = resultArray.get(0).getAsJsonObject();

                // SET VERSION NUMBER
                setLatestVersion(result.get("name").getAsString());

                // DOWNLOAD LINK AND CHANGELOG LINK
                JsonObject downloads = result.getAsJsonObject("downloads");
                JsonObject platform = downloads.getAsJsonObject(this.platform.name());
                setChangelogLink(!platform.get("externalUrl").isJsonNull() ? platform.get("externalUrl").getAsString() : null);
                setDownloadLink(!platform.get("downloadUrl").isJsonNull() ? platform.get("downloadUrl").getAsString() : null);

                // ADD AUTHOR
                addContributor(result.get("author").getAsString());
            }
        } catch (SocketException | UnknownHostException ex) {
            throw new ProviderUnreachableException("An internet connection could not be established, please try again later.");
        } catch (FileNotFoundException ex) {
            throw new ProviderUnreachableException("An error occurred contacting the project page for ({0}). Does it exist?", SLUG);
        } catch (IOException ex) {
            throw new ProviderUnreachableException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName() {
        return "Hangar";
    }
}
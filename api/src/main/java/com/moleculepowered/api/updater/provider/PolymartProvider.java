package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.updater.ProviderUnreachableException;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.network.ProviderConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This implementation of the {@link AbstractProvider} class was created to strictly
 * handle update checking from the PolyMart marketplace.
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
public class PolymartProvider extends AbstractProvider
{
    private final String resourceID;
    private final String HOST;
    private String price;

    /**
     * The main constructor for this provider. It initializes the resource ID that will be used
     * when accessing update information.
     *
     * @param resourceID The ID assigned to your project
     * @throws IllegalArgumentException when an invalid ID type is entered
     */
    public PolymartProvider(@NotNull Object resourceID) {
        if (resourceID instanceof String) this.resourceID = (String) resourceID;
        else if (resourceID instanceof Number) this.resourceID = resourceID.toString();
        else throw new IllegalArgumentException("The ID you provide must represent an integer");

        this.HOST = "https://api.polymart.org/v1/getResourceInfo/resource_id={0}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetch() {
        try (ProviderConnection conn = new ProviderConnection(HOST, resourceID)) {

            // CREATE JSON OBJECTS FOR REQUESTED VALUES
            JsonObject response = new Gson().fromJson(conn.getBufferedReader(), JsonObject.class).getAsJsonObject("response");
            JsonObject resource = response.getAsJsonObject("resource");

            // SET RESOURCE OBJECTS
            JsonObject owner = resource.getAsJsonObject("owner");
            JsonObject latest = resource.getAsJsonObject("updates").getAsJsonObject("latest");

            // SET INFORMATION VALUES
            setLatestVersion(latest.get("version").getAsString());
            price = resource.get("price").getAsString() + " " + resource.get("currency").getAsString();

            // SETTING LINK VALUES
            String rawDownloadLink = resource.get("url").getAsString();
            int questionMarkIndex = rawDownloadLink.indexOf("?");
            setDownloadLink(rawDownloadLink.substring(0, questionMarkIndex));
            setChangelogLink(getDownloadLink() + "/updates");

            // ADD CONTRIBUTORS
            addContributor(owner.get("name").getAsString());
        } catch (SocketException | UnknownHostException ex) {
            throw new ProviderUnreachableException("An internet connection could not be established, please try again later.");
        } catch (NullPointerException ex) {
            throw new ProviderUnreachableException("An error occurred contacting the project page, perhaps the project ID ({0}) is invalid.", resourceID);
        } catch (IOException ex) {
            throw new ProviderUnreachableException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName() {
        return "Polymart";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable String getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPremium() {
        return price != null && !price.contains("0.00");
    }
}

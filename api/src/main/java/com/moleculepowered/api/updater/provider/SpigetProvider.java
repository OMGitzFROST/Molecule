package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moleculepowered.api.updater.exception.ProviderUnreachableException;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.network.ProviderConnection;
import com.moleculepowered.api.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import static com.moleculepowered.api.util.StringUtil.format;

/**
 * <p>This implementation of the {@link AbstractProvider} class was created to strictly
 * handle update checking from the Spiget marketplace.</p>
 *
 * <p>Note that this class itself is not meant to be used on its own or to create an object,
 * but instead intended to be used within the {@link Updater#addProvider(AbstractProvider)} method</p>
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public class SpigetProvider extends AbstractProvider
{
    private final String resourceID;
    private String donationLink;
    private String price;
    private boolean premium;

    /*
    CONSTRUCTOR
     */

    /**
     * The main constructor for this provider, it initializes the resource id that will be used
     * when accessing update information.
     *
     * @param resourceID The ID assigned to your project
     * @throws IllegalArgumentException when an invalid ID type is entered
     */
    public SpigetProvider(@NotNull Object resourceID) {
        if (resourceID instanceof String) this.resourceID = (String) resourceID;
        else if (resourceID instanceof Number) this.resourceID = resourceID.toString();
        else throw new IllegalArgumentException("The ID you provide must represent an integer");
    }

    /*
    CORE FUNCTIONALITY
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetch() {
        try (ProviderConnection conn1 = new ProviderConnection("https://api.spiget.org/v2/resources/{0}", resourceID);
             ProviderConnection conn2 = new ProviderConnection("https://api.spiget.org/v2/resources/{0}/versions/latest", resourceID)){

            // CREATE JSON OBJECTS FOR REQUESTED VALUES
            JsonObject response = new Gson().fromJson(conn1.getBufferedReader(), JsonObject.class);
            JsonObject response2 = new Gson().fromJson(conn2.getBufferedReader(), JsonObject.class);
            JsonObject file = response.get("file").getAsJsonObject();
            JsonArray updates = response.getAsJsonArray("updates");

            // SET RELEASE INFORMATION
            setDownloadLink("https://www.spigotmc.org/{0}", file.get("url").getAsString());
            setLatestVersion(response2.get("name").getAsString());

            // SET CHANGELOG
            if (updates.size() > 0)
                setChangelogLink("https://www.spigotmc.org/resources/{0}/update?update={1}", resourceID, updates.get(0).getAsJsonObject().get("id").getAsString());

            // SET PREMIUM VALUES (IF AVAILABLE)
            price = format("{0} {1}", response.get("price").getAsString(), StringUtil.nonNull(response.get("currency"))).trim();
            premium = response.get("premium").getAsBoolean();

            // SET DONATION LINK
            if (response.get("donationLink") != null)
                setDonationLink(response.get("donationLink").getAsString());

            // ADD CONTRIBUTORS
            if (response.get("contributors") != null)
                addContributor(Arrays.asList(response.get("contributors").getAsString().split(",")));
        }
        catch (SocketException | UnknownHostException ex) {
            throw new ProviderUnreachableException("An internet connection could not be established, please try again later.");
        }
        catch (FileNotFoundException ex) {
            throw new ProviderUnreachableException("An error occurred contacting the project page, perhaps the project id ({0}) is invalid.", resourceID);
        }
        catch (IOException ex) {
            throw new ProviderUnreachableException(ex);
        }
    }

    /*
    GETTER METHODS
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getProviderName() {
        return "Spiget";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDonationLink() {
        return donationLink;
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
        return premium;
    }
}

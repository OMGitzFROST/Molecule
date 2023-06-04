package com.moleculepowered.api.updater.provider;

import com.moleculepowered.api.updater.exception.ProviderUnreachableException;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.network.ProviderConnection;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * <p>This implementation of the {@link AbstractProvider} class was created to strictly
 * handle update checking from the SpigotMC marketplace.</p>
 *
 * <p>Note that this class itself is not meant to be used on its own or to create an object,
 * but instead intended to be used within the {@link Updater#addProvider(AbstractProvider)} method</p>
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public class SpigotProvider extends AbstractProvider
{

    private final String resourceID;

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
    public SpigotProvider(@NotNull Object resourceID) {
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
        try (ProviderConnection conn = new ProviderConnection("https://api.spigotmc.org/legacy/update.php?resource={0}", resourceID)) {

            // SET UPDATE INFORMATION
            setLatestVersion(conn.getBufferedReader().readLine());
            setDownloadLink("https://www.spigotmc.org/resources/" + resourceID);
            setChangelogLink(getDownloadLink() + "/updates");
        }
        catch (SocketException | UnknownHostException ex) {
            throw new ProviderUnreachableException("An internet connection could not be established, please try again later.");
        }
        catch (FileNotFoundException ex) {
            throw new ProviderUnreachableException("An error occurred contacting the project page, perhaps the project id ({0}) is invalid.", String.valueOf(resourceID));
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
        return "Spigot";
    }
}

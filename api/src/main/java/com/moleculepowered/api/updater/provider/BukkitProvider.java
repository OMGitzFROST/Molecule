package com.moleculepowered.api.updater.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.exception.updater.ProviderUnreachableException;
import com.moleculepowered.api.updater.network.ProviderConnection;
import com.moleculepowered.api.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This implementation of the {@link AbstractProvider} class was created to strictly
 * handle update checking from the BukkitDev (CurseForge) marketplace.
 * <p>
 * Note that this class itself is not meant to be used on its own or to create an object,
 * but instead intended to be used within the {@link Updater#addProvider(AbstractProvider)} method.
 * </p>
 *
 * @author OMGitzFROST
 * @see AbstractProvider
 * @see Updater#addProvider(AbstractProvider)
 */
@SuppressWarnings("unused")
public class BukkitProvider extends AbstractProvider {

    private final String resourceID;
    private final String CURSE_FORGE_HOST;
    private final String DEV_BUKKIT_HOST;

    /**
     * The main constructor for this provider, it initializes the resource id that will be
     * used when accessing update information.
     *
     * @param resourceID The ID assigned to your project
     * @throws IllegalArgumentException when an invalid ID type is entered
     */
    public BukkitProvider(@NotNull Object resourceID) {
        if (resourceID instanceof String) this.resourceID = (String) resourceID;
        else if (resourceID instanceof Number) this.resourceID = resourceID.toString();
        else throw new IllegalArgumentException("The ID you provide must represent an integer");

        this.CURSE_FORGE_HOST = "https://api.curseforge.com/servermods/files?projectIds={0}";
        this.DEV_BUKKIT_HOST = "https://dev.bukkit.org/projects/{0}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetch() {
        try (ProviderConnection release = new ProviderConnection(CURSE_FORGE_HOST, resourceID);
             ProviderConnection changelog = new ProviderConnection(DEV_BUKKIT_HOST, resourceID)) {

            JsonArray array = new Gson().fromJson(release.getBufferedReader(), JsonArray.class);
            JsonObject resource = array.get(array.size() - 1).getAsJsonObject();

            // GET VERSION INFORMATION
            setLatestVersion(resource.get("name").getAsString());
            setDownloadLink(resource.get("downloadUrl").getAsString());

            String fileName = StringUtil.lastIndex(changelog.getURL().toString(), "/", +1);
            String fileID = StringUtil.lastIndex(resource.get("fileUrl").getAsString(), "/", +1);

            // GET UPDATE INFORMATION
            setChangelogLink("https://www.curseforge.com/minecraft/bukkit-plugins/{0}/files/{1}", fileName, fileID);
        } catch (SocketException | UnknownHostException ex) {
            throw new ProviderUnreachableException("An internet connection could not be established, please try again later.");
        } catch (IndexOutOfBoundsException ex) {
            throw new ProviderUnreachableException("An error occurred contacting the project page, perhaps the project id ({0}) is invalid.", resourceID);
        } catch (IOException ex) {
            throw new ProviderUnreachableException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName() {
        return "Bukkit";
    }
}

package com.moleculepowered.api.updater.provider;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.util.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.moleculepowered.api.util.StringUtil.format;

/**
 * <p>The {@link AbstractProvider} class can be defined as the core component for our {@link Updater}
 * class. These providers are tasked with fetching update information from their assigned
 * plugin marketplace (e.g SpigotMC, Github, Hangar, etc) and converting them into retrievable
 * information returned by the methods within this class.</p>
 *
 * <p>Please note that this method does not delegate getter methods to its children classes and
 * must override them manually is necessary. Though setting values such as download links
 * are still required using their assigned setter methods, in this case {@link #setDownloadLink(String, Object...)}</p>
 *
 * @author OMGitzFROST
 * @see com.moleculepowered.api.updater.provider View a list of our default providers
 */
@SuppressWarnings("unused")
public abstract class AbstractProvider
{
    private Set<String> contributors = new HashSet<>();
    private String downloadLink, changelogLink, donationLink;
    private ComparableVersion latestVersion;

    /*
    SETTINGS REQUIRED BY ALL PROVIDERS
     */

    /**
     * <p>As this method suggests, its primary purpose is to fetch update information from
     * the remote server. Setting update information such as external links (download, changelog, etc)
     * or even information about the plugin such as if its a premium plugin etc.</p>
     *
     * <p>Please note that the {@link AbstractProvider} class provides setter methods
     * that are inherited to this provider, it is recommended that you use those in-order to set values.
     * Though you could always bypass these tools and set them using your own methods.</p>
     */
    public abstract void fetch();

    /*
    GLOBAL SETTINGS FOR ALL PROVIDERS
     */

    /**
     * <p>This method returns the unique name identifier assigned to this provider. While this identifier
     * does not have many uses within the {@link Updater} its primarily used when logging/debugging
     * information to make identifying bugs easier to resolve.</p>
     *
     * @return The provider's name
     */
    public @NotNull String getName() {
        return getClass().getSimpleName().replace("Provider", "");
    }

    /**
     * <p>Return's the url representing the location where downloads can be made. Please note that if
     * automatic downloads are enabled and this url is a direct download link, it will be used to
     * attempt to download the latest version, though dependent on the {@link AbstractProvider}
     * its not always successful.</p>
     *
     * <p>Please note that this method can return a null value, this was made to prevent
     * the updater from displaying null links when sending update notifications, if this
     * value is set to anything but null (aka an empty string) it will bypass the null check.</p>
     *
     * @return The url where downloads can be made
     */
    public @Nullable String getDownloadLink() {
        return downloadLink;
    }

    /**
     * <p>Return's the url representing the location where version history can be viewed.
     *
     * <p>Please note that this method can return a null value, this was made to prevent
     * the updater from displaying null links when sending update notifications, if this
     * value is set to anything but null (aka an empty string) it will bypass the null check.</p>
     *
     * @return The url where the changelog can be viewed
     */
    public @Nullable String getChangelogLink() {
        return changelogLink;
    }

    /**
     * <p>Return's the url representing the location where users can send donations.
     *
     * <p>Please note that this method can return a null value, this was made to prevent
     * the updater from displaying null links when sending update notifications, if this
     * value is set to anything but null (aka an empty string) it will bypass the null check.</p>
     *
     * @return The url where the changelog can be viewed
     */
    public @Nullable String getDonationLink() {
        return donationLink;
    }

    /**
     * Returns a {@link ComparableVersion} representing the remote version identified
     * by this provider. The updater uses this version object to compare itself between
     * the version currently installed and identify if an update is necessary.
     *
     * @return A {@link ComparableVersion} representing the latest release
     */
    public @NotNull ComparableVersion getVersion() {
        return latestVersion;
    }

    /**
     * Returns a set of contributor names identified by this provider. Please note that some providers
     * do not support this feature and therefore could return an empty set.
     *
     * @return A set of contributors for the remote project
     */
    public @Unmodifiable @NotNull Set<String> getContributors() {
        return contributors;
    }

    /*
    SETTINGS FOR PREMIUM SUPPORTED PLUGINS
     */

    /**
     * Returns the price for a premium resource, typically this method should follow
     * a format such as "0.00 USD" to make it easy to read what the price is. Please note that
     * some providers do not support this feature, therefore, this method could return a null value.
     *
     * @return Returns the premium price
     */
    public @Nullable String getPrice() {
        return null;
    }

    /**
     * Returns true if the latest release is considered a premium plugin.
     *
     * @return true if the release is premium
     */
    public boolean isPremium() {
        return false;
    }

    /*
    SETTER METHODS
     */

    /**
     * A utility method used to add a single contributor name to the set. Note that if the name
     * you provide is already present in the final set, this method will do nothing
     *
     * @param name The contributor's name
     * @see #addContributor(String)
     * @see #setContributors(Collection)
     */
    protected final void addContributor(@NotNull String name) {
        contributors.add(name);
    }

    /**
     * <p>A utility method used to add a collection of names into the contributors set, please
     * note that this method will simply add them to the end of the list and will not override
     * the existing list</p>
     *
     * <p>All additional values added using this method will undergo a filtering process to ensure
     * no duplicate names appear in the final set.</p>
     *
     * @param names A collection of contributor names
     * @see #addContributor(String)
     * @see #setContributors(Collection)
     */
    protected final void addContributor(@NotNull Collection<String> names) {
        contributors.addAll(names);
    }

    /**
     * <p>A utility method used to override the existing contributors set with a new collection, please be
     * aware that this does OVERRIDE all existing contributors so be careful when using it, to simply
     * add a contributor to the existing list please view the see also methods listed below.</p>
     *
     * <p>Its important to note that if there are any duplicate names within the provided
     * collection, this method will automatically filter them out ensuring only unique names make
     * it to the final set.</p>
     *
     * @param contributors A collection of contributor names
     * @see #addContributor(String)
     * @see #addContributor(Collection)
     */
    protected final void setContributors(@NotNull Collection<String> contributors) {
        this.contributors = new HashSet<>(contributors);
    }

    /**
     * <p>A utility method used to set the url that users can visit to download the latest version.</p>
     *
     * <p>This url does not have to be a direct download link but can simply link to a page where they
     * can download the file manually, please note that if the url is a direct download link and automatic
     * downloads are enabled, the update will attempt to download the file, though it's not always
     * successful in doing so.</p>
     *
     * <p>Additionally, this method accepts a null value; when this is true the updater has a built-in
     * check to ensure that null links do not display in the update notification</p>
     *
     * @param link The url where the download file can be located
     */
    protected final void setDownloadLink(@Nullable String link, Object... param) {
        this.downloadLink = format(link, param);
    }

    /**
     * <p>A utility method used to set the url that people can use to view the plugin's changelog history.</p>
     *
     * <p>Please note that this method accepts a null value; when this is true the updater has a built-in
     * check to ensure that null links do not display in the update notification</p>
     *
     * @param link The url where the changelog can be found
     */
    protected final void setChangelogLink(@Nullable String link, Object... param) {
        this.changelogLink = format(link, param);
    }

    /**
     * <p>A utility method used to set the url where users can send donations.</p>
     *
     * <p>Please note that this method accepts a null value; when this is true the updater has a built-in
     * check to ensure that null links do not display in the update notification</p>
     *
     * @param link The url where users can send donations
     */
    protected final void setDonationLink(@Nullable String link, Object... param) {
        this.donationLink = format(link, param);
    }

    /**
     * A utility method used to update the most reset version returned by this provider, it
     * accepts a string but will convert it into a {@link ComparableVersion} object that
     * allows you to compare and retrieve different parts of the version, such as major,
     * minor, patch numbers etc.
     *
     * @param version The latest release version
     */
    protected final void setLatestVersion(@Nullable String version) {
        this.latestVersion = new ComparableVersion(version);
    }
}
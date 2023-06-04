package com.moleculepowered.api.updater;

import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.api.util.FileUtil;
import com.moleculepowered.api.util.Time;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class used to handle the default behaviors for our platform updater. At its core, this
 * updater utilizes what we call a "provider framework" which means that it utilizes our {@link AbstractProvider}
 * classes to handle update checks externally, each provider handles a different platform such as CurseForge,
 * Spigot, etc. to check for a new release.
 *
 * @author OMGitzFROST
 * @see com.moleculepowered.api.updater.provider View a list of our default providers
 */
public abstract class Updater
{
    protected final List<AbstractProvider> providers = new ArrayList<>();
    protected final File DATA_FOLDER;
    protected final ComparableVersion CURRENT_VERSION;
    protected static ComparableVersion LATEST_VERSION;
    protected UpdateResult result;
    protected long interval;
    private boolean enabledToggle, unstableToggle, attemptDownload;
    private String permission;

    /*
    CONSTRUCTORS
     */

    /**
     * Creates a constructor accepting the parent data folder for the updater as well
     * as the current plugin version. Please note that this version should be the one
     * currently installed on your server before updating and should not point to
     * a version from a remote source.
     *
     * @param dataFolder     The parent folder for the updater
     * @param currentVersion The current version of your installed plugin
     */
    public Updater(@NotNull File dataFolder, @Nullable String currentVersion) {
        this(dataFolder, new ComparableVersion(currentVersion));
    }

    /**
     * Creates a constructor accepting the parent data folder for the updater as well
     * as the current plugin version. Please note that this version should be the one
     * currently installed on your server before updating and should not point to
     * a version from a remote source.
     *
     * @param dataFolder     The parent folder for the updater
     * @param currentVersion The current version of your installed plugin
     */
    public Updater(@NotNull File dataFolder, @Nullable ComparableVersion currentVersion) {
        this.DATA_FOLDER    = new File(dataFolder, "Updater");
        this.interval       = Time.parseInterval("3h");
        this.result         = UpdateResult.LATEST;
        this.enabledToggle  = true;
        this.unstableToggle = false;
        CURRENT_VERSION     = currentVersion;
    }

    /*
    CHAIN METHODS
     */

    /**
     * Adds a provider to our provider list, Providers added using this method will be called upon
     * in the order they were added when fetching for the latest version.
     *
     * @param provider Target provider
     * @return an instance of the updater chain
     */
    public Updater addProvider(@Nullable AbstractProvider provider) {
        if (provider != null) providers.add(provider);
        return this;
    }

    /**
     * Sets the main toggle for this updater, enabling or disabling the whole component
     *
     * @param toggle whether this updater should be enabled
     * @return an instance of this updater chain
     * @see #isEnabled()
     */
    public @NotNull Updater setEnabledToggle(boolean toggle) {
        this.enabledToggle = toggle;
        return this;
    }

    /**
     * Toggles whether the updater should consider unstable builds (Alpha, Beta, etc.) as new updates
     * available for download, by setting this to true, ALL version releases will be handled instead
     * of only stable releases.
     *
     * @param toggle whether unstable builds should be considered
     * @return an instance of this updater chain
     * @see #isUnstableEnabled()
     */
    public @NotNull Updater setUnstableToggle(boolean toggle) {
        this.unstableToggle = toggle;
        return this;
    }

    /**
     * Toggles whether the updater should attempt downloads when available. Please note that
     * not all providers allow downloadable links
     *
     * @param toggle whether downloads should be attempted
     * @return an instance of this updater chain
     * @see #isDownloadEnabled()
     */
    public @NotNull Updater setDownloadToggle(boolean toggle) {
        this.attemptDownload = toggle;
        return this;
    }

    /**
     * Sets the interval that this updater will use to periodically check for updates.
     *
     * @param input The target interval
     * @return an instance of this updater chain
     */
    public @NotNull Updater setInterval(@Nullable String input) {
        if (input != null) this.interval = Time.parseInterval(input);
        return this;
    }

    /**
     * Sets the interval that this updater will use to periodically check for updates.
     *
     * @param input The target interval
     * @return an instance of this updater chain
     */
    public @NotNull Updater setInterval(@NotNull Duration input) {
        this.interval = input.toMillis() / 50;
        return this;
    }

    /**
     * Sets the interval that this updater will use to periodically check for updates.
     *
     * @param input The target interval
     * @return an instance of this updater chain
     */
    public @NotNull Updater setInterval(long input) {
        interval = input / 50;
        return this;
    }

    /**
     * Sets the permission required by audience members in-order to receive update notifications.
     * This method accepts a null value, but by doing this, all audience members will receive
     * update notifications.
     *
     * @param permission Required permission
     * @return an instance of this updater chain
     */
    public @NotNull Updater setPermission(@Nullable String permission) {
        this.permission = permission;
        return this;
    }

    /*
    ABSTRACT METHODS
     */

    /**
     * Used to schedule the periodic update check for a later time defined by the {@link #interval} provided.
     * Please note that by default, this method will schedule updates every 3 hours if an interval
     * was not set prior.
     *
     * @return An instance of this updater chain
     * @see #initialize()
     */
    public abstract void schedule();

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version, please note that this method forces
     * the update check upon invoking.
     *
     * @see #schedule()
     */
    public abstract void initialize();

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version, please note that this method forces
     * the update check upon invoking. THis method also allows you to indicate
     * whether its logic will run on an async thread.
     *
     * @param async whether this method will run on an async thread
     * @see #schedule()
     */
    public abstract void initialize(boolean async);

    /**
     * Returns the latest release version retrieved by this updater.
     *
     * @return the latest release version
     */
    public abstract @Nullable ComparableVersion getLatestVersion();

    /**
     * Returns the active provider that contains the latest release artifact, please
     * note that this method could return null, for example, when 0 providers are added
     * to this updater.
     *
     * @return the provider containing the latest release artifact
     */
    public abstract @Nullable AbstractProvider getProvider();

    // TODO: 6/3/23 ADD JAVADOC
    public abstract <T> void sendNotification(T player, UpdateResult result);

    // TODO: 6/3/23 ADD JAVADOC
    public abstract void sendNotification(UpdateResult result);

    /*
    UTILITY METHODS
     */

    /**
     * A utility method that attempts to download updates when available, it takes a string location
     * which represents the url in which the download is located with and an output file, if
     * {@link #isDownloadEnabled()} return's false, this method will do nothing.
     *
     * @param location Download location (url)
     * @param output   Output file in which an update will be copied to
     * @throws IOException when the update fails to download
     */
    protected void attemptDownload(@Nullable String location, @NotNull File output) throws IOException {

        // SKIP THIS METHOD IF UPDATES ARE NOT ALLOWED, OR IF DOWNLOAD LINK IS NULL
        if (!attemptDownload || (location == null || location.isEmpty())) return;

        // ATTEMPT TO DOWNLOAD AND UPDATE RESULT
        if (!output.exists()) {
            HttpURLConnection downloadLink = (HttpURLConnection) new URL(location).openConnection();
            downloadLink.setInstanceFollowRedirects(true);

            if (downloadLink.getResponseCode() == HttpURLConnection.HTTP_OK && !output.exists()) {
                FileUtil.copy(downloadLink.getInputStream(), output);
                if (output.exists()) {
                    result = UpdateResult.DOWNLOADED;
                    return;
                }
            }
        }

        // SET RESULT TO EXISTS IF A THE DOWNLOAD WAS ALREADY DOWNLOADED
        if (output.exists()) result = UpdateResult.EXISTS;
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the permission required by audience members in-order to be notified when
     * new updates become available, Note that this method can return a null value,
     * when this happens all audience members, regardless of permission will receive
     * update notifications when available.
     *
     * @return the permission required by audience members
     */
    public @Nullable String getPermission() {return permission;}

    /**
     * <p>Returns the final result configured by the updater.</p>
     * <p>By default, this method will return {@link UpdateResult#LATEST} if the updater
     * failed to configure a result, typically caused by an unknown outcome or an error
     * that occurred when update checking.</p>
     *
     * @return the final result for this updater
     */
    public UpdateResult getResult() {return result;}

    /**
     * Returns true if this updater is enabled in its current state, note that this
     * method can be changed on a plugin by plugin basis but also using the global
     * config file located in the Updater folder.
     *
     * @return true if this updater is enabled
     */
    public boolean isEnabled() {return enabledToggle;}

    /**
     * Returns true if developmental builds are be considered for our notification
     * system, if false only stable releases trigger notifications. Builds that are considered
     * developmental are as follows, Alpha, Beta, and Release Candidate builds.
     *
     * @return true if unstable builds are allowed
     */
    public boolean isUnstableEnabled() {return unstableToggle;}

    /**
     * Returns true if this updater is permitted to attempt downloads, note that this
     * does not mean that all providers will attempt downloads as some do not
     * contain direct download links.
     *
     * @return true if downloads can be attempted
     */
    public boolean isDownloadEnabled() {return attemptDownload;}
}

package com.moleculepowered.api.updater;

import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.updater.provider.BukkitProvider;
import com.moleculepowered.api.updater.provider.GithubProvider;
import com.moleculepowered.api.updater.provider.HangarProvider;
import com.moleculepowered.api.updater.provider.PolymartProvider;
import com.moleculepowered.api.updater.provider.SpigetProvider;
import com.moleculepowered.api.updater.provider.SpigotProvider;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.api.util.FileUtil;
import com.moleculepowered.api.util.StringUtil;
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
 * An abstract class used to handle the default behaviors for our platform updater.
 * The updater utilizes a "provider framework" where each provider handles update checks
 * for a different platform, such as CurseForge, Spigot, etc.
 *
 * <p> This abstract class provides common functionality and can be extended by specific
 * provider implementations to handle update checking for a particular platform.</p>
 *
 * <p>For a list of our default providers, see {@link com.moleculepowered.api.updater.provider}.</p>
 *
 * @author OMGitzFROST
 * @see AbstractProvider
 * @see SpigotProvider
 * @see BukkitProvider
 * @see PolymartProvider
 * @see SpigetProvider
 * @see GithubProvider
 * @see HangarProvider
 */
public abstract class Updater
{
    protected final List<AbstractProvider> providers = new ArrayList<>();
    protected static ComparableVersion currentVersion;
    protected static ComparableVersion latestVersion;
    protected UpdateResult result;
    protected long interval;
    private final File updateDirectory;
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
     * @param dataFolder      The parent folder for the updater
     * @param currentVersion  The current version of your installed plugin
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
        this.updateDirectory = new File(dataFolder.getParentFile(), "Updater");
        this.interval = Time.parseInterval("3h");
        this.result = UpdateResult.LATEST;
        this.attemptDownload = true;
        this.enabledToggle = true;
        this.unstableToggle = false;
        Updater.currentVersion = currentVersion;
    }

    /*
    CHAIN METHODS
    */

    /**
     * Adds a provider to the provider list. Providers added using this method will be called upon
     * in the order they were added when fetching the latest version.
     *
     * @param provider Target provider
     * @return an instance of the updater chain
     */
    public Updater addProvider(@Nullable AbstractProvider provider) {
        if (provider != null) providers.add(provider);
        return this;
    }

    /**
     * Sets the main toggle for this updater, enabling or disabling the whole component.
     *
     * @param toggle Whether this updater should be enabled
     * @return An instance of this updater chain
     * @see #isEnabled()
     */
    public @NotNull Updater setEnabledToggle(boolean toggle) {
        this.enabledToggle = toggle;
        return this;
    }

    /**
     * Toggles whether the updater should consider unstable builds (Alpha, Beta, etc.) as new updates
     * available for download. By setting this to true, all version releases will be handled instead
     * of only stable releases.
     *
     * @param toggle Whether unstable builds should be considered
     * @return An instance of this updater chain
     * @see #isUnstableEnabled()
     */
    public @NotNull Updater setUnstableToggle(boolean toggle) {
        this.unstableToggle = toggle;
        return this;
    }

    /**
     * Toggles whether the updater should attempt downloads when available. Please note that
     * not all providers allow downloadable links.
     *
     * @param toggle Whether downloads should be attempted
     * @return An instance of this updater chain
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
     * @return An instance of this updater chain
     */
    public @NotNull Updater setInterval(@Nullable String input) {
        if (input != null) this.interval = Time.parseInterval(input);
        return this;
    }

    /**
     * Sets the interval that this updater will use to periodically check for updates.
     *
     * @param input The target interval
     * @return An instance of this updater chain
     */
    public @NotNull Updater setInterval(@NotNull Duration input) {
        this.interval = input.toMillis() / 50;
        return this;
    }

    /**
     * Sets the interval that this updater will use to periodically check for updates.
     *
     * @param input The target interval
     * @return An instance of this updater chain
     */
    public @NotNull Updater setInterval(long input) {
        interval = input / 50;
        return this;
    }

    /**
     * Sets the permission required by audience members in order to receive update notifications.
     * This method accepts a null value, but by doing this, all audience members will receive
     * update notifications.
     *
     * @param permission Required permission
     * @return An instance of this updater chain
     */
    public @NotNull Updater setPermission(@Nullable String permission) {
        this.permission = permission;
        return this;
    }

    /*
    ABSTRACT METHODS
     */

    /**
     * Schedules the periodic update check for a later time based on the provided {@link #interval}.
     *
     * <p>
     * This method is used to schedule the periodic update check. By default, if no interval was set prior,
     * it will schedule updates every 3 hours.
     * </p>
     *
     * @see #initialize()
     */
    public abstract void schedule();

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version. Please note that this method forces
     * the update check upon invoking.
     *
     * @see #schedule()
     */
    public abstract void initialize();

    /**
     * Returns the latest release version retrieved by this updater.
     *
     * @return the latest release version
     */
    public abstract @Nullable ComparableVersion getLatestVersion();

    /**
     * Returns the active provider that contains the latest release artifact. Please
     * note that this method could return null, for example, when 0 providers are added
     * to this updater.
     *
     * @return the provider containing the latest release artifact
     */
    public abstract @Nullable AbstractProvider getProvider();

    /**
     * Sends a notification to a player or an audience member indicating the result
     * of the update check.
     *
     * @param player The player or audience member to send the notification to
     * @param result The update result
     */
    public abstract <T> void sendNotification(T player, UpdateResult result);

    /**
     * Sends a notification to all audience members indicating the result of the update check.
     *
     * @param result The update result
     */
    public abstract void sendNotification(UpdateResult result);

    /**
     * Cancels or removes the scheduled task associated with this method.
     *
     * <p>This method unschedules or cancels a previously scheduled task, preventing it from being executed.
     * The specific behavior of unscheduling depends on the implementation.</p>
     *
     * <p>If called on an unscheduled task, this method has no effect.</p>
     *
     * <p>It is recommended to call this method when the task is no longer needed or when the associated object
     * is being disposed or destroyed, to avoid unnecessary execution.</p>
     *
     * <p><strong>Note:</strong> The cancellation of the task does not guarantee that it will not be executed,
     * as it may be in progress or already executed at the time of cancellation. The behavior of the unschedule
     * operation is defined by the implementation.</p>
     *
     * @see #schedule()
     */
    public abstract void unschedule();

    /*
    UTILITY METHODS
     */

    /**
     * A utility method that attempts to download updates when available. It takes a string location
     * which represents the URL from which the download is located, and an output file. If
     * {@link #isDownloadEnabled()} returns false, this method will do nothing.
     *
     * @param location Download location (URL)
     * @param output   Output file to which an update will be copied
     * @throws IOException when the update fails to download
     */
    protected void attemptDownload(@Nullable String location, @NotNull File output) throws IOException {

        // SKIP THIS METHOD IF UPDATES ARE NOT ALLOWED OR IF DOWNLOAD LINK IS NULL
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

        // SET RESULT TO EXIST IF THE DOWNLOAD WAS ALREADY DOWNLOADED
        if (output.exists()) result = UpdateResult.EXISTS;
    }

    /*
    GETTER METHODS
     */

    /**
     * Retrieves the update folder.
     * <p>
     * This method returns the data folder where updates will be downloaded to. The update folder
     * also contains the global configuration for the updater.
     *
     * @return The update folder as a {@link File} object.
     */
    public File getUpdateFolder() {
        return updateDirectory;
    }

    /**
     * Returns the permission required by audience members in order to be notified when
     * new updates become available. Note that this method can return a null value,
     * indicating that all audience members, regardless of permission, will receive
     * update notifications when available.
     *
     * @return the permission required by audience members, or null if not required
     */
    public @NotNull String getPermission() {
        return StringUtil.nonNull(permission);
    }

    /**
     * Returns the final result configured by the updater.
     * By default, this method will return {@link UpdateResult#LATEST} if the updater
     * failed to configure a result, typically caused by an unknown outcome or an error
     * that occurred during update checking.
     *
     * @return the final result for this updater
     */
    public UpdateResult getResult() {
        return result;
    }

    /**
     * Returns true if this updater is enabled in its current state. Note that this
     * setting can be changed on a plugin-by-plugin basis or by modifying the global
     * config file located in the Updater folder.
     *
     * @return true if this updater is enabled
     */
    public boolean isEnabled() {
        return enabledToggle;
    }

    /**
     * Returns true if developmental builds are considered for the notification
     * system. If false, only stable releases trigger notifications. Builds that are considered
     * developmental include Alpha, Beta, and Release Candidate builds.
     *
     * @return true if unstable builds are allowed for notifications
     */
    public boolean isUnstableEnabled() {
        return unstableToggle;
    }

    /**
     * Returns true if this updater is permitted to attempt downloads. Note that this
     * does not guarantee that all providers will attempt downloads, as some providers
     * may not have direct download links available.
     *
     * @return true if downloads can be attempted
     */
    public boolean isDownloadEnabled() {
        return attemptDownload;
    }

    /**
     * This enum allows you to define what audience type should receive update notifications.
     * The audience can be specified as the console, online players, or both.
     *
     * @author OMGitzFROST
     */
    protected enum AudienceType
    {
        /**
         * Indicates that the audience will be the console.
         */
        CONSOLE,
        /**
         * Indicates that the audience will be the online players.
         */
        PLAYER,
        /**
         * Indicates that the audience will consist of both the console and the players.
         */
        ALL
    }
}

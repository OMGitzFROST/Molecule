package com.moleculepowered.api.updater;

import com.moleculepowered.api.event.updater.UpdateCompleteEvent;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.Time;
import com.moleculepowered.api.util.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.moleculepowered.api.util.StringUtil.format;

public final class Updater {

    private final List<AbstractProvider> providers = new ArrayList<>();
    private final Plugin plugin;
    private AbstractProvider activeProvider;
    private boolean enabled, betaEnabled;
    private String permission;
    private long interval;
    private Listener listener;
    private UpdateResult result = UpdateResult.UNKNOWN;

    /*
    CONSTRUCTOR
     */

    public Updater(@NotNull Plugin plugin) {
        this.plugin = plugin;
        interval = 10800000L;
        enabled = true;
        permission = "";
    }

    /*
    MAIN FUNCTIONS
     */

    /**
     * A method used to schedule the updater for execute its repeating tasks, this method
     * is also used to verify that all requirements are met so that we can preform the update checks
     * correctly.
     *
     * @see #scheduleAsync()
     */
    public void schedule() {
        // THROW ERRORS FOR INVALID ESSENTIAL SETTINGS
        Validate.isTrue(interval > 0L, "The interval must be greater than 0 for this updater");
        Validate.isTrue(!providers.isEmpty(), "Please supply at least one provider for your updater");

        // FIRST, VERIFY THAT ALL REQUIREMENTS ARE MET BEFORE SCHEDULING
        if (!enabled) {
            plugin.getLogger().info("Updater disabled, there we will not check for updates.");
            result = UpdateResult.DISABLED;
            return;
        }

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> initialize(false), 0, interval);
    }

    /**
     * A method used to schedule the updater asynchronously to execute its repeating tasks, this method
     * is also used to verify that all requirements are met so that we can preform the update checks
     * correctly.
     *
     * @see #schedule()
     */
    public void scheduleAsync() {
        // THROW ERRORS FOR INVALID ESSENTIAL SETTINGS
        Validate.isTrue(interval > 0L, "The interval must be greater than 0 for this updater");
        Validate.isTrue(!providers.isEmpty(), "Please supply at least one provider for your updater");

        // FIRST, VERIFY THAT ALL REQUIREMENTS ARE MET BEFORE SCHEDULING
        if (!enabled) {
            plugin.getLogger().info("Updater disabled, there we will not check for updates.");
            result = UpdateResult.DISABLED;
            return;
        }

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> initialize(true), 0, interval);
    }

    /**
     * A private utility method that is used to centralize version checking
     *
     * @param async whether this method will run async
     * @see #schedule()
     * @see #scheduleAsync()
     */
    private void initialize(boolean async) {
        Version currentVersion = new Version(plugin.getDescription().getVersion());

        for (AbstractProvider provider : providers) {
            provider.fetch();
            Version fetchedVersion = provider.getLatestVersion();

            if (activeProvider == null) activeProvider = provider;
            if (!fetchedVersion.isGreaterThan(currentVersion)) {
                result = UpdateResult.LATEST;
                continue;
            }
            if (fetchedVersion.isGreaterThan(currentVersion)) {
                if (!fetchedVersion.isUnstable() || (fetchedVersion.isUnstable() && betaEnabled)) {
                    if (!fetchedVersion.isEqual(activeProvider.getLatestVersion())) activeProvider = provider;
                    result = UpdateResult.UPDATE_AVAILABLE;
                }
            }
        }

        // CREATE USABLE VERSIONS
        UpdateCompleteEvent completeEvent = new UpdateCompleteEvent(async, this);
        plugin.getServer().getPluginManager().registerEvents(listener != null ? listener : new UpdateEventHandler(), plugin);

        if (!completeEvent.isCancelled()) plugin.getServer().getPluginManager().callEvent(completeEvent);
    }

    /*
    CHAIN METHODS
     */

    /**
     * Adds an individual provider into our provider's list, the updater will use these providers
     * to check for new updates and return information about them.
     *
     * @param provider an update provider
     * @return an instance of this updater
     * @see #addProvider(AbstractProvider)
     * @see #getProviders()
     */
    public @NotNull Updater addProvider(@NotNull AbstractProvider provider) {
        Validate.notEmpty(provider.getName(), "One or more of your update providers is missing a provider name");
        providers.add(provider);
        return this;
    }

    /**
     * Adds a collection of providers into our provider's list, the updater will use these providers
     * to check for new updates and return information about them.
     *
     * @param providers a collection of providers
     * @return an instance of this updater
     * @see #addProvider(AbstractProvider)
     * @see #getProviders()
     */
    public @NotNull Updater addProviders(@NotNull Collection<AbstractProvider> providers) {
        providers.forEach(this::addProvider);
        return this;
    }

    /**
     * Sets the toggle that determines whether this updater should be enabled, If the toggle is set
     * to false, this updater will not check for new updates.
     *
     * @param enabled whether this updater should be enabled
     * @return an instance of this updater
     * @see #isEnabled()
     */
    public @NotNull Updater setEnableToggle(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Sets the toggle that determines if this updater should notify audience
     * members of unstable builds as well, such as BETA, ALPHA, etc. If the toggle
     * is set to false, this updater will only notify members of stable builds.
     *
     * @param enabled whether unstable builds are enabled
     * @return an instance of this updater
     * @see #isBetaEnabled()
     */
    public @NotNull Updater setBetaToggle(boolean enabled) {
        this.betaEnabled = enabled;
        return this;
    }

    /**
     * <p>Sets the handler that will be called when an update check is completed.</p>
     *
     * <p>Please note that in-order for you to customize the behavior when sending notifications,
     * your handler must include an {@link EventHandler} that listens for the {@link UpdateCompleteEvent},
     * otherwise this updater will provide a default handler.
     *
     * @param listener Designated event handler
     * @return an instance of this updater
     */
    public @NotNull Updater setEventHandler(@NotNull Listener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Used to return whether this updater should also notify when unstable versions are
     * available, please note that if this method returns false, it means that release only
     * versions will trigger notifications, and unstable versions such as BETA, ALPHA, RC will
     * not trigger a notification.
     *
     * @return true if unstable versions are enabled
     * @see #setBetaToggle(boolean)
     */
    public boolean isBetaEnabled() {
        return betaEnabled;
    }

    /**
     * Used to return whether this updater is currently enabled
     *
     * @return true if enabled
     * @see #setEnableToggle(boolean)
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Return's true if the provided player is considered an authorized audience member. Otherwise, this
     * method will return false.
     *
     * @param player Target player
     * @return true if player is an authorized audience member
     * @see #getAudience()
     */
    public boolean isAudienceMember(@Nullable OfflinePlayer player) {
        return player != null && (player.getPlayer() != null && getAudience().contains(player.getPlayer()));
    }

    /**
     * Returns the interval in which this updater will check for updates
     *
     * @return Interval in which updates will be checked.
     * @see #setInterval(String)
     */
    public long getInterval() {
        return interval;
    }

    /**
     * <p>Sets the interval in which this updater will check for updates</p>
     *
     * <p>Please take a look at the table below for examples of valid inputs.</p>
     * <table>
     *     <col width="30%"/>
     *     <col width="10%"/>
     *     <col width="60%"/>
     *     <thead>
     *     <tr>
     *         <th>Unit</th>
     *         <th></th>
     *         <th>Abbreviation</th>
     *     </tr>
     *     <thead>
     *     <tbody>
     *      <tr>
     *          <td>Millisecond</td>
     *          <td></td>
     *          <td>[ms, milli, millisecond, milliseconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Second</td>
     *          <td></td>
     *          <td>[s, sec, second, seconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Minute</td>
     *          <td></td>
     *          <td>[m, min, minute, minutes]</td>
     *      </tr>
     *      <tr>
     *          <td>Hour</td>
     *          <td></td>
     *          <td>[h, hr, hour, hours]</td>
     *      </tr>
     *      <tr>
     *          <td>Day</td>
     *          <td></td>
     *          <td>[d, day, days]</td>
     *      </tr>
     *      <tr>
     *          <td>Week</td>
     *          <td></td>
     *          <td>[wk, week, weeks]</td>
     *      </tr>
     *      <tr>
     *          <td>Month</td>
     *          <td></td>
     *          <td>[mo, month, months]</td>
     *      </tr>
     *      <tr>
     *          <td>Year</td>
     *          <td></td>
     *          <td>[y, year, years]</td>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param input Provided input
     * @return an instance of this updater
     * @throws IllegalArgumentException when the input is not valid
     * @see #getInterval()
     */
    public @NotNull Updater setInterval(@NotNull String input) {
        interval = Time.parseInterval(input);
        return this;
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the required permission that player's need in-order to receive update
     * notifications
     *
     * @return Required permission for notifications
     * @see #setPermission(String)
     */
    public @NotNull String getPermission() {
        return permission;
    }

    /**
     * Sets the permission that will be required by audience members in-order
     * to receive update notifications.
     *
     * @param permission Required permission
     * @return an instance of this updater
     * @see #getPermission()
     */
    public @NotNull Updater setPermission(@NotNull String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Used to return the list of providers assigned to this updater, if no providers
     * were given, this method will return an empty list.
     *
     * @return List of providers
     * @see #addProvider(AbstractProvider)
     * @see #addProviders(Collection)
     */
    public List<AbstractProvider> getProviders() {
        return providers;
    }

    /**
     * Used to return the active provider used by this updater, please note that this method
     * has the potential of returning null, if the active provider was not set prior.
     *
     * @return the active provider
     * @see #getProviders()
     * @see #addProvider(AbstractProvider)
     * @see #addProviders(Collection)
     */
    public @NotNull AbstractProvider getProvider() {
        return activeProvider == null ? providers.get(0) : activeProvider;
    }

    /**
     * Used to return a set of audience members, please note that this method, by default,
     * features a filter to sift out offline players and retaining only those that are online.
     * Additionally, this method will filter out those players that do not have the required
     * permissions for receiving update information.
     *
     * @return A set of audience members that should receive notifications
     * @see #isAudienceMember(OfflinePlayer)
     */
    public Set<Player> getAudience() {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .filter(player -> permission.equals("") || player.hasPermission(getPermission()))
                .collect(Collectors.toSet());
    }

    /**
     * Used to return the final result for this updater
     *
     * @return Update result
     */
    public UpdateResult getResult() {
        return result;
    }

    /*
    EVENT HANDLER CLASS
     */

    /**
     * The default class that will be used to handle our default update notifications. This updater
     * has a built-in check that will ensure that this class is only implemented as the
     * primary update handler if the developer did not provide one for us.
     */
    private class UpdateEventHandler implements Listener {

        private AbstractProvider provider;

        /*
        EVENT HANDLERS
         */

        @EventHandler
        public void onUpdateComplete(@NotNull UpdateCompleteEvent event) {

            provider = event.getProvider();

            // SEND CONSOLE NOTIFICATIONS WHEN THE UPDATER COMPLETES
            switch (event.getResult()) {
                case LATEST:
                    plugin.getLogger().info("You have the latest version of this plugin");
                    break;
                case UPDATE_AVAILABLE:
                    plugin.getLogger().info(StringUtils.repeat('*', 60));
                    plugin.getLogger().info(format("Version ({0}) is now available for {1}.", provider.getLatestVersion().getVersion(), plugin.getName()));
                    if (provider.getDownloadLink() != null)
                        plugin.getLogger().info(format("Download: {0}", provider.getDownloadLink()));
                    if (provider.getChangelogLink() != null)
                        plugin.getLogger().info(format("Changelog: {0}", provider.getChangelogLink()));
                    if (provider.getDonationLink() != null)
                        plugin.getLogger().info(format("Donate: {0}", provider.getDonationLink()));
                    plugin.getLogger().info(StringUtils.repeat('*', 60));
                    break;
                case DISABLED:
                    plugin.getLogger().warning("The auto updater for this plugin is disabled, please enable to receive update notifications");
                    break;
                default:
                    plugin.getLogger().warning("The updater returned an unknown result, its severity is also undefined");
            }

            // SEND PLAYER NOTIFICATION
            getAudience().forEach(this::sendPlayerNotification);
        }

        @EventHandler
        public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

            // CHECK IF UPDATE IS AVAILABLE AND
            if (result == UpdateResult.UPDATE_AVAILABLE && getAudience().contains(event.getPlayer())) {
                sendPlayerNotification(event.getPlayer());
            }
        }

        /*
        NOTIFICATION METHODS
         */

        /**
         * Sends a notification to a player determined by the result configured by the updater.
         *
         * @param player Target player
         */
        private void sendPlayerNotification(@NotNull Player player) {
            // SEND PLAYER NOTIFICATIONS WHEN THE UPDATER COMPLETES
            switch (result) {
                case LATEST:
                    player.sendMessage("You have the latest version of this plugin");
                    break;
                case UPDATE_AVAILABLE:
                    player.sendMessage(format("Version ({0}) is now available for {1}.", provider.getLatestVersion().getVersion(), plugin.getName()));
                    player.sendMessage("Please refer to the console for more information.");
                    break;
                case DISABLED:
                    player.sendMessage("The auto updater for this plugin is disabled, please enable to receive update notifications");
                    break;
                default:
                    player.sendMessage("The updater returned an unknown result, its severity is also undefined");
            }
        }
    }
}

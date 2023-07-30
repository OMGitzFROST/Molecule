package com.moleculepowered.platform.bukkit.updater;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
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
import com.moleculepowered.api.util.Validate;
import com.moleculepowered.platform.bukkit.BukkitPlugin;
import com.moleculepowered.platform.bukkit.component.ComponentBuilder;
import com.moleculepowered.platform.bukkit.event.updater.UpdateCompleteEvent;
import com.moleculepowered.platform.bukkit.event.updater.UpdateFailedEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The BukkitUpdater class represents an updater implementation for Bukkit-based plugins.
 * It extends the Updater abstract class and provides specific functionality for updating
 * plugins in the Bukkit platform.
 *
 * <p>
 * The BukkitUpdater class is responsible for scheduling update checks, managing providers,
 * and handling update notifications for Bukkit-based plugins.
 * </p>
 *
 * <p>
 * To use the BukkitUpdater, create an instance of this class and configure its properties
 * such as providers, update interval, and notification settings. Then, call the {@link #schedule()}
 * or {@link #scheduleAsync()} method to start the periodic update checks. When updates are available,
 * the BukkitUpdater will handle the download and notification process based on the configured settings.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>{@code
 * BukkitUpdater updater = new BukkitUpdater(plugin);
 * updater.addProvider(new SpigotProvider())
 *        .addProvider(new BukkitProvider())
 *        .setInterval("1d")
 *        .setDownloadToggle(true)
 *        .setEnabledToggle(true)
 *        .schedule();
 * }</pre>
 *
 * <p>
 * In the example above, a BukkitUpdater instance is created for the specified plugin. Two providers,
 * SpigotProvider and BukkitProvider, are added to the updater to handle update checks from Spigot
 * and Bukkit platforms. The update interval is set to 1 day, and both downloads and notifications
 * are enabled. Finally, the updater is scheduled to run periodic update checks.
 * </p>
 *
 * @author OMGitzFROST
 * @see Updater
 * @see AbstractProvider
 * @see SpigotProvider
 * @see BukkitProvider
 * @see PolymartProvider
 * @see SpigetProvider
 * @see GithubProvider
 * @see HangarProvider
 */
@SuppressWarnings("unused")
public final class BukkitUpdater extends Updater implements Listener
{
    private final Set<Player> audience = new HashSet<>();
    private static boolean isLegacy;
    private static AbstractProvider provider;
    private static Plugin plugin;
    private BukkitTask task;
    private YamlConfiguration config;

    /*
    CONSTRUCTOR
     */

    /**
     * Constructs and configures the updater class to adapt to the Bukkit platform using the provided plugin.
     *
     * @param plugin The platform plugin.
     */
    public BukkitUpdater(@NotNull Plugin plugin) {
        super(plugin.getDataFolder(), new ComparableVersion(plugin.getDescription().getVersion()));
        isLegacy = !(plugin instanceof MoleculePlugin);
        latestVersion = currentVersion;
        BukkitUpdater.plugin = plugin;

        // HANDLE CONFIGURATION CREATION
        File globalConfigFile = new File(getUpdateFolder(), "config.yml");
        try {
            if (!getUpdateFolder().exists() && !getUpdateFolder().mkdirs()) {
                throw new IllegalAccessError("Unable to create the following directory: " + getUpdateFolder().getName());
            }

            // CREATE CONFIG FILE IF ONE DOES NOT CURRENTLY EXIST
            if (!globalConfigFile.exists()) {
                config = new YamlConfiguration();
                config.addDefault("enabled", true);
                config.options().copyDefaults(true);
                config.save(globalConfigFile);
            }
            config = YamlConfiguration.loadConfiguration(globalConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs and configures the updater class to adapt to the Bukkit platform using the provided plugin.
     *
     * @param plugin The plugin representing the Bukkit platform.
     */
    public BukkitUpdater(@NotNull BukkitPlugin plugin) {
        this((Plugin) plugin);
    }

    /*
    SCHEDULING METHODS
     */

    /**
     * Schedules a periodic update check for a later time based on the provided {@link #interval}.
     *
     * <p>
     * This method is used to schedule the periodic update check. By default, if no interval was set prior,
     * it will schedule updates every 3 hours. The update checks are run on an asynchronous thread to avoid
     * blocking the main thread.
     * </p>
     *
     * @see #initialize()
     * @see #initialize(boolean)
     */
    public void scheduleAsync() {
        if (task != null) task.cancel();
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> initialize(true), 0, interval);
    }

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
    @Override
    public void schedule() {
        if (task != null) task.cancel();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::initialize, 0, interval);
    }

    /*
    INITIALIZE
     */

    /**
     * Initializes the updater by fetching the latest version and performing necessary
     * setup steps. By default, this method runs on the main thread, so ensure that
     * time-consuming operations are handled appropriately to maintain responsiveness.
     *
     * @see #schedule()
     */
    @Override
    public void initialize() {
        initialize(false);
    }

    /**
     * Initializes the updater and fetches the latest version.
     *
     * <p>This method initializes the updater and fetches the latest version. Invoking it forces
     * an update check. You can specify whether this method runs on an asynchronous thread by
     * setting the `async` parameter.</p>
     *
     * @param async Whether the method runs on an asynchronous thread.
     * @see #schedule()
     */
    public void initialize(boolean async) {
        try {
            Validate.notEmpty(providers, "Updater Misconfigured! Please provide at least one provider");
            Validate.isTrue(interval > 0, "The minimum interval for the updater is \"1s\"");

            // REGISTER ALL EVENTS FOR THIS CLASS, EVEN IF NO EVENTS ARE CREATED
            plugin.getServer().getPluginManager().registerEvents(this, plugin);

            // CLEAR EXISTING AUDIENCE, AND LOAD NEW MEMBERS
            audience.clear();
            audience.addAll(plugin.getServer().getOnlinePlayers().stream()
                    .filter(player -> StringUtil.isEmpty(getPermission()) || player.hasPermission(getPermission()))
                    .collect(Collectors.toSet()));

            // SET PROVIDER IF ONE IS NOT ALREADY SET
            if (provider == null) provider = providers.get(0);

            // IF ENABLED, RUN UPDATE CHECK
            if (config.getBoolean("enabled") && isEnabled()) {
                for (AbstractProvider active : providers) {

                    // FETCH LATEST RELEASE
                    active.fetch();

                    // SET AS LATEST IF THE FETCHED UPDATE IS NOT GREATER
                    ComparableVersion fetchedVersion = active.getVersion();
                    if (!fetchedVersion.isGreaterThan(latestVersion)) continue;
                    if (fetchedVersion.isUnstable() && !isUnstableEnabled()) continue;
                    if (fetchedVersion.isEqualTo(currentVersion)) continue;

                    // SET VALUES FOR THE LATEST RELEASE
                    provider = active;
                    latestVersion = active.getVersion();
                }

                // IF LATEST VERSION IS GREATER THAN CURRENT VERSION, ATTEMPT DOWNLOAD AND SET RESULT
                if (latestVersion.isGreaterThan(currentVersion)) {
                    result = UpdateResult.UPDATE_AVAILABLE;
                    String downloadLink = StringUtil.nonNull(provider.getDownloadLink());
                    attemptDownload(downloadLink, new File(getUpdateFolder(), FileUtil.getFileName(downloadLink)));
                }
            } else result = UpdateResult.DISABLED;

            // CALL EVENT WHEN UPDATER COMPLETES
            UpdateCompleteEvent event = new UpdateCompleteEvent(async, this);
            plugin.getServer().getPluginManager().callEvent(event);

            // SEND NOTIFICATIONS TO THE APPROPRIATE AUDIENCE IF EVENT IS NOT CANCELLED
            if (!event.isCancelled()) sendNotification(result);

            // UNSCHEDULE UPDATER IF DISABLED
            if (result == UpdateResult.DISABLED) unschedule();

        } catch (IOException ex) {
            // CALL EVENT WHEN UPDATER COMPLETES
            UpdateFailedEvent event = new UpdateFailedEvent(async, this, ex);
            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    /*
    GETTER METHODS
     */

    /**
     * Retrieves and returns the latest release version from the updater.
     *
     * @return The latest release version.
     */
    @Override
    public @Nullable ComparableVersion getLatestVersion() {
        return latestVersion;
    }

    /**
     * Returns the active provider that contains the latest release artifact. Please
     * note that this method could return null, for example, when 0 providers are added
     * to this updater.
     *
     * @return the provider containing the latest release artifact
     */
    @Override
    public @Nullable AbstractProvider getProvider() {
        return provider;
    }

    /**
     * Returns the set of players in the audience.
     *
     * @return The set of players representing the audience.
     */
    public Set<Player> getAudience() {
        return audience;
    }

    /*
    SEND NOTIFICATIONS
     */

    /**
     * Sends a notification to a player or an audience member indicating the result
     * of the update check.
     *
     * @param player The player or audience member to send the notification to
     * @param result The update result
     */
    @Override
    public <T> void sendNotification(T player, UpdateResult result) {
        Validate.isInstance(player, Player.class, "The object your provide must be a player");
        sendNotification((Player) player, MessageService.valueOf(result));
    }

    /**
     * Sends a notification to all audience members indicating the result of the update check.
     *
     * @param result The update result
     */
    @Override
    public void sendNotification(UpdateResult result) {
        sendNotification(AudienceType.ALL, MessageService.valueOf(result));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unschedule() {
        task.cancel();
    }

    /**
     * A utility method used to send a notification determined by the message service provided
     *
     * @param type    Audience type
     * @param message The message(s) being sent
     */
    private void sendNotification(@NotNull AudienceType type, @NotNull MessageService message) {

        BaseComponent[] messages = message.getMessages();
        BaseComponent[] consoleMessage;

        // ROUTE RESULT MESSAGE TO USE CONSOLE VERSION WHEN UPDATES ARE AVAILABLE
        if (result != UpdateResult.UPDATE_AVAILABLE) consoleMessage = messages;
        else consoleMessage = MessageService.UPDATE_AVAILABLE_CONSOLE.getMessages();

        // DISTRIBUTE A NOTIFICATION BASED ON THE AUDIENCE TYPE
        switch (type) {
            case CONSOLE:
                if (plugin instanceof BukkitPlugin) ((BukkitPlugin) plugin).getConsole().log(consoleMessage);
                else Arrays.stream(consoleMessage).forEach(m -> plugin.getLogger().info(m.toPlainText()));
                break;
            case PLAYER:
                audience.forEach(player -> Arrays.stream(messages).forEach(m -> player.spigot().sendMessage(m)));
                break;
            default:
                sendNotification(AudienceType.CONSOLE, message);
                sendNotification(AudienceType.PLAYER, message);
                break;
        }
    }

    /**
     * A utility method used to send a notification determined by the message service provided.
     *
     * @param player  The target we will send the notification to
     * @param message The message(s) being sent
     */
    private void sendNotification(@NotNull Player player, @NotNull MessageService message) {
        Arrays.stream(message.getMessages()).forEach(m -> player.spigot().sendMessage(m));
    }

    /*
    INTERNAL CLASSES
     */

    /**
     * Enumerates the default messages sent by the updater when it completes its task.
     *
     * <p>This enum defines the default messages sent by the updater when it finishes its task. Each constant
     * represents a specific message and can accept either message components or strings as parameters.</p>
     *
     * @author OMGitzFROST
     */
    private enum MessageService
    {
        DISABLED("&6Updater is currently disabled. No update checks will be performed. Enable the updater to stay up-to-date with the latest improvements."),
        DOWNLOADED(format("&aSuccessfully downloaded (&e{0} v{1}&a). Please install it from your update folder to enjoy the latest improvements. Happy updating!", plugin.getName(), latestVersion.toString())),
        EXISTS(format("&e{0} v{1} update already downloaded! Please check your Update folder and install it for the latest enhancements. Enjoy the new features!", plugin.getName(), latestVersion.toString())),
        LATEST(format("&6No updates found. We're working on enhancing your experience. Stay tuned!")),
        UPDATE_AVAILABLE(getHoverableMSG()),
        UPDATE_AVAILABLE_CONSOLE(
                StringUtil.repeat('*', 60),
                format("&6Version (&4&l{0}&6) is now available for &4&l{1}&6.", latestVersion.toString(), plugin.getName()),
                provider.getDownloadLink() != null ? format("&aDownload: &r{0}", provider.getDownloadLink()) : "",
                provider.getChangelogLink() != null ? format("&aChangelog: &r{0}", provider.getChangelogLink()) : "",
                provider.getDonationLink() != null ? format("&aDonate: &r{0}", provider.getDonationLink()) : "",
                StringUtil.repeat('*', 60)
        );

        // THE OBJECT THAT HOLDS AN ARRAY OF MESSAGES ASSOCIATED WITH EACH CONSTANT
        private final BaseComponent[] messages;

        /**
         * Creates a constant that allows components to be accepted as its parameters.
         *
         * @param component Message components
         */
        MessageService(BaseComponent... component) {
            this.messages = Arrays.stream(component).map(TextComponent::new).toArray(BaseComponent[]::new);
        }

        /**
         * Creates a constant that allows strings to be accepted as its parameters.
         * Note that these strings will be converted to {@link BaseComponent}.
         *
         * @param input Message inputs
         */
        MessageService(@NotNull String... input) {
            input = Arrays.stream(input)
                    .filter(s -> !s.isEmpty())
                    .map(s -> ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, s.replace('&', ChatColor.COLOR_CHAR)))
                    .toArray(String[]::new);
            this.messages = Arrays.stream(input).map(TextComponent::new).toArray(BaseComponent[]::new);
        }

        /**
         * Returns all message strings assigned to the provided default message constant.
         *
         * @param type The provided constant
         * @return All messages assigned to the provided constant
         */
        public static BaseComponent[] getMessages(@NotNull MessageService type) {
            return type.getMessages();
        }

        /**
         * Returns all messages assigned to this constant as an array.
         *
         * @return All messages assigned to this constant
         */
        public @NotNull BaseComponent[] getMessages() {
            return messages;
        }

        /**
         * Creates a link bar displayed in chat when updates are available.
         *
         * @return A link bar containing download, changelog links, and more
         */
        private static @NotNull BaseComponent[] getHoverableMSG() {
            boolean hasDownloadLink = Objects.requireNonNull(provider).getDownloadLink() != null;

            String hoverMSG = format("&b{0} &cv{1} &7-> &av{2}\n" + "&7Click here to {4} update", plugin.getName(), currentVersion, latestVersion, provider.getName(), hasDownloadLink ? "download" : "view");
            return new ComponentBuilder("&aUpdate available! &eClick/Hover &aover this text for more info.")
                    .setHoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMSG)
                    .setClickEvent(ClickEvent.Action.OPEN_URL, provider.getDownloadLink())
                    .create();
        }

        /**
         * Used to translate color codes for the update components.
         *
         * @param input Provided input
         * @param param Optional parameters
         * @return A color-formatted string
         */
        private static @NotNull String format(String input, Object... param) {
            input = isLegacy ? StringUtil.stripColor(input) : input.replace(ChatColor.COLOR_CHAR, '&');
            return ChatColor.translateAlternateColorCodes('&', StringUtil.format(input, param));
        }

        /**
         * Returns the constant of the {@code MessageService} enum with the specified {@code UpdateResult} name.
         *
         * @param result The {@code UpdateResult} whose name is used to retrieve the corresponding {@code MessageService} constant.
         * @return The {@code MessageService} constant with the specified name.
         * @throws IllegalArgumentException If no constant is found with the specified name.
         * @throws NullPointerException     If the {@code result} parameter is {@code null}.
         */
        public static MessageService valueOf(@NotNull UpdateResult result) {
            return MessageService.valueOf(result.name());
        }
    }

    /**
     * This method is used to listen for playing joining and sending an update message
     * if one is available, please note that teh player must be permitted in-order to
     * recieve notifications.
     *
     * @param event The event triggered
     */
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isPermitted = getPermission().isEmpty() || player.hasPermission(getPermission());
        boolean isAudience = audience.contains(player);

        // ADJUST AUDIENCE LIST WHEN APPLICABLE
        if (isAudience && !isPermitted) audience.remove(player);
        if (!isAudience && isPermitted) audience.add(player);

        // NOTIFY AUDIENCE MEMEBERS ON JOIN WHEN UPDATE IS AVAILABLE
        if (audience.contains(player) && result == UpdateResult.UPDATE_AVAILABLE) {
            sendNotification(player, result);
        }
    }
}

package com.moleculepowered.platform.bukkit.updater;

import com.moleculepowered.api.adapter.HoverAdapter;
import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.api.util.FileUtil;
import com.moleculepowered.api.util.StringUtil;
import com.moleculepowered.api.util.Validate;
import com.moleculepowered.platform.bukkit.model.BukkitNMSBridge;
import com.moleculepowered.platform.bukkit.BukkitPlugin;
import com.moleculepowered.platform.bukkit.ComponentBuilder;
import com.moleculepowered.platform.bukkit.updater.event.UpdateCompleteEvent;
import com.moleculepowered.platform.bukkit.updater.event.UpdateFailedEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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
 * A bukkit implementation for the {@link Updater} component.
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public final class BukkitUpdater extends Updater
{
    private final Set<Player> audience = new HashSet<>();
    private static AbstractProvider provider;
    private static Plugin plugin;

    /*
    CONSTRUCTOR
     */

    /**
     * The main constructor for this updater class, it creates and configures
     * itself to adapt to the bukkit platform using the plugin provided
     *
     * @param plugin Platform plugin
     */
    public BukkitUpdater(@NotNull Plugin plugin) {
        super(plugin.getDataFolder(), new ComparableVersion(plugin.getDescription().getVersion()));
        LATEST_VERSION = CURRENT_VERSION;
        BukkitUpdater.plugin = plugin;
    }

    /**
     * The main constructor for this updater class, it creates and configures
     * itself to adapt to the bukkit platform using the plugin provided
     *
     * @param plugin Platform plugin
     */
    public BukkitUpdater(@NotNull BukkitPlugin plugin) {
        this((Plugin) plugin);
    }

    /*
    SCHEDULING METHODS
     */

    /**
     * Used to schedule the periodic update check for a later time defined by the {@link #interval} provided.
     * Please note that by default, this method will schedule updates every 3 hours if an interval
     * was not set prior. This method will ensure update checks run on an asynchronous thread.
     *
     * @see #initialize()
     * @see #initialize(boolean)
     */
    public void scheduleAsync() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::initialize, 0, interval);
    }

    /**
     * Used to schedule the periodic update check for a later time defined by the {@link #interval} provided.
     * Please note that by default, this method will schedule updates every 3 hours if an interval
     * was not set prior.
     *
     * @see #initialize()
     * @see #initialize(boolean)
     */
    @Override
    public void schedule() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::initialize, 0, interval);
    }

    /*
    INITIALIZE
     */

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version, please note that this method forces
     * the update check upon invoking.
     *
     * @see #schedule()
     */
    @Override
    public void initialize() {initialize(false);}

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version, please note that this method forces
     * the update check upon invoking. THis method also allows you to indicate
     * whether its logic will run on an async thread.
     *
     * @param async whether this method will run on an async thread
     * @see #schedule()
     */
    @Override
    public void initialize(boolean async) {
        try {
            Validate.notEmpty(providers, "You must provide at least one provider for this updater to work");
            Validate.isTrue(interval > 0, "The updater requires it's interval to be greater than " + interval);

            // CLEAR EXISTING AUDIENCE, AND LOAD NEW MEMBERS
            audience.clear();
            audience.addAll(plugin.getServer().getOnlinePlayers().stream()
                    .filter(player -> StringUtil.isEmpty(getPermission()) || player.hasPermission(getPermission()))
                    .collect(Collectors.toSet()));

            if (isEnabled()) {
                for (AbstractProvider active : providers) {
                    active.fetch();
                    ComparableVersion fetchedVersion = active.getVersion();

                    // ADD AS ACTIVE PROVIDER IF ONE HAS NOT BEEN SET ALREADY
                    if (provider == null) provider = active;

                    // SET AS LATEST IF THE FETCHED UPDATE IS NOT GREATER
                    if (!fetchedVersion.isGreaterThan(LATEST_VERSION)) continue;
                    if (fetchedVersion.isUnstable() && !isUnstableEnabled()) continue;
                    if (fetchedVersion.isEqual(CURRENT_VERSION)) continue;

                    provider = active;
                    LATEST_VERSION = active.getVersion();
                }

                if (LATEST_VERSION.isGreaterThan(CURRENT_VERSION)) {
                    result = UpdateResult.UPDATE_AVAILABLE;
                    String downloadLink = StringUtil.nonNull(provider.getDownloadLink());
                    attemptDownload(downloadLink, new File(DATA_FOLDER, FileUtil.getFileName(downloadLink)));
                }
            }
            else result = UpdateResult.DISABLED;

            // CALL EVENT WHEN UPDATER COMPLETES
            UpdateCompleteEvent event = new UpdateCompleteEvent(async, this);
            plugin.getServer().getPluginManager().callEvent(event);

            // SEND NOTIFICATIONS TO THE APPROPRIATE AUDIENCE
            if (!event.isCancelled()) sendNotification(result);
        }
        catch (IOException ex) {
            // CALL EVENT WHEN UPDATER COMPLETES
            UpdateFailedEvent event = new UpdateFailedEvent(async, this, ex);
            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the latest release version retrieved by this updater.
     *
     * @return the latest release version
     */
    @Override
    public @Nullable ComparableVersion getLatestVersion() {
        return LATEST_VERSION;
    }

    /**
     * Returns the active provider that contains the latest release artifact, please
     * note that this method could return null, for example, when 0 providers are added
     * to this updater.
     *
     * @return the provider containing the latest release artifact
     */
    @Override
    public @Nullable AbstractProvider getProvider() {
        return provider;
    }

    /*
    SEND NOTIFICATIONS
     */

    // TODO: 6/4/23 ADD JAVADOC
    @Override
    public <T> void sendNotification(T player, UpdateResult result) {
        Validate.isInstance(player, Player.class);
        sendNotification((Player) player, DefaultMessageService.valueOf(result));
    }

    /**
     * Sends a notification to all audience members based on the provided result
     *
     * @param result The update result
     */
    @Override
    public void sendNotification(UpdateResult result) {
        sendNotification(AudienceType.ALL, DefaultMessageService.valueOf(result));
    }

    /**
     * A utility method used to send a notification determined by the message service provided
     *
     * @param type    Audience type
     * @param message The message(s) being sent
     */
    private void sendNotification(@NotNull AudienceType type, @NotNull DefaultMessageService message) {

        BaseComponent[] messages = message.getMessages();
        BaseComponent[] consoleMessage = DefaultMessageService.UPDATE_AVAILABLE_CONSOLE.getMessages();

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
        }
    }

    /**
     * A utility method used to send a notification determined by the message service provided.
     *
     * @param player  The target we will send the notification to
     * @param message The message(s) being sent
     */
    @SuppressWarnings("SameParameterValue")
    private void sendNotification(@NotNull Player player, @NotNull DefaultMessageService message) {
        Arrays.stream(message.getMessages()).forEach(m -> player.spigot().sendMessage(m));
    }

    /*
    INTERNAL CLASSES
     */

    /**
     * Handles the default messages sent by the updater when it completes its task
     *
     * @author OMGitzFROST
     */
    private enum DefaultMessageService
    {
        DISABLED("&6The updater is currently disabled, therefore, we will check for updates..."),
        DOWNLOADED(format("&aSuccessfully downloaded (&e{0} v{1}&a), Please install it from your update folder", plugin.getName(), LATEST_VERSION.getVersion())),
        EXISTS(format("&e{0} v{1} &ais already downloaded, Please Check your Update folder and install it", plugin.getName(), LATEST_VERSION.getVersion())),
        LATEST(format("&6No updates were found...")),
        UPDATE_AVAILABLE(
                new TextComponent(StringUtil.repeat('-', 32)),
                new TextComponent(format("&6Version (&f{0}&6) is now available for &f{1}&6.", LATEST_VERSION.getVersion(), plugin.getName())),
                new TextComponent(getLinkBar()),
                new TextComponent(StringUtil.repeat('-', 32))
        ),
        UPDATE_AVAILABLE_CONSOLE(
                StringUtil.repeat('*', 60),
                format("&6Version (&4&l{0}&6) is now available for &4&l{1}&6.", LATEST_VERSION.getVersion(), plugin.getName()),
                provider.getDownloadLink() != null ? "&aDownload: &r" + provider.getDownloadLink() : "",
                provider.getChangelogLink() != null ? "&aChangelog: &r" + provider.getChangelogLink() : "",
                provider.getDonationLink() != null ? "&aDonate: &r" + provider.getDonationLink() : "",
                StringUtil.repeat('*', 60)
        );

        private final BaseComponent[] messages;

        /**
         * Creates a constant that allows components to be accepted as its parameters
         *
         * @param component Message components
         */
        DefaultMessageService(BaseComponent... component) {
            this.messages = Arrays.stream(component).map(TextComponent::new).toArray(BaseComponent[]::new);
        }

        /**
         * Creates a constant that allows strings to be accepted as its parameters,
         * note that these strings will be converted to {@link BaseComponent}.
         *
         * @param input Message inputs
         */
        DefaultMessageService(@NotNull String... input) {
            input = Arrays.stream(input)
                    .filter(s -> !s.isEmpty())
                    .map(s -> ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, s.replace('&', ChatColor.COLOR_CHAR)))
                    .toArray(String[]::new);
            this.messages = Arrays.stream(input).map(TextComponent::new).toArray(BaseComponent[]::new);
        }

        /**
         * Used to return all message string assigned to the provided default message constant
         *
         * @param type Provided constant
         * @return All messages assigned to provided constant
         */
        public static BaseComponent[] getMessages(@NotNull DefaultMessageService type) {
            return type.getMessages();
        }

        /**
         * Returns all messages assigned to this constant as an array
         *
         * @return A messages assigned
         */
        public @NotNull BaseComponent[] getMessages() {
            return messages;
        }

        /**
         * Creates a link bar displayed in chat when updates are available.
         *
         * @return A link bar containing download, changelog links
         */
        private static @NotNull BaseComponent[] getLinkBar() {
            HoverAdapter<HoverEvent> hover = BukkitNMSBridge.adaptHover();
            boolean hasDownloadLink = Objects.requireNonNull(provider).getDownloadLink() != null;
            boolean hasChangeLogLink = Objects.requireNonNull(provider).getChangelogLink() != null;
            boolean hasDonationLink  = Objects.requireNonNull(provider).getDonationLink() != null;
            assert provider != null;

            return new ComponentBuilder()
                    // DOWNLOAD COMPONENT
                    .append(hasDownloadLink, "&aDownload")
                    .setHoverEvent("Click here to download")
                    .setClickEvent(ClickEvent.Action.OPEN_URL, provider.getDownloadLink())
                    .append(hasDownloadLink && hasChangeLogLink, " &a| ")
                    // CHANGELOG COMPONENT
                    .append(hasChangeLogLink, "&aChangelog")
                    .setHoverEvent("Click here to view changelog")
                    .setClickEvent(ClickEvent.Action.OPEN_URL, provider.getChangelogLink())
                    .append((hasDownloadLink | hasChangeLogLink) && hasDonationLink, " &a| ")
                    // DONATE COMPONENT
                    .append(hasDonationLink, "&aDonate")
                    .setHoverEvent("Click here to send donation")
                    .setClickEvent(ClickEvent.Action.OPEN_URL, provider.getDonationLink())
                    .create();
        }

        /**
         * Used to translate color codes for the update components
         *
         * @param input Provided input
         * @param param Optional parameters
         * @return A color formatted string
         */
        private static @NotNull String format(String input, Object... param) {
            input = input.replace('&', ChatColor.COLOR_CHAR);
            return ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, StringUtil.format(input, param));
        }

        // TODO: 6/3/23 ADD JAVADOC
        public static DefaultMessageService valueOf(@NotNull UpdateResult result) {
            return DefaultMessageService.valueOf(result.name());
        }
    }

    /**
     * Primarily used to define the type of audience the {@link #sendNotification(AudienceType, DefaultMessageService)}
     * method should send a message to.
     *
     * @author OMGitzFROST
     */
    private enum AudienceType
    {
        /**
         * Indicates that the audience will be the console
         */
        CONSOLE,
        /**
         * Indicates that the audience will be the players specified by the {@link #audience}
         * method.
         */
        PLAYER,
        /**
         * Indicates that both the console and the {@link #audience} will be the audience
         * being used by the task.
         */
        ALL
    }
}

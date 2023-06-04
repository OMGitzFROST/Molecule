package com.moleculepowered.platform.bungee.user;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.user.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BungeeUser extends BungeeUserData implements User
{
    private final UUID uuid;
    private String name, locale;

    // SETTINGS

    private final String NAME = "name";
    private final String UID = "uuid";
    private final String LOCALE = "locale";

    /*
    CONSTRUCTOR
     */

    /**
     * Creates a new bukkit user derived from a player object; all values assigned to this user will
     * be updated dynamically using the player object.
     *
     * @param plugin Plugin handling this user
     * @param player Provided player
     */
    public BungeeUser(@NotNull MoleculePlugin plugin, @NotNull ProxiedPlayer player) {
        super(plugin, player.getUniqueId());

        // TODO: 5/27/23 UNCOMMENT
//        PlayerAdapter adapter = BukkitNMSBridge.adaptPlayer(player);

        // INITIALIZE SETTINGS SETTINGS
        this.uuid   = player.getUniqueId();
        this.name   = player.getName();
        // TODO: 5/27/23 UNCOMMENT
//        this.locale = adapter.getLocale();
    }

    /**
     * Creates a new bukkit user derived from a UUID; since the uuid does not provide us with
     * defaults, this constructor will attempt to initialize the description objects using
     * the user's data file (if present).
     *
     * @param plugin Plugin handling this user
     * @param uuid Provided uuid
     */
    public BungeeUser(@NotNull MoleculePlugin plugin, @NotNull UUID uuid) {
        super(plugin, uuid);

        // INITIALIZE SETTINGS SETTINGS
        this.uuid   = uuid;
        this.name   = getConfig().getString(NAME);
        this.locale = getConfig().getString(LOCALE);
    }

    /*
    GETTER AND SETTER METHODS
     */

    /**
     * Retrieve the name of assigned to this user object.
     *
     * @return the name of the user
     */
    @Override
    public @NotNull String getName() {
        return getConfig().getString(NAME, name);
    }

    /**
     * Sets the username of the user.
     *
     * @param name the new name for this user
     */
    @Override
    public void setName(@NotNull String name) {
        this.name = name;
        // TODO: 5/27/23 SHOULD IMPLEMENT CUSTOM FUNCTIONALITY
        getConfig().set(NAME, name);

    }

    /**
     * Retrieve the locale assigned to this user object
     *
     * @return the user's locale
     */
    @Override
    public @NotNull String getLocale() {
        return getConfig().getString(LOCALE, locale);
    }

    /**
     * Sets the new locale that should be assigned to this user
     *
     * @param locale target locale
     */
    @Override
    public void setLocale(@NotNull String locale) {
        this.locale = locale;
        // TODO: 5/27/23 SHOULD IMPLEMENT CUSTOM FUNCTIONALITY
        getConfig().set(LOCALE, locale);
    }

    /**
     * Retrieve the unique id assigned to this user.
     *
     * @return the unique id of this user
     */
    @Override
    public @NotNull UUID getUniqueId() {
        return UUID.fromString(getConfig().getString(UID, uuid.toString()));
    }
}

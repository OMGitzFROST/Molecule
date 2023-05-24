package com.moleculepowered.api.user;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.NMSBridge;
import com.moleculepowered.api.adapter.PlayerAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Creates a {@link User} entity for the bukkit platform
 *
 * @author OMGitzFROST
 */
public class BukkitUser extends BukkitUserData implements User {

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
    public BukkitUser(@NotNull MoleculePlugin plugin, @NotNull Player player) {
        super(plugin, player.getUniqueId());

        PlayerAdapter adapter = NMSBridge.adaptPlayer(player);

        // INITIALIZE SETTINGS SETTINGS
        this.uuid   = player.getUniqueId();
        this.name   = player.getName();
        this.locale = adapter.getLocale();
    }

    /**
     * Creates a new bukkit user derived from a UUID; since the uuid does not provide us with
     * defaults, this constructor will attempt to initialize the description objects using
     * the user's data file (if present).
     *
     * @param plugin Plugin handling this user
     * @param uuid Provided uuid
     */
    public BukkitUser(@NotNull MoleculePlugin plugin, @NotNull UUID uuid) {
        super(plugin, uuid);

        // INITIALIZE SETTINGS SETTINGS
        this.uuid   = uuid;
        this.name   = getString(NAME);
        this.locale = getString(LOCALE);
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
        return getString(NAME, name);
    }

    /**
     * Sets the username of the user.
     *
     * @param name the new name for this user
     */
    @Override
    public void setName(@NotNull String name) {
        this.name = name;
        super.set(NAME, name);
    }

    /**
     * Retrieve the locale assigned to this user object
     *
     * @return the user's locale
     */
    @Override
    public @NotNull String getLocale() {
        return getString(LOCALE, locale);
    }

    /**
     * Sets the new locale that should be assigned to this user
     *
     * @param locale target locale
     */
    @Override
    public void setLocale(@NotNull String locale) {
        this.locale = locale;
        super.set(LOCALE, locale);
    }

    /**
     * Retrieve the unique id assigned to this user.
     *
     * @return the unique id of this user
     */
    @Override
    public @NotNull UUID getUniqueId() {
        return UUID.fromString(getString(UID, uuid.toString()));
    }
}

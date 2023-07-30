package com.moleculepowered.platform.bukkit.user;

import com.moleculepowered.api.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * Creates a {@link User} entity for the Bukkit platform.
 *
 * @author OMGitzFROST
 */
public final class BukkitUser extends BukkitUserData implements User
{
    /*
    CONSTRUCTOR
     */

    /**
     * Creates a new Bukkit user based on a player object.
     *
     * <p>All values assigned to this user will be updated dynamically using the player object.</p>
     *
     * @param plugin The plugin handling this user
     * @param player The provided player object
     */
    public BukkitUser(@NotNull Plugin plugin, @NotNull OfflinePlayer player) {
        super(plugin, player);
    }

    /*
    GETTER METHODS
     */

    /**
     * Retrieves the {@link UUID} assigned to this user.
     *
     * @return The {@link UUID} of this user
     */
    @Override
    public @NotNull UUID getUniqueId() {
        return UUID.fromString(Objects.requireNonNull(getData("uuid")));
    }

    /**
     * Retrieves the name assigned to this user object.
     *
     * @return The name of the user
     */
    @Override
    public @NotNull String getName() {
        return Objects.requireNonNull(getData("name"));
    }

    /**
     * Retrieves the display name assigned to this user object.
     *
     * <p>Please note that this value may be different from what is listed for their name.</p>
     *
     * @return The display name of the user
     */
    @Override
    public @Nullable String getDisplayName() {
        return getData("display-name");
    }

    /**
     * Retrieves the custom name assigned to this user object.
     *
     * <p>Please note that this value may be different from what is listed as their name.</p>
     *
     * @return The custom name of the user
     */
    @Override
    public @Nullable String getCustomName() {
        return getData("custom-name");
    }

    /**
     * Retrieves the locale assigned to this user object.
     *
     * @return The locale of the user
     */
    @Override
    public @Nullable String getLocale() {
        return getData("locale");
    }

    /*
    SETTER METHODS
     */

    /**
     * Sets the new display name that should be assigned to this user.
     *
     * @param input The target display name
     */
    @Override
    public void setDisplayName(@Nullable String input) {
        setData("display-name", input);
    }

    /**
     * Sets the new custom name that should be assigned to this user.
     *
     * @param input The target custom name
     */
    @Override
    public void setCustomName(@Nullable String input) {
        setData("custom-name", input);
    }

    /**
     * Sets the new locale that should be assigned to this user.
     *
     * @param input The target locale
     */
    @Override
    public void setLocale(@NotNull String input) {
        setData("locale", input);
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public @Nullable String toString() {
        return getData().toString();
    }
}

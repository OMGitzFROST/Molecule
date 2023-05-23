package com.moleculepowered.api.user;

import com.moleculepowered.api.MoleculePlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitUser extends BukkitUserData implements User {

    private UUID uuid;
    private String name;

    /*
    PATHS TO SETTINGS
     */

    private final String NAME = "name";
    private final String UUID = "uuid";

    public BukkitUser(@NotNull MoleculePlugin plugin, @NotNull Player player) {
        super(plugin, player.getUniqueId());

        this.name = player.getName();
        this.uuid = player.getUniqueId();
    }

    public BukkitUser(@NotNull MoleculePlugin plugin, @NotNull UUID uuid) {
        super(plugin, uuid);
    }

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
     * Retrieve the unique id assigned to this user.
     *
     * @return the unique id of this user
     */
    @Override
    public @NotNull UUID getUniqueId() {
        return java.util.UUID.fromString(getString(UUID, uuid.toString()));
    }
}

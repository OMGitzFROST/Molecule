package com.moleculepowered.api.user;

import com.moleculepowered.api.MoleculePlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class BukkitUserManager implements UserManager {

    private final MoleculePlugin plugin;
    private final Set<User> users = new HashSet<>();

    public BukkitUserManager(MoleculePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method is typically used to perform necessary setup tasks for your plugin, such as
     * registering event listeners, initializing configurations, setting up database connections,
     * or starting scheduled tasks. This method should be called when the server loads your plugin and
     * is usually where you would initialize any resources or functionality needed for your plugin to
     * operate correctly.
     */
    @Override
    public void onEnable() {
        Arrays.stream(Objects.requireNonNull(plugin.getUserDataFolder().listFiles())).forEach(file -> {
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            users.add(new BukkitUser(plugin, uuid));
        });
    }

    /**
     * Adds a new user to the user collection, this method will return true
     * if the user was added without failure, otherwise it will return false.
     *
     * @param user Target user
     * @return true if the user was added without fail
     */
    @Override
    public boolean addUser(@NotNull User user) {
        return users.add(user);
    }

    /**
     * Removes a new user from the user collection, this method will return true
     * if the user was removed without failure, otherwise it will return false.
     *
     * @param user Target user
     * @return true if the user was removed without fail
     */
    @Override
    public boolean removeUser(@NotNull User user) {
        return users.remove(user);
    }

    /**
     * Returns a collection of users handled by this manager. Note that users are NOT
     * loaded into this collection by default but should typically be loaded using the
     * {@link #onEnable()} method.
     *
     * @return A collection of users
     */
    @Override
    public @NotNull Collection<User> getUsers() {
        return users;
    }
}

package com.moleculepowered.platform.bungee.user;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.user.UserManager;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

// TODO: 5/27/23 JAVADOC
public class BungeeUserManager extends UserManager
{
    private final MoleculePlugin plugin;

    // TODO: 5/27/23 JAVADOC
    public BungeeUserManager(MoleculePlugin plugin) {
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

        // ENSURE USER DATA FOLDER EXISTS BEFORE ANYTHING
        if (!plugin.getUserDataFolder().exists() && !plugin.getUserDataFolder().mkdirs()) {
            throw new IllegalArgumentException("Failed to create user data folder");
        }

        Arrays.stream(Objects.requireNonNull(plugin.getUserDataFolder().listFiles())).forEach(file -> {
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            users.add(new BungeeUser(plugin, uuid));
        });
    }
}

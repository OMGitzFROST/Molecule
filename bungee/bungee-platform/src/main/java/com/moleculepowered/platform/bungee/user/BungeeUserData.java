package com.moleculepowered.platform.bungee.user;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.user.UserData;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Creates a user data class for the bukkit platform, it utilizes the {@link YamlConfiguration} class
 * as it's storage system. It applies custom uses for superseded methods from the parent class.
 *
 * @author OMGitzFROST
 */
public class BungeeUserData implements UserData<Configuration> {

    protected final MoleculePlugin plugin;
    private final File userFile;
    private final InputStream RESOURCE;

    /*
    CONSTRUCTORS
     */

    /**
     * Creates a new instance of a user's {@link UserData}, utilizing the plugin handling this data, as
     * well as the user's unique id, please note that by default this method requires a default user template
     * named "user.yml" within your jar's resource directory.
     *
     * @param plugin Plugin handling the data
     * @param uuid   Target unique id
     */
    public BungeeUserData(@NotNull MoleculePlugin plugin, @NotNull UUID uuid) {
        this(plugin, "user.yml", uuid);
    }

    /**
     * Creates a new instance of a user's {@link UserData}, utilizing the plugin handling this data,
     * as well as the user's unique id, please note that the resource path assigned to this constructor
     * points to the path where your user template file is located, we will use this when creating new user
     * files.
     *
     * @param plugin       Plugin handling the data
     * @param resourcePath Path to internal resource
     * @param uuid         Target unique id
     */
    public BungeeUserData(@NotNull MoleculePlugin plugin, @NotNull String resourcePath, @NotNull UUID uuid) {
        this.plugin = plugin;
        this.userFile = new File(plugin.getUserDataFolder(), uuid + ".yml");
        RESOURCE = plugin.getResource(resourcePath);

    }

    /*
    CORE FUNCTIONALITY
     */

    /**
     * Creates a new user data file if one does not already exist
     *
     * @throws IOException when this method fails to create user data file
     */
    @Override
    public void create() throws IOException {

        // ENSURE DATA FOLDER EXISTS
        if (!userFile.getParentFile().exists() && !userFile.mkdirs()) {
            throw new IllegalArgumentException("Failed to create user data folder");
        }

        // CREATE THE FILE IF ONE DOES NOT EXIST
        if (!userFile.exists()) Files.copy(RESOURCE, userFile.toPath());
    }

    /**
     * Deletes a users data file if one exists, otherwise this method will do nothing.
     */
    @Override
    public void delete() {
        if (userFile.exists() && !userFile.delete()) {
            throw new IllegalArgumentException("Failed to delete user data file: " + userFile.getPath());
        }
    }

    /**
     * Attempts to update a user's data file, usually this method adds new keys to the configuration
     * and attempts to re-add removed comments if applicable.
     *
     * @throws IOException when this method fails to update a user's data file
     */
    @Override
    public void update() throws IOException {

    }

    /*
    GETTER METHODS
     */

    /**
     * Return the data file associated with this user
     *
     * @return User data file
     */
    @Override
    public @NotNull File getFile() {
        return userFile;
    }

    /**
     * Returns the data folder where this user's data is stored
     *
     * @return User data folder
     */
    @Override
    public @NotNull File getDataFolder() {
        return null;
    }

    @Override
    public @NotNull Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(userFile);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to return the bungee configuration object");
        }
    }
}

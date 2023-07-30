package com.moleculepowered.platform.bukkit.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.user.UserDeleteException;
import com.moleculepowered.api.user.UserData;
import com.moleculepowered.platform.bukkit.adapter.PlayerAdapter;
import com.moleculepowered.platform.bukkit.event.user.UserCreatedEvent;
import com.moleculepowered.platform.bukkit.event.user.UserDeletedEvent;
import com.moleculepowered.platform.bukkit.model.BukkitNMSBridge;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Creates a user data class for the bukkit platform, it utilizes the {@link YamlConfiguration} class
 * as it's storage system. It applies custom uses for superseded methods from the parent class.
 *
 * @author OMGitzFROST
 */
public class BukkitUserData implements UserData
{
    // DATA OBJECTS
    private final Plugin plugin;
    private final Gson gson;
    private final File userFile;
    private JsonObject config;

    // USER INFORMATION
    private String name, displayName, customName, locale;
    private UUID uuid;

    /*
    CONSTRUCTORS
     */

    /**
     * <p>The main constructor for the {@link BukkitUserData} class.</p>
     *
     * <p>It's primarily used to initialize all required objects and collect
     * information from the originating player, this information will be stored
     * into their respective user file to be later retrieved other plugin's</p>
     *
     * @param plugin Parent plugin
     * @param player Originating player
     */
    public BukkitUserData(@NotNull Plugin plugin, @NotNull OfflinePlayer player) {

        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        this.userFile = new File(plugin.getDataFolder() + "/user-data", uuid + ".json");

        // INITIALIZE STATIC USER INFORMATION
        this.uuid = player.getUniqueId();
        this.name = player.getName();

        // IF PLAYER IS ONLINE, SET ONLINE SPECIFIC INFORMATION
        if (player.getPlayer() != null || player instanceof Player) {
            PlayerAdapter adapter = BukkitNMSBridge.adaptPlayer((Player) player);
            this.displayName = ((Player) player).getDisplayName();
            this.customName = ((Player) player).getCustomName();
            this.locale = adapter.getLocale();
        }
        create();
    }

    /**
     * <p>This method attempts to create a new user data file.</p>
     *
     * <p>By default, this method will not create a user file if one already exists in the
     * {@link #getDataFolder()}, otherwise this method will create one as usual.
     */
    @Override
    public void create() {
        try {
            if (!getDataFolder().exists() && !getDataFolder().mkdirs())
                throw new IllegalArgumentException("Unable to create user data folder");

            // ATTEMPT TO CREATE USER FILE IF ONE DOES NOT EXIST, OR IS NULL
            if (!userFile.exists() || (config != null && config.isJsonNull())) {
                storeDefaults();

                UserCreatedEvent event = new UserCreatedEvent();
                plugin.getServer().getPluginManager().callEvent(event);
            }

            // LOAD USER SETTINGS ONE FILE IS CONFIRMED TO EXIST
            config = gson.fromJson(new FileReader(userFile), JsonObject.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a user's data file if one exists, otherwise this method will do nothing.
     */
    @Override
    public void delete() {

        // ATTEMPT TO DELETE USER FILE, IF SUCCESSFULL CALL EVENT
        if (userFile.delete()) {
            UserDeletedEvent event = new UserDeletedEvent();
            plugin.getServer().getPluginManager().callEvent(event);
            return;
        }
        throw new UserDeleteException("An error has occurred when trying to delete {0}'s users file", name);
    }

    /**
     * Attempts to update a user's data file, usually this method adds new keys to the configuration
     * and attempts to re-add removed comments if applicable.
     *
     * @param target Originating player
     */
    @Override
    public <T> void update(T target) {

        if (!(target instanceof OfflinePlayer))
            throw new IllegalArgumentException("In-order to update this user-data, you must provide a valid OfflinePlayer object");

        try {
            OfflinePlayer player = (OfflinePlayer) target;

            // INITIALIZE STATIC USER INFORMATION
            this.uuid = player.getUniqueId();
            this.name = player.getName();

            // IF PLAYER IS ONLINE, SET ONLINE SPECIFIC INFORMATION
            if (player.getPlayer() != null || player instanceof Player) {
                PlayerAdapter adapter = BukkitNMSBridge.adaptPlayer((Player) player);
                this.displayName = ((Player) player).getDisplayName();
                this.customName = ((Player) player).getCustomName();
                this.locale = adapter.getLocale();
            }

            storeDefaults();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stores the default information gathered in the constructor into the user's data file.
     * This method saves the user's data for future retrieval and usage.
     *
     * @throws IOException if an I/O error occurs while storing the information into the data file.
     *                     This can happen if the data file is inaccessible or the storage operation fails.
     *                     The exception message provides more specific details about the error.
     */
    private void storeDefaults() throws IOException {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", uuid.toString());
        object.addProperty("name", name);
        object.addProperty("display-name", displayName);
        object.addProperty("custom-name", customName);
        object.addProperty("locale", locale);

        // SAVE DATA TO USER FILE
        FileWriter writer = new FileWriter(userFile);
        gson.toJson(object, writer);
        writer.close();
    }

    /**
     * Adds or updates existing data with a new value.
     *
     * <p>Note: The value provided must follow strict guidelines. It must be one of the following data types:
     * {@link String}, {@link Number}, {@link Boolean}, {@link Character}, or {@link JsonElement}.
     * Otherwise, this method will throw an {@link IllegalArgumentException}.</p>
     *
     * @param key   the target key for the data
     * @param value the target value to be added or updated
     * @throws IllegalArgumentException if the provided value is not supported by the method
     */
    @Override
    public void setData(@NotNull String key, @Nullable Object value) {
        try {
            JsonObject oldObject = getData();

            if (value instanceof JsonElement) oldObject.add(key, (JsonElement) value);
            else oldObject.addProperty(key, String.valueOf(value));

            FileWriter writer = new FileWriter(userFile);
            gson.toJson(oldObject, writer);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves data from a user's data file. If the data key does not exist inside the file,
     * the provided default value will be returned.
     *
     * @param key Data key used to retrieve the data
     * @param def Default value to be returned if the key is not found
     * @return The data assigned to the provided key, or the default value if the key is not found
     */
    @Override
    public @Nullable String getData(@NotNull String key, @Nullable String def) {
        if (config == null) create();
        return getData().get(key) != null && !getData().get(key).isJsonNull() ? getData().get(key).getAsString() : def;
    }

    /**
     * Returns the configuration assigned to this user. The behavior of this method, including autoload
     * features, may vary between platforms.
     *
     * @return The user's configuration as a {@link JsonObject}.
     */
    @Override
    public @NotNull JsonObject getData() {
        return config;
    }

    /**
     * Returns the data file associated with this user.
     *
     * @return The user's data file as a {@link File} object.
     */
    @Override
    public @NotNull File getFile() {
        return userFile;
    }
}

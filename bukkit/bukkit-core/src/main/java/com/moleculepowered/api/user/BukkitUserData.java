package com.moleculepowered.api.user;

import com.moleculepowered.api.MoleculePlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class BukkitUserData extends YamlConfiguration implements UserData {

    private final File userFile;

    public BukkitUserData(@NotNull MoleculePlugin plugin, UUID uuid) {
        super();
        this.userFile = new File(plugin.getUserDataFolder(), uuid + ".yml");
    }

    /**
     * A method used to add the new value into the user's data file.
     *
     * @param path  Path to data
     * @param value Updated value
     */
    @Override
    public void set(String path, Object value) {
        super.set(path, value);
        // TODO: 5/22/23 ADD EVENTS, AND FORCE UPDATE TO FILE
    }

    /**
     * Return the data file associated with this user
     *
     * @return User data file
     */
    @Override
    public @NotNull File getFile() {
        return userFile;
    }
}

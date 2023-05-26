package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.adapter.ConfigAdapter;
import com.moleculepowered.api.adapter.PlayerAdapter;
import com.moleculepowered.api.model.NMSBridge;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * <p>A utility class designed to build and return classes that enable your plugin to work
 * across multiple server versions. This class uses Java's reflection api to achieve this outcome.</p>
 *
 * @author OMGitzFROST
 */
public final class BukkitNMSBridge implements NMSBridge {

    private static BukkitNMSBridge instance;

    /*
    ADAPTING METHODS
     */

    /**
     * Returns a {@link PlayerAdapter} bound to the provided player object
     *
     * @param player Provided player
     * @return A bounded {@link PlayerAdapter}
     */
    public static @NotNull PlayerAdapter adaptPlayer(@NotNull Player player) {
        try {
            Class<?> classDefinition = Class.forName("com.moleculepowered.platform.bukkit.PlayerAdapter_" + getInstance().getServerVersion());
            Constructor<?> cons = classDefinition.getConstructor(Player.class);
            return (PlayerAdapter) cons.newInstance(player);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create player adapter using the provided player object", ex);
        }
    }

    /**
     * Returns a {@link ConfigAdapter} bound to the provided configuration object
     *
     * @param config Provided configuration
     * @return A bounded {@link ConfigAdapter}
     */
    public static @NotNull ConfigAdapter adaptConfig(@NotNull FileConfiguration config) {
        try {
            Class<?> classDefinition = Class.forName(instance.getPackage("platform.bukkit.ConfigAdapter", instance.getServerVersion()));
            Constructor<?> cons = classDefinition.getConstructor(FileConfiguration.class);
            return (ConfigAdapter) cons.newInstance(config);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create config adapter using the provided config object", ex);
        }
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the version number that will difference nms classes, for example, "v1_8_R3"
     *
     * @return Server version
     */
    public @Nullable String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static @NotNull NMSBridge getInstance() {
        if (instance == null) instance = new BukkitNMSBridge();
        return Objects.requireNonNull(instance);
    }
}

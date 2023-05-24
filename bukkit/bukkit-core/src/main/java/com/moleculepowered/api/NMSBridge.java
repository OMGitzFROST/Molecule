package com.moleculepowered.api;

import com.moleculepowered.api.adapter.ConfigAdapter;
import com.moleculepowered.api.adapter.PlayerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>A utility class designed to build and return classes that enable your plugin to work
 * across multiple server versions. This class uses Java's reflection api to achieve this outcome.</p>
 *
 * @author OMGitzFROST
 */
public final class NMSBridge {

    /*
    ADAPTER METHODS
     */

    /**
     * Returns a {@link PlayerAdapter} bound to the provided player object
     *
     * @param player Provided player
     * @return A bounded {@link PlayerAdapter}
     */
    public static @NotNull PlayerAdapter adaptPlayer(@NotNull Player player) {
        try {
            Class<?> classDefinition = Class.forName(getPackage(PlayerAdapter.class));
            Constructor<?> cons = classDefinition.getConstructor(player.getClass());
            return (PlayerAdapter) cons.newInstance(player);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create player adapter using the provided player object");
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
            Class<?> classDefinition = Class.forName(getPackage(ConfigAdapter.class));
            Constructor<?> cons = classDefinition.getConstructor(config.getClass());
            return (ConfigAdapter) cons.newInstance(config);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create config adapter using the provided config object");
        }
    }

    /*
    UTILITY CLASSES
     */

    /**
     * <p>A utility class used to return the corresponding package based on on the class provided and
     * the current server version.</p>
     *
     * <p>For example, assuming your server version is 1.19.4 and the target class is {@link PlayerAdapter}
     * this method would return the package "com.moleculepowered.api.PlayerAdapter_v1_19_R3"</p>
     *
     * @param clazz Target class
     * @return A package pointing to a class version
     */
    private static @NotNull String getPackage(@NotNull Class<?> clazz) {
        return getPackage(clazz.getSimpleName());
    }

    /**
     * <p>A utility class used to return the corresponding package based on on the class provided and
     * the current server version.</p>
     *
     * <p>For example, assuming your server version is 1.19.4 and the target class is {@link PlayerAdapter}
     * this method would return the package "com.moleculepowered.api.PlayerAdapter_v1_19_R3"</p>
     *
     * @param className Target class name
     * @return A package pointing to a class version
     */
    private static @NotNull String getPackage(@NotNull String className) {
        String rawV = Bukkit.getServer().getClass().getPackage().getName();
        String finalV = rawV.substring(rawV.lastIndexOf('.') + 1);
        return "com.moleculepowered.api." + className + "_" + finalV;
    }
}

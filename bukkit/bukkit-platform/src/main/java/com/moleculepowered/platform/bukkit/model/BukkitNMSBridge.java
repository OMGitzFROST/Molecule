package com.moleculepowered.platform.bukkit.model;

import com.moleculepowered.platform.bukkit.adapter.HoverAdapter;
import com.moleculepowered.platform.bukkit.adapter.PlayerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * A utility class designed to build and return classes that enable your plugin to work
 * across multiple server versions. This class uses Java's reflection API to achieve this outcome.
 *
 * @author OMGitzFROST
 */
public final class BukkitNMSBridge
{
    private static final String packagePrefix = "com.moleculepowered.platform.bukkit.";

    /*
    ADAPTING METHODS
     */

    /**
     * Returns a {@link PlayerAdapter} bound to the provided player object.
     *
     * @param player the provided player
     * @return a bounded {@link PlayerAdapter}
     */
    public static @NotNull PlayerAdapter adaptPlayer(@NotNull Player player) {
        try {
            Class<?> classDefinition = Class.forName(packagePrefix + getServerVersion() + ".adapter.PlayerAdapter");
            Constructor<?> cons = classDefinition.getConstructor(Player.class);
            return (PlayerAdapter) cons.newInstance(player);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create a player adapter using the provided player object", ex);
        }
    }

    /**
     * Returns a {@link HoverAdapter} bound for the current server implementation.
     *
     * @return a bounded {@link HoverAdapter}
     */
    public static @NotNull HoverAdapter adaptHover() {
        try {
            Class<?> clazz = Class.forName(packagePrefix + getServerVersion() + ".adapter.HoverAdapter");
            Constructor<?> cons = clazz.getConstructor();
            return (HoverAdapter) cons.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create a hover adapter using the provided config object", ex);
        }
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the version number that will differentiate NMS classes, for example, "v1_8_R3".
     *
     * @return the server version
     */
    public static @Nullable String getServerVersion() {
        return ServerVersion.parse(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
    }

    /**
     * A private enum used to handle parsing server versions and retrieving them.
     */
    private enum ServerVersion
    {

        v1_20_R1,
        v1_19_R3,
        v1_18_R2,
        v1_17_R1,
        v1_16_R3,
        v1_15_R1,
        v1_14_R1,
        v1_13_R2,
        v1_12_R1,
        v1_11_R1,
        v1_10_R1,
        v1_9_R3,
        v1_8_R3;

        /**
         * Parses a {@link ServerVersion} from the version string provided
         *
         * @param input Target version string
         * @return A {@link ServerVersion}
         * @throws IllegalArgumentException When a server version failed to parse
         */
        public static String parse(String input) {
            if (input == null) return null;

            String adjustedVersion = input.replace(input.substring(input.lastIndexOf("_")), "");
            return Arrays.stream(ServerVersion.values())
                    .filter(version -> version.name().contains(adjustedVersion))
                    .findFirst()
                    .map(ServerVersion::name)
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
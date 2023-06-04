package com.moleculepowered.platform.bukkit.model;

import com.moleculepowered.api.adapter.HoverAdapter;
import com.moleculepowered.api.adapter.PlayerAdapter;
import com.moleculepowered.api.model.NMSBridge;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
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
public final class BukkitNMSBridge implements NMSBridge
{
    private static final String packagePrefix = "com.moleculepowered.platform.bukkit.";
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
            Class<?> classDefinition = Class.forName(packagePrefix + getServerVersion() + ".adapter.PlayerAdapter");
            Constructor<?> cons = classDefinition.getConstructor(Player.class);
            return (PlayerAdapter) cons.newInstance(player);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create player adapter using the provided player object", ex);
        }
    }

    /**
     * Returns a {@link HoverAdapter} bounded for the current server implementation
     *
     * @return A bounded {@link HoverAdapter}
     */
    @SuppressWarnings("unchecked")
    public static @NotNull HoverAdapter<HoverEvent> adaptHover() {
        try {
            Class<?> clazz = Class.forName(packagePrefix + getServerVersion() + ".adapter.HoverAdapter");
            Constructor<?> cons = clazz.getConstructor();
            return (HoverAdapter<HoverEvent>) cons.newInstance();
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to create hover adapter using the provided config object", ex);
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
    public static @Nullable String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    /**
     * Returns an instance of the {@link BukkitNMSBridge} class
     *
     * @return Class instance
     */
    private static @NotNull NMSBridge getInstance() {
        if (instance == null) instance = new BukkitNMSBridge();
        return Objects.requireNonNull(instance);
    }
}

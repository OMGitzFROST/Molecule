package com.moleculepowered.api.platform;

import com.moleculepowered.api.util.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * An interface used to provide necessary methods required by each platform type
 *
 * @author OMGitzFROST
 */
public interface Platform
{
    /**
     * Returns the data folder assigned to your plugin
     *
     * @return Plugin data folder
     */
    File getDataFolder();

    /**
     * Returns a {@link ComparableVersion} number for this plugin. This method allows
     * you to compare two version numbers to identify if they are greater than, less than
     * or equal to each other.
     *
     * @return A comparable version
     */
    @NotNull ComparableVersion getVersion();

    /**
     * Returns the platform that the plugin is implementation on.
     *
     * @return Implemented platform
     */
    @Nullable Type getPlatform();

    /**
     * An enum that defines the different types of supported platforms used by Molecule, it provides
     * simple methods that allow you to interact with each platform type.
     *
     * @author OMGitzFROST
     */
    enum Type
    {
        BUKKIT("Bukkit"),
        BUNGEE("Bungee"),
        PAPER("Paper"),
        SPONGE("Sponge"),
        VELOCITY("Velocity"),
        WATERFALL("Waterfall");

        private final String name;

        /**
         * The main constructor used to assign a readable name to each constant
         *
         * @param name Platform name
         */
        Type(String name) {
            this.name = name;
        }

        /**
         * Returns a human-readable name for this platform
         *
         * @return Readable name
         */
        public String getName() {
            return name;
        }
    }
}

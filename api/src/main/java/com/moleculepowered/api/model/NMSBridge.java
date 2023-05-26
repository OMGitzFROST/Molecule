package com.moleculepowered.api.model;

import com.moleculepowered.api.adapter.PlayerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NMSBridge {

    /**
     * Returns the server version number that will difference nms classes, for example, "v1_8_R3"
     *
     * @return Server version
     */
    @Nullable String getServerVersion();

    /*
    DEFAULT METHODS
     */

    /**
     * <p>A utility class used to return the corresponding package based on on the class provided and
     * the current server version.</p>
     *
     * <p>For example, assuming your server version is 1.19.4 and the target class is {@link PlayerAdapter}
     * this method would return the package "com.moleculepowered.PlayerAdapter_v1_19_R3"</p>
     *
     * @param clazz Target class
     * @param version Target version
     * @return A package pointing to a class version
     */
    default @NotNull String getPackage(@NotNull Class<?> clazz, @Nullable String version) {
        return getPackage(clazz.getSimpleName(), version);
    }

    /**
     * <p>A utility class used to return the corresponding package based on on the class provided and
     * the current server version.</p>
     *
     * <p>For example, assuming your server version is 1.19.4 and the target class is {@link PlayerAdapter}
     * this method would return the package "com.moleculepowered.PlayerAdapter_v1_19_R3"</p>
     *
     * @param clazz Target class
     * @return A package pointing to a class version
     */
    default @NotNull String getPackage(@NotNull Class<?> clazz) {
        return getPackage(clazz.getSimpleName(), null);
    }

    /**
     * <p>A utility class used to return the corresponding package based on on the class provided and
     * the current server version. Please note that this class presets the package prefix to
     * "com.moleculepowered", the rest of the package path has to be defined by you.</p>
     *
     * <p>For example, assuming your server version is 1.19.4 and the target class is {@link PlayerAdapter}
     * this method would return the package "com.moleculepowered.PlayerAdapter_v1_19_R3"</p>
     *
     * @param className Target class name
     * @param version Target version
     * @return A package pointing to a class version
     */
    default @NotNull String getPackage(@NotNull String className, @Nullable String version) {
        return "com.moleculepowered." + className + (version != null && !version.isEmpty() ? "_" + version : "");
    }

    /**
     * <p>A utility class used to return the corresponding package based on on the class provided and
     * the current server version.</p>
     *
     * <p>For example, assuming your server version is 1.19.4 and the target class is {@link PlayerAdapter}
     * this method would return the package "com.moleculepowered.PlayerAdapter_v1_19_R3"</p>
     *
     * @param className Target class name
     * @return A package pointing to a class version
     */
    default @NotNull String getPackage(@NotNull String className) {
        return getPackage(className, null);
    }
}

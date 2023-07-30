package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * A utility class for file-related operations.
 *
 * @author OMGitzFROST
 */
public final class FileUtil
{

    /**
     * Returns the file name from a string input, including its file extension.
     *
     * @param input the provided input
     * @return the file name from the string path
     */
    public static @NotNull String getFileName(@NotNull String input) {
        return getFileName(Paths.get(input));
    }

    /**
     * Returns the file name from a file input, including its file extension.
     *
     * @param input the provided input
     * @return the file name from the file path
     */
    public static @NotNull String getFileName(@NotNull File input) {
        return getFileName(input.toPath());
    }

    /**
     * Returns the file name from a path input, including its file extension.
     *
     * @param input the provided input
     * @return the file name from the path
     */
    public static @NotNull String getFileName(@NotNull Path input) {
        return input.getFileName().toString();
    }

    /*
    RESOURCE HANDLING
     */

    /**
     * Copies a resource from an input stream to the requested file location. By default, this method
     * does not replace an existing file.
     *
     * @param in       the target input stream
     * @param location the target location
     * @see #copy(InputStream, File, boolean)
     */
    public static void copy(@NotNull InputStream in, @NotNull File location) {
        copy(in, location, false);
    }

    /**
     * Copies a resource from an input stream to the requested file location. This method allows
     * you to specify whether existing files should be replaced or not.
     *
     * @param in       the target input stream
     * @param location the target location
     * @param replace  whether existing files should be replaced
     * @see #copy(InputStream, File)
     */
    public static void copy(@NotNull InputStream in, @NotNull File location, boolean replace) {
        try {
            if (!location.getParentFile().exists() && !location.getParentFile().mkdirs()) {
                throw new IOException("Failed to create parent directory for " + location.getName());
            }

            if (replace) {
                Files.copy(in, location.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(in, location.toPath());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves an internal resource to the specified location. By default, this method overwrites
     * existing files.
     *
     * @param input    the target resource path
     * @param location the target location
     */
    public static void saveResource(InputStream input, File location) {
        saveResource(input, location, true);
    }

    /**
     * Saves an internal resource to the specified location. You can specify whether to overwrite
     * an existing file by setting the boolean parameter to true, otherwise set it to false.
     *
     * @param input    the target input stream
     * @param location the target location
     * @param replace  whether to overwrite existing files
     */
    public static void saveResource(InputStream input, File location, boolean replace) {
        copy(input, location, replace);
    }
}
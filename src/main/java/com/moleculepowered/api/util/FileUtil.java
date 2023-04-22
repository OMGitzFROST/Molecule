package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    /**
     * Used to return the file name from a string input and will include its file extension
     *
     * @param input Provided input
     * @return The file name from a string path
     */
    public static @NotNull String getFileName(String input) {
        return getFileName(Paths.get(input));
    }

    /**
     * Used to return the file name from a path input and will include its file extension
     *
     * @param input Provided input
     * @return The file name from a string path
     */
    public static @NotNull String getFileName(@NotNull Path input) {
        return input.getFileName().toString();
    }
}

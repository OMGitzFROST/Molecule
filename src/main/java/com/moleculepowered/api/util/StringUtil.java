package com.moleculepowered.api.util;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;

public final class StringUtil {

    /**
     * Formats a string to include the optional parameters within its formatted self. This method
     * utilizes number formats such as "{0}" within the string in order to replace it with its
     * assigned value.
     *
     * @param input Provided input
     * @param param Optional parameters
     * @return A formatted string
     */
    public static @NotNull String format(String input, Object... param) {
        return MessageFormat.format(input, param);
    }

    /**
     * Used to return a non-null string, this method accomplishes this by checking if the original input
     * is null, and if so, this method will return the default value provided.
     *
     * @param input Provided input
     * @param def   Fallback value
     * @return A non-null string
     */
    public static @NotNull String nonNull(@Nullable String input, @NotNull String def) {
        return input != null ? input : def;
    }

    /**
     * Used to return a non-null string, this method accomplishes this by checking if the original input
     * is null, and if so, this method will return an empty string.
     *
     * @param input Provided input
     * @return A non-null string
     */
    public static @NotNull String nonNull(@Nullable String input) {
        return nonNull(input, "");
    }

    /**
     * Used to return a non-null string, this method accomplishes this by checking if the original input
     * is null, and if so, this method will return an empty string.
     *
     * @param element Provided {@link JsonElement}
     * @return A non-null string
     */
    public static @NotNull String nonNull(JsonElement element, String def) {
        return element != null ? element.getAsString() : def;
    }

    /**
     * Used to return a non-null string, this method accomplishes this by checking if the original input
     * is null, and if so, this method will return an empty string.
     *
     * @param element Provided {@link JsonElement}
     * @return A non-null string
     */
    public static @NotNull String nonNull(JsonElement element) {
        return nonNull(element, "");
    }
}

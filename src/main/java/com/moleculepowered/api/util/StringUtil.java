package com.moleculepowered.api.util;

import com.google.gson.JsonElement;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;

public final class StringUtil {

    /*
    FORMATTING METHODS
     */

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
        return translateColor(MessageFormat.format(input, param));
    }

    /**
     * Used to return a formatted string with color codes removed. This method
     * utilizes number formats such as "{0}" within the string in order to replace it with its
     * assigned value.
     *
     * @param strip Whether color codes should be stripped
     * @param input Provided input
     * @param param Optional Params
     * @return A formatted, colorless string
     */
    public static @NotNull String format(boolean strip, String input, Object... param) {
        return strip ? stripColor(format(input, param)) : format(input, param);
    }

    /**
     * Returns a colorized string, this method normalizes the string by replacing all instances of
     * the '§' char with a '&' char.
     *
     * @param input Provided input
     * @return A color formatted string
     */
    public static @NotNull String translateColor(@Nullable String input) {
        input = StringUtil.nonNull(input).replace(ChatColor.COLOR_CHAR, '&');
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * Returns a string that has all color codes removed from its content.
     *
     * @param input Provided input
     * @return A colorless string
     */
    public static String stripColor(String input) {
        return ChatColor.stripColor(input);
    }

    /*
    ENSURING METHODS
     */

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

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
     * Strips the provided input from all color codes, it normalizes the string by replacing
     * all "&" color codes with "§" in-order to properly strip them.
     *
     * @param input Provided input
     * @return a colorless string
     */
    public static @NotNull String stripColor(@Nullable String input) {
        input = StringUtil.nonNull(input, "null").replace('&', ChatColor.COLOR_CHAR);
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

    /*
    SUBSTRING METHODS
     */

    /**
     * Returns the substring from the last instance of the str.
     *
     * @param input  Provided input
     * @param str    Substring reference
     * @param offset Offset for substring
     * @return a substring derived from the main input.
     * @see #lastIndex(String, String)
     */
    public static @NotNull String lastIndex(@NotNull String input, String str, int offset) {
        int index = input.lastIndexOf(str) + offset;
        return input.substring(index);
    }

    /**
     * Returns the substring from the last instance of the str.
     *
     * @param input Provided input
     * @param str   Substring reference
     * @return a substring derived from the main input.
     * @see #lastIndex(String, String, int)
     */
    public static @NotNull String lastIndex(@NotNull String input, String str) {
        return lastIndex(input, str, 0);
    }

    /**
     * Returns the substring from the last instance of the str.
     *
     * @param input  Provided input
     * @param str    Substring reference
     * @param offset Offset for substring
     * @return a substring derived from the main input.
     * @see #lastIndex(String, String)
     */
    public static @NotNull String substring(@NotNull String input, String str, int offset) {
        int index = input.indexOf(str) + offset;
        return input.substring(index);
    }

    /**
     * Returns the substring from the last instance of the str.
     *
     * @param input Provided input
     * @param str   Substring reference
     * @return a substring derived from the main input.
     * @see #lastIndex(String, String, int)
     */
    public static @NotNull String substring(@NotNull String input, String str) {
        return substring(input, str, 0);
    }

    /*
    REPEAT METHODS
     */

    /**
     * Builds a string with the string repeated up to the defined quantity
     *
     * @param s        Desired string
     * @param quantity Desired number of repeats
     * @return A new string with the character repeated
     */
    public static @NotNull String repeat(String s, int quantity) {
        int currentIndex = 0;
        StringBuilder builder = new StringBuilder();

        while (currentIndex < quantity) {
            builder.append(s);
            currentIndex++;
        }
        return builder.toString();
    }

    /**
     * Builds a string with the character repeated up to the defined quantity
     *
     * @param s        Desired string
     * @param quantity Desired number of repeats
     * @return A new string with the character repeated
     */
    public static @NotNull String repeat(char s, int quantity) {
        return repeat(String.valueOf(s), quantity);
    }
}

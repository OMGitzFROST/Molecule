package com.moleculepowered.api.util;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for String-related operations and manipulations.
 *
 * @author OMGitzFROST
 */
public class StringUtil
{
    /*
    FORMATTING METHODS
     */

    /**
     * Formats a string to include the optional parameters within its formatted self. This method
     * utilizes number formats such as "{0}" within the string in order to replace it with its
     * assigned value.
     *
     * @param input the provided input
     * @param param optional parameters
     * @return a formatted string
     */
    public static String format(String input, Object... param) {
        return input != null ? MessageFormat.format(input, param) : null;
    }

    /**
     * Strips color codes from a string.
     *
     * @param input the input string
     * @return the input string without color codes
     */
    public static @NotNull String stripColor(String input) {
        Matcher color = Pattern.compile("[§&]([a-zA-Z0-9])").matcher(StringUtil.nonNull(input));
        while (color.find()) {
            input = input.replace(color.group(), "");
        }
        return input;
    }

    /**
     * Used to convert an integer into its corresponding roman numeral
     *
     * @param number Target number
     * @return A roman numeral for the integer
     */
    public static @NotNull String intToRoman(int number) {
        int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder romanNumeral = new StringBuilder();
        int remaining = number;

        for (int i = 0; i < VALUES.length; i++) {
            int value = VALUES[i];
            String symbol = SYMBOLS[i];

            while (remaining >= value) {
                romanNumeral.append(symbol);
                remaining -= value;
            }
        }
        return romanNumeral.toString();
    }

    /*
    ENSURING METHODS
     */

    /**
     * Returns a non-null string. If the original input is null, the default value provided is returned.
     *
     * @param input the provided input
     * @param def   the fallback value
     * @return a non-null string
     */
    public static @NotNull String nonNull(@Nullable String input, @NotNull String def) {
        return input != null ? input : def;
    }

    /**
     * Checks if a string is empty or null.
     *
     * @param input the input string
     * @return true if the string is empty or null, false otherwise
     */
    public static boolean isEmpty(@Nullable String input) {
        return input == null || input.isEmpty();
    }

    /**
     * Returns a non-null string. If the original input is null, an empty string is returned.
     *
     * @param input the provided input
     * @return a non-null string
     */
    public static @NotNull String nonNull(@Nullable String input) {
        return nonNull(input, "");
    }

    /**
     * Returns a non-null string from a {@link JsonElement}. If the element is null, the default value
     * is returned.
     *
     * @param element the provided {@link JsonElement}
     * @param def     the fallback value
     * @return a non-null string
     */
    public static @NotNull String nonNull(JsonElement element, String def) {
        return element != null ? element.getAsString() : def;
    }

    /**
     * Returns a non-null string from a {@link JsonElement}. If the element is null, an empty string
     * is returned.
     *
     * @param element the provided {@link JsonElement}
     * @return a non-null string
     */
    public static @NotNull String nonNull(JsonElement element) {
        return nonNull(element, "");
    }

    /*
    SUBSTRING METHODS
     */

    /**
     * Returns the substring from the last instance of the provided substring reference.
     *
     * @param input  the provided input
     * @param str    the substring reference
     * @param offset the offset for the substring
     * @return a substring derived from the main input
     * @see #lastIndex(String, String)
     */
    public static @NotNull String lastIndex(@NotNull String input, String str, int offset) {
        int index = input.lastIndexOf(str) + offset;
        return input.substring(index);
    }

    /**
     * Returns the substring from the last instance of the provided substring reference.
     *
     * @param input the provided input
     * @param str   the substring reference
     * @return a substring derived from the main input
     * @see #lastIndex(String, String, int)
     */
    public static @NotNull String lastIndex(@NotNull String input, String str) {
        return lastIndex(input, str, 0);
    }

    /**
     * Returns the substring from the first instance of the provided substring reference.
     *
     * @param input  the provided input
     * @param str    the substring reference
     * @param offset the offset for the substring
     * @return a substring derived from the main input
     * @see #lastIndex(String, String)
     */
    public static @NotNull String substring(@NotNull String input, String str, int offset) {
        int index = input.indexOf(str) + offset;
        return input.substring(index);
    }

    /**
     * Returns the substring from the first instance of the provided substring reference.
     *
     * @param input the provided input
     * @param str   the substring reference
     * @return a substring derived from the main input
     * @see #lastIndex(String, String, int)
     */
    public static @NotNull String substring(@NotNull String input, String str) {
        return substring(input, str, 0);
    }

    /*
    REPEAT METHODS
     */

    /**
     * Builds a string with the input string repeated a specified number of times.
     *
     * @param s        the desired string
     * @param quantity the desired number of repeats
     * @return a new string with the character repeated
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
     * Builds a string with the character repeated a specified number of times.
     *
     * @param s        the desired string
     * @param quantity the desired number of repeats
     * @return a new string with the character repeated
     */
    public static @NotNull String repeat(char s, int quantity) {
        return repeat(String.valueOf(s), quantity);
    }
}

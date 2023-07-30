package com.moleculepowered.platform.bukkit;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class StringUtil extends com.moleculepowered.api.util.StringUtil
{
    /**
     * Colorizes the input string by replacing color codes with the '&' symbol and formatting
     * the string using the given parameters.
     *
     * <p>Color codes in the input string are denoted by the special character ChatColor.COLOR_CHAR,
     * which is replaced by the ampersand ('&') symbol. Color codes are used to specify the color
     * and formatting of text in Minecraft's chat and command output.
     *
     * <p>The method uses {@link ChatColor#translateAlternateColorCodes(char, String)} to translate
     * the color codes in the input string. It also uses {@link String#format(String, Object...)}
     * to format the string using the provided parameters.
     *
     * @param input The input string to be colorized and formatted.
     * @param param The parameters to be used for formatting the input string.
     * @return The colorized and formatted string.
     * @throws NullPointerException if the input string is null.
     * @see ChatColor#translateAlternateColorCodes(char, String)
     * @see String#format(String, Object...)
     */
    public static @NotNull String colorize(String input, Object... param) {
        input = input.replace(ChatColor.COLOR_CHAR, '&');
        return ChatColor.translateAlternateColorCodes('&', format(input, param));
    }
}
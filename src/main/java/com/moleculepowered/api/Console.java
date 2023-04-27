package com.moleculepowered.api;

import com.moleculepowered.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class used to handle all tasks that require logging, and console color conversions
 *
 * @author OMGitzFROST
 * @since 1.0
 */
public final class Console {

    private static final Logger LOGGER = Logger.getLogger("MoleculeAPI");
    private static final String DEBUG_PREFIX = "[DEBUG]";
    private static boolean prettyPrint = true, canDebug = false;
    private static String PREFIX;

    /*
    GLOBALLY SETS THE DEBUG FILTER WITHOUT NEEDING TO BE EXPLICITLY CALLED UPON
     */
    static {
        LOGGER.setFilter(new DebugFilter());
    }

    /**
     * Used to disable this constructor from being used, this is a utility class
     */
    private Console() {}

    /**
     * A main constructor used by this class. With it, you can set the prefix that will
     * be used when logging messages.
     *
     * @param prefix Desired prefix
     * @see #Console()
     */
    public Console(@Nullable String prefix) {
        Console.PREFIX = prefix != null ? ConsoleColor.parse("[" + prefix + "&r] ") : "";
    }

    /*
    SPECIALTY LOGGING METHODS
     */

    /**
     * <p>Prints a debug message to console. Please note that in-order for you to see
     * these messages, debug must be enabled for the console, by default debug messages are disabled.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param message Provided message
     * @param param   Optional parameters
     * @see #setDebugToggle(boolean)
     * @see #debug(boolean, String, Object...)
     * @see #debug(boolean, Collection, Object...)
     * @see #debug(Collection, Object...)
     */
    public static void debug(String message, Object... param) {
        debug(true, message, param);
    }

    /**
     * <p>Prints a collection of debug messages to console. Please note that in-order for
     * you to see these messages, debug must be enabled for the console, by default debug messages are disabled.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param messages Collection of messages
     * @param param   Optional parameters
     * @see #setDebugToggle(boolean)
     * @see #debug(String, Object...)
     * @see #debug(boolean, String, Object...)
     * @see #debug(boolean, Collection, Object...)
     */
    public static void debug(Collection<String> messages, Object... param) {
        debug(true, messages, param);
    }

    /**
     * <p>Prints a debug message to console if a condition is met. Please note that in-order for you to see
     * these messages, debug must be enabled for the console, by default debug messages are disabled.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param message Provided message
     * @param param   Optional parameters
     * @see #setDebugToggle(boolean)
     * @see #debug(String, Object...)
     * @see #debug(boolean, Collection, Object...)
     */
    public static void debug(boolean condition, String message, Object... param) {
        log(condition, Level.INFO, DEBUG_PREFIX + " " + message, param);
    }

    /**
     * <p>Prints a collection of debug messages to console if a condition is met. Please note that in-order for
     * you to see these messages, debug must be enabled for the console, by default debug messages are disabled.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param messages Collection of messages
     * @param param   Optional parameters
     * @see #setDebugToggle(boolean)
     * @see #debug(String, Object...)
     * @see #debug(boolean, String, Object...)
     */
    public static void debug(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> debug(condition, m, param));
    }

    /**
     * Prints an informational message to console.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param message Provided message
     * @param param   Optional parameters
     * @see #info(boolean, String, Object...)
     * @see #info(boolean, Collection, Object...)
     * @see #info(Collection, Object...)
     */
    public static void info(String message, Object... param) {
        info(true, message, param);
    }

    /**
     * Prints a collection of informational messages to console.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param messages   Collection of messages message
     * @param param     Optional parameters
     * @see #info(String, Object...)
     * @see #info(boolean, String, Object...)
     * @see #info(boolean, Collection, Object...)
     */
    public static void info(Collection<String> messages, Object... param) {
        info(true, messages, param);
    }

    /**
     * Prints an informational message to console if a condition is met.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param message   Provided message
     * @param param     Optional parameters
     * @see #info(String, Object...)
     * @see #info(boolean, Collection, Object...)
     * @see #info(Collection, Object...)
     */
    public static void info(boolean condition, String message, Object... param) {
        log(condition, Level.INFO, message, param);
    }

    /**
     * Prints a collection informational messages to console if a condition is met.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param messages   Collection of messages message
     * @param param     Optional parameters
     * @see #info(String, Object...)
     * @see #info(boolean, String, Object...)
     * @see #info(Collection, Object...)
     */
    public static void info(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> info(condition, m, param));
    }

    /**
     * Prints an success message to console.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param message Provided message
     * @param param   Optional parameters
     * @see #success(boolean, String, Object...)
     * @see #success(boolean, Collection, Object...)
     * @see #success(Collection, Object...)
     */
    public static void success(String message, Object... param) {
        success(true, message, param);
    }

    /**
     * Prints a collection of success messages to console.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param messages Collection of messages message
     * @param param    Optional parameters
     * @see #success(String, Object...)
     * @see #success(boolean, String, Object...)
     * @see #success(boolean, Collection, Object...)
     */
    public static void success(Collection<String> messages, Object... param) {
        success(true, messages, param);
    }

    /**
     * Prints a success message to console if a condition is met.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param message   Provided message
     * @param param     Optional parameters
     * @see #success(String, Object...)
     * @see #success(boolean, Collection, Object...)
     * @see #success(Collection, Object...)
     */
    public static void success(boolean condition, String message, Object... param) {
        log(condition, Level.INFO, ConsoleColor.colorize(ConsoleColor.GREEN, message), param);
    }

    /**
     * Prints a collection success messages to console if a condition is met.
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param messages  Collection of messages message
     * @param param     Optional parameters
     * @see #success(String, Object...)
     * @see #success(boolean, String, Object...)
     * @see #success(Collection, Object...)
     */
    public static void success(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> success(condition, m, param));
    }

    /**
     * <p>Prints a warning message to console</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param message Provided message
     * @param param   Optional parameters
     * @see #warning(boolean, String, Object...)
     * @see #warning(boolean, Collection, Object...)
     * @see #warning(Collection, Object...)
     */
    public static void warning(String message, Object... param) {
        warning(true, message, param);
    }

    /**
     * <p>Prints a collection of warning messages to console.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param messages   Collection of messages
     * @param param     Optional parameters
     * @see #warning(String, Object...)
     * @see #warning(boolean, String, Object...)
     * @see #warning(boolean, Collection, Object...)
     */
    public static void warning(Collection<String> messages, Object... param) {
        warning(true, messages, param);
    }

    /**
     * <p>Prints a warning message to console if a condition is met</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param message   Provided message
     * @param param     Optional parameters
     * @see #warning(String, Object...)
     * @see #warning(boolean, Collection, Object...)
     * @see #warning(Collection, Object...)
     */
    public static void warning(boolean condition, String message, Object... param) {
        log(condition, Level.WARNING, ConsoleColor.colorize(ConsoleColor.GOLD, message), param);
    }

    /**
     * <p>Prints a collection of warning messages to console if a condition is met</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param messages   Collection of messages
     * @param param     Optional parameters
     * @see #warning(String, Object...)
     * @see #warning(boolean, String, Object...)
     * @see #warning(Collection, Object...)
     */
    public static void warning(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> warning(condition, m, param));
    }

    /**
     * <p>Prints a severe message to console.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param message Provided message
     * @param param   Optional parameters
     * @see #severe(boolean, String, Object...)
     * @see #severe(boolean, Collection, Object...)
     * @see #severe(Collection, Object...)
     */
    public static void severe(String message, Object... param) {
        severe(true, message, param);
    }

    /**
     * <p>Prints a collection of severe messages to console.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param messages   Collection of messages
     * @param param     Optional parameters
     * @see #severe(String, Object...)
     * @see #severe(boolean, String, Object...)
     * @see #severe(boolean, Collection, Object...)
     */
    public static void severe(Collection<String> messages, Object... param) {
        severe(true, messages, param);
    }

    /**
     * <p>Prints a severe message to console if a condition is met.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param message   Provided message
     * @param param     Optional parameters
     * @see #severe(String, Object...)
     * @see #severe(boolean, Collection, Object...)
     * @see #severe(Collection, Object...)
     */
    public static void severe(boolean condition, String message, Object... param) {
        log(condition, Level.SEVERE, ConsoleColor.colorize(ConsoleColor.RED, message), param);
    }

    /**
     * <p>Prints a collection of severe messages to console if a condition is met.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param messages   Collection of messages
     * @param param     Optional parameters
     * @see #severe(String, Object...)
     * @see #severe(boolean, String, Object...)
     * @see #severe(Collection, Object...)
     */
    public static void severe(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> severe(condition, m, param));
    }

    /*
    DEFAULT LOGGING METHODS
     */

    /**
     * <p>Prints a message to console with a specified level of severity if a condition is met</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param level     Level of severity
     * @param message   Provided message
     * @param param     Optional parameters
     * @see #log(boolean, Level, Collection, Object...)
     * @see #log(Level, Collection, Object...)
     * @see #log(Level, String, Object...)
     * @see #log(Collection, Object...)
     * @see #log(String, Object...)
     */
    public static void log(boolean condition, Level level, String message, Object... param) {
        if (condition) LOGGER.log(level, getPrefix() + ConsoleColor.parse(message), param);
    }

    /**
     * <p>Prints a collection of messages to console with a specified level of severity if a condition is met</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param condition Whether this logger should actually log
     * @param level     Level of severity
     * @param messages   Collection of messages
     * @param param     Optional parameters
     * @see #log(boolean, Level, String, Object...)
     * @see #log(Level, Collection, Object...)
     * @see #log(Level, String, Object...)
     * @see #log(Collection, Object...)
     * @see #log(String, Object...)
     */
    public static void log(boolean condition, Level level, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> log(condition, level, m, param));
    }

    /**
     * <p>Prints a message to console with a specified level of severity</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param level   Level of severity
     * @param message Provided message
     * @param param   Optional parameters
     * @see #log(boolean, Level, String, Object...)
     * @see #log(boolean, Level, Collection, Object...)
     * @see #log(Level, Collection, Object...)
     * @see #log(Collection, Object...)
     * @see #log(String, Object...)
     */
    public static void log(Level level, String message, Object... param) {
        log(true, level, message, param);
    }

    /**
     * <p>Prints a collection of messages to console with a specified level of severity</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param level   Level of severity
     * @param messages Provided message
     * @param param   Optional parameters
     * @see #log(boolean, Level, String, Object...)
     * @see #log(boolean, Level, Collection, Object...)
     * @see #log(Collection, Object...)
     * @see #log(Level, String, Object...)
     * @see #log(String, Object...)
     */
    public static void log(Level level, Collection<String> messages, Object... param) {
        log(true, level, messages, param);
    }

    /**
     * <p>Prints a standard logging message to console.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param message Provided message
     * @param param   Optional parameters
     * @see #log(boolean, Level, String, Object...)
     * @see #log(boolean, Level, Collection, Object...)
     * @see #log(Level, String, Object...)
     * @see #log(Level, Collection, Object...)
     * @see #log(Collection, Object...)
     */
    public static void log(String message, Object... param) {
        log(true, Level.INFO, message, param);
    }

    /**
     * <p>Prints a collection of standard logging messages to console.</p>
     *
     * <p>If {@link #prettyPrint} is set to true, this method will also parse color
     * code, providing a color console output, otherwise this method will simply log the message</p>
     *
     * @param messages Collection of messages
     * @param param   Optional parameters
     * @see #log(boolean, Level, String, Object...)
     * @see #log(boolean, Level, Collection, Object...)
     * @see #log(Level, String, Object...)
     * @see #log(String, Object...)
     * @see #log(Level, Collection, Object...)
     */
    public static void log(Collection<String> messages, Object... param) {
        log(true, Level.INFO, messages, param);
    }

    /*
    SETTER METHODS
     */

    /**
     * An additional way to set the console's prefix, the recommended way is through the
     * {@link #Console(String)} constructor but this method serves as an alternative they
     * could fit your needs better.
     *
     * @param prefix Desired prefix
     * @see #getPrefix()
     */
    public static void setPrefix(@Nullable String prefix) {
        Console.PREFIX = prefix != null ? ConsoleColor.parse("[" + prefix + "&r] ") : "";
    }

    /**
     * Used to toggle debug loggers for this class, when the toggle is set
     * to false, all debug messages will be disabled and vice versa, all debug
     * messages will be enabled.
     *
     * @param toggle Whether debug messages are enabled
     * @see #isDebugging()
     */
    public static void setDebugToggle(boolean toggle) {
        canDebug = toggle;
    }

    /**
     * A chain method that allows you to toggle pretty print globally, any logging
     * printed after modifying this setting will take on the new setting.
     *
     * @param toggle whether it should be enabled or not
     * @see #isPrettyPrint()
     */
    public static void setPrettyPrint(boolean toggle) {
        prettyPrint = toggle;
    }

    /*
    GETTER METHODS
     */

    /**
     * A utility method used to return the plugin's name if available, if not, this method will
     * return as an empty string. This method is mainly used for the {@link #getPrefix()} method.
     *
     * @return The plugin's name or an empty string
     */
    private static @NotNull String getPluginName() {
        return MoleculeAPI.getPlugin() != null ? MoleculeAPI.getPlugin().getName() : "";
    }

    /**
     * Used to return the prefix assigned to the console, if one does not exist, this method
     * will attempt to use the plugin's name, otherwise it will use an empty string.
     *
     * @return Console prefix or empty string
     * @see #setPrefix(String)
     */
    public static String getPrefix() {
        return PREFIX != null ? PREFIX : (!getPluginName().isEmpty() ? ConsoleColor.parse("[" + getPluginName() + "&r] ") : "");
    }

    /**
     * Returns whether this console is allowed to output messages with colored
     * formatting (aka pretty print), if false, it means this console will send
     * messages out without formatting.
     *
     * @return true if pretty print is enabled
     * @see #setPrettyPrint(boolean)
     */
    public static boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Used to return whether this console is currently allowed to send debug
     * messages, otherwise this method will return false.
     *
     * @return true if debugging is enabled
     * @see #setDebugToggle(boolean)
     */
    public static boolean isDebugging() {
        return canDebug;
    }

    /*
    CONSOLE COLOR ENUM
     */

    /**
     * This class is designed to handle all tasks that require adding, removing colors from
     * the console's output. It can convert minecraft color codes to ANSI color codes effortlessly.
     *
     * @author OMGitzFROST
     */
    private enum ConsoleColor {

        BLACK('0', 0, "\u001b[38;5;%dm"),
        DARK_GREEN('2', 2, "\u001b[38;5;%dm"),
        DARK_RED('4', 1, "\u001b[38;5;%dm"),
        GOLD('6', 172, "\u001b[38;5;%dm"),
        DARK_GREY('8', 8, "\u001b[38;5;%dm"),
        GREEN('a', 10, "\u001b[38;5;%dm"),
        RED('c', 9, "\u001b[38;5;%dm"),
        YELLOW('e', 11, "\u001b[38;5;%dm"),
        DARK_BLUE('1', 4, "\u001b[38;5;%dm"),
        DARK_AQUA('3', 30, "\u001b[38;5;%dm"),
        DARK_PURPLE('5', 54, "\u001b[38;5;%dm"),
        GRAY('7', 246, "\u001b[38;5;%dm"),
        BLUE('9', 4, "\u001b[38;5;%dm"),
        AQUA('b', 51, "\u001b[38;5;%dm"),
        LIGHT_PURPLE('d', 13, "\u001b[38;5;%dm"),
        WHITE('f', 15, "\u001b[38;5;%dm"),
        STRIKETHROUGH('m', 9, "\u001b[%dm"),
        ITALIC('o', 3, "\u001b[%dm"),
        BOLD('l', 1, "\u001b[%dm"),
        UNDERLINE('n', 4, "\u001b[%dm"),
        RESET('r', 0, "\u001b[%dm");

        private final char bukkitColor;
        private final String ansiColor;

        ConsoleColor(char bukkitColor, int ansiCode, String pattern) {
            this.bukkitColor = bukkitColor;
            this.ansiColor = String.format(pattern, ansiCode);
        }

        /**
         * Filters through the {@link ConsoleColor}'s to find the one associated with the provided
         * char. If one cannot be found, this method will return as null.
         *
         * @param code Target code
         * @return A {@link ConsoleColor} or null
         */
        public static @Nullable ConsoleColor getColorByCode(char code) {
            for (ConsoleColor color : values()) if (color.bukkitColor == code) return color;
            return null;
        }

        /**
         * <p>Parses all color codes within the string into their ANSI counter-parts.</p>
         *
         * <p>Additionally, the outcome of this method is dictated on whether {@link #prettyPrint} is set to true.
         * If false, this method will not add the coloring and simply strip the input of its existing color codes.</p>
         *
         * @param input Provided input
         * @return A formatted string
         * @see #colorize(ConsoleColor, String)
         */
        public static @NotNull String parse(@Nullable String input) {
            if (!prettyPrint || input == null) return stripColor(input);
            else input = input.replace('§', '&');

            // copy of string
            String messageCopy = String.copyValueOf(input.toCharArray()) + ConsoleColor.RESET.ansiColor;
            // create Matcher to search for color codes
            Matcher matcher = Pattern.compile("(&[0-9a-fk-or])(?!.*\1)").matcher(input);
            // run through the result
            while (matcher.find()) {
                // get Result
                String result = matcher.group(1);
                // get ColorCode
                ConsoleColor color = ConsoleColor.getColorByCode(result.charAt(1));
                // replace color
                messageCopy = messageCopy.replace(result, color != null ? color.getAnsiColor() : result);
            }
            // return converted String
            return messageCopy;
        }

        /**
         * <p>Used to surround a string with the specified color, please note that this method
         * does not parse additional color codes within the string but actually removes them
         * entirely.</p>
         *
         * <p>Additionally, the outcome of this method is dictated on whether {@link #prettyPrint} is set to true.
         * If false, this method will not add the coloring and simply strip the input of its existing color codes.</p>
         *
         * @param color Target color
         * @param input Provided input
         * @return A formatted string
         * @see #parse(String)
         */
        public static @NotNull String colorize(@NotNull ConsoleColor color, @Nullable String input) {
            if (!prettyPrint || input == null) return stripColor(input);
            else input = input.replace('§', '&');

            return color.getAnsiColor() + stripColor(input) + ConsoleColor.RESET.getAnsiColor();
        }

        /**
         * Strips the provided input from all color codes, it normalizes the string by replacing
         * all "&" color codes with "§" in-order to properly strip them.
         *
         * @param input Provided input
         * @return a colorless string
         */
        public static @NotNull String stripColor(@Nullable String input) {
            input = StringUtil.nonNull(input, "null").replaceAll("&", "§");
            return ChatColor.stripColor(input);
        }

        /**
         * Returns the ANSI code assigned to this {@link ConsoleColor}
         *
         * @return ansi code assigned
         */
        public String getAnsiColor() {
            return ansiColor;
        }
    }

    /**
     * A utility class used to initialize implement this filter within our loggers
     * it is tasked with filtering out debug messages when not enabled.
     */
    private static class DebugFilter implements Filter {

        /**
         * Check if a given log record should be published.
         *
         * @param record a LogRecord
         * @return true if the log record should be published.
         */
        @Override
        public boolean isLoggable(@NotNull LogRecord record) {
            return !record.getMessage().contains(DEBUG_PREFIX) || canDebug;
        }
    }
}

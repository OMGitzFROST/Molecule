package com.moleculepowered.api;

import com.moleculepowered.api.localization.Translator;
import com.moleculepowered.api.localization.i18n;
import com.moleculepowered.api.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.moleculepowered.api.localization.i18n.tl;

/**
 * An abstract class that enhances the native logging support of a platform by providing
 * additional logging settings such as translated messages and color coding. Each platform
 * may have different logging mechanisms, and this class adapts to those mechanisms accordingly.
 *
 * <p>This class serves as a foundation for platform-specific console implementations
 * that extend it to incorporate platform-specific logging functionality.</p>
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public abstract class Console
{
    private final Logger LOGGER;
    private final String DEBUG_PREFIX = "[DEBUG] ";
    private final String PREFIX;
    private static boolean prettyPrint = true;
    private boolean canDebug = false;

    /**
     * The main constructor for the {@link Console} class. It initializes default objects required by
     * this class, such as setting the debug filter and console prefix.
     *
     * @param name Typically the plugin's name, but can be anything.
     */
    public Console(@NotNull String name) {
        this.LOGGER = Logger.getLogger(name);
        this.LOGGER.setFilter(new DebugFilter());
        this.PREFIX = ConsoleColor.parse("[" + name + "&r] ");
    }

    /*
    SPECIALTY LOGGING METHODS
     */

    /**
     * Prints a debug message to the console. Please note that in order for you to see these messages,
     * debug must be enabled for the console (by default, debug messages are disabled).
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the message.
     *
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     * @see #setDebugToggle(boolean)
     */
    public void debug(String message, Object... param) {
        debug(true, message, param);
    }

    /**
     * Prints a collection of debug messages to the console. Please note that in order for you to see
     * these messages, debug must be enabled for the console (by default, debug messages are disabled).
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the messages.
     *
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     * @see #setDebugToggle(boolean)
     */
    public void debug(Collection<String> messages, Object... param) {
        debug(true, messages, param);
    }

    /**
     * Prints a debug message to the console if a condition is met. Please note that in order for you to see
     * these messages, debug must be enabled for the console (by default, debug messages are disabled).
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the message.
     *
     * @param condition Whether this logger should actually log the message
     * @param message   The message to be printed
     * @param param     Optional parameters to be included in the message
     * @see #setDebugToggle(boolean)
     */
    public void debug(boolean condition, String message, Object... param) {
        log(condition, Level.INFO, DEBUG_PREFIX + tl(message, param));
    }

    /**
     * Prints a collection of debug messages to the console if a condition is met. Please note that in order for you to see
     * these messages, debug must be enabled for the console (by default, debug messages are disabled).
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the messages.
     *
     * @param condition Whether this logger should actually log the messages
     * @param messages  The collection of messages to be printed
     * @param param     Optional parameters to be included in the messages
     * @see #setDebugToggle(boolean)
     */
    public void debug(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> debug(condition, m, param));
    }

    /**
     * Prints an informational message to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the message.
     *
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     */
    public void info(String message, Object... param) {
        info(true, message, param);
    }

    /**
     * Prints a collection of informational messages to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the messages.
     *
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     */
    public void info(Collection<String> messages, Object... param) {
        info(true, messages, param);
    }

    /**
     * Prints an informational message to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the message.
     *
     * @param condition Whether this logger should actually log the message
     * @param message   The message to be printed
     * @param param     Optional parameters to be included in the message
     */
    public void info(boolean condition, String message, Object... param) {
        log(condition, Level.INFO, message, param);
    }

    /**
     * Prints a collection of informational messages to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing color console output; otherwise, it will simply log the messages.
     *
     * @param condition Whether this logger should actually log the messages
     * @param messages  The collection of messages to be printed
     * @param param     Optional parameters to be included in the messages
     */
    public void info(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> info(condition, m, param));
    }

    /**
     * Prints a success message to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     */
    public void success(String message, Object... param) {
        success(true, message, param);
    }

    /**
     * Prints a collection of success messages to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     */
    public void success(Collection<String> messages, Object... param) {
        success(true, messages, param);
    }

    /**
     * Prints a success message to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param condition Whether this logger should actually log the message
     * @param message   The message to be printed
     * @param param     Optional parameters to be included in the message
     */
    public void success(boolean condition, String message, Object... param) {
        log(condition, Level.INFO, ConsoleColor.colorize(ConsoleColor.GREEN, tl(message)), param);
    }

    /**
     * Prints a collection of success messages to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param condition Whether this logger should actually log the messages
     * @param messages  The collection of messages to be printed
     * @param param     Optional parameters to be included in the messages
     */
    public void success(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> success(condition, m, param));
    }

    /**
     * Prints a warning message to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     */
    public void warning(String message, Object... param) {
        warning(true, message, param);
    }

    /**
     * Prints a collection of warning messages to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     */
    public void warning(Collection<String> messages, Object... param) {
        warning(true, messages, param);
    }

    /**
     * Prints a warning message to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param condition Whether this logger should actually log the message
     * @param message   The message to be printed
     * @param param     Optional parameters to be included in the message
     */
    public void warning(boolean condition, String message, Object... param) {
        log(condition, Level.WARNING, ConsoleColor.colorize(ConsoleColor.GOLD, tl(message)), param);
    }

    /**
     * Prints a collection of warning messages to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param condition Whether this logger should actually log the messages
     * @param messages  The collection of messages to be printed
     * @param param     Optional parameters to be included in the messages
     */
    public void warning(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> warning(condition, m, param));
    }

    /**
     * Prints a severe message to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     */
    public void severe(String message, Object... param) {
        severe(true, message, param);
    }

    /**
     * Prints a collection of severe messages to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     */
    public void severe(Collection<String> messages, Object... param) {
        severe(true, messages, param);
    }

    /**
     * Prints a severe message to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param condition Whether this logger should actually log the message
     * @param message   The message to be printed
     * @param param     Optional parameters to be included in the message
     */
    public void severe(boolean condition, String message, Object... param) {
        log(condition, Level.SEVERE, ConsoleColor.colorize(ConsoleColor.RED, tl(message)), param);
    }

    /**
     * Prints a collection of severe messages to the console if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param condition Whether this logger should actually log the messages
     * @param messages  The collection of messages to be printed
     * @param param     Optional parameters to be included in the messages
     */
    public void severe(boolean condition, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> severe(condition, m, param));
    }

    /*
    DEFAULT LOGGING METHODS
     */

    /**
     * Prints a message to the console with a specified level of severity if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param condition Whether this logger should actually log the message
     * @param level     The level of severity
     * @param message   The message to be printed
     * @param param     Optional parameters to be included in the message
     */
    public void log(boolean condition, Level level, String message, Object... param) {
        if (condition) LOGGER.log(level, getPrefix() + ConsoleColor.parse(tl(message)), param);
    }

    /**
     * Prints a collection of messages to the console with a specified level of severity if a condition is met.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param condition Whether this logger should actually log the messages
     * @param level     The level of severity
     * @param messages  The collection of messages to be printed
     * @param param     Optional parameters to be included in the messages
     */
    public void log(boolean condition, Level level, @NotNull Collection<String> messages, Object... param) {
        messages.forEach(m -> log(condition, level, m, param));
    }

    /**
     * Prints a message to the console with a specified level of severity.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param level   The level of severity
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     */
    public void log(Level level, String message, Object... param) {
        log(true, level, message, param);
    }

    /**
     * Prints a collection of messages to the console with a specified level of severity.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param level    The level of severity
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     */
    public void log(Level level, Collection<String> messages, Object... param) {
        log(true, level, messages, param);
    }

    /**
     * Prints a standard logging message to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the message.
     *
     * @param message The message to be printed
     * @param param   Optional parameters to be included in the message
     */
    public void log(String message, Object... param) {
        log(true, Level.INFO, message, param);
    }

    /**
     * Prints a collection of standard logging messages to the console.
     * <p>
     * If {@link #prettyPrint} is set to true, this method will also parse color codes,
     * providing a color console output; otherwise, it will simply log the messages.
     *
     * @param messages The collection of messages to be printed
     * @param param    Optional parameters to be included in the messages
     */
    public void log(Collection<String> messages, Object... param) {
        log(true, Level.INFO, messages, param);
    }

    /*
    SETTER METHODS
     */

    /**
     * Sets the debug toggle for this class. When the toggle is set to false,
     * all debug messages will be disabled, and vice versa, all debug messages
     * will be enabled.
     *
     * @param toggle Whether debug messages are enabled
     * @see #isDebugging()
     */
    public void setDebugToggle(boolean toggle) {
        canDebug = toggle;
    }

    /**
     * Sets the pretty print toggle globally. Any logging printed after modifying
     * this setting will take on the new setting.
     *
     * @param toggle Whether pretty print is enabled
     * @see #isPrettyPrint()
     */
    public void setPrettyPrint(boolean toggle) {
        prettyPrint = toggle;
    }

    /**
     * Sets the translator for the project. Please note that using this method will
     * override the translator for any other class using translation methods. It is
     * recommended to set this value once within your project.
     *
     * @param translator Functional translator
     */
    public void setTranslator(@Nullable Translator translator) {
        i18n.setTranslator(translator);
    }

    /*
    GETTER METHODS
    */

    /**
     * Returns the prefix assigned to the console. If no prefix exists, this method
     * will attempt to use the plugin's name. If the plugin's name is also unavailable,
     * it will return an empty string.
     *
     * @return The console prefix or an empty string
     */
    public String getPrefix() {
        return PREFIX != null ? PREFIX : ConsoleColor.parse("[Console]");
    }

    /**
     * Returns whether this console is allowed to output messages with colored
     * formatting (pretty print). If false, it means this console will send
     * messages without formatting.
     *
     * @return true if pretty print is enabled, false otherwise
     * @see #setPrettyPrint(boolean)
     */
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Returns whether this console is currently allowed to send debug messages.
     *
     * @return true if debugging is enabled, false otherwise
     * @see #setDebugToggle(boolean)
     */
    public boolean isDebugging() {
        return canDebug;
    }

    /*
    CONSOLE COLOR ENUM
     */

    /**
     * This enum class handles all tasks related to adding and removing colors from the console's output.
     * It can convert Minecraft color codes to ANSI color codes effortlessly.
     *
     * <p>
     * The enum constants represent different colors and formatting options. Each constant has a corresponding
     * Minecraft color code, ANSI code, and pattern used for formatting. The enum provides methods to parse color
     * codes within a string, surround a string with a specified color, and retrieve the ANSI code for a color.
     * </p>
     *
     * <p>
     * This class is used internally by the console logger and is not intended to be used directly.
     * </p>
     *
     * @author OMGitzFROST
     */
    private enum ConsoleColor
    {
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

        /**
         * Constructs a ConsoleColor enum constant with the provided bukkitColor, ansiCode, and pattern.
         *
         * @param bukkitColor The bukkit color code
         * @param ansiCode    The ANSI code
         * @param pattern     The pattern used for formatting
         */
        ConsoleColor(char bukkitColor, int ansiCode, String pattern) {
            this.bukkitColor = bukkitColor;
            this.ansiColor = String.format(pattern, ansiCode);
        }

        /**
         * Filters through the ConsoleColor values to find the one associated with the provided
         * bukkitColor code. If one cannot be found, this method will return null.
         *
         * @param code The bukkitColor code
         * @return The corresponding ConsoleColor or null
         */
        public static @Nullable ConsoleColor getColorByCode(char code) {
            for (ConsoleColor color : values()) {
                if (color.bukkitColor == code) {
                    return color;
                }
            }
            return null;
        }

        /**
         * Parses all color codes within the input string into their ANSI counterparts.
         *
         * <p>
         * Additionally, the outcome of this method is determined by the value of the `prettyPrint` flag.
         * If `prettyPrint` is false, this method will not add color formatting and simply strip the input of its
         * existing color codes.
         * </p>
         *
         * @param input The input string
         * @return The formatted string with ANSI color codes
         * @see #colorize(ConsoleColor, String)
         */
        public static String parse(@Nullable String input) {
            if (!prettyPrint || input == null) {
                return StringUtil.stripColor(input);
            } else {
                input = input.replace('§', '&');

                String messageCopy = String.copyValueOf(input.toCharArray()) + ConsoleColor.RESET.ansiColor;
                Matcher matcher = Pattern.compile("(&[0-9a-fk-or])(?!.*\\1)").matcher(input);
                while (matcher.find()) {
                    String result = matcher.group(1);
                    ConsoleColor color = ConsoleColor.getColorByCode(result.charAt(1));
                    messageCopy = messageCopy.replace(result, color != null ? color.getAnsiColor() : result);
                }
                return messageCopy;
            }
        }

        /**
         * Surrounds the input string with the specified color.
         *
         * <p>
         * Note that this method does not parse additional color codes within the string but removes them entirely.
         * </p>
         *
         * <p>
         * Additionally, the outcome of this method is determined by the value of the `prettyPrint` flag.
         * If `prettyPrint` is false, this method will not add color formatting and simply strip the input of its
         * existing color codes.
         * </p>
         *
         * @param color The target color
         * @param input The input string
         * @return The formatted string with ANSI color codes
         * @see #parse(String)
         */
        public static @NotNull String colorize(@NotNull ConsoleColor color, @Nullable String input) {
            if (!prettyPrint || input == null) {
                return StringUtil.stripColor(input);
            } else {
                input = input.replace('§', '&');

                return color.getAnsiColor() + StringUtil.stripColor(input) + ConsoleColor.RESET.getAnsiColor();
            }
        }

        /**
         * Returns the ANSI code assigned to this ConsoleColor.
         *
         * @return The ANSI code
         */
        public String getAnsiColor() {
            return ansiColor;
        }
    }

    /**
     * A utility class used to implement a filter within our loggers. It is responsible for filtering out debug messages
     * when debug logging is not enabled.
     *
     * <p>
     * This class is used internally by the console logger and is not intended to be used directly.
     * </p>
     *
     * @author OMGitzFROST
     */
    private class DebugFilter implements Filter
    {
        /**
         * Check if a given log record should be published.
         *
         * @param record The log record
         * @return true if the log record should be published, false otherwise
         */
        @Override
        public boolean isLoggable(@NotNull LogRecord record) {
            return !record.getMessage().contains(DEBUG_PREFIX) || canDebug;
        }
    }
}
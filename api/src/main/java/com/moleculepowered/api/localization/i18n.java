package com.moleculepowered.api.localization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;

/**
 * A utility class designed to handle translations. All core functions of this class are
 * accessed as static methods. Constructors are disabled for this class.
 *
 * <p>Use this class as a static utility for handling translations without the need for instantiation.</p>
 *
 * <p>Localization can be achieved by setting a translator using the {@link #setTranslator(Translator)} method.</p>
 *
 * @author OMGitzFROST
 */
public final class i18n
{
    private static i18n instance;
    private Translator translator;

    /*
    CONSTRUCTORS
     */

    /**
     * Constructs an instance of the `i18n` class. (Private constructor)
     *
     * <p>This constructor initializes the default translator, which uses the key as the default translation.
     * As a result, all messages inputted will display as provided by default.</p>
     *
     * <p>To enable localization, use the {@link #setTranslator(Translator)} method.</p>
     */
    private i18n() {
        translator = key -> key;
    }

    /*
    FUNCTIONAL METHODS
     */

    /**
     * Sets the translator to be used for translation methods.
     *
     * @param translator The functional translator
     */
    public static void setTranslator(@Nullable Translator translator) {
        getInstance().translator = translator;
    }

    /**
     * Translates the given key into a localized message using the defined translator.
     *
     * <p>Prior to using this method, ensure that a translator is defined using the {@link #setTranslator(Translator)} method.</p>
     *
     * @param key   The translation key
     * @param param Optional parameters for message interpolation
     * @return The localized message
     * @see #translate(String, Object...)
     */
    public static @NotNull String tl(String key, Object... param) {
        return getInstance().translate(key, param);
    }

    /*
    UTILITY METHODS
     */

    /**
     * Translates the given key into a localized message using the defined translator.
     *
     * <p>Prior to using this method, ensure that a translator is defined using the {@link #setTranslator(Translator)} method.</p>
     *
     * @param key   The translation key
     * @param param Optional parameters for message interpolation
     * @return The localized message
     * @see #tl(String, Object...)
     */
    private String translate(String key, Object... param) {
        try {
            return MessageFormat.format(translator.translate(key), param);
        } catch (NullPointerException ignored) {
            return key;
        }
    }

    /**
     * Returns an instance of the `i18n` class, enabling access to non-static objects and methods.
     *
     * <p>This method initializes the {@link #instance} variable if it is null.</p>
     *
     * @return An instance of the `i18n` class
     */
    private static i18n getInstance() {
        if (instance == null) instance = new i18n();
        return instance;
    }
}
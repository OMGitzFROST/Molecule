package com.moleculepowered.api.localization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.moleculepowered.api.util.StringUtil.format;

public class i18n {

    private static i18n instance;
    private Translator translator;

    /*
    CONSTRUCTORS
     */

    /**
     * The main constructor used to initialize all required objects for this class.
     */
    public i18n() {
        translator = key -> key;
    }

    /*
    SETTER METHODS
     */

    /**
     * Used to set the translator that will be used for our translation methods
     *
     * @param translator Functional translator
     */
    public static void setTranslator(@Nullable Translator translator) {
        getInstance().translator = translator;
    }

    /*
    TRANSLATING METHOD
     */

    /**
     * <p>Returns a localized message utilizing the defined translator.</p>
     *
     * <p>A translator must be defined prior to using this method, to define a translator
     * consider using the {@link #setTranslator(Translator)} method.</p>
     *
     * @param key   Translation key
     * @param param Optional parameters
     * @return A localized message
     * @see #tl(String, Object...)
     */
    public final String translate(String key, Object... param) {
        try {
            return format(translator.translate(key), param);
        } catch (NullPointerException ignored) {
            return key;
        }
    }

    /**
     * <p>Returns a localized message utilizing the defined translator.</p>
     *
     * <p>A translator must be defined prior to using this method, to define a translator
     * consider using the {@link #setTranslator(Translator)} method.</p>
     *
     * @param key   Translation key
     * @param param Optional parameters
     * @return A localized message
     * @see #translate(String, Object...)
     */
    public static @NotNull String tl(String key, Object... param) {
        return getInstance().translate(key, param);
    }

    /**
     * A utility method used to return an instance of this class, it enabled easy access to non-static
     * objects and methods. Please note that this method has a built-in check to initialize the
     * {@link #instance} variable, if its null it initializes it.
     *
     * @return An instance of this class
     */
    private static i18n getInstance() {
        if (instance == null) instance = new i18n();
        return instance;
    }
}

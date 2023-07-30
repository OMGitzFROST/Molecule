package com.moleculepowered.api.localization;

/**
 * This functional interface is responsible for handling translations for the {@link i18n} class.
 * It can be implemented to provide custom translation logic.
 *
 * @author OMGitzFROST
 */
@FunctionalInterface
public interface Translator {

    /**
     * Converts the provided key into a translated key.
     *
     * @param key The target key to be translated
     * @return The translated key
     */
    String translate(String key);
}
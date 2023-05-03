package com.moleculepowered.api.localization;

@FunctionalInterface
public interface Translator {

    /**
     * A method used to convert the provided key into a translated key.
     *
     * @param key Target key
     * @return a translated key
     */
    String translate(String key);
}

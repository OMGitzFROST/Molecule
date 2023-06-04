package com.moleculepowered.api.adapter;

import org.jetbrains.annotations.Nullable;

public interface PlayerAdapter {

    /**
     * Returns the locale of the adapted player
     *
     * @return Player locale
     */
    String getLocale();

    /**
     * Returns a players current ping
     *
     * @return Players ping
     */
    int getPing();

    /**
     * Sends the adapted player a action bar in-game
     *
     * @param message Action bar message
     */
    void sendActionBar(String message);

    /**
     * <p>Sends a title and a subtitle message to the player. If either of these values are null, they will
     * not be sent and the display will remain unchanged. If they are empty strings, the display will be
     * updated as such. If the strings contain a new line, only the first line will be sent. The titles
     * will be displayed with the client's default timings.</p>
     *
     * <p>The default timings are Fade-In = 10, Stay = 70, Fade-Out = 20</p>
     *
     * @param title Title text
     * @param subtitle Subtitle text
     */
    void sendTitle(@Nullable String title, @Nullable String subtitle);

    /**
     * <p>Sends a title and a subtitle message to the player.</p>
     * <p>If either of these values is null, they will not be sent and the display will remain unchanged.
     * If they are empty strings, the display will be updated as such. If the strings contain a new line,
     * only the first line will be sent. All timing values may take a value of -1 to indicate that they will
     * use the last value sent (or the defaults if no title has been displayed).</p>
     *
     * @param title Title text
     * @param subtitle Subtitle text
     * @param fadeIn time in ticks for titles to fade in. Defaults to 10.
     * @param stay time in ticks for titles to stay. Defaults to 70.
     * @param fadeOut time in ticks for titles to fade out. Defaults to 20.
     */
    void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut);
}

package com.moleculepowered.platform.bukkit.adapter;

import org.jetbrains.annotations.Nullable;

public interface PlayerAdapter
{
    /**
     * Returns the locale of the adapted player.
     *
     * @return The player's locale
     */
    String getLocale();

    /**
     * Returns the current ping of the player.
     *
     * @return The player's ping
     */
    int getPing();

    /**
     * Sends an action bar message to the adapted player in-game.
     *
     * @param message The action bar message
     */
    void sendActionBar(String message);

    /**
     * Sends a title and subtitle message to the player. If either of these values is null, they will
     * not be sent, and the display will remain unchanged. If they are empty strings, the display will be
     * updated as such. If the strings contain a new line, only the first line will be sent. The titles
     * will be displayed with the client's default timings.
     *
     * <p>The default timings are Fade-In = 10, Stay = 70, Fade-Out = 20.</p>
     *
     * @param title    The title text
     * @param subtitle The subtitle text
     */
    void sendTitle(@Nullable String title, @Nullable String subtitle);

    /**
     * Sends a title and subtitle message to the player.
     *
     * <p>If either of these values is null, they will not be sent, and the display will remain unchanged.
     * If they are empty strings, the display will be updated as such. If the strings contain a new line,
     * only the first line will be sent. All timing values may take a value of -1 to indicate that they will
     * use the last value sent (or the defaults if no title has been displayed).</p>
     *
     * @param title    The title text
     * @param subtitle The subtitle text
     * @param fadeIn   Time in ticks for titles to fade in. Defaults to 10.
     * @param stay     Time in ticks for titles to stay. Defaults to 70.
     * @param fadeOut  Time in ticks for titles to fade out. Defaults to 20.
     */
    void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut);
}

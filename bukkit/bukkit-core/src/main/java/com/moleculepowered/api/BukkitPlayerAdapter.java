package com.moleculepowered.api;

import com.moleculepowered.api.adapter.PlayerAdapter;

import java.util.Objects;

public class BukkitPlayerAdapter implements PlayerAdapter {

    /**
     * Returns the locale of the adapted player
     *
     * @return Player locale
     */
    @Override
    public String getLocale() {
        return Objects.requireNonNull(NMSBridge.player()).getLocale();
    }
}

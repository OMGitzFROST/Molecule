package com.moleculepowered.api;

import com.moleculepowered.api.adapter.PlayerAdapter;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PlayerAdapter_v1_15_R1 implements PlayerAdapter {

    private final Player player;

    public PlayerAdapter_v1_15_R1(Player player) {
        this.player = player;
    }

    /**
     * Returns the locale of the adapted player
     *
     * @return Player locale
     */
    @Override
    public String getLocale() {
        return player.getLocale();
    }
}

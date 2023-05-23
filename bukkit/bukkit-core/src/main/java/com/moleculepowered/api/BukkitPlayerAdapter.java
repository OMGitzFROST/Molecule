package com.moleculepowered.api;

import com.moleculepowered.api.adapter.PlayerAdapter;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BukkitPlayerAdapter implements PlayerAdapter {

    private final Player player;

    public BukkitPlayerAdapter(Player player) {
        this.player = player;
    }

    /**
     * Returns the locale of the adapted player
     *
     * @return Player locale
     */
    @Override
    public String getLocale() {
        return Objects.requireNonNull(NMSBridge.player(player)).getLocale();
    }
}

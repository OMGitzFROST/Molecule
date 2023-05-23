package com.moleculepowered.api;

import com.moleculepowered.api.adapter.PlayerAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PlayerAdapter_v1_8_R3 implements PlayerAdapter{

    private final Player player;

    PlayerAdapter_v1_8_R3(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public String getLocale() {
        return player.spigot().getLocale();
    }
}

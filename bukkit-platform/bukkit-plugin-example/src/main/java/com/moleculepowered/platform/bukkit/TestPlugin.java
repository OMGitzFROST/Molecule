package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.adapter.PlayerAdapter;
import com.moleculepowered.api.config.ConfigManager;
import com.moleculepowered.api.user.UserManager;
import com.moleculepowered.platform.bukkit.user.BukkitUserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class TestPlugin extends BukkitPlugin implements Listener {

    private UserManager userManager;

    @Override
    public void onEnable() {
        userManager = new BukkitUserManager(this);
        userManager.onEnable();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        PlayerAdapter adapter = BukkitNMSBridge.adaptPlayer(event.getPlayer());
        adapter.sendTitle("Test Title", "With subtitle");
    }

    /**
     * Return's an instance of the {@link UserManager} class.
     *
     * @return {@link UserManager} class
     */
    @Override
    public @NotNull UserManager getUserManager() {
        return userManager;
    }

    /**
     * Returns an instance of the {@link ConfigManager} class
     *
     * @return {@link ConfigManager} class
     */
    @Override
    public @NotNull ConfigManager getConfigManager() {
        return null;
    }
}

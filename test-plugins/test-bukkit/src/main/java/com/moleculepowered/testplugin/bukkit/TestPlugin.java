package com.moleculepowered.testplugin.bukkit;

import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.GithubProvider;
import com.moleculepowered.platform.bukkit.BukkitPlugin;
import com.moleculepowered.platform.bukkit.ComponentBuilder;
import com.moleculepowered.platform.bukkit.updater.BukkitUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public final class TestPlugin extends BukkitPlugin implements Listener
{
    private Updater updater;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        updater = new BukkitUpdater(this)
                .addProvider(new GithubProvider("EssentialsX/Essentials"));
        updater.schedule();
    }

    /**
     * This event is called to handle when an audience member joins the server. If they
     * are permitted to receive notifications, this method will send them one if the update
     * result equals {@link UpdateResult#UPDATE_AVAILABLE}.
     *
     * @param event Player join event
     */
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

        if (updater.getPermission() != null && !event.getPlayer().hasPermission(updater.getPermission())) return;
        if (updater.getResult() != UpdateResult.UPDATE_AVAILABLE) return;

        // SEND NOTIFICATION IF PLAYER IS AUDIENCE MEMBER
        updater.sendNotification(event.getPlayer(), updater.getResult());
    }
}

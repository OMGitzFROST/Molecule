package com.moleculepowered.api.event.updater;

import com.moleculepowered.api.event.AbstractEvent;
import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.Version;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class UpdateCompleteEvent extends AbstractEvent implements Cancellable {

    private final AbstractProvider provider;
    private final Updater updater;
    private final UpdateResult result;
    private boolean cancelled;

    /*
    CONSTRUCTOR
     */

    public UpdateCompleteEvent(boolean async, @NotNull Updater updater) {
        super(async);
        this.updater = updater;
        this.provider = updater.getProvider();
        this.result = updater.getResult();
    }

    /*
    MODIFYING METHODS
     */

    /**
     * Return's the provider containing the latest version
     *
     * @return the active provider
     */
    public @NotNull AbstractProvider getProvider() {
        return provider;
    }

    /*
    GETTER METHODS
     */

    /**
     * Used to return the latest version configured by the updater, with this method
     * you can access a good range of values regarding the version itself, such as isolating
     * the versions, major, minor, and patch integers, as well as the version type such as BETA, ALPHA,
     * etc.
     *
     * @return The latest version
     */
    public @NotNull Version getRelease() {
        return provider.getRelease();
    }

    /**
     * Return's a set of players, also known as the audience that is permitted to receive
     * update notifications, this method inherits its functionality from the {@link Updater} class
     * and will automatically filter out non permitted users from the set.
     *
     * @return A set of permitted audience members
     */
    public @NotNull Set<Player> getAudience() {
        return updater.getAudience();
    }

    /**
     * Return's the final result configured by the updater
     *
     * @return the final result
     */
    public @NotNull UpdateResult getResult() {
        return result;
    }

    /**
     * Used to return an instance of the updater that is handling the update checks.
     *
     * @return an instance of the updater class
     */
    public Updater getUpdater() {
        return updater;
    }

    /**
     * Return's true if the provided player is considered an authorized audience member.
     * Otherwise, this method will return false.
     *
     * @param player Target player
     * @return true if player is an authorized audience member
     */
    public boolean isAudienceMember(@Nullable OfflinePlayer player) {
        return updater.isAudienceMember(player);
    }

    /*
    BOOLEAN METHODS
     */

    /**
     * Return's true if this event was canceled
     *
     * @return true if canceled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Used to determine whether this event should be cancelled, setting this to true will cancel the event.
     *
     * @param b true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

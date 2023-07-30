package com.moleculepowered.platform.bukkit.event.updater;

import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.platform.bukkit.event.AbstractEvent;
import com.moleculepowered.platform.bukkit.updater.BukkitUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents an event that is triggered when the {@link Updater} completes its update check.
 * This event provides information about the update status and any available updates.
 *
 * @author OMGitzFROST
 * @see Updater
 */
public final class UpdateCompleteEvent extends AbstractEvent implements Cancellable
{
    private final AbstractProvider provider;
    private final ComparableVersion version;
    private final BukkitUpdater updater;
    private final UpdateResult result;
    private boolean cancelled;

    /*
    CONSTRUCTOR
     */

    /**
     * Constructs a new UpdateCompleteEvent instance.
     *
     * @param async   true if the event should be handled asynchronously
     * @param updater the updater instance handling the update checks
     */
    public UpdateCompleteEvent(boolean async, @NotNull BukkitUpdater updater) {
        super(async);
        this.updater = updater;
        this.provider = updater.getProvider();
        this.version = updater.getLatestVersion();
        this.result = updater.getResult();
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the provider containing the latest version.
     *
     * @return the active provider
     */
    public @NotNull AbstractProvider getProvider() {
        return provider;
    }

    /**
     * Returns the latest release version retrieved by this updater.
     *
     * @return the latest release version
     */
    public @NotNull ComparableVersion getVersion() {
        return version;
    }

    /**
     * Returns the final result configured by the updater.
     *
     * @return the final result
     */
    public @NotNull UpdateResult getResult() {
        return result;
    }

    /**
     * Returns an instance of the updater that is handling the update checks.
     *
     * @return an instance of the updater class
     */
    public @NotNull Updater getUpdater() {
        return updater;
    }

    // TODO: 6/26/23 ADD JAVADOC
    public @NotNull Set<Player> getAudience() {
        return updater.getAudience();
    }

    /*
    BOOLEAN METHODS
     */

    /**
     * Returns true if this event was canceled.
     *
     * @return true if canceled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be canceled.
     *
     * @param cancelled true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
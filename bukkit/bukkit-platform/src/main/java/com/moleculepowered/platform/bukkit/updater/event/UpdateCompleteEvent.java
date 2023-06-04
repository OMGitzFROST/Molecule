package com.moleculepowered.platform.bukkit.updater.event;

import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.ComparableVersion;
import com.moleculepowered.platform.bukkit.event.AbstractEvent;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

// TODO: 5/28/23 JAVADOC
public final class UpdateCompleteEvent extends AbstractEvent implements Cancellable
{
    private final AbstractProvider provider;
    private final ComparableVersion version;
    private final Updater updater;
    private final UpdateResult result;
    private boolean cancelled;

    /*
    CONSTRUCTOR
     */

    // TODO: 5/28/23 JAVADOC
    public UpdateCompleteEvent(boolean async, @NotNull Updater updater) {
        super(async);
        this.updater  = updater;
        this.provider = updater.getProvider();
        this.version  = updater.getLatestVersion();
        this.result   = updater.getResult();
    }

    /*
    GETTER METHODS
     */

    /**
     * Return's the provider containing the latest version
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
    public @NotNull Updater getUpdater() {
        return updater;
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
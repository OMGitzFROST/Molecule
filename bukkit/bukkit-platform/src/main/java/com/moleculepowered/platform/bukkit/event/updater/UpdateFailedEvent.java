package com.moleculepowered.platform.bukkit.event.updater;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.platform.bukkit.event.AbstractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A custom event triggered when the {@link Updater} fails to run its update check. Generally
 * this could be due to a misconfigured provider, the provider failing to run its fetch, etc.
 * Please note that typically these errors should not be fatal.
 *
 * @author OMGitzFROST
 */
public final class UpdateFailedEvent extends AbstractEvent
{
    private final Updater updater;
    private final Throwable thrown;

    /*
    CONSTRUCTOR
     */

    /**
     * The main constructor for the {@link UpdateFailedEvent}, it accepts the parent updater
     * and the thrown error as its parameters.
     *
     * @param async   Whether this event should run on an async thread
     * @param updater The parent updater which triggered the event
     * @param thrown  The exception thrown
     */
    public UpdateFailedEvent(boolean async, @NotNull Updater updater, @Nullable Throwable thrown) {
        super(async);
        this.updater = updater;
        this.thrown = thrown;
    }

    /**
     * Returns an instance of the updater that is handling the update checks.
     *
     * @return an instance of the updater class
     */
    public @NotNull Updater getUpdater() {
        return updater;
    }

    /**
     * Returns the exception thrown during the update process, if any.
     *
     * @return the exception thrown, or null if no exception occurred
     */
    public @Nullable Throwable getException() {
        return thrown;
    }
}

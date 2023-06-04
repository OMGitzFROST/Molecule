package com.moleculepowered.platform.bukkit.updater.event;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.platform.bukkit.event.AbstractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: 6/3/23 ADD JAVADOC
public final class UpdateFailedEvent extends AbstractEvent
{
    private final Updater updater;
    private final Throwable thrown;

    /*
    CONSTRUCTOR
     */

    // TODO: 5/28/23 JAVADOC
    public UpdateFailedEvent(boolean async, @NotNull Updater updater) {
        this(async, updater, null);
    }

    // TODO: 6/3/23 ADD JAVADOC
    public UpdateFailedEvent(boolean async, @NotNull Updater updater, @Nullable Throwable thrown) {
        super(async);
        this.updater = updater;
        this.thrown = thrown;
    }

    /**
     * Used to return an instance of the updater that is handling the update checks.
     *
     * @return an instance of the updater class
     */
    public @NotNull Updater getUpdater() {
        return updater;
    }

    // TODO: 6/3/23 ADD JAVADOC
    public @Nullable Throwable getException() {
        return thrown;
    }
}

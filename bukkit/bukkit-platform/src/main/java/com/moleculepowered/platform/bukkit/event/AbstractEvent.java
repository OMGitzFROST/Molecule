package com.moleculepowered.platform.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class representing a custom event in the Bukkit platform.
 * Extend this class to create custom events that can be listened to and handled.
 * This class extends the Bukkit {@link Event} class.
 *
 * @author OMGitzFROST
 */
public abstract class AbstractEvent extends Event
{
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * Constructs a new AbstractEvent.
     */
    public AbstractEvent() {
        super();
    }

    /**
     * Constructs a new AbstractEvent with the specified async flag.
     *
     * @param async true if the event should be processed asynchronously, false otherwise
     */
    public AbstractEvent(boolean async) {
        super(async);
    }

    /*
    GETTER METHODS
     */

    /**
     * Returns the list of handlers for this event.
     *
     * @return the list of handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Returns the list of handlers for this event.
     *
     * @return the list of handlers
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
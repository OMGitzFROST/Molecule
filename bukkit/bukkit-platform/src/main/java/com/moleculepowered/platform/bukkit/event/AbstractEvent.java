package com.moleculepowered.platform.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

// TODO: 5/28/23 JAVADOC
public abstract class AbstractEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    // TODO: 5/28/23 JAVADOC
    public AbstractEvent() {
        super();
    }

    // TODO: 5/28/23 JAVADOC
    public AbstractEvent(boolean async) {
        super(async);
    }

    /*
    GETTER METHODS
     */

    /**
     * Return's a list of handler's for this event
     *
     * @return a list of handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Return's a list of handler's for this event
     *
     * @return a list of handlers
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
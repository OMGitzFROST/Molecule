package com.moleculepowered.api.model;

/**
 * A model designed to provide service managers with the framework needed to properly execute their tasks.
 * Please note that child classes implementing this interface must inherit its methods manually.
 *
 * <p>The {@code Manager} interface provides two methods: {@link #onEnable()} and {@link #onDisable()}.
 * These methods are typically called when the manager is enabled or disabled, respectively, and can be
 * used to perform setup tasks and cleanup actions.</p>
 *
 * <p>Implementing classes should override these methods as necessary to add their own functionality.</p>
 *
 * @author OMGitzFROST
 */
public interface Manager
{
    /**
     * This method is typically used to perform necessary setup tasks for your manager, such as registering
     * event listeners, initializing configurations, setting up database connections, or starting scheduled tasks.
     * It should be called when the server loads your manager and is usually where you would initialize any resources
     * or functionality needed for your manager to operate correctly.
     */
    default void onEnable() {
    }

    /**
     * This method is typically called when the manager is being disabled or unloaded from the server. It's the
     * appropriate place to clean up any resources, save data, cancel tasks, or perform any necessary shutdown actions.
     */
    default void onDisable() {
    }
}

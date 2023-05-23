package com.moleculepowered.api.model;

/**
 * <p>A model designed to provide our service managers the framework needed to
 * properly execute their tasks.Please note that children classes implementing this
 * interface must inherit its methods manually.</p>
 *
 * @author OMGitzFROST
 */
public interface Manager {

    /**
     * This method is typically used to perform necessary setup tasks for your plugin, such as
     * registering event listeners, initializing configurations, setting up database connections,
     * or starting scheduled tasks. This method should be called when the server loads your plugin and
     * is usually where you would initialize any resources or functionality needed for your plugin to
     * operate correctly.
     */
    default void onEnable() {}

    /**
     * This method is typically called when the plugin is being disabled or unloaded from the server. It's the
     * appropriate place to clean up any resources, save data, cancel tasks, or perform any necessary
     * shutdown actions.
     */
    default void onDisable() {}
}

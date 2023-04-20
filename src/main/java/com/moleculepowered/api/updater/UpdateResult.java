package com.moleculepowered.api.updater;

public enum UpdateResult {

    /**
     * Indicates that the updater has determined that a newer version of this
     * plugin is available for download
     */
    UPDATE_AVAILABLE,
    /**
     * Indicates that there are no available updates available for this plugin, please note
     * that if beta versions are disabled, the updater would still mark the result to this.
     */
    LATEST,
    /**
     * Indicates that the updater has been disabled by the server owner and will prevent
     * the updater from checking for new updates
     */
    DISABLED,
    /**
     * Indicates that the result for the update check was unknown
     */
    UNKNOWN
}

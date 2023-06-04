package com.moleculepowered.api.updater;

/**
 * This enum class is designed to provide the different stages that can occur
 * within the {@link Updater} process.
 *
 * @author OMGitzFROST
 */
public enum UpdateResult
{
    /**
     * Indicates that the updater has been disabled by the server owner and will prevent
     * the updater from checking for new updates
     */
    DISABLED,
    /**
     * Indicates that a newer version was available, and a download was completed successfully.
     */
    DOWNLOADED,
    /**
     * Indicates that an update is available, but it has already been downloaded
     */
    EXISTS,
    /**
     * Indicates that there are no available updates available for this plugin, please note
     * that if beta versions are disabled, the updater would still mark the result to this.
     */
    LATEST,
    /**
     * Indicates that the updater has determined that a newer version of this
     * plugin, but the updater is unable to download it using the download link
     * provided.
     */
    UPDATE_AVAILABLE
}

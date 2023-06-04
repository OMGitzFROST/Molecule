package com.moleculepowered.platform.bungee.updater;

import com.moleculepowered.api.updater.UpdateResult;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.util.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class BungeeUpdater extends Updater
{
    /**
     * Creates a constructor accepting the parent data folder for the updater as well
     * as the current plugin version. Please note that this version should be the one
     * currently installed on your server before updating and should not point to
     * a version from a remote source.
     *
     * @param dataFolder     The parent folder for the updater
     * @param currentVersion The current version of your installed plugin
     */
    public BungeeUpdater(@NotNull File dataFolder, @Nullable String currentVersion) {
        super(dataFolder, currentVersion);
    }

    /**
     * Creates a constructor accepting the parent data folder for the updater as well
     * as the current plugin version. Please note that this version should be the one
     * currently installed on your server before updating and should not point to
     * a version from a remote source.
     *
     * @param dataFolder     The parent folder for the updater
     * @param currentVersion The current version of your installed plugin
     */
    public BungeeUpdater(@NotNull File dataFolder, @Nullable ComparableVersion currentVersion) {
        super(dataFolder, currentVersion);
    }

    /**
     * Used to schedule the periodic update check for a later time defined by the {@link #interval} provided.
     * Please note that by default, this method will schedule updates every 3 hours if an interval
     * was not set prior.
     *
     * @see #initialize()
     */
    @Override
    public void schedule() {

    }

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version, please note that this method forces
     * the update check upon invoking.
     *
     * @see #schedule()
     */
    @Override
    public void initialize() {

    }

    /**
     * This method provides the core functionality of the updater class, initializing
     * the updater and fetching the latest version, please note that this method forces
     * the update check upon invoking. THis method also allows you to indicate
     * whether its logic will run on an async thread.
     *
     * @param async whether this method will run on an async thread
     * @see #schedule()
     */
    @Override
    public void initialize(boolean async) {

    }

    /**
     * Returns the latest release version retrieved by this updater.
     *
     * @return the latest release version
     */
    @Override
    public @Nullable ComparableVersion getLatestVersion() {
        return null;
    }

    /**
     * Returns the active provider that contains the latest release artifact, please
     * note that this method could return null, for example, when 0 providers are added
     * to this updater.
     *
     * @return the provider containing the latest release artifact
     */
    @Override
    public @Nullable AbstractProvider getProvider() {
        return null;
    }

    @Override
    public <T> void sendNotification(T player, UpdateResult result) {

    }

    @Override
    public void sendNotification(UpdateResult result) {

    }
}

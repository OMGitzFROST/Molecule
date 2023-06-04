package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.platform.PlatformConsole;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A bukkit implementation for the {@link PlatformConsole} class, it provides
 * platform-specific methods that further extend the console platform.
 *
 * @author OMGitzFROST
 */
public final class BukkitConsole extends PlatformConsole
{
    /*
    CONSTRUCTORS
     */

    /**
     * The main constructor for the BukkitConsole class, it's tasked with initializing
     * all required objects for this bukkit component.
     *
     * @param plugin Parent plugin
     */
    public BukkitConsole(@NotNull Plugin plugin) {
        super(plugin.getName());
    }

    /*
    LOGGING METHODS
     */

    /**
     * <p>Prints a {@link BaseComponent} to console, this method is adapted to work
     * on multiple platform versions.</p>
     *
     * @param components Target components
     */
    public void log(@NotNull BaseComponent... components) {
        Arrays.stream(components).forEach(bc -> super.log(bc.toLegacyText()));
    }
}

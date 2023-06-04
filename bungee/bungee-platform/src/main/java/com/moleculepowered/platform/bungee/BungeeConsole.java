package com.moleculepowered.platform.bungee;

import com.moleculepowered.api.MoleculePlugin;
import com.moleculepowered.api.platform.PlatformConsole;
import org.jetbrains.annotations.NotNull;

public class BungeeConsole extends PlatformConsole
{
    public BungeeConsole(@NotNull MoleculePlugin plugin) {
        super(plugin.getName());
    }
}

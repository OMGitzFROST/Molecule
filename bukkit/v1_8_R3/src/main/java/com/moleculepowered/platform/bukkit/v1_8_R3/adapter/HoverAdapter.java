package com.moleculepowered.platform.bukkit.v1_8_R3.adapter;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Used to adapt hover events for Spigot 1.8.x
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public final class HoverAdapter implements com.moleculepowered.platform.bukkit.adapter.HoverAdapter
{
    /**
     * An adapter method used to display a text when a component is hovered over.
     *
     * @param value Text displayed on hover
     * @return An adapted hover event
     */
    @Override
    public @NotNull HoverEvent showText(Object value) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(join(value)).create());
    }
}

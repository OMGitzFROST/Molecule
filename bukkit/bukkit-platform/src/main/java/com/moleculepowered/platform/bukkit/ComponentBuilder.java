package com.moleculepowered.platform.bukkit;

import com.moleculepowered.api.adapter.HoverAdapter;
import com.moleculepowered.platform.bukkit.model.BukkitNMSBridge;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.moleculepowered.api.util.StringUtil.format;

// TODO: 6/4/23 ADD JAVADOC
public class ComponentBuilder
{
    private final HoverAdapter<HoverEvent> hover = BukkitNMSBridge.adaptHover();
    private final net.md_5.bungee.api.chat.ComponentBuilder builder;

    /*
    CONSTRUCTORS
     */

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder(String text) {
        builder = new net.md_5.bungee.api.chat.ComponentBuilder(text);
    }

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder() {
        this("");
    }

    /*
    CHAIN METHODS
     */

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder append(String input, Object... param) {
        return append(true, input, param);
    }

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder append(boolean condition, String input, Object... param) {
        builder.append(condition && input != null ? colorize(input, param) : "", net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention.FORMATTING);
        return this;
    }

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder append(boolean condition, BaseComponent... component) {
        Arrays.stream(component).forEach(c -> {
            if (condition) {
                builder.append(c.toLegacyText(), net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention.FORMATTING);
                builder.event(c.getHoverEvent());
                builder.event(c.getClickEvent());
            }
        });
        return this;
    }

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder append(BaseComponent... component) {
        return append(true, component);
    }

    /*
    COMPONENT EVENTS
     */

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder setHoverEvent(String value) {
        builder.event(hover.showText(value));
        return this;
    }

    // TODO: 6/4/23 ADD JAVADOC
    public ComponentBuilder setClickEvent(ClickEvent.Action action, String value) {
        builder.event(new ClickEvent(action, value));
        return this;
    }

    /*
    BASE COMPONENT BUILDER
     */

    // TODO: 6/4/23 ADD JAVADOC
    public BaseComponent[] create() {
        return builder.create();
    }

    /*
    UTILITY METHODS
     */

    // TODO: 6/4/23 ADD JAVADOC
    private @NotNull String colorize(String input, Object... param) {
        input = input.replace(ChatColor.COLOR_CHAR, '&');
        return format(ChatColor.translateAlternateColorCodes('&', input), param);
    }
}

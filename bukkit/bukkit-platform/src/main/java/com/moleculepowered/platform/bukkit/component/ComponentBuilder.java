package com.moleculepowered.platform.bukkit.component;

import com.moleculepowered.platform.bukkit.adapter.HoverAdapter;
import com.moleculepowered.platform.bukkit.model.BukkitNMSBridge;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.moleculepowered.api.util.StringUtil.format;

/**
 * An expansion to the {@link net.md_5.bungee.api.chat.ComponentBuilder} class, it provides
 * methods and upgrades to allow cross-version support and auto colorization for components.
 *
 * @author OMGitzFROST
 */
public class ComponentBuilder
{
    private final HoverAdapter hover = BukkitNMSBridge.adaptHover();
    private final net.md_5.bungee.api.chat.ComponentBuilder builder;

    /*
    CONSTRUCTORS
     */

    /**
     * The main constructor used to create a new instance of the {@link ComponentBuilder} class
     *
     * @param text Initial Text
     */
    public ComponentBuilder(String text, Object... param) {
        builder = new net.md_5.bungee.api.chat.ComponentBuilder(colorize(format(text, param)));
    }

    /**
     * Creates an empty component builder, please note that any changes made to this component
     * without calling adding text will create an empty component.
     */
    public ComponentBuilder() {
        this("");
    }

    /*
    CHAIN METHODS
     */

    /**
     * A method used to append a string to the tail end of the current component.
     *
     * @param input Provided input
     * @param param Optional Parameters
     * @return An instance of this component builder
     */
    public ComponentBuilder append(String input, Object... param) {
        return append(true, input, param);
    }

    /**
     * A method used to append a string to the tail end of the current component if the condition
     * is met, otherwise this method will append an empty string.
     *
     * @param condition Required condition
     * @param input     Provided input
     * @param param     Optional Parameters
     * @return An instance of this component builder
     */
    public ComponentBuilder append(boolean condition, String input, Object... param) {
        builder.append(condition && input != null ? colorize(input, param) : "", net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention.FORMATTING);
        return this;
    }

    /**
     * A method used to append an array of base components if the provided condition is true, all
     * components added using this method will be applied to the tail end of the current component.
     *
     * @param condition Required condition
     * @param component Target component(s)
     * @return An instance of this component builder
     */
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

    /**
     * A method used to append an array of base components to this builder, all components
     * added using this method will be applied to the tail end of the current component.
     *
     * @param component Target component(s)
     * @return An instance of this component builder
     */
    public ComponentBuilder append(BaseComponent... component) {
        return append(true, component);
    }

    /**
     * Used to apply a hover event to the current component within this builder
     *
     * @param action Hover event action
     * @param value  Hover event value (text)
     * @return An instance of this component builder
     */
    public ComponentBuilder setHoverEvent(HoverEvent.@NotNull Action action, Object value) {
        switch (action) {
            case SHOW_ITEM:
                builder.event(hover.showItem((ItemStack) value));
                break;
            case SHOW_ENTITY:
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, (BaseComponent[]) value));
                break;
            case SHOW_ACHIEVEMENT:
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, (BaseComponent[]) value));
                break;
            default:
                builder.event(hover.showText(value));
                break;
        }
        return this;
    }

    /**
     * Used to apply a click event to the current component within the builder
     *
     * @param action Click event action
     * @param value  Event value (url, command, etc)
     * @return An instance of this component builder
     */
    public ComponentBuilder setClickEvent(ClickEvent.Action action, String value) {
        builder.event(new ClickEvent(action, colorize(value)));
        return this;
    }

    /*
    BASE COMPONENT BUILDER
     */

    /**
     * Used to convert the component builder into a base component array
     *
     * @return A base component array
     */
    public BaseComponent[] create() {
        return builder.create();
    }

    /*
    UTILITY METHODS
     */

    /**
     * A utility used to apply bukkit color codes to the provided input
     *
     * @param input Provided input
     * @param param Optional parameters
     * @return A colorized messages string
     */
    private @NotNull String colorize(String input, Object... param) {
        char COLOR_CHAR = ChatColor.COLOR_CHAR;
        input = input != null ? input.replace(COLOR_CHAR, '&') : "";
        return format(ChatColor.translateAlternateColorCodes('&', input), param);
    }
}

package com.moleculepowered.api.adapter;

import org.jetbrains.annotations.NotNull;

/**
 * An adapter class that is used to provide cross-platform compatability for the bukkit platform, this
 * adapter is intended to be used for HoverEvents.
 *
 * @param <T> The object this adapter will adapt to
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public interface HoverAdapter<T>
{
    /**
     * An adapter method used to display a text when a component is hovered over.
     *
     * @param value Text displayed on hover
     * @return An adapted hover event
     */
    @NotNull T showText(String value);

    /**
     * An adapter method used to display an item when a component is hovered over. Please note
     * that this method will throw an {@link IllegalArgumentException} when the provided object
     * is not an ItemStack.
     *
     * @param item The ItemStack to display
     * @return An adapted hover event
     * @throws IllegalArgumentException thrown when an invalid object is provided
     */
    @NotNull <V> T showItem(V item);

    /**
     * An adapter method used to display an entity when a component is hovered over.
     *
     * @param entity The entity to display
     * @return An adapted hover event
     */
    @NotNull <V> T showEntity(V entity);

    /**
     * An adapter method used to display an achievement when a component is hovered over.
     *
     * @param name Achievement name to display
     * @return An adapted hover event
     */
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    @NotNull T showAchievement(String name);
}

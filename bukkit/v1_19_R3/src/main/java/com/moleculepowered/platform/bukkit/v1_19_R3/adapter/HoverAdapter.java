package com.moleculepowered.platform.bukkit.v1_19_R3.adapter;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Used to adapt hover events for Spigot 1.19.x
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public final class HoverAdapter implements com.moleculepowered.api.adapter.HoverAdapter<HoverEvent>
{
    /**
     * An adapter method used to display a text when a component is hovered over.
     *
     * @param value Text displayed on hover
     * @return An adapted hover event
     */
    @Override
    public @NotNull HoverEvent showText(String value) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(value));
    }

    /**
     * An adapter method used to display an item when a component is hovered over. Please note
     * that this method will throw an {@link IllegalArgumentException} when the provided object
     * is not an ItemStack.
     *
     * @param item The ItemStack to display
     * @return An adapted hover event
     * @throws IllegalArgumentException thrown when an invalid object is provided
     */
    @Override
    public @NotNull <V> HoverEvent showItem(V item) {

        if (!(item instanceof org.bukkit.inventory.ItemStack))
            throw new IllegalArgumentException("The object you provided must be an ItemStack");

        String         itemName  = ((org.bukkit.inventory.ItemStack) item).getType().name().toLowerCase();
        ItemStack      itemStack = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item);
        NBTTagCompound nbtTag    = itemStack.u() != null ? itemStack.u() : new NBTTagCompound();
        ItemTag        tag       = ItemTag.ofNbt(nbtTag.toString());

        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, new Item(itemName, 1, tag));
    }

    /**
     * An adapter method used to display an entity when a component is hovered over.
     *
     * @param entity The entity to display
     * @return An adapted hover event
     */
    @Override
    public @NotNull <V> HoverEvent showEntity(V entity) {

        if (!(entity instanceof org.bukkit.entity.Entity))
            throw new IllegalArgumentException("The object you provided must be an Entity");

        String entityType = ((org.bukkit.entity.Entity) entity).getType().getKey().toString();
        String entityID   = ((org.bukkit.entity.Entity) entity).getUniqueId().toString();
        String entityName = ((org.bukkit.entity.Entity) entity).getName();

        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new Entity(entityType, entityID, new TextComponent(entityName)));
    }

    /**
     * An adapter method used to display an achievement when a component is hovered over.
     *
     * @param name Achievement name to display
     * @return An adapted hover event
     */
    @Override
    @Deprecated
    public @NotNull HoverEvent showAchievement(String name) {
        return new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ComponentBuilder(name).create());
    }
}

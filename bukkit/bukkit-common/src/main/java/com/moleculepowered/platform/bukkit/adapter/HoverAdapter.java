package com.moleculepowered.platform.bukkit.adapter;

import com.moleculepowered.api.util.MapUtil;
import com.moleculepowered.platform.bukkit.StringUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.moleculepowered.api.util.StringUtil.format;

/**
 * An adapter class used to provide cross-platform compatibility for the Bukkit platform. This
 * adapter is intended to be used for HoverEvents.
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public interface HoverAdapter
{
    /**
     * Displays a text when a component is hovered over.
     *
     * @param value The text displayed on hover
     * @return An adapted hover event
     */
    @NotNull HoverEvent showText(Object value);

    /**
     * Displays an item when a component is hovered over. Please note that this method will throw an
     * {@link IllegalArgumentException} when the provided object is not an ItemStack.
     *
     * @param item The ItemStack to display
     * @return An adapted hover event
     * @throws IllegalArgumentException Thrown when an invalid object is provided
     */
    default @NotNull HoverEvent showItem(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        String itemName = null;
        String enchants = null;
        String lore = null;
        String author = null;

        if (meta != null) {
            Map<Enchantment, Integer> enchantments = meta.getEnchants();
            List<String> loreLines = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

            StringBuilder eb = new StringBuilder();
            StringBuilder lb = new StringBuilder(!enchantments.isEmpty() && !loreLines.isEmpty() ? "\n" : "");

            // BUILD ENCHANTMENT SECTION
            enchantments.forEach((enchantment, level) -> {
                String roman = StringUtil.intToRoman(level);
                String key = WordUtils.capitalize(enchantment.getName().toLowerCase().replace("_", " "));
                String displayFormat = "&7{0} &a{1}&r";

                if (MapUtil.isLastEntry(enchantments, enchantment)) eb.append(format(displayFormat, key, roman));
                else eb.append(format(displayFormat, key, roman)).append("\n");
            });

            // BUILD LORE SECTION
            loreLines.forEach(s -> {
                if (loreLines.indexOf(s) != (loreLines.size() - 1)) lb.append(s).append("\n");
                else lb.append(s);
            });

            // COMPILE SECTIONS
            enchants = !eb.toString().equals("") ? eb.toString() : null;
            lore = !lb.toString().equals("") ? "&6" + lb + "&r" : null;
            itemName = !meta.getDisplayName().equals("") ? "&b" + meta.getDisplayName() + "&r" : null;
            author = (meta instanceof BookMeta) ? "&8By " + ((BookMeta) meta).getAuthor() + "&r\n" : null;
        }
        return showText(StringUtil.colorize(join(itemName, author, enchants, lore)));
    }

    /**
     * Joins an array of objects into a string, please note that this method
     * allows null values, but all null values will be removed from the final string.
     *
     * @param content Content array
     * @return A joined string
     */
    default String join(@Nullable Object... content) {
        String rawString = StringUtils.join(Arrays.asList(content), "\n").replaceAll("\n$", "");
        return ChatColor.translateAlternateColorCodes('&', rawString);
    }
}
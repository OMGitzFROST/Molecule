package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A utility class that provides additional methods for manipulating {@link Map} objects,
 * including accessing map entries at specific indexes and other similar functionalities.
 *
 * <p>This class offers utility methods that are not typically provided by classes implementing
 * the {@link Map} interface.</p>
 *
 * @author OMGitzFROST
 */
public final class MapUtil
{
    /**
     * Retrieves the entry at the specified index from the given map. Please note that the behavior of this method
     * may vary depending on the map implementation. If the map does not maintain insertion order by default, the
     * returned value may not correspond to the expected entry. If no entry exists at the provided index, a null
     * entry will be returned.
     *
     * <p>Please note that indexes start from 0. To retrieve the first entry, use index 0; for the second entry, use
     * index 1, and so on.</p>
     *
     * @param <K>   The type of keys maintained by the map.
     * @param <V>   The type of mapped values.
     * @param map   The parent map from which to retrieve the entry.
     * @param index The index of the target entry.
     * @return The entry at the specified index, or null if no entry exists at the index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public static @NotNull <K, V> Map.Entry<K, V> getEntry(@NotNull Map<K, V> map, int index) {
        int currentIndex = 0;
        for (Map.Entry<K, V> currentEntry : map.entrySet()) if (currentIndex++ == index) return currentEntry;
        throw new IndexOutOfBoundsException("The provided map does not contain an entry at the provided index: " + index);
    }

    /**
     * Retrieves the last entry from the given map. Please note that the behavior of this method
     * may vary depending on the map implementation. If the map does not maintain insertion order by default, the
     * returned value may not correspond to the expected entry.
     *
     * @param map The parent map from which to retrieve the entry.
     * @return The last entry from the map.
     * @throws NoSuchElementException if the map is empty.
     */
    public static @NotNull <K, V> Map.Entry<K, V> getLastEntry(@NotNull Map<K, V> map) {
        if (map.isEmpty()) throw new NoSuchElementException("The provided map does not contain any entries");
        return getEntry(map, map.size() - 1);
    }

    /**
     * Retrieves the first entry from the given map. Please note that the behavior of this method
     * may vary depending on the map implementation. If the map does not maintain insertion order by default, the
     * returned value may not correspond to the expected entry.
     *
     * @param map The parent map from which to retrieve the entry.
     * @return The first entry from the map.
     * @throws NoSuchElementException if the map is empty.
     */
    public static @NotNull <K, V> Map.Entry<K, V> getFirstEntry(@NotNull Map<K, V> map) {
        if (map.isEmpty()) throw new NoSuchElementException("The provided map does not contain any entries");
        return getEntry(map, 0);
    }

    /**
     * Returns true if the provided key is the last entry within a map. Please note that the behavior of this method
     * may vary depending on the map implementation. If the map does not maintain insertion order by default, the
     * returned value may not correspond to the expected entry.
     *
     * @param map The parent map from which to retrieve the entry.
     * @return The first entry from the map.
     * @throws NoSuchElementException if the map is empty.
     */
    public static <K> boolean isLastEntry(Map<K, ?> map, K key) {
        return getLastEntry(map).getKey().equals(key);
    }
}

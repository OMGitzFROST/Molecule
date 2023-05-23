package com.moleculepowered.api.user;

import com.moleculepowered.api.model.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A service manager designed to handle all tasks related to user management, allowing developers
 * to retrieve, add, and remove user's from the user collection.
 *
 * @author OMGitzFROST
 */
public interface UserManager extends Manager {

    /**
     * Adds a new user to the user collection, this method will return true
     * if the user was added without failure, otherwise it will return false.
     *
     * @param user Target user
     * @return true if the user was added without fail
     */
    boolean addUser(@NotNull User user);

    /**
     * Removes a new user from the user collection, this method will return true
     * if the user was removed without failure, otherwise it will return false.
     *
     * @param user Target user
     * @return true if the user was removed without fail
     */
    boolean removeUser(@NotNull User user);

    /**
     * Returns a collection of users handled by this manager. Note that users are NOT
     * loaded into this collection by default but should typically be loaded using the
     * {@link #onEnable()} method.
     *
     * @return A collection of users
     */
    @NotNull Collection<User> getUsers();

    /*
    DEFAULT METHODS
     */

    /**
     * Returns a user based on the name provided, using a filter system; this method will attempt to locate a
     * user with the matching name. Otherwise, if a user is not found, this will throw an exception.
     *
     * @param name Target name
     * @return A user based on the name provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     * @apiNote This method is case-insensitive so any input could return a user
     */
    default @NotNull User getUser(String name) {
        return getUser(user -> user.getName().equalsIgnoreCase(name));
    }

    /**
     * Returns a user based on the uuid provided, using a filter system; this method will attempt to locate a
     * user with the matching uuid. Otherwise, if a user is not found, this will throw an exception.
     *
     * @param uuid Target {@link UUID}
     * @return A user based on the uuid provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    default @NotNull User getUser(UUID uuid) {
        return getUser(user ->  user.getUniqueId().equals(uuid));
    }

    /**
     * Return's a user based on a specific filter, please note that this method will not return a null user,
     * but if it cannot find a user using the provided filter, this method will throw an exception.
     *
     * @param filter User filter
     * @return A user based on the filter provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    default @NotNull User getUser(Predicate<User> filter) {
        return getUsers().stream().filter(filter).findFirst().orElseThrow(NullPointerException::new);
    }
}

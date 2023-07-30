package com.moleculepowered.api.user;

import com.moleculepowered.api.model.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A service manager designed to handle all tasks related to user management, allowing developers
 * to retrieve, add, and remove users from the user collection.
 *
 * @author OMGitzFROST
 */
public abstract class UserManager implements Manager
{
    protected final Set<User> users = new HashSet<>();

    /**
     * Adds a new user to the user collection. This method returns true if the user was added
     * without failure, otherwise it returns false.
     *
     * @param user the target user
     * @return true if the user was added without fail
     */
    public boolean addUser(@NotNull User user) {
        return users.add(user);
    }

    /**
     * Removes a user from the user collection. This method returns true if the user was removed
     * without failure, otherwise it returns false.
     *
     * @param user the target user
     * @return true if the user was removed without fail
     */
    public boolean removeUser(@NotNull User user) {
        return users.remove(user);
    }

    /**
     * Returns a collection of users handled by this manager. Note that users are NOT
     * loaded into this collection by default but should typically be loaded using the
     * {@link #onEnable()} method.
     *
     * @return a collection of users
     */
    public @NotNull Collection<User> getUsers() {
        return users;
    }

    /*
    DEFAULT METHODS
     */

    /**
     * Returns a user based on the name provided, using a filter system. This method attempts to locate a
     * user with the matching name. If a user is not found, it throws an exception.
     * <p>
     * This method is case-insensitive, so any input could return a user.
     *
     * @param name the target name
     * @return a user based on the name provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    public @NotNull User getUser(String name) {
        return getUser(user -> user.getName().equalsIgnoreCase(name));
    }

    /**
     * Returns a user based on the UUID provided, using a filter system. This method attempts to locate a
     * user with the matching UUID. If a user is not found, it throws an exception.
     *
     * @param uuid the target UUID
     * @return a user based on the UUID provided
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    public @NotNull User getUser(UUID uuid) {
        return getUser(user -> user.getUniqueId().equals(uuid));
    }

    /**
     * Returns a user based on a specific filter. If a user cannot be found using the provided filter,
     * this method throws an exception.
     *
     * @param filter the user filter
     * @return a user based on the provided filter
     * @throws NullPointerException when a user cannot be found with the provided filter.
     */
    public @NotNull User getUser(Predicate<User> filter) {
        return getUsers().stream().filter(filter).findFirst().orElseThrow(NullPointerException::new);
    }
}
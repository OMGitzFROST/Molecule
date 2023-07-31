package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A utility class that provides methods for validating conditions and values.
 *
 * @author OMGitzFROST
 */
public final class Validate
{

    /**
     * Checks if the given expression is true, otherwise throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param expression the boolean expression to evaluate
     * @param message    the error message to be used in the exception
     * @throws IllegalArgumentException if the expression is false
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) throw new IllegalArgumentException(message);
    }

    /**
     * Checks if the given expression is false, otherwise throws a {@link RuntimeException} with the provided message.
     *
     * @param expression the boolean expression to evaluate
     * @param message    the error message to be used in the exception
     * @throws RuntimeException if the expression is true
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) throw new RuntimeException(message);
    }

    /**
     * Checks if the given collection is not empty, otherwise throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param collection the collection to check for emptiness
     * @param message    the error message to be used in the exception
     * @throws IllegalArgumentException if the collection is empty
     */
    public static <T extends Collection<?>> void notEmpty(@NotNull T collection, String message) {
        if (collection.isEmpty()) throw new IllegalArgumentException(message);
    }

    /**
     * Checks if the given collection is empty, otherwise throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param collection the collection to check for emptiness
     * @param message    the error message to be used in the exception
     * @throws IllegalArgumentException if the collection is not empty
     */
    public static <T extends Collection<?>> void isEmpty(@NotNull T collection, String message) {
        if (!collection.isEmpty()) throw new IllegalArgumentException(message);
    }

    /**
     * Checks if the given object is not null, otherwise throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param object  the object to check for nullity
     * @param message the error message to be used in the exception
     * @param <T>     the type of the object
     * @throws IllegalArgumentException if the object is null
     */
    public static <T> void notNull(T object, String message) {
        if (object == null) throw new IllegalArgumentException(message);
    }

    /**
     * Checks if the given object is null, otherwise throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param object  the object to check for nullity
     * @param message the error message to be used in the exception
     * @param <T>     the type of the object
     * @throws IllegalArgumentException if the object is not null
     */
    public static <T> void isNull(T object, String message) {
        if (!(object == null)) throw new IllegalArgumentException(message);
    }

    /**
     * Checks if the given object is an instance of the specified target class, otherwise throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param clazz   the object to check for the instance
     * @param target  the target class to check against
     * @param message the error message to be used in the exception
     * @param <T>     the type of the object
     * @throws IllegalArgumentException if the object is not an instance of the target class
     */
    public static <T> void isInstance(@NotNull T clazz, @NotNull Class<?> target, String message) {
        if (!target.isInstance(clazz)) throw new IllegalArgumentException(message);
    }
}
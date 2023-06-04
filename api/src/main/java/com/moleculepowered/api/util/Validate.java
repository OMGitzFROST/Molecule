package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

// TODO: 6/3/23 ADD JAVADOC
public final class Validate
{
    // TODO: 6/3/23 ADD JAVADOC
    public static void isTrue(boolean expression, Throwable thrown) {
        if (!expression) thrown.printStackTrace();
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) throw new IllegalArgumentException(message);
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "The expression is not true");
    }

    // TODO: 6/3/23 ADD JAVADOC
    public static void isFalse(boolean expression, Throwable thrown) {
        if (expression) thrown.printStackTrace();
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) throw new RuntimeException(message);
    }

    public static <T> void isInstance(@NotNull T clazz, @NotNull Class<?> target) {
        if (!target.isInstance(clazz)) throw new IllegalArgumentException("Not instance");
    }

    public static void notEmpty(@NotNull Collection<?> collection) {
        if (collection.isEmpty()) throw new IllegalArgumentException("Collection empty");
    }

    // TODO: 6/3/23 ADD JAVADOC
    public static void notEmpty(@NotNull Collection<?> collection, Throwable thrown) {
        if (collection.isEmpty()) thrown.printStackTrace();
    }

    public static void notEmpty(@NotNull Collection<?> collection, String message) {
        if (collection.isEmpty()) throw new IllegalArgumentException(message);
    }
}

package com.moleculepowered.api;

import com.moleculepowered.api.adapter.ConfigAdapter;
import com.moleculepowered.api.adapter.PlayerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NMSBridge {

    public static @Nullable PlayerAdapter player() {
        try {
            Class<?> classDefinition = Class.forName(getPackage(PlayerAdapter.class));
            Constructor<?> cons = classDefinition.getConstructor();
            return (PlayerAdapter) cons.newInstance();
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static @Nullable ConfigAdapter<?> config() {
        try {
            Class<?> classDefinition = Class.forName(getPackage(ConfigAdapter.class));
            Constructor<?> cons = classDefinition.getConstructor();
            return (ConfigAdapter<?>) cons.newInstance();
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static @NotNull String getPackage(@NotNull Class<?> clazz) {
        String rawV = Bukkit.getServer().getClass().getPackage().getName();
        String finalV = rawV.substring(rawV.lastIndexOf('.') + 1);
        return "com.moleculepowered.api." + clazz.getName() + "_" + finalV;
    }
}

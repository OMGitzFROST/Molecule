package com.moleculepowered.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Test1_19 test1_19 = new Test1_19();
        test1_19.test();

        Test1_8 test1_8 = new Test1_8();
        test1_8.test();

        getLogger().log(Level.INFO, "Uber jar enabled");
    }
}

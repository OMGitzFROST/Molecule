package com.moleculepowered.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.moleculepowered.api.TestPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;

public abstract class AbstractMock {

    private static final ServerMock server;
    private static final TestPlugin plugin;

    // INITIALIZE THE REQUIRED MOCK ITEMS BEFORE SETUP
    static {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(TestPlugin.class);
    }

    protected ServerMock getServer() {
        return server;
    }

    protected TestPlugin getPlugin() {
        return plugin;
    }

    @AfterAll
    @DisplayName("Tear down the server and mock plugin once all tests are complete")
    public static void tearDown() {
        MockBukkit.unmock();
    }
}

package com.moleculepowered.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.moleculepowered.api.TestPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The parent class that all test cases must extend, this class is used to initialize
 * a fake server and allow testing to work as intended.
 */
public abstract class AbstractTest {

    protected static ServerMock server;
    protected static TestPlugin plugin;

    /**
     * Called before all other test's initializes the mock plugin and server
     */
    @BeforeAll
    @DisplayName("Initialize the mock plugin and server")
    public static void initialize() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(TestPlugin.class);
    }

    @AfterAll
    @DisplayName("Tear down the server and mock plugin once all tests are complete")
    public static void tearDown() {
        MockBukkit.unmock();
    }


    @Test
    @DisplayName("Simply allows me to initialize tests from this class")
    public void emptyTest() {
    }
}

package com.moleculepowered.test.updater;

import com.moleculepowered.api.event.updater.UpdateCompleteEvent;
import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.BukkitProvider;
import com.moleculepowered.api.updater.provider.GithubProvider;
import com.moleculepowered.api.updater.provider.SpigetProvider;
import com.moleculepowered.api.updater.provider.SpigotProvider;
import com.moleculepowered.api.util.Time;
import com.moleculepowered.test.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class TestUpdater extends AbstractTest {

    private static Updater updater;

    @BeforeAll
    @DisplayName("Initialize the main updater object before the main tests")
    public static void initializeUpdater() {
        // INITIALIZE UPDATER
        updater = new Updater(plugin)
                .addProvider(new GithubProvider("EssentialsX/Essentials"))
                .addProvider(new SpigetProvider(9089))
                .addProvider(new SpigotProvider(9089))
                .addProvider(new BukkitProvider(93271));
        updater.initialize();
    }

    @Test
    @DisplayName("Ensure updater is working as intended")
    public void testUpdater() {

        // ASSERT INTERVAL WAS PARSED CORRECTLY AND CORRECTLY MIRRORS DEFAULT
        Assertions.assertEquals(updater.getInterval(), Time.parseInterval("3h"));

        // ASSERT REQUIREMENTS ARE NOT NULL
        Assertions.assertNotNull(updater.getResult());
        Assertions.assertNotNull(updater.getPermission());

        // ASSERT BETA IS NOT ENABLED SINCE IT WAS NOT ENABLED IN THE CHAIN
        Assertions.assertFalse(updater.isBetaEnabled());

        // ASSERT UPDATER IS ENABLED BY DEFAULT
        Assertions.assertTrue(updater.isEnabled());

        // ENSURE UPDATE COMPLETE EVENT IS FIRED
        server.getPluginManager().assertEventFired(UpdateCompleteEvent.class);
    }

    @Test
    @DisplayName("Ensure updater runs well on the main thread and and async thread")
    public void testScheduler() {
        server.getScheduler().runTaskTimer(plugin, () -> updater.initialize(), 0, Time.parseInterval("5h"));
        server.getScheduler().runTaskTimerAsynchronously(plugin, () -> updater.initialize(true), 0, Time.parseInterval("5h"));
    }
}

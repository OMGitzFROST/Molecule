package com.moleculepowered.test.cases;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.BukkitProvider;
import com.moleculepowered.api.updater.provider.GithubProvider;
import com.moleculepowered.api.updater.provider.SpigetProvider;
import com.moleculepowered.api.updater.provider.SpigotProvider;
import com.moleculepowered.api.util.Time;
import com.moleculepowered.test.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class TestUpdater extends AbstractTest {

    @Test
    @DisplayName("Ensure update configures itself correctly")
    public void testUpdater() {

        Updater updater = new Updater(plugin)
                .addProvider(new GithubProvider("EssentialsX/Essentials"))
                .addProvider(new SpigetProvider(9089))
                .addProvider(new SpigotProvider(9089))
                .addProvider(new BukkitProvider(93271));
        updater.initialize();

        // ASSERT DEFAULTS ARE INTACT
        Assertions.assertEquals(updater.getInterval(), Time.parseInterval("3h"));
        Assertions.assertNotNull(updater.getResult());
        Assertions.assertNotNull(updater.getPermission());
        Assertions.assertFalse(updater.isBetaEnabled());
        Assertions.assertTrue(updater.isEnabled());
    }
}

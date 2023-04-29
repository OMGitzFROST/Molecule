package com.moleculepowered.test.cases;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import com.moleculepowered.api.updater.provider.BukkitProvider;
import com.moleculepowered.api.updater.provider.GithubProvider;
import com.moleculepowered.api.updater.provider.PolymartProvider;
import com.moleculepowered.api.updater.provider.SpigetProvider;
import com.moleculepowered.api.updater.provider.SpigotProvider;
import com.moleculepowered.test.AbstractTest;
import org.apache.commons.lang.Validate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestUpdater extends AbstractTest {

    @Test
    @DisplayName("Test updater and all our default providers")
    public void testUpdater() {

        // SETS ALL PROVIDER
        Updater updater = new Updater(plugin)
                .addProvider(new GithubProvider("EssentialsX/Essentials"))
                .addProvider(new SpigetProvider(9089))
                .addProvider(new SpigotProvider(1234))
                .addProvider(new BukkitProvider(98985))
                .addProvider(new PolymartProvider(4));
        updater.initialize();

        // TEST EACH PROVIDER TO ENSURE ALL REQUIREMENTS ARE MET
        for (AbstractProvider provider : updater.getProviders()) {

            // VALIDATE PROVIDER
            Validate.notNull(provider.getName());
            Validate.notNull(provider.getRelease());
            Validate.notNull(provider.getDownloadLink());
            Validate.notNull(provider.getChangelogLink());

            // VALIDATE FETCHED RELEASE
            Validate.notNull(provider.getRelease().getVersion());
            Validate.notNull(provider.getRelease().getIdentifier());
            Validate.notNull(provider.getRelease());
        }
    }
}

---
description: Responsible for handling update checks for some of the major platforms
---

# Providers

## Overview

Welcome to the provider's page for our updater, these providers are the most important piece that makes our updater different, they provide developers with an easy-to-use way to access release information from any platform they wish.

We believe in making our products beginner friendly, therefore we have provided our users with default classes that let them access updates from some of the most popular platform's out there. Please check below to see information about each provider

#### Default Providers

<table><thead><tr><th>Provider Name</th><th data-type="rating" data-max="5">Speed</th><th align="center">Download Link</th><th align="center">Changelog Link</th><th align="center">Donate Link</th></tr></thead><tbody><tr><td>Github</td><td>1</td><td align="center"><strong>✅</strong></td><td align="center"><strong>✅</strong></td><td align="center"><strong>⛔️</strong></td></tr><tr><td>Spiget</td><td>3</td><td align="center"><strong>✅</strong></td><td align="center"><strong>✅</strong></td><td align="center"><strong>✅</strong></td></tr><tr><td>Polymart</td><td>5</td><td align="center"><strong>✅</strong></td><td align="center"><strong>✅</strong></td><td align="center"><strong>⛔️</strong></td></tr><tr><td>Spigot</td><td>4</td><td align="center"><strong>✅</strong></td><td align="center"><strong>✅</strong></td><td align="center"><strong>⛔️</strong></td></tr><tr><td>Bukkit</td><td>5</td><td align="center"><strong>✅</strong></td><td align="center"><strong>✅</strong></td><td align="center"><strong>⛔️</strong></td></tr><tr><td>Custom</td><td>null</td><td align="center"><strong>ℹ️</strong></td><td align="center"><strong>ℹ️</strong></td><td align="center"><strong>ℹ️</strong></td></tr></tbody></table>

{% hint style="info" %}
**LEGEND:    ✅ = Supported    ⛔️ = Unsupported    ℹ️ = Unkown**
{% endhint %}

### Getting Started

So now that you have looked at each default provider we offer, you are ready to start using them. Please take a look at the code below to see how you can add them to your updater.

```java
package your.custom.package;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Updater updater = new Updater(this)
                // THE PARAMETERS SHOULD BE ADJUSTED FOR EACH PROVIDER
                .addProvider(new GithubProvider("Username/Repository"))
                .addProvider(new SpigetProvider(PROJECT_ID))
                .addProvider(new SpigotProvider(PROJECT_ID))
                .addProvider(new BukkitProvider(PROJECT_ID))
                .addProvider(new PolymartProvider(PROJECT_ID))
        updater.schedule();
    }
}
```

As you can see above, we added each default provider to the updater chain and just like that, this updater will start checking for updates within each provider. Just ensure

{% hint style="info" %}
Please note that the updater will check for updates in the order that you placed your providers, in the example above, Github will be the first provider we check and Polymart will be the last.
{% endhint %}

## Custom Providers

As promised, providers are ultra-customizable. With them, you can access a personal server and set each value according to what your custom provider offers. Below we will provide an example of what a custom provider would look like for EssentialsX.

```java
package your.custom.package

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moleculepowered.api.exception.RateLimitReachedException;
import com.moleculepowered.api.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EssentialsXProvider extends AbstractProvider {

    private final Set<String> contributors = new HashSet<>();
    private String downloadLink, latestVersion, changelogLink;
    private int totalDownloads;

    public EssentialsXProvider() {

        String contributorList = "SupaHam, khyperia, kukelekuuk, mdcfe, Flask-Bot, snowleo, triagonal, md-5, Skylexia, " +
                "drtshock, JRoy, Ichbinjoe, khobbits, montlikadani, joejenniges, ElgarL, pop4959, darbyjack, " +
                "chrisgward, evonuts, necrodoom, delbertina, caojohnny, vemacs, JasonHorkles, LCookman, " +
                "LaxWasHere, Iaccidentally, okamosy, ementalo";

        Arrays.stream(contributorList.split(",")).forEach(s -> contributors.add(s.trim()));
    }

    /**
     * A method used to fetch the latest information for this provider, it handles
     * the main logic and is recommended only to assign values to the other methods
     * within this provider, such as changelog link, download link, etc.
     */
    @Override
    public void fetch() {
        try {
            HttpURLConnection releaseConn = connection("https://webapi.essentialsx.net/api/v1/github/essx-v2/releases/latest");

            if (releaseConn.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN)
                throw new RateLimitReachedException();

            BufferedReader releaseReader = new BufferedReader(new InputStreamReader(releaseConn.getInputStream()));
            JsonObject resource = new Gson().fromJson(releaseReader, JsonObject.class);
            JsonArray assets = resource.get("assets").getAsJsonArray();

            // SET REMOTE VERSION AND CHANGELOG LINK
            latestVersion = resource.get("tag_name").getAsString();
            changelogLink = resource.get("html_url").getAsString();

            // ACCESS THE REPOSITORIES ASSETS AND SET THE DOWNLOAD URL IF ONE EXISTS

            if (assets.size() > 0) {
                JsonObject latestResource = assets.get(0).getAsJsonObject();
                downloadLink = latestResource.get("browser_download_url").getAsString();
                totalDownloads = latestResource.get("download_count").getAsInt();
            }

            // DISCONNECT FROM RELEASE
            releaseConn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the name assigned to this provider, this name will be used for logging messages
     *
     * @return Provider name
     */
    @Override
    public @NotNull String getName() {
        return "EssentialsX";
    }

    /**
     * Returns the URL pointing to the location from which the latest update can be downloaded.
     *
     * @return Download link
     */
    @Override
    public @Nullable String getDownloadLink() {
        return downloadLink;
    }

    /**
     * Returns the url pointing to the changelog for the latest update
     *
     * @return Update changelog
     */
    @Override
    public @Nullable String getChangelogLink() {
        return changelogLink;
    }

    /**
     * Return the author's donation link
     *
     * @return Donation link
     */
    @Override
    public @Nullable String getDonationLink() {
        return "https://github.com/sponsors/EssentialsX";
    }

    /**
     * Returns the latest version number returned by this provider
     *
     * @return Latest version number
     */
    @Override
    public @NotNull Version getLatestVersion() {
        return new Version(latestVersion);
    }

    /**
     * Return the price of the latest update
     *
     * @return Update price
     */
    @Override
    public @Nullable String getPrice() {
        return null;
    }

    /**
     * Returns a set of contributors for the latest update
     *
     * @return Set of contributors
     */
    @Override
    public @Nullable Set<String> getContributors() {
        return contributors;
    }

    /**
     * Used to return the total number of downloads for this release
     *
     * @return Total downloads
     */
    @Override
    public int getDownloads() {
        return totalDownloads;
    }

    /**
     * Return's true if the latest release is a premium resource
     *
     * @return true if premium
     */
    @Override
    public boolean isPremium() {
        return false;
    }
}

```

In this example, we are using EssentialX's proxy server to show this works with any platform, it mostly follows the same setup as the GithubProvider, but instead, we manually added the contributors since the proxy server does not have them listed. We also added a donation link since GitHub itself doesn't really provide it either. And just like that EssentialsX now has a working provider using our API


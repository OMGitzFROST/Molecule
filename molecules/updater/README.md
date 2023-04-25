---
description: >-
  The easiest way to keep your plugins updated. Learn all about how you can use
  this feature in your project today!
---

# Updater

{% hint style="warning" %}
The docs are currently a work in progress, And therefore some parts may be outdated or no longer supported.&#x20;
{% endhint %}

## Overview

Welcome to our Updater Wiki, in this article you will learn some of the basics that will help you get started. But what makes our updater different?

Our updater is built upon what we call a "Provider Framework". It relies on these providers to access the web APIs for some of the most popular platforms (e.g. Spigot, Bukkit, Github, etc). Included with this updater, we have provided some default values that, if not specified by you in an updater chain, will be used as default properties, the table below shows what default values will be used.

#### Default Properties

| Setting              | Default Value                                 | Assignment Method         |
| -------------------- | --------------------------------------------- | ------------------------- |
| Enabled              | Always enabled by default                     | setEnabled(Boolean)       |
| Permission           | Empty, All players will receive notifications | setPermission(String)     |
| Notification Handler | Handled Internally                            | setEventHandler(Listener) |
| Interval             | "3h"                                          | setInterval(String)       |

## Example Usage

Below we will provide some examples of how you can start using our updater, we will first show an example Main class, and then we will explain what is going on with each&#x20;

#### Basic Setup

Below you can see an example of what is REQUIRED in order to use our updater, this example does not contain any setting modifications. With this setup, you can

* Check on GitHub for updates using the "EssentialsX/Essentials" repository.&#x20;
* And according to our [default values](./#default-properties) (since none were defined)&#x20;
  * It will check every 3 hours.
  * ALL players will receive update notifications.

```java
package your.custom.package;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.GithubProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        new Updater(this)
                // A PROVIDER IS REQUIRED, IN THIS EXAMPLE WE ARE USING GITHUB
                .addProvider(new GithubProvider("EssentialsX/Essentials"))
                
                // AFTER ALL YOUR SETTINGS ARE CONFIGURED, CALL THIS TO
                // INITIALIZE THE UPDATER, THERE IS ALSO A scheduleAync()
                // ALTERNATIVE FOR THIS METHOD
                .schedule();
    }
}
```

#### Custom Setup

In this example, it's more customized than our [basic setup](./#basic-setup), It will also check on GitHub for updates but instead:

* It will check every 2 hours for updates
* Only players with the "your.custom.permission" in their permissions list will receive update notifications.&#x20;
* And the server owner now has the ability to enable and disable the updater from within their config.yml file using the toggle setting.

```java
package your.custom.package;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.GithubProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        new Updater(this)
                // A PROVIDER IS REQUIRED, IN THIS EXAMPLE WE ARE USING GITHUB
                .addProvider(new GithubProvider("EssentialsX/Essentials"))
                
                // HERE YOU CAN ADD A TOGGLE THAT WILL DISABLE/ENABLE THE
                // UPDATER ON THE SERVER OWNER REQUEST
                .setEnableToggle(getConfig().getBoolean("auto-update"))
                
                // HERE WE SET THE INTERVAL IN WHICH WE WILL CHECK FOR UPDATES
                .setInterval("2h")
                
                // PERMISSION REQUIRED BY PLAYERS IN ORDER TO RECEIVE UPDATE
                // NOTIFICATIONS, TO ALLOW ALL PLAYERS TO RECEIVE NOTIFICATIONS,
                // REMOVE THIS SETTING COMPLETELY OR SET IT TO ""
                .setPermission("your.custom.permission")
                
                // AFTER ALL YOUR SETTINGS ARE CONFIGURED, CALL THIS TO
                // INITIALIZE THE UPDATER, THERE IS ALSO A scheduleAync()
                // ALTERNATIVE FOR THIS METHOD
                .schedule();
    }
}
```

### Downloading Updates

Our updater creates a directory called "Update" in your plugins folder, here is where you will be able to find all updates downloaded by this updater, please note that not all providers allow us to download updates, unfortunately for those, you will have to download them manually.

### Learn More

* Have Questions? Take a look at our [FAQ](../../getting-started/faqs.md), if you can't find your answer there, feel free to reach out on our [discord](https://discord.gg/38JRNJxAVD) server
* Learn about our [Providers](providers.md) and [Default Providers](providers.md#default-providers)



&#x20;

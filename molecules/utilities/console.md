---
description: An enhanced Console utility designed to make logging more efficient
---

# Console

{% hint style="warning" %}
The docs are currently a work in progress, And therefore some parts may be outdated or no longer supported.
{% endhint %}

## Overview

Welcome to the Console Wiki, in this article you will learn about our console class and how to start using it in your plugin today! But what is the Console class?&#x20;

We designed it as an expansion of Spigot's default logging system. Adding new features such as colored logging, debug logging, and more! Our console currently supports the following settings that can be adjusted whenever, wherever within your plugin, Please take a look at the table below for all available settings currently supported

#### Console Settings

| Setting      | Description                                                                                                                                                           |
| ------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Prefix       | Configures the prefix that will be used along with your messages. By default, this setting defaults to your plugin's name.                                            |
| Debug        | Determines whether debug messages should print to the console. When this setting is set to false, no messages using our debug methods will be sent to the console.    |
| Pretty Print | A formatting setting that determines whether colored logging should be enabled. When false, none of the loggers in this class will output colors with their messages. |

#### Types of Loggers

Different loggers serve different purposes, take a look at the table below for a quick description of each type of logger and what their intended purpose is. Also, take a look at which logger type parses color codes.

|   Type  | Description                                                                                                                         | Color Parsing | Forces Color |
| :-----: | ----------------------------------------------------------------------------------------------------------------------------------- | :-----------: | :----------: |
|   Info  | Intended to print informational output messages to the console                                                                      |     **✅**     |    **⛔️**    |
| Warning | Sends warning messages to the console. These messages are colored using the color code "&6" and are intended for noncritical issues |     **⛔️**    |     **✅**    |
|  Severe | Sends severe messages to the console. These messages are colored using the color code "&6" and are intended for errors              |     **⛔️**    |     **✅**    |
|   Log   | Identical to the "Info" logger, except this logger allows you to define a level of severity                                         |     **✅**     |    **⛔️**    |

{% hint style="info" %}
**LEGEND:    ✅ = Supported    ⛔️ = Unsupported**
{% endhint %}

## Color Formatting

We made color coding your messages easy to use, in this section we will focus mainly on parsing color codes. Achieving this is also very simple, simply add color codes to your messages and that's it, the logger will handle the rest, although please keep in mind that the logger you use must support color parsing, otherwise your message will take form to accommodate the preset color defined by the logger.

Disabling color formatting for ALL loggers is very simple. You can simply call the static method `Console#setPrettyPrint(Boolean)`anywhere within your plugin to disable it, just make sure you set the Boolean value to 'false'. Currently, we don't support disabling color formatting for individual loggers so to bypass this, you can use the log method and define the level you wish to use.

## Example Usage

Making the console unique is very important to us, therefore we are constantly adding new customization features to it. Also, we made customization as easy as possible as well please take a look at the example below to learn how to implement customization for this class.

```java
package com.moleculepowered.api;

import com.moleculepowered.api.updater.Updater;
import com.moleculepowered.api.updater.provider.BukkitProvider;
import com.moleculepowered.api.updater.provider.GithubProvider;
import com.moleculepowered.api.updater.provider.SpigetProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // CUSTOMIZATION EXAMPLES WITH SOME OUR ITS SETTINGS
        Console.setPrettyPrint(true);
        Console.setDebugToggle(true);
        Console.setPrefix("[Example Prefix]");

        // EXAMPLES FOR USING EACH LOGGER TYPE, EACH LOGGER HAS MULTIPLE METHODS
        Console.info("Example text");
        Console.warning("Example text");
        Console.severe("Example text");
        Console.log("Example text");
    }
}
```

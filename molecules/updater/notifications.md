---
description: >-
  Learn how we handle sending update notifications to players and how you can
  customize them.
---

# Notifications

## Overview

Welcome to the notifications page for our updater, in this wiki you will learn how our notification system works, as well as how you can customize one yourself. Our notifications are event-based, which means that when the updater completes its update check, it will trigger an event that is used to send notifications to the proper audience.&#x20;

By default, if a custom notification handler is not provided by you, the developer, we will default to an internal handler to send out these notifications. Please take a look below at an example custom handler

#### Custom Example

```java
package com.moleculepowered.api;

import com.moleculepowered.api.event.updater.UpdateCompleteEvent;
import com.moleculepowered.api.updater.provider.AbstractProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ExampleHandler implements Listener {
    
    @EventHandler
    public void onUpdateComplete(@NotNull UpdateCompleteEvent event) {

        AbstractProvider provider = event.getProvider();
        String latestVersion = provider.getRelease().getVersion();
        
        switch (event.getResult()) {
            case LATEST:
                Console.info("You have the latest version installed");
                break;
            case UPDATE_AVAILABLE:
                Console.info("Update available: {0}", latestVersion);
                break;
            case EXISTS:
                Console.info("Check your Update folder for the updated version");
                break;
            case DISABLED:
                Console.warning("The updater is disabled");
                break;
            default:
                Console.log("The updater result is unknown");
        }
    }
}
```

{% hint style="warning" %}
Your custom handler MUST be registered using the chain method #setEventHandler(Listener) in order for these notifications to work as intended.&#x20;
{% endhint %}


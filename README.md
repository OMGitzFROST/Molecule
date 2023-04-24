
# Molecule API

MoleculeAPI is a spigot resource designed to provide plugin developers with a powerful suite
of tools that streamline their coding process, providing them with some fundamental components, such as an Updater, a
feature packed
console logger, localization, user management and so much more.

We're constantly working on expanding this project, mainly focused on spigot/paper but will eventually expand to provide
support for other
frameworks such as Sponge, Velocity, etc.

## Features

Currently, this API is under constant development, therefore, we're constantly adding new features within it, but as of now these are the features it currently supports
* Feature packed updater
* Enhanced console logging

### Upcoming Features
* Localization with properties file or yml files
* Enhanced Configurations
* User Management System

Please take a look at our [Documentation](https://docs.moleculepowered.com) for detailed tutorials on how to use each of
these features.

## Installation

Adding our API to your project is simple, simply find your desired build tool from
the options below and copy it into their designated file, and voilà!
It's that simple, take a look at the supported build tools below.

### Maven

````xml
<!--ADD REPOSITORY-->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

        <!--ADD DEPENDENCY-->
<dependency>
<groupId>com.github.OMGitzFROST</groupId>
<artifactId>MoleculeAPI</artifactId>
<version>latest</version>
</dependency>
````

### Gradle

````
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.OMGitzFROST:MoleculeAPI:latest'
}
````

## Additional Resources

Didn't find what your looking for? Please take a look at these additional resources

* [Issue tracker](https://github.com/OMGitzFROST/MoleculeAPI/issues) — Track, Create, View existing issues
* [Feature request]() — Have a good idea? Let's hear it!
* [Discord Support](https://discord.gg/38JRNJxAVD) — Personalized support
* [Documentation](https://docs.moleculepowered.com) - Learn how to use our API

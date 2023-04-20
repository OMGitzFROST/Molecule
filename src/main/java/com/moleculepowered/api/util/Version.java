package com.moleculepowered.api.util;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Version {
    private final String VERSION_PATTERN, MODIFIER_PATTERN;
    private String version, modifier;
    private String[] versionParts = {};

    /**
     * <p>The main constructor for this class, the string input will be used to create a usable
     * version that can be compared, and broken down into its parts, such as major, minor, patch versions, and
     * its version type such as BETA, ALPHA, etc.</p>
     *
     * <p>Please note that this method does allow you to provide a null value, this method has built in checks for
     * that and will return N/A as its value</p>
     *
     * @param input Provided input
     * @see #Version(Integer)
     * @see #Version(Double)
     */
    public Version(@Nullable String input) {
        VERSION_PATTERN = "(?:(\\d+)\\.)?(?:(\\d+)\\.)?(\\*|\\d+)";
        MODIFIER_PATTERN = "B(ETA)?|A(LPHA)?|RC|SNAPSHOT";

        if (input != null) {
            // SET VERSION NUMBER
            Matcher versionMatch = parseVersion(input);
            if (versionMatch.find()) version = versionMatch.group();

            // SET VERSION TYPE
            Matcher modifierMatch = parseModifier(input);
            if (modifierMatch.find()) modifier = modifierMatch.group();

            // SPLIT THE VERSION INTO PARTS
            versionParts = version.split("\\.");
        }
    }

    /**
     * <p>The main constructor for this class, the string input will be used to create a usable
     * version that can be compared, and broken down into its parts, such as major, minor, patch versions, and
     * its version type such as BETA, ALPHA, etc.</p>
     *
     * <p>Please note that this method does allow you to provide a null value, this method has built in checks for
     * that and will return N/A as its value</p>
     *
     * @param input Provided input
     * @see #Version(Double)
     * @see #Version(String)
     */
    public Version(@NotNull Integer input) {
        this(input.toString());
    }

    /**
     * <p>The main constructor for this class, the string input will be used to create a usable
     * version that can be compared, and broken down into its parts, such as major, minor, patch versions, and
     * its version type such as BETA, ALPHA, etc.</p>
     *
     * <p>Please note that this method does allow you to provide a null value, this method has built in checks for
     * that and will return N/A as its value</p>
     *
     * @param input Provided input
     * @see #Version(Integer)
     * @see #Version(String)
     */
    public Version(@NotNull Double input) {
        this(input.toString());
    }

    /*
    VERSION PARTS
     */

    /**
     * Used to return the version number parsed by this class, please note that this
     * method does not return the original full version number. If there was an error parsing
     * the version number with the input provided, this method will return "0.0" by default.
     *
     * @return Formatted version number
     */
    public @NotNull String getVersion() {
        return version != null ? version : "0.0";
    }

    /**
     * A method used to return the version type such as BETA, ALPHA, etc. If no modifier was
     * found, this method will return "null"
     *
     * @return Version modifier
     */
    public @Nullable String getModifier() {
        return modifier;
    }

    /**
     * Returns the major release for this version, if this version doesn't have one, this method will return as 0
     *
     * @return Major release
     */
    public Integer getMajor() {
        return versionParts.length >= 1 ? Integer.parseInt(versionParts[0]) : 0;
    }

    /**
     * Returns the minor release for this version, if this version doesn't have one, this method will return as 0
     *
     * @return Minor release
     */
    public Integer getMinor() {
        return versionParts.length >= 2 ? Integer.parseInt(versionParts[1]) : 0;
    }

    /**
     * Returns the patch release for this version, if this version doesn't have one, this method will return as 0
     *
     * @return Patch release
     */
    public Integer getPatch() {
        return versionParts.length >= 3 ? Integer.parseInt(versionParts[2]) : 0;
    }

    /*
    COMPARATIVE METHODS
     */

    /**
     * Returns true if the compared version is greater than this version
     *
     * @param version Target version
     * @return true if greater than
     */
    public boolean isGreaterThan(@NotNull Version version) {
        ComparableVersion version1 = new ComparableVersion(getVersion());
        ComparableVersion version2 = new ComparableVersion(version.getVersion());
        return version1.compareTo(version2) > 0;
    }

    /**
     * Returns true if the compared version is less than this version
     *
     * @param version Target version
     * @return true if less than
     */
    public boolean isLessThan(@NotNull Version version) {
        ComparableVersion version1 = new ComparableVersion(getVersion());
        ComparableVersion version2 = new ComparableVersion(version.getVersion());
        return version1.compareTo(version2) < 0;
    }

    /**
     * Returns true if the compared version is equal to this version
     *
     * @param version Target version
     * @return true if equal
     */
    public boolean isEqual(@NotNull Version version) {
        ComparableVersion version1 = new ComparableVersion(getVersion());
        ComparableVersion version2 = new ComparableVersion(version.getVersion());
        return version1.compareTo(version2) == 0;
    }

    /*
    DESCRIPTION METHODS
     */

    /**
     * Returns whether this version is considered an unstable build; this is determined by the modifier
     * that was associated with the original version number; examples of unstable builds are "BETA", "ALPHA",
     * "Release Candidate (RC), and "SNAPSHOT" builds.
     *
     * @return true if this version is unstable
     */
    public boolean isUnstable() {
        String modifier = getModifier() != null ? getModifier().toLowerCase() : null;
        return modifier != null && (modifier.startsWith("a") || modifier.startsWith("b") || modifier.startsWith("rc") || modifier.startsWith("snap"));
    }

    /*
    UTILITY METHODS
     */

    /**
     * A utility method used to return a parsed version number derived from the provided string. Please
     * note that if this the provided input is null, this method will return an empty string
     *
     * @param input Provided input
     * @return The version modifier or an null string
     */
    private @NotNull Matcher parseVersion(String input) {
        return Pattern.compile(VERSION_PATTERN, Pattern.CASE_INSENSITIVE).matcher(input);
    }

    /**
     * A utility method used to return a parsed modifier derived from a version string, these
     * modifiers include BETA, ALPHA, RELEASE CANDIDATE, ETC. Please note that if the provided string is
     * null, this method will return an null string.
     *
     * @param input Provided input
     * @return The version modifier or an empty string
     */
    private @NotNull Matcher parseModifier(String input) {
        return Pattern.compile(MODIFIER_PATTERN, Pattern.CASE_INSENSITIVE).matcher(input);
    }
}

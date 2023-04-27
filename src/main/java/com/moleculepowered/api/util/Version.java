package com.moleculepowered.api.util;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The purpose of this class is to create functional version numbers; it will separate
 * each part of its number, such as Major, Minor and make them accessible with easy to use methods.</p>
 *
 * <p>Additionally, this class provides methods that allow you to compare version numbers and check if
 * one is less than, greater than, or equal to its target.</p>
 *
 * @author OMGitzFROST
 * @since 1.0.0
 */
public final class Version {
    private String VERSION_PATTERN, MODIFIER_PATTERN;
    private String version, modifier;
    private String[] versionParts = {};

    /**
     * Used to disable this constructor from being used, this is a utility class
     */
    private Version() {
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
     * @see #Version(double)
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
     * @see #Version(String)
     */
    public Version(double input) {
        this(String.valueOf(input));
    }

    /*
    VERSION PARTS
     */

    /**
     * <p>Used to return the version number parsed by this class, please note that this
     * method doesn't return the original full version number.</p>
     *
     * <p>If there was an error parsing the version number with the input provided,
     * this method will return "0.0" by default.</p>
     *
     * @return Formatted version number
     */
    public @NotNull String getVersion() {
        return StringUtil.nonNull(version, "0.0");
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
    public int getMajor() {
        return versionParts.length >= 1 ? Integer.parseInt(versionParts[0]) : 0;
    }

    /**
     * Returns the minor release for this version, if this version doesn't have one, this method will return as 0
     *
     * @return Minor release
     */
    public int getMinor() {
        return versionParts.length >= 2 ? Integer.parseInt(versionParts[1]) : 0;
    }

    /**
     * Returns the patch release for this version, if this version doesn't have one, this method will return as 0
     *
     * @return Patch release
     */
    public int getPatch() {
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
     * @return The version modifier or a null string
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

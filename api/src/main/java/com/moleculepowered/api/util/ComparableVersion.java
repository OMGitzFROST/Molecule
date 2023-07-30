package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The purpose of this class is to create functional version numbers; it separates
 * each part of the number, such as major, minor, and makes them accessible with easy-to-use methods.
 * Additionally, this class provides methods to compare version numbers and check if
 * one is less than, greater than, or equal to its target.
 *
 * @author OMGitzFROST
 */
public final class ComparableVersion {

    private String VERSION_PATTERN;
    private String MODIFIER_PATTERN;
    private String version;
    private String identifier;
    private String[] versionParts;

    /**
     * Used to disable the default constructor, as this is a utility class.
     */
    private ComparableVersion() { }

    /**
     * Constructs a {@code ComparableVersion} object from a string input.
     * The input string is used to create a usable version that can be compared,
     * and it is broken down into parts such as major, minor, patch versions, and
     * a version type such as BETA, ALPHA, etc.
     *
     * @param input the version string
     */
    public ComparableVersion(@Nullable String input) {
        VERSION_PATTERN = "(?:(\\d+)\\.)?(?:(\\d+)\\.)?(\\*|\\d+)";
        MODIFIER_PATTERN = "BETA|ALPHA|RC|SNAPSHOT";

        if (input != null) {
            // Set version number
            Matcher versionMatch = parseVersion(input);
            if (versionMatch.find()) version = versionMatch.group();

            // Set version type
            Matcher identifierMatch = parseIdentifier(input);
            if (identifierMatch.find()) identifier = identifierMatch.group();

            // Split the version into parts
            versionParts = version.split("\\.");
        }
    }

    /**
     * Constructs a {@code ComparableVersion} object from a double input.
     * The double value is converted to a string and used to create a usable version
     * that can be compared, and it is broken down into parts such as major, minor,
     * patch versions, and a version type such as BETA, ALPHA, etc.
     *
     * @param input the version number as a double
     * @see #ComparableVersion(String)
     */
    public ComparableVersion(double input) {
        this(String.valueOf(input));
    }

    /**
     * Returns the version number parsed by this class. Please note that this
     * method doesn't return the original full version number.
     * If there was an error parsing the version number with the input provided,
     * this method will return "0" by default.
     *
     * @return the formatted version number
     */
    @Override
    public String toString() {
        return version != null && !version.isEmpty() ? version : "0";
    }

    /**
     * Returns the version identifier (modifier) for this version.
     * If an identifier was not parsed correctly, this method will return
     * {@link Identifier#RELEASE} by default.
     *
     * @return the version identifier
     */
    public @NotNull Identifier getIdentifier() {
        return Identifier.parse(identifier);
    }

    /**
     * Returns the major release for this version.
     * If this version doesn't have a major release, this method will return 0.
     *
     * @return the major release
     */
    public int getMajor() {
        return versionParts.length >= 1 ? Integer.parseInt(versionParts[0]) : 0;
    }

    /**
     * Returns the minor release for this version.
     * If this version doesn't have a minor release, this method will return 0.
     *
     * @return the minor release
     */
    public int getMinor() {
        return versionParts.length >= 2 ? Integer.parseInt(versionParts[1]) : 0;
    }

    /**
     * Returns the patch release for this version.
     * If this version doesn't have a patch release, this method will return 0.
     *
     * @return the patch release
     */
    public int getPatch() {
        return versionParts.length >= 3 ? Integer.parseInt(versionParts[2]) : 0;
    }

    /**
     * Compares this version to another version. Returns a negative integer, zero, or a positive integer
     * as this version is less than, equal to, or greater than the specified version.
     *
     * @param target the version to compare to
     * @return the comparison result
     */
    public int compareTo(@NotNull ComparableVersion target) {
        int[] thisParts = toIntArray(versionParts);
        int[] thatParts = toIntArray(target.versionParts);
        int length = Math.max(thisParts.length, thatParts.length);

        for (int i = 0; i < length; i++) {
            int thisPart = (i < thisParts.length) ? thisParts[i] : 0;
            int thatPart = (i < thatParts.length) ? thatParts[i] : 0;
            if (thisPart < thatPart) return -1;
            if (thisPart > thatPart) return 1;
        }

        // Compare version identifiers (modifiers) if present
        if (identifier != null && target.identifier != null) {
            Identifier thisIdentifier = Identifier.parse(identifier);
            Identifier thatIdentifier = Identifier.parse(target.identifier);
            return thisIdentifier.compareTo(thatIdentifier);
        } else if (identifier != null) {
            return 1;
        } else if (target.identifier != null) {
            return -1;
        }

        return 0;
    }

    /**
     * Checks if this version is less than the specified target version.
     *
     * @param target the version to compare to
     * @return {@code true} if this version is less than the target version, {@code false} otherwise
     */
    public boolean isLessThan(@NotNull ComparableVersion target) {
        return compareTo(target) < 0;
    }

    /**
     * Checks if this version is greater than the specified target version.
     *
     * @param target the version to compare to
     * @return {@code true} if this version is greater than the target version, {@code false} otherwise
     */
    public boolean isGreaterThan(@NotNull ComparableVersion target) {
        return compareTo(target) > 0;
    }

    /**
     * Checks if this version is equal to the specified target version.
     *
     * @param target the version to compare to
     * @return {@code true} if this version is equal to the target version, {@code false} otherwise
     */
    public boolean isEqualTo(@NotNull ComparableVersion target) {
        return compareTo(target) == 0;
    }

    /**
     * Checks if this version is considered unstable, based on the identifier (modifier).
     * An unstable version usually contains an identifier like BETA, ALPHA, or RC.
     *
     * @return {@code true} if the version is unstable, {@code false} otherwise
     */
    public boolean isUnstable() {
        return identifier != null && getIdentifier().isUnstable();
    }

    /**
     * Parses the version number from the input string using a regular expression pattern.
     *
     * @param input the input string
     * @return the matcher object containing the version number
     */
    private @NotNull Matcher parseVersion(String input) {
        Pattern pattern = Pattern.compile(VERSION_PATTERN);
        return pattern.matcher(input);
    }

    /**
     * Parses the version identifier (modifier) from the input string using a regular expression pattern.
     *
     * @param input the input string
     * @return the matcher object containing the version identifier
     */
    private @NotNull Matcher parseIdentifier(String input) {
        Pattern pattern = Pattern.compile(MODIFIER_PATTERN, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(input);
    }

    /**
     * Converts an array of strings to an array of integers.
     *
     * @param versionParts the array of strings
     * @return the array of integers
     */
    private int @NotNull [] toIntArray(String @NotNull [] versionParts) {
        int[] result = new int[versionParts.length];
        for (int i = 0; i < versionParts.length; i++) {
            result[i] = Integer.parseInt(versionParts[i]);
        }
        return result;
    }

    /**
     * The possible version identifiers (modifiers) for a version.
     *
     * @author OMGitzFROST
     */
    public enum Identifier {
        RELEASE,
        SNAPSHOT,
        ALPHA,
        BETA,
        RC;

        /**
         * Parses a string to an Identifier enum value.
         * If the input string doesn't match any valid identifier,
         * the default identifier {@link Identifier#RELEASE} is returned.
         *
         * @param identifier the identifier string
         * @return the parsed identifier enum value
         */
        public static Identifier parse(@NotNull String identifier) {
            try {
                return Identifier.valueOf(identifier.toUpperCase());
            } catch (IllegalArgumentException e) {
                return RELEASE;
            }
        }

        /**
         * Checks if the identifier is considered unstable.
         * An unstable identifier usually represents a pre-release version.
         *
         * @return {@code true} if the identifier is unstable, {@code false} otherwise
         */
        public boolean isUnstable() {
            return this != RELEASE;
        }
    }
}
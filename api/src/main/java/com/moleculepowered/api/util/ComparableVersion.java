package com.moleculepowered.api.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>The purpose of this class is to create functional version numbers; it will separate
 * each part of its number, such as Major, Minor and make them accessible with easy to use methods.</p>
 *
 * <p>Additionally, this class provides methods that allow you to compare version numbers and check if
 * one is less than, greater than, or equal to its target.</p>
 *
 * @author OMGitzFROST
 */
public final class ComparableVersion {

    private String VERSION_PATTERN, MODIFIER_PATTERN;
    private String version, identifier;
    private String[] versionParts = {};

    /**
     * Used to disable this constructor from being used, this is a utility class
     */
    private ComparableVersion() { }

    /**
     * <p>The main constructor for this class, the string input will be used to create a usable
     * version that can be compared, and broken down into its parts, such as major, minor, patch versions, and
     * its version type such as BETA, ALPHA, etc.</p>
     *
     * <p>Please note that this method does allow you to provide a null value, this method has built in checks for
     * that and will return N/A as its value</p>
     *
     * @param input Provided input
     * @see #ComparableVersion(double)
     */
    public ComparableVersion(@Nullable String input) {
        VERSION_PATTERN = "(?:(\\d+)\\.)?(?:(\\d+)\\.)?(\\*|\\d+)";
        MODIFIER_PATTERN = "BETA|ALPHA|RC|SNAPSHOT";

        if (input != null) {

            // SET VERSION NUMBER
            Matcher versionMatch = parseVersion(input);
            if (versionMatch.find()) version = versionMatch.group();

            // SET VERSION TYPE
            Matcher identifierMatch = parseIdentifier(input);
            if (identifierMatch.find()) identifier = identifierMatch.group();

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
     * @see #ComparableVersion(String)
     */
    public ComparableVersion(double input) {
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
        return version != null && version.isEmpty() ? version : "0";
    }

    /**
     * A method used to return the pre-release type such as BETA, ALPHA, etc. If an identifier
     * was not parsed correctly, this method will return {@link Identifier#RELEASE} by default.
     *
     * @return Version modifier
     */
    public @NotNull Identifier getIdentifier() {
        return Identifier.parse(identifier);
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
    public boolean isGreaterThan(@NotNull ComparableVersion version) {
        return compare(getVersion(), version.getVersion()) == 1;
    }

    /**
     * Returns true if the compared version is less than this version
     *
     * @param version Target version
     * @return true if less than
     */
    public boolean isLessThan(@NotNull ComparableVersion version) {
        return compare(getVersion(), version.getVersion()) == -1;
    }

    /**
     * Returns true if the compared version is equal to this version
     *
     * @param version Target version
     * @return true if equal
     */
    public boolean isEqual(@NotNull ComparableVersion version) {
        return compare(getVersion(), version.getVersion()) == 0;
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
        return getIdentifier() != Identifier.RELEASE;
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
     * null, this method will return a null string.
     *
     * @param input Provided input
     * @return The version modifier or an empty string
     */
    private @NotNull Matcher parseIdentifier(String input) {
        return Pattern.compile(MODIFIER_PATTERN, Pattern.CASE_INSENSITIVE).matcher(input);
    }

    /**
     * <p>A utility method used to return an integer that will determine whether
     * the provided versions are greater than, less than, or equal to.</p>
     *
     * <p>Below are the results that will be returned and what they mean.</p>
     * <p>(-1) = Less than | (0) = Equal to | (1) = Greater than</p>
     *
     * @param version1 Original version
     * @param version2 Target version
     * @return an integer determining whether its greater, less, or equal to
     */
    private int compare(@NotNull String version1, @NotNull String version2) {
        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");

        int i = 0;
        while (i < arr1.length || i < arr2.length) {
            if (i < arr1.length && i < arr2.length) {
                if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
                    return -1;
                } else if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
                    return 1;
                }
            } else if (i < arr1.length) {
                if (Integer.parseInt(arr1[i]) != 0) {
                    return 1;
                }
            } else {
                if (Integer.parseInt(arr2[i]) != 0) {
                    return -1;
                }
            }
            i++;
        }
        return 0;
    }

    /*
    EXTERNAL CLASSES
     */

    /**
     * An enum used to handle all tasks associated with version identifiers, this class
     * parses, defines and returns an identifier based on a provided input.
     *
     * @author OMGitzFROST
     */
    private enum Identifier {

        ALPHA("a", "alpha"),
        BETA("b", "beta"),
        RELEASE_CANDIDATE("rc"),
        SNAPSHOT("snapshot"),
        PRE_RELEASE("pre"),
        DEVELOPMENTAL("dev"),
        RELEASE;

        private final String[] identifiers;

        Identifier(String... identifiers) {
            this.identifiers = identifiers;
        }

        /**
         * Used to parse a string into a usable version {@link Identifier}, please note
         * that if an identifier cannot be derived by the provided input, this
         * method will return {@link #RELEASE} by default.
         *
         * @param input Provided input
         * @return An identifier or {@link #RELEASE} by default.
         */
        public static Identifier parse(@Nullable String input) {
            return Arrays.stream(Identifier.values())
                    .filter(identifier -> input != null && identifier.getIdentifiers().contains(input.toLowerCase()))
                    .findFirst()
                    .orElse(Identifier.RELEASE);
        }

        /**
         * A utility method that returns all identifiers associated with this constant.
         *
         * @return A list of identifiers
         */
        public Set<String> getIdentifiers() {
            return Arrays.stream(identifiers).collect(Collectors.toSet());
        }

        /**
         * Used to return the name associated with this identifier
         *
         * @return Identifier name
         */
        @Contract(pure = true)
        public @NotNull String getName() {
            return name();
        }
    }
}
package com.moleculepowered.test;

import com.moleculepowered.api.util.Version;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class TestVersion {

    @Test
    @DisplayName("Validate Version Parsing")
    public void validateVersionParsing() {
        Version v1 = new Version("1.0.0-B");
        Validate.notNull(v1.getVersion());
        Validate.notNull(v1.getModifier());
        Validate.isTrue(Objects.equals(v1.getVersion(), "1.0.0"));
        Validate.isTrue(Objects.equals(v1.getModifier(), "B"));

        Version v2 = new Version("1.0.0-alpha");
        Validate.notNull(v2.getVersion());
        Validate.notNull(v2.getModifier());
        Validate.isTrue(Objects.equals(v2.getVersion(), "1.0.0"));
        Validate.isTrue(Objects.equals(v2.getModifier().toLowerCase(), "alpha"));
    }

    @Test
    @DisplayName("Test Version Comparisons")
    public void versionComparing() {
        // GREATER THAN
        Version greater1 = new Version("1.2.0");
        Version greater2 = new Version("1.0.0");
        Validate.isTrue(greater1.isGreaterThan(greater2));

        // EQUAL TO
        Version equal1 = new Version("1.0.0");
        Version equal2 = new Version("1.0.0");
        Validate.isTrue(equal1.isEqual(equal2));

        // LESS THAN
        Version less1 = new Version("0.9.0");
        Version less2 = new Version("1.0.0");
        Validate.isTrue(less1.isLessThan(less2));
    }

    @Test
    @DisplayName("Ensure modifiers are parsed correctly")
    public void testModifier() {

        Version v1 = new Version("1.0.0-a");
        Version v2 = new Version("1.0.0-snapshot");
        Version v3 = new Version("1.0.0");

        Validate.isTrue(v1.isUnstable());
        Validate.isTrue(v2.isUnstable());
        Validate.isTrue(!v3.isUnstable());

        Validate.isTrue(Objects.requireNonNull(v1.getModifier()).equalsIgnoreCase("a"));
        Validate.isTrue(Objects.requireNonNull(v2.getModifier()).equalsIgnoreCase("snapshot"));
        Validate.isTrue(v3.getModifier() == null);
    }
}

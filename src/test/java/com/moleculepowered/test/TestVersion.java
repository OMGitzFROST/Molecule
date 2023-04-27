package com.moleculepowered.test;

import com.moleculepowered.api.util.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class TestVersion {

    @Test
    @DisplayName("Validate Version Parsing")
    public void validateVersionParsing() {
        Version v1 = new Version("1.0.0-B");
        Assertions.assertNotNull(v1.getVersion());
        Assertions.assertNotNull(v1.getModifier());
        Assertions.assertEquals("1.0.0", v1.getVersion());
        Assertions.assertEquals("B", v1.getModifier());

        Version v2 = new Version("1.0.0-alpha");
        Assertions.assertNotNull(v2.getVersion());
        Assertions.assertNotNull(v2.getModifier());
        Assertions.assertEquals("1.0.0", v2.getVersion());
        Assertions.assertEquals("alpha", v2.getModifier().toLowerCase());
    }

    @Test
    @DisplayName("Test Version Comparisons")
    public void versionComparing() {
        // GREATER THAN
        Version greater1 = new Version("1.2.0");
        Version greater2 = new Version("1.0.0");
        Assertions.assertTrue(greater1.isGreaterThan(greater2));

        // EQUAL TO
        Version equal1 = new Version("1.0.0");
        Version equal2 = new Version("1.0.0");
        Assertions.assertTrue(equal1.isEqual(equal2));

        // LESS THAN
        Version less1 = new Version("0.9.0");
        Version less2 = new Version("1.0.0");
        Assertions.assertTrue(less1.isLessThan(less2));
    }

    @Test
    @DisplayName("Ensure modifiers are parsed correctly")
    public void testModifier() {

        Version v1 = new Version("1.0.0-a");
        Version v2 = new Version("1.0.0-snapshot");
        Version v3 = new Version("1.0.0");

        Assertions.assertTrue(v1.isUnstable());
        Assertions.assertTrue(v2.isUnstable());
        Assertions.assertFalse(v3.isUnstable());

        Assertions.assertTrue(Objects.requireNonNull(v1.getModifier()).equalsIgnoreCase("a"));
        Assertions.assertTrue(Objects.requireNonNull(v2.getModifier()).equalsIgnoreCase("snapshot"));
        Assertions.assertNull(v3.getModifier());
    }
}

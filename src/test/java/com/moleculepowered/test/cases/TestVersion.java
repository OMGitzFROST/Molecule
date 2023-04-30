package com.moleculepowered.test.cases;

import com.moleculepowered.api.util.Version;
import com.moleculepowered.test.AbstractCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestVersion extends AbstractCase {

    @Test
    @DisplayName("Test Version Comparisons")
    public void testVersionComparing() {
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
    @DisplayName("Ensure version's broken down correctly")
    public void testVersionBuilding() {

        // VERSION 1
        Version v1 = new Version("1.2.3");
        Assertions.assertFalse(v1.isUnstable());
        Assertions.assertEquals(1, v1.getMajor());
        Assertions.assertEquals(2, v1.getMinor());
        Assertions.assertEquals(3, v1.getPatch());
        Assertions.assertEquals(v1.getIdentifier(), Version.Identifier.RELEASE);

        // VERSION 2
        Version v2 = new Version("1.0.0-alpha");
        Assertions.assertTrue(v2.isUnstable());
        Assertions.assertEquals(1, v2.getMajor());
        Assertions.assertEquals(0, v2.getMinor());
        Assertions.assertEquals(0, v2.getPatch());
        Assertions.assertEquals(v2.getIdentifier(), Version.Identifier.ALPHA);

        // VERSION 3
        Version v3 = new Version("1.0.0-snapshot");
        Assertions.assertTrue(v3.isUnstable());
        Assertions.assertEquals(1, v3.getMajor());
        Assertions.assertEquals(0, v3.getMinor());
        Assertions.assertEquals(0, v3.getPatch());
        Assertions.assertEquals(v3.getIdentifier(), Version.Identifier.SNAPSHOT);

        // VERSION 4
        Version v4 = new Version("0.9.0rc");
        Assertions.assertTrue(v4.isUnstable());
        Assertions.assertEquals(0, v4.getMajor());
        Assertions.assertEquals(9, v4.getMinor());
        Assertions.assertEquals(0, v4.getPatch());
        Assertions.assertEquals(v4.getIdentifier(), Version.Identifier.RELEASE_CANDIDATE);

        // VERSION 5
        Version v5 = new Version("6.0.1beta-1.0");
        Assertions.assertTrue(v5.isUnstable());
        Assertions.assertEquals(6, v5.getMajor());
        Assertions.assertEquals(0, v5.getMinor());
        Assertions.assertEquals(1, v5.getPatch());
        Assertions.assertEquals(v5.getIdentifier(), Version.Identifier.BETA);

        // VERSION 6
        Version v6 = new Version("6.0.1.4");
        Assertions.assertFalse(v6.isUnstable());
        Assertions.assertEquals(6, v6.getMajor());
        Assertions.assertEquals(0, v6.getMinor());
        Assertions.assertEquals(1, v6.getPatch());
        Assertions.assertEquals(v6.getIdentifier(), Version.Identifier.RELEASE);

        Version v7 = new Version("9.8.7-whatever+meta+meta");
        Assertions.assertFalse(v7.isUnstable());
        Assertions.assertEquals(9, v7.getMajor());
        Assertions.assertEquals(8, v7.getMinor());
        Assertions.assertEquals(7, v7.getPatch());
        Assertions.assertEquals(v7.getIdentifier(), Version.Identifier.RELEASE);
    }
}

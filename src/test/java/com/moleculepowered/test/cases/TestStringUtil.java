package com.moleculepowered.test.cases;

import com.moleculepowered.api.util.StringUtil;
import com.moleculepowered.test.AbstractTest;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestStringUtil extends AbstractTest {

    @Test
    @DisplayName("Ensure that colors are stripped correctly when prompted")
    public void testColorStripping() {
        String testString = StringUtil.stripColor("&6This is a test string, §ahear me roar");
        Assertions.assertFalse(testString.contains("&"));
        Assertions.assertFalse(testString.contains("§"));
    }

    @Test
    @DisplayName("Ensure that the nonNull method returns a non null string")
    public void testStringNonNull() {
        Assertions.assertEquals(StringUtil.nonNull((@Nullable String) null), "");
        Assertions.assertNotEquals(StringUtil.nonNull((@Nullable String) null), null);
    }

    @Test
    @DisplayName("Ensure string modifiers work as intended")
    public void testStringManipulation() {
        String t1 = "This is a test string, what an interesting test, this portion should be substringed";
        Assertions.assertEquals(StringUtil.substring(t1, "this portion should be substringed"), "this portion should be substringed");
        Assertions.assertEquals(StringUtil.lastIndex(t1, ","), ", this portion should be substringed");
    }
}

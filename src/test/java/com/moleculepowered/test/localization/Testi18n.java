package com.moleculepowered.test.localization;

import com.moleculepowered.api.localization.i18n;
import com.moleculepowered.test.AbstractTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.moleculepowered.api.localization.i18n.tl;

public class Testi18n extends AbstractTest {

    public Testi18n() {
        i18n.setTranslator(this::translate);
    }

    @Test
    public void testLocalization() {
        Assertions.assertEquals(tl(TestMessages.TEST_ONE.getPath()), "Test #1");
        Assertions.assertEquals(tl(TestMessages.TEST_TWO.getPath()), "Test #2");
        Assertions.assertEquals(tl(TestMessages.TEST_THREE.getPath()), "Test #3");
    }

    private String translate(@NotNull String path) {
        return Arrays.stream(TestMessages.values())
                .filter(m -> m.getPath().equalsIgnoreCase(path))
                .findFirst()
                .orElseThrow(NullPointerException::new)
                .getMessage();
    }

    private enum TestMessages {

        TEST_ONE("test.message.one", "Test #1"),
        TEST_TWO("test.message.two", "Test #2"),
        TEST_THREE("test.message.three", "Test #3");

        private final String message, path;

        TestMessages(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }
    }
}

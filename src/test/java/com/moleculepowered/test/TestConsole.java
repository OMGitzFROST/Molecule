package com.moleculepowered.test;

import com.moleculepowered.api.Console;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestConsole {

    @Test
    @DisplayName("Ensure color codes are removed")
    public void stripColors() {
        String test1 = "&cHello";
        String test3 = "§cHello";
        Validate.isTrue(!Console.ConsoleColor.stripColor(test1).contains("&"));
        Validate.isTrue(!Console.ConsoleColor.stripColor(test3).contains("§"));
    }

    @Test
    @DisplayName("Ensure color codes are handled properly")
    public void translateColors() {
        String test1 = "&c&kHello";

        // ENSURE GETTING COLOR CODES ONLY WORKS FOR VALID COLORS, AND RETURNS NULL FOR INVALID COLORS
        boolean condition1 = Console.ConsoleColor.getColorByCode('a') != null && Console.ConsoleColor.getColorByCode('k') == null;
        Validate.isTrue(condition1);

        // ENSURE PARSER ONLY REMOVED VALID COLOR CODES, LEAVING INVALID ONES ALONE
        boolean condition2 = !Console.ConsoleColor.parse(test1).contains("&c") && Console.ConsoleColor.parse(test1).contains("&k");
        Validate.isTrue(condition2);
    }
}

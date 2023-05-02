package com.moleculepowered.test.cases;

import com.moleculepowered.api.util.Time;
import com.moleculepowered.test.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;


public final class TestTime extends AbstractTest {

    @Test
    @DisplayName("Ensure date comparing works as intended")
    public void testDateComparing() {

        Date start = Time.parseDate("01/01/" + Time.getToday().get(Calendar.YEAR));
        Date end = Time.parseDate("12/31/" + Time.getToday().get(Calendar.YEAR));

        // TEST BEFORE
        Assertions.assertTrue(Time.isBeforeDate(start, end));
        Assertions.assertFalse(Time.isBeforeDate(end, start));

        // TEST AFTER
        Assertions.assertTrue(Time.isAfterDate(end, start));
        Assertions.assertFalse(Time.isAfterDate(start, end));
    }

    @Test
    @DisplayName("Ensure that time remaining returns correct time")
    public void testTimeRemaining() {

        // ASSERT THAT MISMATCHING DATES RETURN THE CORRECT REMAINING PERIOD
        Assertions.assertEquals(Time.getTimeRemaining("01/01/2023", "12/31/2023"), "11mo 30d");
        Assertions.assertEquals(Time.getTimeRemaining("05/03/2023", "12/31/2023"), "07mo 28d 01h");
        Assertions.assertEquals(Time.getTimeRemaining("11/09/2023", "12/31/2023"), "01mo 22d");

        // ASSERT THAT EQUAL DATES RETURN AN EMPTY STRING
        Assertions.assertEquals(Time.getTimeRemaining("01/01/2023", "01/01/2023"), "");
        Assertions.assertNotEquals(Time.getTimeRemaining("01/01/2023", "12/31/2023"), "");
    }

    @Test
    @DisplayName("Ensure that intervals are parsed correctly for bukkit schedulers")
    public void testIntervalParsing() {

        // ENSURE INTERVAL IS SCHEDULER FRIENDLY AND NOT THE STANDARD
        Assertions.assertEquals(Time.parseInterval("3h"), 216000);
        Assertions.assertNotEquals(Time.parseInterval("3h"), 10800000);

        // ENSURE DOUBLES ARE PARSED CORRECTLY
        Assertions.assertEquals(Time.parseInterval("9.5h"), 684000);
        Assertions.assertNotEquals(Time.parseInterval("9.5h"), 648000); // <-- 9h Scheduler Friendly
        Assertions.assertNotEquals(Time.parseInterval("9.5h"), 34200000); // <-- Non Scheduler Friendly
        Assertions.assertNotEquals(Time.parseInterval("9.5h"), 32400000); // <-- 9h Non Scheduler Friendly
    }

    @Test
    @DisplayName("Ensure adding and subtracting work as intended")
    public void testDateModification() {
        String DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm:ss";
        String DATE_FORMAT = "MM/dd/yyyy";

        // MODIFY DATE AND TIME
        Assertions.assertEquals(Time.add("01/01/2023 00:00:00", "2y 8mo 10d 5h 12m 30s"), Time.parseDate(DATE_TIME_FORMAT, "09/11/2025 05:12:30"));

        // MODIFY DATE
        Assertions.assertEquals(Time.add("01/01/2023", "37mo 9d"), Time.parseDate(DATE_FORMAT, "02/10/2026"));
    }
}

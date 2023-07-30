package com.moleculepowered.api.util;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A utility class that provides various methods for handling time and dates.
 *
 * <p>This class offers a set of useful methods for performing operations related
 * to time, dates, and other related functionalities.</p>
 *
 * <p>For example, it provides methods for converting between different time formats,
 * calculating time differences, and manipulating dates.</p>
 *
 * @author OMGitzFROST
 */
@SuppressWarnings("unused")
public final class Time
{
    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyy";

    /*
    DATE MODIFIERS
     */

    /**
     * Adds a specified amount of time to the start date.
     *
     * <p>This method requires a specific input format: "[#][unit]". For example, "2h" adds 2 hours to the provided date.
     * See the table below for all accepted unit abbreviations:</p>
     *
     * <table>
     *   <col width="30%"/>
     *   <col width="10%"/>
     *   <col width="60%"/>
     *   <thead>
     *     <tr>
     *       <th>Unit</th>
     *       <th></th>
     *       <th>Abbreviation</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td>Millisecond</td>
     *       <td></td>
     *       <td>[ms, milli, millisecond, milliseconds]</td>
     *     </tr>
     *     <tr>
     *       <td>Second</td>
     *       <td></td>
     *       <td>[s, sec, second, seconds]</td>
     *     </tr>
     *     <tr>
     *       <td>Minute</td>
     *       <td></td>
     *       <td>[m, min, minute, minutes]</td>
     *     </tr>
     *     <tr>
     *       <td>Hour</td>
     *       <td></td>
     *       <td>[h, hr, hour, hours]</td>
     *     </tr>
     *     <tr>
     *       <td>Day</td>
     *       <td></td>
     *       <td>[d, day, days]</td>
     *     </tr>
     *     <tr>
     *       <td>Week</td>
     *       <td></td>
     *       <td>[wk, week, weeks]</td>
     *     </tr>
     *     <tr>
     *       <td>Month</td>
     *       <td></td>
     *       <td>[mo, month, months]</td>
     *     </tr>
     *     <tr>
     *       <td>Year</td>
     *       <td></td>
     *       <td>[y, year, years]</td>
     *     </tr>
     *   </tbody>
     * </table>
     *
     * @param start the date that will be modified
     * @param input the time to be added to the start date
     * @return the new date
     * @throws IllegalArgumentException if an invalid input is entered
     * @apiNote Decimal values are not allowed in the input; if provided, they will be treated as whole numbers.
     */
    public static @NotNull Date add(@NotNull Date start, @NotNull String input) {

        for (String current : input.split("\\s+")) {

            // CONFIGURE PARAMETERS
            double quantity = getQuantity(current);
            String unit = getUnit(current);

            // CREATE CALENDAR OBJECT
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(start);

            switch (unit.toLowerCase()) {
                case "ms":
                case "milli":
                case "millisecond":
                case "milliseconds":
                    startDate.add(Calendar.MILLISECOND, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "s":
                case "sec":
                case "second":
                case "seconds":
                    startDate.add(Calendar.SECOND, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "m":
                case "min":
                case "minute":
                case "minutes":
                    startDate.add(Calendar.MINUTE, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "h":
                case "hr":
                case "hour":
                case "hours":
                    startDate.add(Calendar.HOUR, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "d":
                case "day":
                case "days":
                    startDate.add(Calendar.DATE, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "wk":
                case "week":
                case "weeks":
                    startDate.add(Calendar.WEEK_OF_MONTH, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "mo":
                case "month":
                case "months":
                    startDate.add(Calendar.MONTH, (int) quantity);
                    start = startDate.getTime();
                    break;
                case "y":
                case "year":
                case "years":
                    startDate.add(Calendar.YEAR, (int) quantity);
                    start = startDate.getTime();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time format provided: " + current);
            }
        }
        return start;
    }

    /**
     * <p>Adds a specified amount of time to the start date, please note that this method does require
     * a specific format to be entered as its input, That format is as follows "[#][unit]", for example "2h".</p>
     *
     * <p>With that input, it will tell this method to add 2 hours to the provided date, please take a look at
     * the table below for all available unit abbreviations that are accepted by this method</p>
     *
     * <table>
     *     <col width="30%"/>
     *     <col width="10%"/>
     *     <col width="60%"/>
     *     <thead>
     *     <tr>
     *         <th>Unit</th>
     *         <th></th>
     *         <th>Abbreviation</th>
     *     </tr>
     *     </thead>
     *     <tbody>
     *      <tr>
     *          <td>Millisecond</td>
     *          <td></td>
     *          <td>[ms, milli, millisecond, milliseconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Second</td>
     *          <td></td>
     *          <td>[s, sec, second, seconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Minute</td>
     *          <td></td>
     *          <td>[m, min, minute, minutes]</td>
     *      </tr>
     *      <tr>
     *          <td>Hour</td>
     *          <td></td>
     *          <td>[h, hr, hour, hours]</td>
     *      </tr>
     *      <tr>
     *          <td>Day</td>
     *          <td></td>
     *          <td>[d, day, days]</td>
     *      </tr>
     *      <tr>
     *          <td>Week</td>
     *          <td></td>
     *          <td>[wk, week, weeks]</td>
     *      </tr>
     *      <tr>
     *          <td>Month</td>
     *          <td></td>
     *          <td>[mo, month, months]</td>
     *      </tr>
     *      <tr>
     *          <td>Year</td>
     *          <td></td>
     *          <td>[y, year, years]</td>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param start The date that will be modified
     * @param input The time that will be added to the start date
     * @return The new date
     * @throws IllegalArgumentException when an invalid input is entered
     * @apiNote Decimal values are not allowed in the input, if you provide some, they will
     * lose their decimal point, and we will add as usual.
     */
    public static @NotNull Date add(@NotNull String start, @NotNull String input) {
        return add(parseDate(start), input);
    }

    /**
     * <p>Adds a specified amount of time to today's date, please note that this method does require a specific
     * format to be entered as its input, That format is as follows "[#][unit]", for example "2h".</p>
     *
     * <p>With that input, it will tell this method to add 2 hours to today's date, please take a look at
     * the table below for all available unit abbreviations that are accepted by this method</p>
     *
     * <table>
     *     <col width="30%"/>
     *     <col width="10%"/>
     *     <col width="60%"/>
     *     <thead>
     *     <tr>
     *         <th>Unit</th>
     *         <th></th>
     *         <th>Abbreviation</th>
     *     </tr>
     *     <thead>
     *     <tbody>
     *      <tr>
     *          <td>Millisecond</td>
     *          <td></td>
     *          <td>[ms, milli, millisecond, milliseconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Second</td>
     *          <td></td>
     *          <td>[s, sec, second, seconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Minute</td>
     *          <td></td>
     *          <td>[m, min, minute, minutes]</td>
     *      </tr>
     *      <tr>
     *          <td>Hour</td>
     *          <td></td>
     *          <td>[h, hr, hour, hours]</td>
     *      </tr>
     *      <tr>
     *          <td>Day</td>
     *          <td></td>
     *          <td>[d, day, days]</td>
     *      </tr>
     *      <tr>
     *          <td>Week</td>
     *          <td></td>
     *          <td>[wk, week, weeks]</td>
     *      </tr>
     *      <tr>
     *          <td>Month</td>
     *          <td></td>
     *          <td>[mo, month, months]</td>
     *      </tr>
     *      <tr>
     *          <td>Year</td>
     *          <td></td>
     *          <td>[y, year, years]</td>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param input The time that will be added to today's date
     * @return The new date
     * @throws IllegalArgumentException when an invalid input is entered
     * @apiNote Decimal values are not allowed in the input, if you provide some, they will
     * lose their decimal point, and we will add as usual.
     */
    public static @NotNull Date add(@NotNull String input) {
        return add(new Date(), input);
    }

    /**
     * <p>Subtracts a specified amount of time to the start date, please note that this method does require
     * a specific format to be entered as its input, That format is as follows "[#][unit]", for example "2h".</p>
     *
     * <p>With that input, it will tell this method to subtracts 2 hours to the date, please take a look at
     * the table below for all available unit abbreviations that are accepted by this method</p>
     *
     * <table>
     *     <col width="30%"/>
     *     <col width="10%"/>
     *     <col width="60%"/>
     *     <thead>
     *     <tr>
     *         <th>Unit</th>
     *         <th></th>
     *         <th>Abbreviation</th>
     *     </tr>
     *     <thead>
     *     <tbody>
     *      <tr>
     *          <td>Millisecond</td>
     *          <td></td>
     *          <td>[ms, milli, millisecond, milliseconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Second</td>
     *          <td></td>
     *          <td>[s, sec, second, seconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Minute</td>
     *          <td></td>
     *          <td>[m, min, minute, minutes]</td>
     *      </tr>
     *      <tr>
     *          <td>Hour</td>
     *          <td></td>
     *          <td>[h, hr, hour, hours]</td>
     *      </tr>
     *      <tr>
     *          <td>Day</td>
     *          <td></td>
     *          <td>[d, day, days]</td>
     *      </tr>
     *      <tr>
     *          <td>Week</td>
     *          <td></td>
     *          <td>[wk, week, weeks]</td>
     *      </tr>
     *      <tr>
     *          <td>Month</td>
     *          <td></td>
     *          <td>[mo, month, months]</td>
     *      </tr>
     *      <tr>
     *          <td>Year</td>
     *          <td></td>
     *          <td>[y, year, years]</td>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param start The date that will be modified
     * @param input The time that will be subtracted from the start date
     * @return A new date
     * @throws IllegalArgumentException when an invalid input is entered
     * @apiNote Decimal values are not allowed in the input, if you provide some, they will
     * lose their decimal point, and we will subtract as usual.
     */
    public static @NotNull Date subtract(@NotNull Date start, @NotNull String input) {

        // ADD NEGATIVE VALUES TO EACH TIME UNIT TO ALLOW THIS METHOD TO SUBTRACT
        for (String current : input.split("\\s+")) {
            input = input.replaceAll(current, "-" + current);
        }
        return add(start, input);
    }

    /**
     * <p>Subtracts a specified amount of time to the start date, please note that this method does require
     * a specific format to be entered as its input, That format is as follows "[#][unit]", for example "2h".</p>
     *
     * <p>With that input, it will tell this method to subtract 2 hours to the date, please take a look at
     * the table below for all available unit abbreviations that are accepted by this method</p>
     *
     * <table>
     *     <col width="30%"/>
     *     <col width="10%"/>
     *     <col width="60%"/>
     *     <thead>
     *     <tr>
     *         <th>Unit</th>
     *         <th></th>
     *         <th>Abbreviation</th>
     *     </tr>
     *     <thead>
     *     <tbody>
     *      <tr>
     *          <td>Millisecond</td>
     *          <td></td>
     *          <td>[ms, milli, millisecond, milliseconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Second</td>
     *          <td></td>
     *          <td>[s, sec, second, seconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Minute</td>
     *          <td></td>
     *          <td>[m, min, minute, minutes]</td>
     *      </tr>
     *      <tr>
     *          <td>Hour</td>
     *          <td></td>
     *          <td>[h, hr, hour, hours]</td>
     *      </tr>
     *      <tr>
     *          <td>Day</td>
     *          <td></td>
     *          <td>[d, day, days]</td>
     *      </tr>
     *      <tr>
     *          <td>Week</td>
     *          <td></td>
     *          <td>[wk, week, weeks]</td>
     *      </tr>
     *      <tr>
     *          <td>Month</td>
     *          <td></td>
     *          <td>[mo, month, months]</td>
     *      </tr>
     *      <tr>
     *          <td>Year</td>
     *          <td></td>
     *          <td>[y, year, years]</td>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param input The time that will be subtracted to the start date
     * @return A new date
     * @throws IllegalArgumentException when an invalid input is entered
     * @apiNote Decimal values are not allowed in the input, if you provide some, they will
     * lose their decimal point, and we will subtract as usual.
     */
    public static @NotNull Date subtract(@NotNull String input) {
        return subtract(new Date(), input);
    }

    /*
    DATE PARSERS
     */

    /**
     * Parses the input into a usable {@link Date} object. This method takes a format as its first
     * parameter, please note that this format must match the format provided as the input.
     *
     * @param format Format representation of the input
     * @param input  Target date
     * @return A date from a string
     * @throws IllegalArgumentException when this method fails to parse the input into a date
     */
    public static @NotNull Date parseDate(@NotNull String format, @NotNull String input) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            return df.parse(input);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse date for this input: " + input);
        }
    }

    /**
     * Parses the input into a usable {@link Date} object. Please note that the input must follow
     * the following format <b>"MM/dd/yyyy"</b> or otherwise an error will be thrown.
     *
     * @param input Target date
     * @return A date from a string
     * @throws IllegalArgumentException when this method fails to parse the input into a date
     */
    public static @NotNull Date parseDate(@NotNull String input) {
        return parseDate(DEFAULT_DATE_FORMAT, input);
    }

    /*
    INTERVAL PARSING
     */

    /**
     * <p>Mainly designed for repeating tasks, this method takes an input and converts it into a usable
     * value, used by schedulers and similar.</p>
     *
     * <p>Please take a look at the table below for examples of valid inputs.</p>
     * <table>
     *     <col width="30%"/>
     *     <col width="10%"/>
     *     <col width="60%"/>
     *     <thead>
     *     <tr>
     *         <th>Unit</th>
     *         <th></th>
     *         <th>Abbreviation</th>
     *     </tr>
     *     <thead>
     *     <tbody>
     *      <tr>
     *          <td>Millisecond</td>
     *          <td></td>
     *          <td>[ms, milli, millisecond, milliseconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Second</td>
     *          <td></td>
     *          <td>[s, sec, second, seconds]</td>
     *      </tr>
     *      <tr>
     *          <td>Minute</td>
     *          <td></td>
     *          <td>[m, min, minute, minutes]</td>
     *      </tr>
     *      <tr>
     *          <td>Hour</td>
     *          <td></td>
     *          <td>[h, hr, hour, hours]</td>
     *      </tr>
     *      <tr>
     *          <td>Day</td>
     *          <td></td>
     *          <td>[d, day, days]</td>
     *      </tr>
     *      <tr>
     *          <td>Week</td>
     *          <td></td>
     *          <td>[wk, week, weeks]</td>
     *      </tr>
     *      <tr>
     *          <td>Month</td>
     *          <td></td>
     *          <td>[mo, month, months]</td>
     *      </tr>
     *      <tr>
     *          <td>Year</td>
     *          <td></td>
     *          <td>[y, year, years]</td>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param input Provided input
     * @return A usable interval
     * @throws IllegalArgumentException when the input is not valid
     */
    public static long parseInterval(@NotNull String input) {
        double quantity = getQuantity(input);
        String unit = getUnit(input);

        switch (unit) {
            case "ms":
            case "milli":
            case "millisecond":
            case "milliseconds":
                return (long) (quantity / 50);
            case "s":
            case "sec":
            case "second":
            case "seconds":
                return (long) ((1000L * quantity) / 50);
            case "m":
            case "min":
            case "minute":
            case "minutes":
                return (long) ((60000L * quantity) / 50);
            case "h":
            case "hr":
            case "hour":
            case "hours":
                return (long) ((3600000L * quantity) / 50);
            case "d":
            case "day":
            case "days":
                return (long) ((86400000L * quantity) / 50);
            case "wk":
            case "week":
            case "weeks":
                return (long) ((604800000L * quantity) / 50);
            case "mo":
            case "month":
            case "months":
                return (long) ((2629800000L * quantity) / 50);
            case "y":
            case "year":
            case "years":
                return (long) ((31557600000L * quantity) / 50);
            default:
                throw new IllegalArgumentException("Invalid interval format provided: " + input);
        }
    }

    /*
    DATE COMPARING
     */

    /**
     * A method used to test whether a date is before a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is before the compared date.
     */
    public static boolean isBeforeDate(@NotNull String target, @NotNull String comparedTo) {
        return parseDate(target).before(parseDate(comparedTo));
    }

    /**
     * A method used to test whether a date is before a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is before the compared date.
     */
    public static boolean isBeforeDate(@NotNull String target, @NotNull Date comparedTo) {
        return parseDate(target).before(comparedTo);
    }

    /**
     * A method used to test whether a date is before a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is before the compared date.
     */
    public static boolean isBeforeDate(@NotNull Date target, @NotNull String comparedTo) {
        return target.before(parseDate(comparedTo));
    }

    /**
     * A method used to test whether a date is before a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is before the compared date.
     */
    public static boolean isBeforeDate(@NotNull Date target, @NotNull Date comparedTo) {
        return target.before(comparedTo);
    }

    /**
     * A method used to test whether a date is after a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is after the compared date.
     */
    public static boolean isAfterDate(@NotNull String target, @NotNull String comparedTo) {
        return parseDate(target).after(parseDate(comparedTo));
    }

    /**
     * A method used to test whether a date is after a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is after the compared date.
     */
    public static boolean isAfterDate(@NotNull Date target, @NotNull Date comparedTo) {
        return target.after(comparedTo);
    }

    /**
     * A method used to test whether a date is after a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is after the compared date.
     */
    public static boolean isAfterDate(@NotNull String target, @NotNull Date comparedTo) {
        return parseDate(target).after(comparedTo);
    }

    /**
     * A method used to test whether a date is after a different date.
     *
     * @param target     The date being tested.
     * @param comparedTo The date you will compare the target to.
     * @return Whether the target date is after the compared date.
     */
    public static boolean isAfterDate(@NotNull Date target, @NotNull String comparedTo) {
        return target.after(parseDate(comparedTo));
    }

    /*
    TIME REMAINING
     */

    /**
     * A method used to return the amount of time remaining until a specified date. If any of the values
     * listed in the format below return 0, this method will remove it from the returned string
     * <br><br/>
     * <strong>String Format: year(s) day(s) hour(s) minute(s) second(s).</strong>
     *
     * @param start Starting date
     * @param end   Target date
     * @return Time until target date.
     */
    public static String getTimeRemaining(@NotNull Date start, @NotNull Date end) {
        Duration d1 = Duration.between(start.toInstant(), end.toInstant());
        long seconds = (d1.getSeconds()) % 60;
        long minutes = (d1.toMinutes()) % 60;
        long hours = (d1.toHours()) % 24;

        LocalDate ld1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ld2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period age = Period.between(ld1, ld2);
        long days = age.getDays();
        long months = age.getMonths();
        long years = age.getYears();

        String[] remainFormat = String.format("%02dy %02dmo %02dd %02dh %02dm %02ds", years, months, days, hours, minutes, seconds).split("\\s+");
        return Arrays.stream(remainFormat).filter(current -> !current.contains("00") && !current.equals("00s")).collect(Collectors.joining(" "));
    }

    /**
     * A method used to return the amount of time remaining until a specified date. If any of the values
     * listed in the format below return 0, this method will remove it from the returned string
     * <br><br/>
     * <strong>String Format: year(s) day(s) hour(s) minute(s) second(s).</strong>
     *
     * @param start Starting date
     * @param end   Target date
     * @return Time until target date.
     */
    public static String getTimeRemaining(@NotNull Date start, @NotNull String end) {
        return getTimeRemaining(start, parseDate(end));
    }

    /**
     * A method used to return the amount of time remaining until a specified date. If any of the values
     * listed in the format below return 0, this method will remove it from the returned string
     * <br><br/>
     * <strong>String Format: year(s) day(s) hour(s) minute(s) second(s).</strong>
     *
     * @param start Starting date
     * @param end   Target date
     * @return Time until target date.
     */
    public static String getTimeRemaining(@NotNull String start, @NotNull Date end) {
        return getTimeRemaining(parseDate(start), end);
    }

    /**
     * A method used to return the amount of time remaining until a specified date. If any of the values
     * listed in the format below return 0, this method will remove it from the returned string
     * <br><br/>
     * <strong>String Format: year(s) day(s) hour(s) minute(s) second(s).</strong>
     *
     * @param start Starting date
     * @param end   Target date
     * @return Time until target date.
     */
    public static String getTimeRemaining(@NotNull String start, @NotNull String end) {
        return getTimeRemaining(parseDate(start), end);
    }

    /**
     * A method used to return the amount of time remaining until a specified date. If any of the values
     * listed in the format below return 0, this method will remove it from the returned string
     * <br><br/>
     * <strong>String Format: year(s) day(s) hour(s) minute(s) second(s).</strong>
     *
     * @param end Target date
     * @return Time until target date.
     */
    public static String getTimeRemaining(@NotNull String end) {
        return getTimeRemaining(getToday().getTime(), end);
    }

    /**
     * <p>A method used to return the amount of time remaining until a specified date.
     * If any of the values listed in the format below return 0, this method will remove
     * it from the returned string.</p>
     *
     * <p><strong>String Format: year(s) day(s) hour(s) minute(s) second(s).</strong></p>
     *
     * @param end Target date
     * @return Time until target date.
     */
    public static String getTimeRemaining(@NotNull Date end) {
        return getTimeRemaining(getToday().getTime(), end);
    }

    /*
    GET TODAY
     */

    /**
     * Used to return today's date.
     *
     * @return Today's date
     */
    public static @NotNull Calendar getToday() {
        return Calendar.getInstance();
    }

    /**
     * Used to return today's date as a string.
     *
     * @return Today's date as a string
     */
    public static @NotNull String getTodayAsString() {
        return getToday().toString();
    }

    /**
     * Used to return today's date as a string, this method will parse the date into a
     * more readable format.
     *
     * @param format Target format
     * @return Today's date as a formatted string
     */
    public static @NotNull String getTodayAsString(String format) {
        return new SimpleDateFormat(format).format(getToday());
    }

    /*
    UTILITY METHODS
     */

    /**
     * <p>Returns the quantity portion from a string, in the example "2h", the 2 is considered the
     * quantity and thus will be returned by this method.</p>
     *
     * <p>Please note that if a valid quantity could not be found, this method will return 0 in its place,
     * essentially rendering this method pointless</p>
     *
     * @param input Provided input
     * @return The quantity or "0"
     */
    private static @NotNull Double getQuantity(String input) {
        Matcher quantityMatcher = Pattern.compile("(-?\\d+(\\.\\d*)?)|(-?\\.\\d+)").matcher(input != null ? input : "0");
        return quantityMatcher.find() ? Double.parseDouble(quantityMatcher.group()) : 0;
    }

    /**
     * Returns the unit portion from a string, in the example "2h", the "h" is considered the unit
     * and thus will be returned by this method.
     *
     * <p>Please note that if a valid unit could not be found, this method will return "s" in its place, which
     * stands for "seconds", essentially making any side effect minor</p>
     *
     * @param input Provided input
     * @return The unit or "s"
     */
    private static @NotNull String getUnit(String input) {
        Matcher unitMatcher = Pattern.compile("[a-zA-Z]+").matcher(input != null ? input : "s");
        return unitMatcher.find() ? unitMatcher.group() : "s";
    }
}

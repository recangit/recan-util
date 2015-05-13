package se.recan.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Time starts at 00:00:00.
 * Time ends at 23:59:59.
 *
 * 2013-apr-22
 * @author Anders Recksén recan
 */
 
public class DateUtil {

    public static final Locale LOCALE = new Locale("sv", "SE");

    public static final String DATE_PATTERN = "yyyy-MMMM-dd";                   // 2010-maj-11

    public static final long MILLIS_IN_SECOND = 1000;                           // Milliseconds in one second.
    public static final long MILLIS_IN_MINUTE = (60 * 1000);                    // Milliseconds in one minute.
    public static final long MILLIS_IN_HOUR =   (60 * 60 * 1000);               // Milliseconds in one hour.
    public static final long MILLIS_IN_DAY =    (60 * 60 * 1000 * 24);          // Milliseconds in one day.

    private static final int[] NUMB_MONTHS = {
                                                31, 28, 31, 30,                 // jan, feb, mars, apr
                                                31, 30, 31, 31,                 // maj, juni, juli, aug
                                                30, 31, 30, 31                  // sept, okt, nov, dec
                                            };
    private final static String[] DAYNAMES = {   "Söndag", "Måndag", "Tisdag", "Onsdag",
                                                "Torsdag", "Fredag", "Lördag"
                                            };
    private final static String[] MONTHNAMES = { "Januari", "Februari", "Mars", "April",
                                                "Maj", "Juni", "Juli", "Augusti",
                                                "September", "Oktober", "November", "December"
                                              };

    /**
     * Parse a long to a readable time format.
     * Set pattern in the form yyyy-MMMM-dd or yyyy-MMMM-dd-HH-m-s-S.
     * @param milliseconds
     * @param pattern
     * @return  Time as String, example 2009-10-06
     */
    public static String dateToString(long milliseconds, String pattern) {
        // Otherwise 0 will be formatted into givenpattern, like 01:00.
        if(milliseconds==0) { return ""; }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, LOCALE);
        return formatter.format(milliseconds);
    }

    /**
     * Parse a String describing a Date to long.
     * Describe date in the form yyyy-MM-dd-mm-ss-mm or yy-MM-dd-mm-ss-mm.
     * If year is described with two numbers it's prefixed with 20.
     * Lowest allowed input is yy + delimeter.
     * @param inputDate
     * @param delimeter
     * @return  Time as long.
     * @throws java.lang.Exception
     */
    public static long dateToLong(String inputDate, String delimeter) throws Exception {
        int index = inputDate.indexOf(delimeter);
        if(index==-1 || index==0) {
            throw new Exception("This [\"" + delimeter + "\"] is not a valid delimeter in String (" + inputDate + "!");
        }

        Calendar cal = Calendar.getInstance(LOCALE);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        StringTokenizer date = new StringTokenizer(inputDate, delimeter);

        if (date.hasMoreTokens()) {
            String token = date.nextToken();
            if(token.length()==2) { token="20" + token; }
            cal.set(Calendar.YEAR, Integer.parseInt(token));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.MONTH, Integer.parseInt(date.nextToken())-1);
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.MINUTE, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.SECOND, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.MILLISECOND, Integer.parseInt(date.nextToken()));
        }

        return cal.getTimeInMillis();
    }

    /**
     * Parse milliseconds to minutes.
     * @param millisecond
     * @return long representing minutes.
     */
    public static long toMinutes(long millisecond) {
        return millisecond/MILLIS_IN_MINUTE;
    }

    /**
     * Parse milliseconds to hours.
     * @param millisecond
     * @return  long representing hours.
     */
    public static long toHours(long millisecond) {
        return millisecond/MILLIS_IN_HOUR;
    }

    // Stupid as only one pattern is allowed.
    // If I nead this method then rename and redo.
    public static boolean validateDate(String date) {
        return Pattern.matches("\\d{4}[-]\\d{2}[-]\\d{2}", date);
    }
    
    /**
     * Return full dayname in swedish.
     * @param millisecond
     * @return  String  dayname
     */
    public static String getWeekday(long millisecond) {
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.setTimeInMillis(millisecond);
        int weekday = cal.get(Calendar.DAY_OF_WEEK)-1;

        return DAYNAMES[weekday];
    }

    /**
     * Return full monthname in swedish.
     * @param millisecond
     * @return  String monthname
     */
    public static String getMonthName(long millisecond) {
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.setTimeInMillis(millisecond);
        int month = cal.get(Calendar.MONTH);

        return MONTHNAMES[month];
    }

    /**
     * If year divided with four equals zero, then februari contains 29 days.
     * @param   year
     * @param   month
     * @return  int     number of days
     */
    public static int getDaysInMonth(int year, int month) {
        int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        int numbday = months[month];

        if(numbday==28) {
            if(year % 4 == 0) {
                numbday = 29;
            }
        }

        return numbday;
    }

    /**
     * Parse milliseconds to an int describing a year.
     * If parameter milliseconds equals 0 then current year will be returned.
     * @param   millisecond
     * @return  year
     */
    public static int getYear(long millisecond) {
        return getCalendar(millisecond).get(Calendar.YEAR);
    }

    /**
     * Parse milliseconds to an int describing a month.
     * If parameter milliseconds equals 0 then current month will be returned.
     * @param   millisecond
     * @return  month
     */
    public static int getMonth(long millisecond) {
        return getCalendar(millisecond).get(Calendar.MONTH);
    }

    /**
     * Parse milliseconds to an int describing a week.
     * If parameter milliseconds equals 0 then current week will be returned.
     * @param millisecond
     * @return weeknumber 
     */
    public static int getWeek(long millisecond) {
        return getCalendar(millisecond).get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Parse milliseconds to an int describing a day.
     * If parameter milliseconds equals 0 then current day will be returned.
     * @param millisecond
     * @return  int     day
     */
    public static int getDay(long millisecond) {
        return getCalendar(millisecond).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Parse milliseconds to an int describing a hour.
     * If parameter milliseconds equals 0 then current hour will be returned.
     * @param millisecond
     * @return hour
     */
    public static int getHour(long millisecond) {
        return getCalendar(millisecond).get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Parse milliseconds to an int describing a minute.
     * If parameter milliseconds equals 0 then current minute will be returned.
     * @param millisecond
     * @return minute
     */
    public static int getMinute(long millisecond) {
        return getCalendar(millisecond).get(Calendar.MINUTE);
    }

    /**
     * Get absolute year start.
     * A year starts at 09-01-01 24-00-00.
     * @param year
     * @return A year start as long.
     */
    public static long getYearStart(int year) {
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);

        return cal.getTimeInMillis();
    }

    /**
     * Get absolute last in year.
     * A year ends at 09-12-31 23-59-59.
     * @param year
     * @return End of a year as long.
     */
    public static long getYearStop(int year) {
        long thisYear = getYearStart(year);
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.setTimeInMillis(thisYear);
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.MILLISECOND, -1);

        return cal.getTimeInMillis();
    }

    /**
     * Get absolute first in month.
     * @param year
     * @param month 1-12
     * @return
     */
    public static long getMonthStart(int year, int month) {
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    /**
     * Get absolute last in month.
     * @param year
     * @param month 1-12
     * @return
     */
    public static long getMonthEnd(int year, int month) {
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, NUMB_MONTHS[month-1]);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
    
    // Returnera den första i nuvarande månad minus antal månader
    // Är subtract 1 returneras början på föregående månad
    // Är subtract -1 returneras början på nästkommande månad
    public static long getMonthStart(int subtract) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, - subtract);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
    
    // Returnera den sista i nuvarande månad
    public static long getMonthEnd() {
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);
        
        return cal.getTimeInMillis();
    }
    
    /**
     * Number of days between two dates
     * @param date1 first date as long
     * @param date2 second date as long
     * @return number of days as int
     */
    public static int numberOfDays(long date1, long date2) {
        long diffInMillis = date2 - date1;
        return (int) (diffInMillis / (24* 1000 * 60 * 60));
    }
    
    /** 
     * Private helper method.
     * @param arg1    parameter
     * @param arg2    parameter
     * @return        
     * @throws Exception
     */
    private static Calendar getCalendar(long millisecond) {
        if(millisecond==0) { millisecond = System.currentTimeMillis(); }
        Calendar cal = Calendar.getInstance(LOCALE);
        cal.setTimeInMillis(millisecond);
        return cal;
    }

    /**
     * Helper method to use when developing.
     */
    public static void printConstants() {
        Calendar c = Calendar.getInstance();
        System.out.println("  YEAR                 : " + c.get(Calendar.YEAR));
        System.out.println("  MONTH                : " + c.get(Calendar.MONTH));
        System.out.println("  DAY_OF_MONTH         : " + c.get(Calendar.DAY_OF_MONTH));
        System.out.println("  DAY_OF_WEEK          : " + c.get(Calendar.DAY_OF_WEEK));
        System.out.println("  DAY_OF_YEAR          : " + c.get(Calendar.DAY_OF_YEAR));
        System.out.println("  WEEK_OF_YEAR         : " + c.get(Calendar.WEEK_OF_YEAR));
        System.out.println("  WEEK_OF_MONTH        : " + c.get(Calendar.WEEK_OF_MONTH));
        System.out.println("  DAY_OF_WEEK_IN_MONTH : " + c.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("  HOUR                 : " + c.get(Calendar.HOUR));
        System.out.println("  AM_PM                : " + c.get(Calendar.AM_PM));
        System.out.println("  HOUR_OF_DAY (24-hour): " + c.get(Calendar.HOUR_OF_DAY));
        System.out.println("  MINUTE               : " + c.get(Calendar.MINUTE));
        System.out.println("  SECOND               : " + c.get(Calendar.SECOND));
    }
}
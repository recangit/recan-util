package se.recan.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

/**
 * Initialize this Constructor with:
 * new Scheduler(this, "void-method-to-call"); Will run "forever" once a minute.
 * new Scheduler(this, "void-method-to-call", 10); Will run "forever" every ten second.
 * new Scheduler(this, "void-method-to-call", 10, "12:10:17:20:00"); Will run until given date every ten second.
 * new Scheduler(this, "void-method-to-call", 10, 30); Will run for 30 seconds every ten second.
 * new Scheduler(this, "void-method-to-call", "12:10:17:20:00", 10, 5); Will run for 5 minutes every ten second.
 *
 * Inner classes can only be called from outer class.
 *
 * Created: 2012-10-17 Last Modified: 2012-10-17
 *
 * @author Anders Recksén recan
 */
public class ScheduleUtil {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    
    private Timer timer;
    private static final long START = 10 * 1000;  // 10 seconds before first method call.
    private static final long DELAY = 60 * 1000; // 60 seconds between method calls.
 
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;

    /**
     * This will run forever.
     * @param obj Object that called this method
     * @param methodName Method in calling object to run
     * @throws java.lang.Exception
     */
    public ScheduleUtil(Object obj, String methodName) throws Exception {
        timer = new Timer();
        Schedule schedule = new Schedule(obj, methodName);
        timer.schedule(schedule, START, DELAY);
    }

    /**
     * @param obj Object that called this method
     * @param methodName Method in calling object to run
     * @param delay Time to wait between method call in Seconds
     * @throws Exception
     */
    public ScheduleUtil(Object obj, String methodName, int delay) throws Exception {
        timer = new Timer();
        Schedule schedule = new Schedule(obj, methodName);
        timer.schedule(schedule, START, delay*1000);
    }

    /**
     * @param obj Object that called this method
     * @param methodName Method in calling object to run
     * @param delay Time to wait between method call in milliSeconds
     * @param shutdownTime Set time when to shutdown in the form
     * yy:mm:dd:hh:mm:ss
     * @throws Exception
     */
    public ScheduleUtil(Object obj, String methodName, long delay, String shutdownTime) throws Exception {
        long shutdown = DateUtil.dateToLong(shutdownTime, ":");
        timer = new Timer();
        Schedule schedule = new Schedule(obj, methodName);
        schedule.setShutdown(shutdown);
        timer.schedule(schedule, START, delay);
    }
    
    /**
     * @param obj Object that called this method
     * @param methodName Method in calling object to run
     * @param startTime Time when to start in the form yy:mm:dd:hh:mm:ss
     * @param delay Time to wait between method call in seconds
     * @param shutdownTime Time when to shutdown in the form yy:mm:dd:hh:mm:ss
     * @throws Exception
     */
    public ScheduleUtil(Object obj, String methodName, String startTime, int delay, String shutdownTime) throws Exception {
        long start = DateUtil.dateToLong(startTime, ":");
        long shutdown = DateUtil.dateToLong(shutdownTime, ":");
        Date date = new Date();
        date.setTime(start);
        timer = new Timer();
        Schedule schedule = new Schedule(obj, methodName);
        schedule.setShutdown(shutdown);
        timer.schedule(schedule, date, delay*1000);
    }
    
    /**
     * @param obj Object that called this method
     * @param methodName Method in calling object to run
     * @param startTime Time when to start in the form yy:mm:dd:hh:mm:ss
     * @param delay Time to wait between method call in seconds
     * @param timeToRun Time to run before shutdown in minutes
     * @throws Exception
     */
    public ScheduleUtil(Object obj, String methodName, String startTime, int delay, int timeToRun) throws Exception {
        long start = DateUtil.dateToLong(startTime, ":");
        long shutdown = System.currentTimeMillis() + (timeToRun * MINUTE);
        Date date = new Date();
        date.setTime(start);
        timer = new Timer();
        Schedule schedule = new Schedule(obj, methodName);
        schedule.setShutdown(shutdown);
        timer.schedule(schedule, date, delay*1000);
    }

    /**
     * @param obj Object that called this method
     * @param methodName Method in calling object to run
     * @param delay Time to wait between method call in milliSeconds
     * @param timeToRun Time to run before shutdown in milliSeconds
     * @throws Exception
     */
    public ScheduleUtil(Object obj, String methodName, long delay, long timeToRun) throws Exception {
        long shutdown = System.currentTimeMillis() + timeToRun;
        timer = new Timer();
        Schedule schedule = new Schedule(obj, methodName);
        schedule.setShutdown(shutdown);
        timer.schedule(schedule, delay, delay);
    }

    private void halt(long shutdown, Object obj) throws Exception {
        timer.cancel();
        System.out.println("\tShutdown Scheduler: " + DateUtil.dateToString(shutdown, "kk:mm:ss"));
        cleanUp(obj);
    }

    /*
     * Verify that there is a cleanup method in caller!
     */
    private void cleanUp(Object obj) throws Exception {
        try {
            Class c = Class.forName(obj.getClass().getName());
            Method method = c.getMethod("cleanUp");
            method.invoke(obj);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new ClassNotFoundException("The class cannot be located [" + obj.getClass().getName() + "]");
        }
    }

    private class Schedule extends TimerTask {
        
        private final Object obj;
        private Method method;
        private long shutdown = -1;

        private Schedule(Object obj, String methodName) throws Exception {
            this.obj = obj;
            try {
                Class c = Class.forName(obj.getClass().getName());
                method = c.getMethod(methodName);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
                throw new ClassNotFoundException("Schedule The class cannot be located [" + obj.getClass().getName() + "]");
            }
        }

        private void setShutdown(long shutdown) {
            this.shutdown = shutdown;
        }

        @Override
        public void run() {
            if(shutdown != -1 && shutdown < System.currentTimeMillis()) {
                try {
                    halt(shutdown, obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }

            try {
                System.out.println("\tExecute Scheduler: " + DateUtil.dateToString(System.currentTimeMillis(), "kk:mm:ss"));
                method.invoke(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static class DateUtil {

        private static final Locale locale = new Locale("sv", "SE");

        /**
         * Parse a long to a readable time format. Set pattern in the form
         * yyyy-MMMM-dd or yyyy-MMMM-dd-HH-m-s-S.
         *
         * @param long milliseconds
         * @param String pattern
         * @return Time as String, example 2009-10-06
         */
        private static String dateToString(long milliseconds, String pattern) {
            if (milliseconds == 0) {
                return "";
            }

            SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
            return formatter.format(milliseconds);
        }

        /**
         * Parse a String describing a Date to long. Describe date in the form
         * yyyy-MM-dd-mm-ss-mm or yy-MM-dd-mm-ss-mm. If year is described with
         * two numbers it's prefixed with 20. Lowest allowed input is yy +
         * delimeter.
         *
         * @param String inputDate
         * @param String delimeter
         * @return Time as long.
         */
        private static long dateToLong(String inputDate, String delimeter) throws Exception {
            int index = inputDate.indexOf(delimeter);
            if (index == -1 || index == 0) {
                throw new Exception("This [\"" + delimeter + "\"] is not a valid delimeter in String (" + inputDate + "!");
            }

            Calendar cal = Calendar.getInstance(locale);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            StringTokenizer date = new StringTokenizer(inputDate, delimeter);

            if (date.hasMoreTokens()) {
                String token = date.nextToken();
                if (token.length() == 2) {
                    token = "20" + token;
                }
                cal.set(Calendar.YEAR, Integer.parseInt(token));
            }
            if (date.hasMoreTokens()) {
                cal.set(Calendar.MONTH, Integer.parseInt(date.nextToken()) - 1);
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
    }
}
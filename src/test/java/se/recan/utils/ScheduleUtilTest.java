package se.recan.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;

/**
 * Denna kan inte köras som en test. Förmodligen för att instansen dör efter genomfört test.
 * 
 * Både testen och Utilklassen är lite Hawaii tror jag.
 * 2014-aug-11
 * @author Anders Recksén (recan)
 */
public class ScheduleUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private int counter = 0;

     public void testScheduleUtil() {
         LOGGER.debug("");
         
         long delay = 1000; // 1 sek
         long timeToRun = 5000; // 5 sek
         try {
            LOGGER.info("Initiera Scheduleraren med värden start:" + (delay/1000) + ", sekunder mellan anrop:" + (delay/1000) + ", sekunder att köra:" + (timeToRun/1000));
            new ScheduleUtil(this, "runThisMethod", delay, timeToRun);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement elements[] = e.getStackTrace();
            for (int i = 0, n = elements.length; i < n; i++) {
                LOGGER.info(elements[i].getFileName()
                        + ":" + elements[i].getLineNumber()
                        + " => "
                        + elements[i].getMethodName() + "()");
            }
        }
     }

     public void runThisMethod() {
         counter++;
         LOGGER.debug(counter + "\tExecute Scheduler: " + DateUtil.dateToString(System.currentTimeMillis(), "kk:mm:ss"));
     }
     
     public static void main(String[] args) {
        try {
            new ScheduleUtilTest().testScheduleUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
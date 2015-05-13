package se.recan.utils;

import java.util.Calendar;
import java.util.Locale;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @date 2014-aug-06
 * @author Anders Recksén (recan)
 */
public class DateUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    private static long MILLIS;

     @Test
     public void testDatePattern() {
        
         String date = DateUtil.dateToString(0, "y");
         Assert.assertTrue("Tom long skall ej kasta ett exception utan returnerar en tom sträng", date.isEmpty());
         
         date = DateUtil.dateToString(MILLIS, "y");
         Assert.assertThat(date, containsString("1962"));
         
         date = DateUtil.dateToString(MILLIS, "MM");
         Assert.assertThat(date, containsString("10"));
         
         date = DateUtil.dateToString(MILLIS, "MMM");
         Assert.assertThat(date, containsString("okt"));
         
         date = DateUtil.dateToString(MILLIS, "MMMM");
         Assert.assertThat(date, containsString("oktober"));
         
         date = DateUtil.dateToString(MILLIS, "dd");
         Assert.assertThat(date, containsString("02"));
         
         date = DateUtil.dateToString(MILLIS, "w");
         Assert.assertThat("Veckonummer", date, containsString("40"));
         
         date = DateUtil.dateToString(MILLIS, "EEEE");
         Assert.assertThat("Veckonummer", date, containsString("tisdag"));

         date = DateUtil.dateToString(MILLIS, "HH mm s S");
         Assert.assertThat("Timme, minut sekund, millisekund", date, containsString("04 10 30 0"));
     }
     
     @Test
     public void testGetters() {

         Assert.assertThat(DateUtil.getYear(MILLIS), is(1962));
         Assert.assertThat(DateUtil.getMonth(MILLIS), is(9));
         Assert.assertThat(DateUtil.getMonthName(MILLIS), containsString("Oktober"));
         Assert.assertThat(DateUtil.getDay(MILLIS), is(02));
         Assert.assertThat(DateUtil.getDaysInMonth(1962, 10), is(30));
     }

     @BeforeClass
     public static void init() {
        Calendar cal = Calendar.getInstance(new Locale("sv", "SE"));
        cal.set(Calendar.YEAR, 1962);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DATE, 2);
        cal.set(Calendar.HOUR_OF_DAY, 4);
        cal.set(Calendar.MINUTE, 10);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 0);
        MILLIS = cal.getTimeInMillis();
     }
}
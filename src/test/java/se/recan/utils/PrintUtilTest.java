package se.recan.utils;

import java.util.MissingFormatArgumentException;
import org.junit.Test;

/**
 * 2015-apr-14
 *
 * @author Anders Recksén (recan, Prolore)
 */
public class PrintUtilTest {

    @Test
    public void testPrintUtil() {
        
        String concat = PrintUtil.concat("Bokstäverna {1} och {2} och {3} och {4} och {5} och {6}", "Å", "Ä", "Ö", "å", "ä", "ö");
        PrintUtil.print(concat);

        String[] fmt = {
            "%3s:%s:%s%n",      // Flytta vänstermarginal
            "%6S:%s:%s%n",      // Flytta vänstermarginal
            "%s:%s:%s%n",       // Ingen förflyttning
            "%-5s%-5s%s%n",     // Minus flyttar nästkommande sträng till höger
            "%s:%-2s:%s%n",     // Minus flyttar nästkommande sträng till höger
            "%s:%-12s:%s%n",    // Minus flyttar nästkommande sträng till höger
            "%s:%12s:%s%n",     // Flyttar denna sträng till höger
            "   %s   %s   %s%n" // Kan också göras så här
        };

        for (String s : fmt) {
            PrintUtil.print(s, "A", "B", "C");
        }

        // Lägg till flera parametrar
        PrintUtil.print("%-5s%-5s%-5s%s%n", "A", "B", "C", "D");

        // Endast det antal parametrar som angets i formateringen skrivs ut
        // Alltså tillåtet att skicka in för många parametrar
        PrintUtil.print("%-5s%s%n", "A", "B", "C", "D", "E", "F");
    }
    
    @Test(expected=MissingFormatArgumentException.class)
    public void testRongNumberOfParams() {
        // Färre parametrar än som angets i formateringen ger ett MissingFormatArgumentException
        // Alltså inte tillåtet att skicka in för få paramatrerar
        
        PrintUtil.print("%-5s%-5s%-5s%s%n", "A", "B");
    }
    
//    @Test
    public void testCast() {
        print("A", 2, false);
    }
    
    void print(Object... obj) {
        for(Object o: obj) {
            String s = (String)obj[0];
//            if(o instanceof Boolean) {
//                String s= (String)Boolean.toString(o);
//            }
            System.out.println(o);
        }
        
    }
}

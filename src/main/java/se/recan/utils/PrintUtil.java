package se.recan.utils;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * 2015-apr-14
 *
 * @author Anders Recksén (recan, Prolore)
 */
public class PrintUtil {

    private static String fmt = "%6S:  %-12s = %s%n";
    
    public static void print(boolean b) {
        print(Boolean.toString(b));
    }
    
    public static void print(int i) {
        print(Integer.toString(i));
    }
    
    public static String concat(String... parameters) {
        String concat = parameters[0];
        for (int i = 1; i < parameters.length; i++) {
            concat = concat.replace("{" + (i) + "}", parameters[i]);
        }
        
        return concat;
    }
    
    public static void print(String... parameters) {
        
        if (parameters[0].contains("%")) {
            fmt = parameters[0];
            format(parameters);
            return;
        }
        
        StringBuilder b = new StringBuilder();
        for (String parameter : parameters) {
            b.append(parameter);
        }
        
        PrintStream out;
        try {
            out = new PrintStream(System.out, true, "UTF-8");
            out.println(b.toString());
            out.flush();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
    }
    
     // Parameter parameters[0] contains the format, fmt, and shall not be printed.
    private static void format(String... parameters) {
        
        PrintStream out;
        try {
            out = new PrintStream(System.out, true, "UTF-8");
            switch (parameters.length) {
                case 2:
                    out.format(fmt, parameters[1]);
                    break;
                case 3:
                    out.format(fmt, parameters[1], parameters[2]);
                    break;
                case 4:
                    out.format(fmt, parameters[1], parameters[2], parameters[3]);
                    break;
                case 5:
                    out.format(fmt, parameters[1], parameters[2], parameters[3], parameters[4]);
                    break;
            }
            out.flush();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
    }
}

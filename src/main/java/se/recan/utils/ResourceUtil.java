package se.recan.utils;

import java.util.ResourceBundle;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @date 2014-jul-19
 * @author Anders Recksén (recan)
 */
public class ResourceUtil {

    private static final ResourceBundle RB = ResourceBundle.getBundle("main");

    public static final boolean SHOW_HEADER = getBolProperty("SHOW_HEADER");
    public static final boolean SHOW_PARAM = getBolProperty("SHOW_PARAM");
    public static final boolean SHOW_ATTR = getBolProperty("SHOW_ATTR");
    public static final boolean SHOW_SESS = getBolProperty("SHOW_SESS");
    public static final int PORT = getIntProperty("PORT");

    private static boolean getBolProperty(String prop) throws NullPointerException {
        return Boolean.parseBoolean(RB.getString(prop));
    }

    private static int getIntProperty(String prop) throws NullPointerException {
        return Integer.parseInt(RB.getString(prop));
    }

    private static File getFileProperty(String prop) throws NullPointerException {
        return new File(RB.getString(prop));
    }

    private static URL getUrlProperty(String prop) throws NullPointerException, MalformedURLException {
        return new URL(RB.getString(prop));
    }
}

package se.recan.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import org.apache.log4j.*;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.xml.DOMConfigurator;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;

/**
 *
 * 2014-aug-14
 * @author Anders Recksén (recan)
 */
public class LoggerUtil {

//    static String FQCN = LoggerUtil.class.getName() + ".";
    private static final Logger LOGGER = Logger.getLogger("Logger");
    
    public LoggerUtil(String name) {
//        super(name);
//        LOGGER = Logger.getLogger("Logger");
    }
    
    public static LoggerUtil getLogger(String name) {
//        LOGGER = Logger.getLogger("Logger");
        return new LoggerUtil(name);
    }

//    public static Logger getLogger(Class clazz) {
//        return Logger.getLogger(clazz);
//    }
    
    public void debug(String s1) {
        LOGGER.debug(s1);
    }
    
    public void debug(String s1, String s2) {
        LOGGER.debug(s1 + " " + s2);
    }
    
    public void debug(String s1, String s2, String s3) {
        LOGGER.debug(s1 + " " + s2 + " " + s3);
    }
    
//    public void debug(Object message) {
//    super.log(FQCN, Level.DEBUG, message + " world.", null);    
//  }
    
}

package se.recan.utils;

import org.junit.Test;

/**
 *
 * 2014-aug-14
 * @author Anders Recksén (recan)
 */
public class LoggerUtilTest {

    private static final LoggerUtil LOGGER = LoggerUtil.getLogger("Logger");

    @Test
    public void testLoggerUtil() {
        LOGGER.debug("Anders");
        LOGGER.debug("Anders", "Kajsa", "Annie");
    }

}
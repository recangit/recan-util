package se.recan.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @date 2014-aug-05
 * @author Anders Recksén (recan)
 */
public class CacheUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

     @Test
     public void testCacheUtil() throws Exception {
         Assert.assertEquals("se.recan.app.person.Person.ok", CacheUtil.getProperty("se.recan.app.person.Person.ok"));
         CacheUtil.initCache("SE");
         Assert.assertEquals("Validering OK", CacheUtil.getProperty("se.recan.app.person.Person.ok"));
     }

}
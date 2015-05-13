package se.recan.utils;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @date 2014-jul-27
 * @author Anders Recksén (recan)
 */
public class IntrospectUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    @Test
    public void testConstructor() throws Exception {
        LOGGER.debug("");
        
        IntrospectUtil is = IntrospectUtil.getInstance();
        
        Assert.assertNotNull(is);
        
        String constructors = is.getConstructors("se.recan.pojo.Person");
        String methods = is.getMethods("se.recan.pojo.Person");
        
        Assert.assertTrue(constructors.contains("Person"));
        Assert.assertTrue(methods.contains("setFirstName"));
        Assert.assertTrue(methods.contains("setUserName"));
    }
}
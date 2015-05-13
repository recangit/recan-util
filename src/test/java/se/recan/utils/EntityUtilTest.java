package se.recan.utils;

import se.recan.pojo.Person;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * 2014-aug-06
 * @author Anders Recksén (recan)
 */
public class EntityUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    @Test
    public void testInitialize() {
        EntityUtil entity = EntityUtil.getInstance();
        Assert.assertEquals("se.recan.utils.EntityUtil", entity.getClass().getName());
        Assert.assertNotNull(entity);
    }
    
    @Test
    public void testEntityUtil() throws SecurityException, Exception {
        List<String> list = new ArrayList<String>();
        list.add("firstName:Anders");
        list.add("age:51");
        list.add("exist:true");
        
        EntityUtil entity = EntityUtil.getInstance();
        Person e = (Person) entity.process(list, "se.recan.pojo.Person");

        Assert.assertEquals("Anders", e.getFirstName());
        Assert.assertEquals(51, e.getAge());
        Assert.assertTrue(e.isExist());
        
        LOGGER.debug(entity.feedback());
        
        LOGGER.debug(entity.getUri());
    }
}

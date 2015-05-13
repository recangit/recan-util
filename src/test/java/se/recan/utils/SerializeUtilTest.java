package se.recan.utils;

import se.recan.pojo.Person;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Klassen att serialisera, i detta fall Entity, måste implementera Serializable.
 * 
 * 2014-aug-11
 * @author Anders Recksén (recan)
 */
public class SerializeUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    private static final List<Object> items = new ArrayList<Object>();

    @Test
    public void testSave() {
        File file = new File("Items.bin");
        Assert.assertTrue(file.exists());
    }

    @Test
    public void testRead() {
        List<Object> relations = SerializeUtil.readFromFile();

        for (Object obj : relations) {
            LOGGER.debug(((Person)obj).getFirstName());
        }

        Assert.assertTrue(relations.size() == 3);
    }

    @BeforeClass
    public static void init() {
        items.add(new Person("Anders"));
        items.add(new Person("Kajsa"));
        items.add(new Person("Annie"));
        
        SerializeUtil.saveToFile(items);
    }

    @AfterClass
    public static void clean() {
        File file = new File("Items.bin");
        if (file.exists()) {
            file.delete();
        }
    }
}

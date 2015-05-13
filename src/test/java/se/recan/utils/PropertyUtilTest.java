package se.recan.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 2014-jul-27
 * @author Anders Recksén (recan)
 */
public class PropertyUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private static final PropertyUtil prop = new PropertyUtil("src/test/resources/config.properties");
    
    @Test
    public void testPropertiesReader() throws Exception {
        Assert.assertThat(prop.getStringProperty("stringprop"), containsString("En vanlig Sträng"));
        Assert.assertThat(prop.getIntProperty("intprop"), is(1234));
        Assert.assertTrue(prop.getBolProperty("booleanprop"));
        
        File file = prop.getFileProperty("fileprop");
        file.createNewFile();
        Assert.assertTrue(file.exists());
        
        file.delete();
        Assert.assertFalse(file.exists());

        try {
            URL url = prop.getUrlProperty("urlprop");
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            Assert.fail("The URL is not in a valid form");
        } catch (IOException e) {
            Assert.fail("The connection couldn't be established");
        }
        
        Assert.assertTrue(prop.getBolProperty("unset", "true"));
        List<String> list = prop.getProperties();
        Assert.assertTrue(list.size() > 0);
    }

    @BeforeClass
    public static void init() throws IOException {

        prop.addProperty("stringprop", "En vanlig Sträng");
        prop.addProperty("intprop", "1234");
        prop.addProperty("booleanprop", "true");
        prop.addProperty("fileprop", "src/test/resources/tmp.properties");
        prop.addProperty("urlprop", "http://recksen.se");
    }
    
    @AfterClass
    public static void tearDown() {
        File file = new File("src/test/resources/config.properties");
        if (file.exists()) {
            file.delete();
        }
    }
}

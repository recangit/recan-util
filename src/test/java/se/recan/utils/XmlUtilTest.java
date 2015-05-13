package se.recan.utils;

import se.recan.pojo.Person;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.containsString;
import org.jdom2.Element;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @date 2014-aug-06
 * @author Anders Recksén (recan)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XmlUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    
    private static final String FILE = "src/test/resources/person.xml";

    /**
     * Denna måste exekveras först
     * @throws Exception 
     */
    @Test
    public void testAddToXml() throws Exception {
        
        XmlUtil util = new XmlUtil(new File(FILE));

        Person p = new Person(2, "Annie", "Recksén", "pannie", "pannie123", "pannie123", "0703287664", 7, 0);

        Element e = p.toXml();
        util.addXmlPost(e);
        
        p = new Person(3, "Kajsa", "Recksén", "karra", "bucken", "bucken", "0403139462", 10, 0);
        util.addXmlPost(p.toXml());

        Assert.assertTrue(util.getNumberOfPosts() > 1);
    }
    
    @Test
    public void testUpdateXml() throws Exception {
        
        XmlUtil util = new XmlUtil(new File(FILE));
        
        util.updateXmlPost("id", "3", "userName", "satanIgatan");
        
        Element result = util.searchXmlPost("id", "3");
        List<Element> elements = result.getChildren();
        Assert.assertEquals("satanIgatan", elements.get(2).getAttributeValue("value"));
    }
    
    @Test
    public void testSearchInXml() throws Exception {
        
        XmlUtil util = new XmlUtil(new File(FILE));
        
        Element result = util.searchXmlPost("id", "2");
        List<Element> elements = result.getChildren();

        Assert.assertEquals("Recksén", elements.get(1).getAttributeValue("value"));
    }

    @Test
    public void testGetXmlAsList() throws Exception {
        
        XmlUtil util = new XmlUtil(new File(FILE));

        List<String> xmlList = util.getXmlAsList();

        List<Person> persons = new ArrayList<>();
        Person person = null;
        for (String s : xmlList) {
            String key = s.substring(0, s.indexOf("="));
            String value = s.substring(s.indexOf("=") + 1);

            switch (key) {
                case "person":
                    person = new Person(Integer.parseInt(value));
                    persons.add(person);
                    break;
                case "firstName":
                    if (person == null) {
                        break;
                    }
                    person.setFirstName(value);
                    break;
                case "lastName":
                    if (person == null) {
                        break;
                    }
                    person.setLastName(value);
                    break;
                case "userName":
                    if (person == null) {
                        break;
                    }
                    person.setUserName(value);
                    break;
                case "password":
                    if (person == null) {
                        break;
                    }
                    person.setPassword(value);
                    break;
                case "password2":
                    if (person == null) {
                        break;
                    }
                    person.setPassword2(value);
                    break;
                case "socialNumb":
                    if (person == null) {
                        break;
                    }
                    person.setSocialNumb(value);
                    break;
                case "age":
                    if (person == null) {
                        break;
                    }
                    int age = Integer.parseInt(value);
                    person.setAge(age);
                    break;
                case "gender":
                    if (person == null) {
                        break;
                    }
                    int gender = Integer.parseInt(value);
                    person.setGender(gender);
                    break;
            }
        }

        Assert.assertTrue(persons.size() >= 1);
        
//        for (Person p : persons) {
//            LOGGER.debug(p.toString());
//        }
    }

    @Test
    public void testGetXmlAsString() throws Exception {
        XmlUtil util = new XmlUtil(new File(FILE));
        
        String s = util.getXmlAsString();
        
        Assert.assertThat("Dessa taggar finns", s, containsString("<persons>"));
        Assert.assertThat("Dessa taggar finns", s, containsString("<person id="));
        Assert.assertThat("Dessa taggar finns", s, containsString("<firstName value="));
    }

    @Test
    public void testDeleteFromXml() throws Exception {
        XmlUtil util = new XmlUtil(new File(FILE));
        util.deleteXmlPost("id", "1");
        
        Assert.assertTrue(util.getNumberOfPosts() < 3);
//        String result = util.getXmlAsString();
//        LOGGER.debug(result);
    }
    
    @BeforeClass
    public static void init() throws Exception {
        XmlUtil util = new XmlUtil(new File(FILE));
        Person p = new Person(1, "Anders", "Recksén", "recan", "malla", "malla", "6210024318", 51, 1);

        Element e = p.toXml();
        
        util.createXmlFile(e, "persons");
        
        Assert.assertEquals(1, util.getNumberOfPosts());
    }
    
    @AfterClass
    public static void clean() throws Exception {
        XmlUtil util = new XmlUtil(new File(FILE));

        boolean deleted = util.deleteXmlFile();
        
        Assert.assertTrue(deleted);
    }
}

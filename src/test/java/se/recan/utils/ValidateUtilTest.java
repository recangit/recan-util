package se.recan.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @date 2014-aug-05
 * @author Anders Recksén (recan)
 */
public class ValidateUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

     @Test
     public void testValidateUtil() {
         Assert.assertTrue("Sträng måste innehålla minst 6 tecken", ValidateUtil.validate("Kidsen", 6));
         Assert.assertFalse("Sträng måste innehålla minst 6 tecken", ValidateUtil.validate("Kidsen", 7));
         
         Assert.assertTrue("Sträng får inte vara tom", ValidateUtil.validate("Kidsen"));
         Assert.assertFalse("Sträng får inte vara tom", ValidateUtil.validate(""));
         
         Assert.assertTrue("Sträng måste innehålla angett tecken", ValidateUtil.validate("Kajsa", "a"));
         Assert.assertFalse("Sträng måste innehålla angett tecken", ValidateUtil.validate("Annie", "a"));
         
         Assert.assertTrue("Mailadress", ValidateUtil.validateMail("anders@recksen.se"));
         Assert.assertTrue("Mailadress", ValidateUtil.validateMail("anders@recksen.bla.se"));
         Assert.assertFalse("Mailadress", ValidateUtil.validateMail("anders@recksen.bla"));
         Assert.assertFalse("Mailadress", ValidateUtil.validateMail("anders@recksense"));
         Assert.assertFalse("Mailadress", ValidateUtil.validateMail("@recksen.se"));
         Assert.assertFalse("Mailadress", ValidateUtil.validateMail("anders.recksen.se"));
         
         
         Assert.assertTrue("Personnummer", ValidateUtil.validateSocialSecurityNumber("1962-10-02-43-18"));
         Assert.assertTrue("Personnummer", ValidateUtil.validateSocialSecurityNumber("1962:10:02 43:18"));
         Assert.assertTrue("Personnummer", ValidateUtil.validateSocialSecurityNumber("6210024318"));
         Assert.assertFalse("Personnummer", ValidateUtil.validateSocialSecurityNumber("6210024319"));
         Assert.assertFalse("Personnummer", ValidateUtil.validateSocialSecurityNumber("621002431"));
         
         Assert.assertTrue("Man", ValidateUtil.getGender("6210024318")==ValidateUtil.MALE);
         Assert.assertTrue("Kvinna", ValidateUtil.getGender("6210024328")==ValidateUtil.FEMALE);
     }
     
     @Test
    public void testBinary() {
        ValidateUtil util = new ValidateUtil();

        util.setVeryLowRisk(true);
        util.setMediumRisk(true);
        util.setRanteFonder(true);
        int filter = util.getFilter();
        Assert.assertTrue(util.validate(filter));
    }
}
package se.recan.utils;

import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @date 2014-aug-06
 * @author Anders Recksén (recan)
 */
public class LoremIpsumUtilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    @Test
    public void testLoremIpsum() {
        String[] actual = LoremIpsumUtil.randomCorpusLines(5);
        String[] expected = LoremIpsumUtil.randomCorpusLines(5);
        Assert.assertNotEquals("Två random genererade arrayer skall ej vara lika", actual, expected);

        String string = LoremIpsumUtil.randomCorpus(1);
        // Denna spricker ibland
//        LOGGER.debug(string);
        Assert.assertThat("En random genererad mening skall avslutas med punkt", string, containsString("."));
        
        string = LoremIpsumUtil.randomCorpusWords(2);
        Assert.assertThat("Två random genererade ord skall innehålla mellanslag", string, containsString(" "));
    }

}

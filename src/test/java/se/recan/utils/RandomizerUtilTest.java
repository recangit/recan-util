package se.recan.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;

/**
 * @date 2014-jul-27
 * @author Anders Recksén (recan)
 */
public class RandomizerUtilTest {

    private static final Logger LOGGER = Logger.getLogger("RandomizerUtilTest");

    @Test
    public void testRandomize() {
        RandomizerUtil instance = new RandomizerUtil(3);

        for (int i = 0; i < 10; i++) {
            int result = instance.randomize();
            Assert.assertTrue(result < 3);
        }
    }

    @Test
    public void testRandomize_int_int() {
        int start = 10;
        int stop = 3;

        for (int i = 0; i < 10; i++) {
            int result = RandomizerUtil.randomize(start, stop);
            Assert.assertTrue(result > 9);
            Assert.assertTrue(result < 13);
        }
    }
}
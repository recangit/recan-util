package se.recan.utils;

import java.util.LinkedList;

/**
 * Created: 2010-12-31 Last Modified: 2010-12-31
 *
 * @author Anders Recksén (anders[at]recksen[dot]se)
 */
public class RandomizerUtil {

    private final LinkedList<Integer> LIST = new LinkedList<Integer>();
    private final int POSSIBILITIES;

    public RandomizerUtil(int POSSIBILITIES) {
        this.POSSIBILITIES = POSSIBILITIES;
        populatePossibilities();
    }

    private void populatePossibilities() {
        for (int i = 0; i < POSSIBILITIES; i++) {
            LIST.add(i);
        }
    }

    // I detta utförande returneras 0 till värde POSSIBILITIES
    public int randomize() {
        if (LIST.size() == 0) {
            populatePossibilities();
        }

        int position = 0 + (int) (LIST.size() * Math.random());
        int result = LIST.get(position);
        LIST.remove(position);
        
        return result;
    }

    // Lowest int to return, number of possible values.
    public static int randomize(int start, int stop) {
        return start + (int) (stop * Math.random());
    }
}

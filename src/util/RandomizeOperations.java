package util;

import java.util.Random;

/**
 * Created by dybisz on 20/10/2015.
 */
public abstract class RandomizeOperations {
    protected static Random random = new Random();

    /**
     * http://stackoverflow.com/questions/9723765/generating-a-random-double-number-of-a-certain-range-in-java
     */
    public static double randomInRange(double min, double max) {
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }
}

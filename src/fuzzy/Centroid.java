package fuzzy;

import util.RandomizeOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dybisz on 20/10/2015.
 */
public class Centroid {
    private final double EPSILON = 0.00000000001;
    List<Double> center = new ArrayList<>();

    public Centroid(double... values) {
        checkForNull(values);
        assignValues(values);
    }

    private void checkForNull(double[] values) {
        if (values == null) {
            throw new NullPointerException("Centroid, values == null");
        }
    }

    private void assignValues(double[] values) {
        for (int i = 0; i < values.length; i++) {
            center.add(values[i]);
        }
    }

    @Override
    public String toString() {
        String foo = "Centroid: ";
        for (Double val : center) {
            foo += val + " ";
        }
        return foo;
    }

    @Override
    public boolean equals(Object obj) {
        Centroid other = (Centroid) obj;
        for (int i = 0; i < center.size(); i++) {
            if (Math.abs((center.get(i) - other.getDim(i))) > EPSILON)
                return false;
        }

        return true;
    }

    /**
     * We assume that centroid can be represented in many dimension, hence
     * this method returns its value in {@code dimNumber} dimension.
     * E.g. in case of 3D, 0 corresponds to x, 1 to y and, 2 to z.
     *
     * @param dimNumber Number of dimension, from which value we want to gather.
     * @return Centroid value in certain dimension.
     */
    public double getDim(int dimNumber) {
        return center.get(dimNumber);
    }

    public double getNumberOfDim() {
        return center.size();
    }

}

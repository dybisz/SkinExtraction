package fuzzy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dybisz on 22/10/2015.
 */
public class Features {
    private List<Double> features = new ArrayList<>();

    public Features() {};

    public Features(Double ... values) {
        assignNewFeatures(values);
    }

    public void addFeature(Double value) {
        features.add(value);
    }

    //TODO(dybisz) exception or if();
    public double getDim(int dimension) {
        return features.get(dimension);
    }

    public int getNumberOfDim() {
        return features.size();
    }

    private void assignNewFeatures(Double[] values) {
        for(int i = 0; i < values.length; i++) {
            features.add(values[i]);
        }
    }


}

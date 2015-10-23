package fuzzy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dybisz on 20/10/2015.
 */
public class DistanceMatrix {
    private List<List<Double>> matrix = new ArrayList<>();

    /**
     * User can fill out matrix column by column.
     * @param column New column to add to the matrix.
     */
    public void addColumn(List<Double> column) {
        matrix.add(column);
    }

    @Override
    public String toString() {
        String retString = "";
        for(int row = 0; row < matrix.get(0).size(); row++) {
            String rowStr = "";
            for(int col = 0; col < matrix.size(); col++) {
                rowStr += matrix.get(col).get(row) + " ";
            }
            System.out.println(rowStr);
        }

        return "^ PRINTED: Distance Matrix: " + super.toString();
    }
}

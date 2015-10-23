package fuzzy;

import java.util.Calendar;
import java.util.Random;

/**
 * // TODO(dybisz) describe dunction
 * Created by dybisz on 22/10/2015.
 */
public class MembershipMatrix {
    private double[][] matrix;
    private int numberOfPoints;
    private double numberOfClusters;

    /**
     * Randomizes {@link #matrix} entries and preserves rule: all memberships
     * of a point must sum up to 1.
     */
    public MembershipMatrix(int numberOfPoints, int numberOfClusters) {
        this.numberOfPoints = numberOfPoints;
        this.numberOfClusters = numberOfClusters;
        this.matrix = new double[numberOfPoints][numberOfClusters];
        fillOutMatrixInRandomWay();
    }

    private void fillOutMatrixInRandomWay() {
        Random rand = new Random(Calendar.getInstance().getTimeInMillis());
        for (int i = 0; i < numberOfPoints; i++) {
            double rowSum = 0;
            for (int j = 0; j < numberOfClusters; j++) {
                double randomMembership = rand.nextDouble();
                if (randomMembership + rowSum >= 1) {
                    matrix[i][j] = 1 - rowSum;
                    break;
                } else {
                    matrix[i][j] = randomMembership;
                    rowSum += randomMembership;
                }
            }
        }
    }

    public double get(int point, int cluster) {
        return matrix[point][cluster];
    }

    public void set(int point, int cluster, double value) {
        matrix[point][cluster] = value;
    }

    @Override
    public String toString() {
        String foo = "";
        if(matrix != null) {
            printMatrix();
        }

        return foo;
    }

    private void printMatrix() {
        for(int p = 0; p < numberOfPoints; p++) {
            for(int c = 0; c < numberOfClusters; c++) {
                System.out.print(matrix[p][c] + " ");
            }
            System.out.print("\n");
        }
    }
}

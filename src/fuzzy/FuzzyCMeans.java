package fuzzy;

import javafx.beans.property.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class encapsulates whole process of fuzzy numberOfClusters-mean clustering.
 * <p></p>
 * Created by dybisz on 22/10/2015.
 */
public class FuzzyCMeans {
    /**
     * For user convenience, there is option to bind to this property
     * and follow clustering process. It can be e.g. conencted to some label
     * in GUI.
     */
    private StringProperty info = new SimpleStringProperty("");
    private List<Features> points;
    private MembershipMatrix membershipMatrix;
    private int numberOfClusters;
    private int numberOfPoints;
    private double fuzziness;
    private int pointsDimensions;

    public FuzzyCMeans(List<Features> points, int numberOfCentroids, double fuzziness) throws Exception {
        checkForNull(points);
        this.points = points;
        this.numberOfClusters = numberOfCentroids;
        this.numberOfPoints = points.size();
        this.fuzziness = fuzziness;
        this.pointsDimensions = extractPointsDimensions();
    }

    private void checkForNull(Object... objects) throws Exception {
        if (objects == null) {
            throw new NullPointerException("Fuzzy C-Means: some element is, and should not be, == null.");
        }
    }

    private int extractPointsDimensions() {
        return points.get(0).getNumberOfDim();
    }

    // TODO(dybisz) change void to some list
    // TODO(dybisz) clean it UP
    public FuzzyCMeansResults calculate() throws FileNotFoundException, UnsupportedEncodingException {
        membershipMatrix = new MembershipMatrix(numberOfPoints, numberOfClusters);
        List<Centroid> newCentroids = calculateNewCentroids();
        List<Centroid> oldCentroids;
        double exponent = 2.0 / (fuzziness - 1.0);

        for (int loop = 0; ; loop++) {
            oldCentroids = newCentroids;
            setInfo("[Fuzzy_C_Means] Loop " + loop);
            for (int p = 0; p < numberOfPoints; p++) {
                setInfo("[Fuzzy_C_Means] Loop " + loop + " | point " + p );
                for (int c = 0; c < numberOfClusters; c++) {
                    setInfo("[Fuzzy_C_Means] Loop " + loop + " | point " + p + " | cluster " + c);
                    double denominator = 0;
                    double TEMP_denominator = 0;
                    double point_cluster_dist = euclideanDistance(points.get(p), newCentroids.get(c));
                    double TEMP_nominator = Math.pow((1.0 / point_cluster_dist), exponent);

                    for (int r = 0; r < numberOfClusters; r++) {
                        double summary_point_cluster_dist = euclideanDistance(points.get(p), newCentroids.get(r));
                        double ratio = Math.pow(point_cluster_dist / summary_point_cluster_dist, exponent);
                        denominator += ratio;
                        TEMP_denominator += Math.pow((1.0 / summary_point_cluster_dist), exponent);
                    }

//                    membershipMatrix.set(p, c, 1.0 / denominator);
                    membershipMatrix.set(p, c, TEMP_nominator / TEMP_denominator);
                }
            }
            newCentroids = calculateNewCentroids();
            if (newCentroids.equals(oldCentroids)) {
                break;
            }

        }
        System.out.println(newCentroids);
        setInfo("Centroids found.");
        return new FuzzyCMeansResults(newCentroids, membershipMatrix);
    }

    private double euclideanDistance(Features point, Centroid centroid) {
        if (point.getNumberOfDim() != centroid.getNumberOfDim())
            throw new IllegalArgumentException();

        double sumOfSquares = 0;
        for (int i = 0; i < point.getNumberOfDim(); i++) {
            sumOfSquares += (point.getDim(i) - centroid.getDim(i)) *
                    (point.getDim(i) - centroid.getDim(i));
        }

        return Math.sqrt(sumOfSquares);
    }

    private List<Centroid> calculateNewCentroids() {
        List<Centroid> newCentroids = new ArrayList<>();
        for (int c = 0; c < numberOfClusters; c++) {
            Centroid centroid = calculateCentroidOfIndex(c);
            newCentroids.add(centroid);
        }
        return newCentroids;
    }

    private Centroid calculateCentroidOfIndex(int index) {
        double[] values = new double[pointsDimensions];
        double nominator, denominator;

        for (int dim = 0; dim < pointsDimensions; dim++) {
            nominator = 0;
            denominator = 0;

            for (int i = 0; i < numberOfPoints; i++) {
                nominator += Math.pow(membershipMatrix.get(i, index), fuzziness) * points.get(i).getDim(dim);
                denominator += Math.pow(membershipMatrix.get(i, index), fuzziness);
            }

            values[dim] = nominator / denominator;
        }

        return new Centroid(values);
    }

    public String getInfo() {
        return info.get();
    }

    public StringProperty infoProperty() {
        return info;
    }

    public void setInfo(String info) {
        this.info.set(info);
    }
}

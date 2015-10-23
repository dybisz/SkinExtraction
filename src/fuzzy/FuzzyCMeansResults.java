package fuzzy;

import java.util.List;

/**
 * Created by dybisz on 22/10/2015.
 */
public class FuzzyCMeansResults {
    public List<Centroid> centroids;
    public MembershipMatrix membershipMatrix;
    public FuzzyCMeansResults(List<Centroid> centroids, MembershipMatrix membershipMatrix) {
        this.centroids = centroids;
        this.membershipMatrix = membershipMatrix;
    }
}

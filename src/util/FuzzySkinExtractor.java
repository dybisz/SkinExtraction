package util;

import controllers.MainWindow;
import fuzzy.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dybisz on 20/10/2015.
 */
public class FuzzySkinExtractor extends Task {
    private StringProperty messagesPipeline = new SimpleStringProperty("");
    private static final int NUMBER_OF_CLUSTERS = 2;
    private static final double FUZZINESS = 1.25;
    private ImageView imageView;
    private List<Pixel> pixels;
    private List<Features> points;
    private FuzzyCMeans fuzzyCMeans;
    private int width;
    private int height;

    public FuzzySkinExtractor(ImageView imageView, List<Pixel> pixels, int width, int height) throws Exception {
        this.imageView = imageView;
        this.pixels = pixels;
        this.points = generateFeaturesFromPixels();
        this.fuzzyCMeans = new FuzzyCMeans(points, NUMBER_OF_CLUSTERS, FUZZINESS);
        this.messagesPipeline = generateMessagesPipeline();
        this.width = width;
        this.height = height;
    }

    private StringProperty generateMessagesPipeline() {
        StringProperty stringProperty = new SimpleStringProperty("");
        stringProperty.bind(fuzzyCMeans.infoProperty());
        stringProperty.addListener((a,b,c) -> {
            updateMessage(c);
        });
        return stringProperty;
    }

    @Override
    protected List<Pixel> call() throws Exception {
        FuzzyCMeansResults clusteringResults = fuzzyCMeans.calculate();
        List<Pixel> processedPixels = applyClusteringToPixels(clusteringResults);
        updateImageView(processedPixels);
        updateProgress(0, 0);
        MainWindow.duringSkinExtraction.setValue(false);
        MainWindow.skinExtracted.setValue(true);
        return processedPixels;
    }

    private void updateImageView(List<Pixel> processedPixels) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pw = image.getPixelWriter();

        for(Pixel pixel : processedPixels) {
            int x = pixel.getX();
            int y = pixel.getY();
            double r = pixel.getR() / 255.0;
            double g = pixel.getG() / 255.0;
            double b = pixel.getB() / 255.0;
            Color color = new Color(r, g, b, 1.0);
            pw.setColor(x , y, color);
        }

        imageView.setImage(image);
    }

    private List<Pixel> applyClusteringToPixels(FuzzyCMeansResults clusteringResults) {
        List<Pixel> outputPixels = new ArrayList<>();
        MembershipMatrix membershipMatrix = clusteringResults.membershipMatrix;
        int numberOfPoints = points.size();

        for(int p = 0; p < numberOfPoints; p++) {
            double memCentroid1 = membershipMatrix.get(p, 0);
            double memCentroid2 = membershipMatrix.get(p, 1);
            int color = (memCentroid1 > memCentroid2) ? 255 : 0;
            int x = pixels.get(p).getX();
            int y = pixels.get(p).getY();
            outputPixels.add(new Pixel(color, color, color, x, y));
        }

        return outputPixels;
    }

    private List<Features> generateFeaturesFromPixels() {
        List<Features> returnPoints = new ArrayList<>();

        for (int p = 0; p < pixels.size(); p++) {
            double Cr = pixels.get(p).getCr();
            double Cg = pixels.get(p).getCg();
            double Cb = pixels.get(p).getCb();
            returnPoints.add(new Features(Cr, Cg, Cb));
        }

        return returnPoints;
    }
}

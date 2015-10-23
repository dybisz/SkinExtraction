package controllers;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.*;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

/**
 * Contains all elements of the application and
 * oversees information flow between them.
 * <p></p>
 * Created by dybisz on 11/10/2015.
 */
public class MainWindow extends AnchorPane {

    private final static String DEFAULT_IMAGE_URL = "default.jpg";
    /**
     * Pixel is 1D list because during extraction process we save each pixel's
     * coordinate on image, hence we can recover image later and process
     * originPixels as set of points.
     */
    List<Pixel> originPixels = null;
    List<Pixel> outputPixels = null;

    FadeTransition dragAndDropTextAnimation;
    FadeTransition dragAndDropBorderAnimation;

    public static BooleanProperty imageLoaded = new SimpleBooleanProperty(false);
    public static BooleanProperty pixelsExtracted = new SimpleBooleanProperty(false);
    public static BooleanProperty skinExtracted = new SimpleBooleanProperty(false);
    public static BooleanProperty duringPixelsExtraction = new SimpleBooleanProperty(false);
    public static BooleanProperty duringSkinExtraction = new SimpleBooleanProperty(false);

    Thread imageLoaderT;
    Thread pixelExtractorT;
    Thread skinExtractorT;

    @FXML
    ScrollPane originalImagePane;

    @FXML
    ScrollPane resultImagePane;

    @FXML
    Button startPixelExtraction;

    @FXML
    Button savePicture;

    @FXML
    Button startSkinExtraction;

    @FXML
    ProgressBar progress;

    @FXML
    Label infoStrip;

    @FXML
    Label dragInfo;

    @FXML
    Pane dragPane;

    public MainWindow() {
        FxmlLoader.loadFxml(this, "/fxml/main_window2.fxml");
        originalImagePane.setStyle("-fx-background-color:transparent;");
        resultImagePane.setStyle("-fx-background-color:transparent;");
        dragPane.setStyle("-fx-background-color:white;");

        createDragAndDropAnimation();
        dragAndDropTextAnimation.play();
        dragAndDropBorderAnimation.play();

        /* Disable button if either image is not loaded or it is currently extracting originPixels */
        startPixelExtraction.disableProperty().bind(Bindings.or(imageLoaded.not(), duringPixelsExtraction));
        startSkinExtraction.disableProperty().bind(Bindings.or(pixelsExtracted.not(), duringPixelsExtraction));
        savePicture.disableProperty().bind(skinExtracted.not());
    }

    private void createDragAndDropAnimation() {
        dragAndDropTextAnimation = new FadeTransition(Duration.millis(1200), dragInfo);
        dragAndDropTextAnimation.setFromValue(0.2);
        dragAndDropTextAnimation.setToValue(0.7);
        dragAndDropTextAnimation.setCycleCount(Animation.INDEFINITE);
        dragAndDropTextAnimation.setAutoReverse(true);

        dragAndDropBorderAnimation = new FadeTransition(Duration.millis(1200), dragPane);
        dragAndDropBorderAnimation.setFromValue(0.2);
        dragAndDropBorderAnimation.setToValue(0.7);
        dragAndDropBorderAnimation.setCycleCount(Animation.INDEFINITE);
        dragAndDropBorderAnimation.setAutoReverse(true);
    }

    private void loadPicture(String url) {
        ImageView imageView = prepareNewImageView(originalImagePane);
        ImageLoader imageLoader = new ImageLoader(url, imageView);
        bindTaskToProgressBar(imageLoader);
        imageLoaderT = new Thread(imageLoader);
        imageLoaderT.start();
        MainWindow.imageLoaded.setValue(true);
    }

    private void bindTaskToProgressBar(Task task) {
        progress.progressProperty().bind(task.progressProperty());
    }

    private ImageView prepareNewImageView(ScrollPane parentPane) {
        ImageView imageView = new ImageView();
        parentPane.setContent(imageView);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(parentPane.widthProperty());
        imageView.fitHeightProperty().bind(parentPane.heightProperty());
        return imageView;
    }

    @FXML
    protected void extractPixels() {
        stopSkinExtractionTask();
        duringPixelsExtraction.setValue(true);
        PixelsExtractor pixelsExtractor = preparePixelExtractor();
        bindTaskToProgressBar(pixelsExtractor);
        infoStrip.textProperty().bind(pixelsExtractor.messageProperty());
        startPixelExtractorTask(pixelsExtractor);
    }

    private void stopSkinExtractionTask() {
        if(skinExtractorT != null) {
            skinExtractorT.interrupt();
        }
    }

    private void startPixelExtractorTask(PixelsExtractor pixelsExtractor) {
        pixelExtractorT = new Thread(pixelsExtractor);
        pixelExtractorT.start();
    }

    private PixelsExtractor preparePixelExtractor() {
        PixelsExtractor pixelsExtractor = new PixelsExtractor(((ImageView) originalImagePane.getContent()).getImage());
        setOnSucceedBehaviour(pixelsExtractor);
        return pixelsExtractor;
    }

    private void setOnSucceedBehaviour(PixelsExtractor pixelsExtractor) {
        pixelsExtractor.setOnSucceeded((t) -> {
            originPixels = (List<Pixel>) pixelsExtractor.getValue();
        });
    }

    @FXML
    protected void extractSkin() {
        if (originPixels != null) {
            try {
                duringSkinExtraction.setValue(true);
                startSkinExtraction();
            } catch (Exception e) {
                duringSkinExtraction.setValue(false);
                e.printStackTrace();
            }
        }
    }

    private void startSkinExtraction() throws Exception {
        ImageView imageView = prepareNewImageView(resultImagePane);
        FuzzySkinExtractor fuzzySkinExtractor = prepareFuzzySkinExtractor(imageView);
        infoStrip.textProperty().bind(fuzzySkinExtractor.messageProperty());
        bindTaskToProgressBar(fuzzySkinExtractor);
        new Thread(fuzzySkinExtractor).start();
    }

    private FuzzySkinExtractor prepareFuzzySkinExtractor(ImageView imageView) throws Exception {
        int width = getOriginalImageWidth();
        int height = getOriginalImageHeight();
        FuzzySkinExtractor fuzzySkinExtractor = new FuzzySkinExtractor(imageView, originPixels, width, height);
        fuzzySkinExtractor.setOnSucceeded((t) -> {
            outputPixels = (List<Pixel>) fuzzySkinExtractor.getValue();
        });
        return fuzzySkinExtractor;
    }

    private int getOriginalImageWidth() {
        return (int)((ImageView) originalImagePane.getContent()).getImage().getWidth();
    }

    private int getOriginalImageHeight() {
        return (int)((ImageView) originalImagePane.getContent()).getImage().getHeight();
    }

    @FXML
    protected void savePicture() {
        Image imageToSave = ((ImageView) resultImagePane.getContent()).getImage();
        ImageDialogSaver imageDialogSaver = new ImageDialogSaver(imageToSave);
        imageDialogSaver.openSaveDialog();
    }

    @FXML
    protected void onDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() && isValidImageFile(db.getUrl())
                && !duringPixelsExtraction.getValue()
                && !duringSkinExtraction.getValue())
            event.acceptTransferModes(TransferMode.LINK);
        else
            event.consume();
    }

    @FXML
    protected void onDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            db.getFiles().stream().forEach(file -> {
                try {
                    loadPicture(file.toURI().toURL().toString());
                    resetData();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void resetData() {
        resultImagePane.setContent(null);
        pixelsExtracted.setValue(false);
        skinExtracted.setValue(false);
        dragAndDropTextAnimation.stop();
        dragAndDropBorderAnimation.stop();
        dragInfo.setVisible(false);
        dragPane.setVisible(false);
        originPixels = null;
        outputPixels = null;
    }

    private boolean isValidImageFile(String url) {
        List<String> imgTypes = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
        return imgTypes.stream().anyMatch(t -> url.endsWith(t));
    }


}

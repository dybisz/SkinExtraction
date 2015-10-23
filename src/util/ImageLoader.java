package util;

import controllers.MainWindow;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract class for handling image loading to provided ImageView
 * object. It runs loading procedure in JavaFX thread.
 * <p></p>
 * Created by dybisz on 11/10/2015.
 */
public class ImageLoader extends Task {
    String url;
    ImageView imageView;

    public ImageLoader(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Object call() throws Exception {
        Image image = new Image(url, false);
        imageView.setImage(image);
        MainWindow.imageLoaded.setValue(true);
        updateProgress(0,0);
        return null;
    }
}

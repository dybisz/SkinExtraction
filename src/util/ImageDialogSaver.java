package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by dybisz on 22/10/2015.
 */
public class ImageDialogSaver {
    private Image image;
    private Stage stage;

    public ImageDialogSaver(Image image) {
        this.stage = new Stage();
        this.image = image;
    }

    public void openSaveDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,
                        null), "png", file);
            } catch (IOException ex) {
                // TODO(dybisz) connect with infoStrip
            }
        }
    }
}

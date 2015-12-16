package util;

import controllers.MainWindow;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * There is no need to freeze the application during pixel extraction
 * process, hence this task will be done in a background.
 * <p></p>
 * Class encapsulates all required methods.
 * <p></p>
 * Created by dybisz on 13/10/2015.
 */
public class PixelsExtractor extends Task {
    Image image;
    ClockMeasure clock = new ClockMeasure();
    public PixelsExtractor(Image image) {
        this.image = image;
    }

    //TODO
    @Override
    protected List<Pixel> call() throws Exception {
        updateProgress(0, 0);
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        List<Pixel> pixels = new ArrayList<>();
        clock.clockStart();
        for (int readY = 0; readY < height; readY++) {
            for (int readX = 0; readX < width; readX++) {
                updateMessage("[Extracting_Pixels] reading coordinates: (" + readX + " , " + readY + ")");
                Color color = pixelReader.getColor(readX, readY);
                int red = (int) (255 * color.getRed());
                int green = (int) (255 * color.getGreen());
                int blue = (int) (255 * color.getBlue());
                updateMessage("[Extracting_Pixels] saving color: (" + red + " , " + green + " , " + blue+ ")");
                pixels.add(new Pixel(red, green, blue, readX, readY));
                updateProgress(readY * width + readX, width * height);
            }
        }
        double time = clock.clockEnd();
        updateProgress(0, 0);
        updateMessage("Pixels extracted in " + time + " seconds.");
        MainWindow.duringPixelsExtraction.setValue(false);
        MainWindow.pixelsExtracted.setValue(true);
        return pixels;
    }
}

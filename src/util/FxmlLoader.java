package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Simple abstract class to wrap method for loading fxml file of
 * specific items.
 * <p></p>
 * Created by dybisz on 2015-04-27.
 */
public abstract class FxmlLoader {
    /**
     * Loads fxml and sets {@param node} as root.
     *
     * @param node    Node to become a root of .fxml.
     * @param fxmlUrl String with url pointing to .fxml file.
     */
    public static void loadFxml(Node node, String fxmlUrl) {
        /* Load fxml file */
        FXMLLoader fxmlLoader = new FXMLLoader(node.getClass().getResource(
                fxmlUrl));
        fxmlLoader.setRoot(node);
        fxmlLoader.setController(node);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

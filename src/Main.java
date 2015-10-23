import controllers.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private MainWindow root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Data Mining: Fuzzy Skin Extractor");
        primaryStage.getIcons().add(new Image("hand_icon.png"));
        root = new MainWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/main_menu.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package student_tracker;

import java.io.IOException;

//JavaFX
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

//Stage
import javafx.stage.Stage;

//Scene
import javafx.scene.Scene;
import javafx.scene.Parent;

//Layout
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//Elements
import javafx.scene.control.*;

public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage pStage) throws Exception  {
        stage = pStage;
        Parent root = loadFXML("profile.fxml");
        Scene scene = new Scene(root, 1024, 568);
        scene.getStylesheets().add(
            getClass().getResource("/css/profile.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource("/fxml/" + fxml)).load();
    }

    public static void main(String[] args) {
        launch();
    }
}
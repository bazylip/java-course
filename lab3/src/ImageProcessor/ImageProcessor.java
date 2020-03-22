/* Bazyli Polednia
 * Laboratorium 3
 * Platformy Technologiczne
 * 2020
 */

package ImageProcessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ImageProcessor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent fxmlConfig = FXMLLoader.load(getClass().getResource("UI.fxml"));
        primaryStage.setTitle("Image Processor");
        primaryStage.setScene(new Scene(fxmlConfig, 1000, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

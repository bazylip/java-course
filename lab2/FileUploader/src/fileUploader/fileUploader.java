/* Bazyli Polednia
 * Laboratorium 2
 * Platformy Technologiczne
 * 2020
 */

package fileUploader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class fileUploader extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent fxmlConfig = FXMLLoader.load(getClass().getResource("UI.fxml"));
        Scene scene = new Scene(fxmlConfig);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        //System.out.println("Main test");
    }
}

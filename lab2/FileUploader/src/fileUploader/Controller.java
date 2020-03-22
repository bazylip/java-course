/* Bazyli Polednia
 * Laboratorium 2
 * Platformy Technologiczne
 * 2020
 */

package fileUploader;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressBar;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Run sendFileTask after the file has been chosen
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void buttonClick(ActionEvent event) throws Exception {
        File uploadedFile = new FileChooser().showOpenDialog(null);
        if(uploadedFile == null){
            throw new Exception("No file provided");
        }

        Task<Void> sendFileTask = new SendFileTask(uploadedFile); //klasa zadania
        statusLabel.textProperty().bind(sendFileTask.messageProperty());
        progressBar.progressProperty().bind(sendFileTask.progressProperty());
        executor.submit(sendFileTask); //uruchomienie zadania w tle

    }
}

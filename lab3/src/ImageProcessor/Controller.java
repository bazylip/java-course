/* Bazyli Polednia
 * Laboratorium 3
 * Platformy Technologiczne
 * 2020
 */

package ImageProcessor;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable{
    //pola klasy kontrolera:
    @FXML private TableColumn<ImageProcessingJob, String> imageName;
    @FXML private TableColumn<ImageProcessingJob, Double> imageProgress;
    @FXML private TableColumn<ImageProcessingJob, String> imageStatus;
    @FXML private TableView tableView;
    @FXML private Button button;
    @FXML private RadioButton sequential;
    @FXML private RadioButton concurrentDefault;
    @FXML private RadioButton concurrentCustom;
    @FXML private TextField threadsNumber;
    @FXML private Label timeValue;
    private SimpleStringProperty timeValueProperty = new SimpleStringProperty("-");
    private ObservableList<ImageProcessingJob> imageProcessingJobsList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageName.setCellValueFactory( //nazwa pliku
                p -> new SimpleStringProperty(p.getValue().getFile().getName()));
        imageStatus.setCellValueFactory( //imageStatus przetwarzania
                p -> p.getValue().messageProperty());
        imageProgress.setCellFactory( //wykorzystanie paska postępu
                ProgressBarTableCell.<ImageProcessingJob>forTableColumn());
        imageProgress.setCellValueFactory( //postęp przetwarzania
                p -> p.getValue().progressProperty().asObject());

        this.timeValue.textProperty().bind(timeValueProperty);
        this.imageProcessingJobsList = FXCollections.observableList(new ArrayList<>());
        tableView.setItems(this.imageProcessingJobsList);
    }


    @FXML
    private void buttonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if(selectedFiles != null) {
            for (File file : selectedFiles) {
                this.imageProcessingJobsList.add(new ImageProcessingJob(file));
            }
            tableView.setItems(this.imageProcessingJobsList);
            this.triggerButton(true);
            new Thread(this::backgroundJob).start();
        }
    }

    //metoda uruchamiana w tle (w tej samej klasie)
    private void backgroundJob(){
        Platform.runLater(() -> timeValueProperty.setValue("-"));
        long start = System.currentTimeMillis(); //zwraca aktualny czas [ms]
        if(this.sequential.isSelected()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            for (ImageProcessingJob job : imageProcessingJobsList) {
                executor.submit(job);
            }
            awaitTerminationAfterShutdown(executor);
        }else if(this.concurrentDefault.isSelected()){
            ForkJoinPool pool = new ForkJoinPool(4); //pożądana liczba wątków
            for (ImageProcessingJob job : imageProcessingJobsList) {
                pool.submit(job);
            }
            awaitTerminationAfterShutdown(pool);
        }else if(this.concurrentCustom.isSelected()){
            System.out.println("Custom, liczba wątków: " + Integer.parseInt(this.threadsNumber.getText()));
            ForkJoinPool pool = new ForkJoinPool(Integer.parseInt(this.threadsNumber.getText())); //pożądana liczba wątków
            for (ImageProcessingJob job : imageProcessingJobsList) {
                pool.submit(job);
            }
            awaitTerminationAfterShutdown(pool);
        }
        long end = System.currentTimeMillis(); //czas po zakończeniu operacji [ms]
        long duration = end-start; //czas przetwarzania [ms]
        Platform.runLater(() -> timeValueProperty.setValue(Long.toString(duration) + "ms"));

        triggerButton(false);
    }

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void triggerButton(boolean status){
        this.button.setDisable(status);
    }
}

/* Bazyli Polednia
 * Laboratorium 3
 * Platformy Technologiczne
 * 2020
 */

package ImageProcessor;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingJob extends Task<Void> {
    public File sourceFile;
    public File destinationFile;
    public DoubleProperty progress;

    public ImageProcessingJob(File file) {
        this.sourceFile = file;
        File directory = new File("generatedImages");
        if(!directory.exists()) { directory.mkdir(); }
        this.destinationFile = new File(directory.getPath() + "/" + this.sourceFile.getName());
        updateMessage("Waiting...");
        updateProgress(0.0, 0.0);
    }

    @Override
    protected Void call(){
        convertToGrayscale(this.sourceFile, this.destinationFile, this.progress);
        return null;
    }

    private void convertToGrayscale(
            File originalFile, //oryginalny plik graficzny
            File outputFile, //docelowy plik graficzny
            DoubleProperty progressProp //własność określająca postęp operacji
    ) {
        updateMessage("Processing...");

        try {
            //wczytanie oryginalnego pliku do pamięci
            BufferedImage original = ImageIO.read(originalFile);

            //przygotowanie bufora na grafikę w skali szarości
            BufferedImage grayscale = new BufferedImage(
                    original.getWidth(), original.getHeight(), original.getType());
            //przetwarzanie piksel po pikselu
            for (int i = 0; i < original.getWidth(); i++) {
                for (int j = 0; j < original.getHeight(); j++) {
                    //pobranie składowych RGB
                    int red = new Color(original.getRGB(i, j)).getRed();
                    int green = new Color(original.getRGB(i, j)).getGreen();
                    int blue = new Color(original.getRGB(i, j)).getBlue();
                    //obliczenie jasności piksela dla obrazu w skali szarości
                    int luminosity = (int) (0.21*red + 0.71*green + 0.07*blue);
                    //przygotowanie wartości koloru w oparciu o obliczoną jaskość
                    int newPixel =
                            new Color(luminosity, luminosity, luminosity).getRGB();
                    //zapisanie nowego piksela w buforze
                    grayscale.setRGB(i, j, newPixel);
                }
                //obliczenie postępu przetwarzania jako liczby z przedziału [0, 1]
                double progress = (1.0 + i) / original.getWidth();
                //aktualizacja własności zbindowanej z paskiem postępu w tabeli
                updateProgress(progress, 1);
            }

            //zapisanie zawartości bufora do pliku na dysku
            ImageIO.write(grayscale, "jpg", outputFile);

            updateMessage("Finished!");
        } catch (IOException ex) {
            //translacja wyjątku
            throw new RuntimeException(ex);
        }
    }

    public File getFile() {
        return this.sourceFile;
    }
}

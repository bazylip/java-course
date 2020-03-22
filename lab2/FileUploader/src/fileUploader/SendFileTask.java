/* Bazyli Polednia
 * Laboratorium 2
 * Platformy Technologiczne
 * 2020
 */

package fileUploader;

import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;

public class SendFileTask extends Task<Void> {
    File file;
    public SendFileTask(File file) { this.file = file; }

    /**
     * Method called after button click, creates buffered stream to server and sends content from given file
     *
     * @return NULL
     * @throws Exception
     */
    @Override protected Void call() throws Exception {
        updateMessage("Initiating...");
        updateProgress(0, 100);

        try {
            System.out.println("Trying to connect to server");
            Socket server = new Socket("localhost", 1000); // creating a socket to connect to the server
            System.out.println("Successfully connected to server");
            updateMessage("Uploading...");
            updateProgress(0, 0);

            try (DataOutputStream dos = new DataOutputStream(server.getOutputStream()); BufferedOutputStream bos = new BufferedOutputStream(dos); // Opening data streams using try-with-resources
                 FileInputStream fis = new FileInputStream(this.file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                long bytesToWrite = this.file.length();
                long currentWrittenBytes = 0;

                dos.writeUTF(this.file.getName()); // write name of the file to stream so the server knows what file to create

                byte[] buffer = new byte[4096]; //bufor 4KB
                int readSize;
                while ((readSize = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, readSize); // write buffer contents to stream
                    currentWrittenBytes += readSize;
                    updateProgress(currentWrittenBytes, bytesToWrite);
                }
            } catch (IOException ex) {
                updateMessage("Error when uploading file: " + ex.getMessage());
                return null;
            }

        } catch (IOException e) {
            updateMessage("Failed to connect to server");
            return null;
        }

        updateMessage("File uploaded!");
        updateProgress(100, 100);
        return null;
    }
}

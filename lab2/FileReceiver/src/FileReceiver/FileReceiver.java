/* Bazyli Polednia
 * Laboratorium 2
 * Platformy Technologiczne
 * 2020
 */

package FileReceiver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileReceiver{

    /**
     * Opens buffered input stream from provided socket and writes content to a file
     *
     * @param receivingSocket
     * @throws IOException
     */
    private static void receiveFile(Socket receivingSocket) throws IOException {

        try (DataInputStream dis = new DataInputStream(receivingSocket.getInputStream()); BufferedInputStream bis = new BufferedInputStream(dis)) {
            String directoryPath = "downloadedFiles";
            File dir = new File(directoryPath);
            if(!dir.exists()){
                if(dir.mkdir()) {
                    System.out.println("Successfully created directory to store downloaded files");
                }else{
                    System.out.println("Failed to create a directory to store downloaded files");
                }
            }
            String fileName = dis.readUTF(); // name of downloaded file
            String path = directoryPath + '\\' + fileName;

            File file = new File(path);
            try{
                if(file.createNewFile()){
                    System.out.println("Created new file: " + fileName);
                }else{
                    System.out.println("File " + fileName + " already existed");
                }

            }catch (IOException ex) {
                System.out.println("Error while creating a file: " + ex.getMessage());
            }

            System.out.println("Trying to download " + fileName);
            try(FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buffer = new byte[4096]; //bufor 4KB
                int readSize;
                while ((readSize = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, readSize);
                }
            }
            System.out.println("Successfully downloaded " + fileName);

        }catch (IOException ex) {
            System.out.println("Error when downloading file: " + ex.getMessage());
        }

        receivingSocket.close();

        System.out.println("Closed receiving socket (port " + receivingSocket.getPort() + ")");
    }

    /**
     * Main program for downloading a file
     *
     * @param args
     */
    public static void main(String[] args){
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(1000)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println("Receiving file...");
                executor.submit(() -> {
                    try {
                        FileReceiver.receiveFile(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException ex) {
            System.out.println("Error while creating server socket: " + ex.getMessage());
        }
    }
}

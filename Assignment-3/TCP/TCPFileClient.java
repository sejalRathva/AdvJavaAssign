import java.io.*;
import java.net.*;

public class TCPFileClient {
    public static void main(String[] args) {

        String serverAddress = "127.0.0.1";
        int port = 400;

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to server: " + serverAddress + " on port " + port);
            receiveFile(socket);
        } catch (IOException e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }

    private static void receiveFile(Socket socket) {
        String outputFilePath = "received_file.txt";

        try (InputStream inputStream = socket.getInputStream();
                BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }

            System.out.println("File received successfully and saved as " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error receiving file: " + e.getMessage());
        }
    }
}

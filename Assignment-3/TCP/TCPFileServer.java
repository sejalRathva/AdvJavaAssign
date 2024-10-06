import java.io.*;
import java.net.*;

public class TCPFileServer {
    public static void main(String[] args) {
        // Set the port number to listen on
        int port = 12345;
        // Specify the file path to send
        String filePath = "D://Java/swing";

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                // Accept incoming client connection
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                    // Send the file to the client
                    sendFile(socket, filePath);
                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port + ": " + e.getMessage());
        }
    }

    private static void sendFile(Socket socket, String filePath) {
        try (BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(filePath));
                OutputStream outputStream = socket.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInput.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully!");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        }
    }
}

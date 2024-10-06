import java.io.*;
import java.net.*;

public class UDPFileServer {
    public static void main(String[] args) {
        // Specify the port number and the file path
        int port = 9876;
        String filePath = "path/to/your/file.txt"; // Change to your file path

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP server is listening on port " + port);

            // Read the file and send its content
            sendFile(socket, filePath);
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }

    private static void sendFile(DatagramSocket socket, String filePath) {
        byte[] buffer = new byte[4096];
        try (BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(filePath))) {
            int bytesRead;

            // Read and send the file in chunks
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, InetAddress.getByName("127.0.0.1"), 9877);
                socket.send(packet);
            }
            System.out.println("File sent successfully!");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        }
    }
}

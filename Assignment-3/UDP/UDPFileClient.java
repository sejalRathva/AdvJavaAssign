import java.io.*;
import java.net.*;

public class UDPFileClient {
    public static void main(String[] args) {
        // Server's IP address and port number
        String serverAddress = "127.0.0.1"; // Change to the server's IP address
        int port = 9877;

        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.println("Connected to server: " + serverAddress + " on port " + port);
            receiveFile(socket, serverAddress, port);
        } catch (IOException e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }

    private static void receiveFile(DatagramSocket socket, String serverAddress, int port) {
        byte[] buffer = new byte[4096];
        String outputFilePath = "received_file.txt"; // Change to your desired output file path

        try (BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {
            DatagramPacket packet;

            while (true) {
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // Receive the packet

                // If the packet's length is 0, break (end of transmission)
                if (packet.getLength() == 0) {
                    break;
                }

                // Write the received bytes to the output file
                fileOutput.write(packet.getData(), 0, packet.getLength());
            }

            System.out.println("File received successfully and saved as " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error receiving file: " + e.getMessage());
        }
    }
}

import java.io.*;
import java.net.*;

public class ProxyServer {
    private static final int LOCAL_PORT = 8080; // Port for Proxy Server to listen on
    private static final String REMOTE_HOST = "example.com"; // Target server
    private static final int REMOTE_PORT = 80; // Target server port (HTTP)

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(LOCAL_PORT)) {
            System.out.println("Proxy Server is running on port " + LOCAL_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle the client's requests
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (Socket targetSocket = new Socket(REMOTE_HOST, REMOTE_PORT);
                    BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader targetIn = new BufferedReader(new InputStreamReader(targetSocket.getInputStream()));
                    PrintWriter targetOut = new PrintWriter(targetSocket.getOutputStream(), true)) {

                // Reading request from the client and forwarding it to the target server
                String clientRequest;
                while ((clientRequest = clientIn.readLine()) != null && !clientRequest.isEmpty()) {
                    targetOut.println(clientRequest);
                    System.out.println("Forwarding request: " + clientRequest);
                }

                // Reading response from the target server and forwarding it to the client
                String targetResponse;
                while ((targetResponse = targetIn.readLine()) != null) {
                    clientOut.println(targetResponse);
                    System.out.println("Forwarding response: " + targetResponse);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

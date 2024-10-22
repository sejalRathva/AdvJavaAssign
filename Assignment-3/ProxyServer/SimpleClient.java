import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) {
        String proxyHost = "localhost";
        int proxyPort = 8080;

        try (Socket socket = new Socket(proxyHost, proxyPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send HTTP GET request through the proxy
            out.println("GET / HTTP/1.1");
            out.println("Host: example.com");
            out.println("Connection: close");
            out.println(); // Blank line to indicate end of request

            // Read and print the response from the proxy (and ultimately the target server)
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

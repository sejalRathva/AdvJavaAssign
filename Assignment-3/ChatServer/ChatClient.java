import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new IncomingReader()).start();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Server: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;
        new ChatClient(serverAddress, port);
    }
}

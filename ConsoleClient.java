import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleClient {

    public static void main(String[] args) {

        try {

            // connect to the server
            Socket socket = new Socket("localhost", 59001);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            // thread that listens for messages from server
            Thread listener = new Thread(() -> {
                try {
                    String message;

                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }

                } catch (Exception e) {
                    System.out.println("Disconnected from server.");
                }
            });

            listener.start();

            // main thread sends messages
            while (true) {

                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    break;
                }

                out.println(userInput);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
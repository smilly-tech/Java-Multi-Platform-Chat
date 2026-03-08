import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {

        System.out.println("Chat server running on port 59001");

        try (ServerSocket serverSocket = new ServerSocket(59001)) {

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {

                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(
                        socket.getOutputStream(), true);

                out.println("SERVER: Enter your name:");
                name = in.readLine();

                synchronized (clients) {
                    clients.add(out);
                }

                broadcast(name + " joined the chat.");

                String message;

                while ((message = in.readLine()) != null) {

                    if (message.equalsIgnoreCase("QUIT")) {
                        break;
                    }

                    broadcast(name + ": " + message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    socket.close();
                } catch (IOException e) {}

                synchronized (clients) {
                    clients.remove(out);
                }

                broadcast(name + " left the chat.");
            }
        }

        private void broadcast(String message) {

            synchronized (clients) {

                for (PrintWriter writer : clients) {
                    writer.println(message);
                }

            }
        }
    }
}
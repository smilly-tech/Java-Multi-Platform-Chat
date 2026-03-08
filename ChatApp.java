import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

public class ChatApp extends Application {

    private TextArea chatArea;
    private TextField messageField;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void start(Stage stage) {

        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageField = new TextField();
        Button sendButton = new Button("Send");

        HBox bottomBox = new HBox(10);
        bottomBox.getChildren().addAll(messageField, sendButton);

        BorderPane root = new BorderPane();
        root.setCenter(chatArea);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 450, 300);

        stage.setTitle("Chat Client");
        stage.setScene(scene);
        stage.show();

        connectToServer();

        sendButton.setOnAction(e -> sendMessage());

        messageField.setOnAction(e -> sendMessage());
    }

    private void connectToServer() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Connect");
        dialog.setHeaderText("Enter your name");

        dialog.showAndWait().ifPresent(name -> {

            try {

                socket = new Socket("localhost", 59001);

                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream(), true);

                out.println(name);

                //listens for messages
                Thread listener = new Thread(() -> {

                    try {

                     String line;

                    while ((line = in.readLine()) != null) {

                     String message = line;

                    Platform.runLater(() -> {
                     chatArea.appendText(message + "\n");
                         });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

                listener.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void sendMessage() {

        String message = messageField.getText();

        if (!message.isEmpty()) {
            out.println(message);
            messageField.clear();
        }
    }

    @Override
    public void stop() throws Exception {

        if (socket != null) {
            socket.close();
        }

        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
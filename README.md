# Java-Multi-Platform-Chat

Project: The Lehman Multi-Platform Chat System
Overview


In class, we developed the ChatServer, a multi-threaded server capable of broadcasting messages to all connected clients. Now, you must develop the "Client Side" of this architecture. You are required to implement two versions of the client:
The Console Client: A command-line interface for rapid testing.
The Desktop Client: A JavaFX graphical user interface.

Phase 1: The Console Client (CLI)
  Objective: Establish a basic connection and handle bidirectional communication.
  Technical Requirements:
  Socket Connectivity: Connect to localhost on port 59001.
  Multi-threading (Crucial): Your client must do two things at once:
  Read input from the user (via Scanner or BufferedReader).
  Listen for incoming messages from the server and print them.
  Architecture: * The main thread should handle user input and send messages to the server.
  A background thread (a separate Runnable or Thread) should continuously listen to the server's InputStream.
  Common Pitfall: If you do not use a separate thread for receiving, your program will "block" while waiting for user input, and you won't see messages from other users until 
  you press Enter!


Phase 2: The JavaFX Desktop Client (GUI)
  Objective: Build a professional, responsive chat window.
  UI Requirements:
  Chat Area: A non-editable TextArea that displays the conversation history.
  Input Field: A TextField where the user types their message.
  Send Button: A Button to trigger the transmission.
  Connection Logic: A way to enter the server IP and Name before starting.
  Technical Requirements:
  Threading & The UI Thread: You must use a background thread to listen for server messages.
  Platform.runLater(): You cannot update the TextArea directly from the background listening thread. You must wrap UI updates in Platform.runLater(() -> { ... }) to avoid an IllegalStateException.
  Event Handling: The "Send" action should be triggered by both clicking the button and pressing the "Enter" key in the text field.


Phase 3: Technical Specifications & Hints
  1. The Connection Protocol
  The ChatServer expects the first message from a client to be the User Name.
  Logic: 1. Connect Socket. 2. Wait for the server's prompt: "SERVER: Enter your name: ". 3. Send the name string. 4. Enter the main loop of sending/receiving.
  2. Clean Shutdown
  Ensure that when a user closes the window or types "QUIT" in the console, the socket is closed gracefully. The server will detect the closed stream and announce the user's departure.
  3. Sample Client Logic (Pseudo-code)
  Socket socket = new Socket("localhost", 59001);
  PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

// Background thread for receiving
new Thread(() -> {
    try {
        String line;
        while ((line = in.readLine()) != null) {
            // In JavaFX: Platform.runLater(() -> textArea.appendText(line + "\n"));
            // In Console: System.out.println(line);
        }
    } catch (IOException e) { e.printStackTrace(); }
}).start();

Deliverables & Evaluation
ConsoleClient.java: Must allow multiple instances to run and chat simultaneously.
ChatApp.java (JavaFX): Must have a clean layout and follow thread-safety rules for UI updates.
Integration Test: Provide a screenshot of your Desktop Client chatting with a Console Client.
Pro-Tip: Use GridPane or BorderPane for your JavaFX layout. A BorderPane with the TextArea in the Center and an HBox (containing the TextField and Button) in the Bottom works very well for chat apps.
Platform.runLater() schedules a Runnable task to be executed on the JavaFX Application Thread at some point in the future. Here is an example:
// Start a background thread to do work
Thread taskThread = new Thread(() -> {
    double progress = 0;
    for (int i = 0; i < 10; i++) {
        try {
            Thread.sleep(1000); // Simulate work
        } catch (InterruptedException e) { e.printStackTrace(); }
        progress += 0.1;
        final double currentProgress = progress;
        // Use Platform.runLater to update the UI safely
        Platform.runLater(() -> {
            progressBar.setProgress(currentProgress);
        });
    }
});
taskThread.start();

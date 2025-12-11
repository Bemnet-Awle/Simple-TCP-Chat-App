import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class Server extends JFrame {
    private JTextField portField, messageField;
    private JButton startButton, sendButton;
    private JTextArea logArea;
    private JLabel statusLabel;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private ServerSocket serverSocket;
    private PrintWriter out;

    public Server() {
        setTitle("Server Application");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("PORT"));
        portField = new JTextField("8000", 5);
        topPanel.add(portField);
        startButton = new JButton("Start");
        topPanel.add(startButton);
        add(topPanel, BorderLayout.NORTH);

        statusLabel = new JLabel("Status: DISCONNECTED");
        add(statusLabel, BorderLayout.SOUTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        messageField = new JTextField("Waiting for client to connect...");
        messageField.setEnabled(false);
        southPanel.add(messageField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        southPanel.add(sendButton, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startServer());
        messageField.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                } catch (IOException ex) {
                    // Ignore
                }
            }
        });

        setVisible(true);
    }

    private void log(String type, String msg) {
        String time = sdf.format(new Date());
        logArea.append("[" + time + "] " + type + " " + msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText());
            serverSocket = new ServerSocket(port);
            log("System:", "Server started. Listening on port " + port + "..");
            statusLabel.setText("Status: LISTENING");
            startButton.setEnabled(false);
            portField.setEnabled(false);

            new Thread(() -> {
                try {
                    Socket socket = serverSocket.accept();
                    log("System:", "Client connected from /" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                    messageField.setEnabled(true);
                    sendButton.setEnabled(true);
                    messageField.setText("");
                    messageField.requestFocus();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    String msg;
                    while ((msg = in.readLine()) != null) {
                        log("Client:", "Received: \"" + msg + "\"");
                    }
                    log("System:", "Client disconnected.");
                    messageField.setEnabled(false);
                    sendButton.setEnabled(false);
                    messageField.setText("Client disconnected");
                } catch (IOException ex) {
                    log("System:", "Server stopped.");
                    statusLabel.setText("Status: DISCONNECTED");
                    messageField.setEnabled(false);
                    sendButton.setEnabled(false);
                }
            }).start();
        } catch (Exception ex) {
            log("Error:", ex.getMessage());
        }
    }

    private void sendMessage() {
        if (out != null) {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                out.println(msg);
                log("Server:", "Sending: \"" + msg + "\"");
                messageField.setText("");
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
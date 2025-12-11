import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class Client extends JFrame {
    private JTextField ipField, portField, messageField;
    private JButton connectButton, sendButton;
    private JTextArea logArea;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client() {
        setTitle("Client Application");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("SERVER IP"));
        ipField = new JTextField("localhost", 10);
        topPanel.add(ipField);
        topPanel.add(new JLabel("PORT"));
        portField = new JTextField("8000", 5);
        topPanel.add(portField);
        connectButton = new JButton("Connect");
        topPanel.add(connectButton);
        add(topPanel, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        messageField = new JTextField("Connect to server to chat");
        messageField.setEnabled(false);
        southPanel.add(messageField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        southPanel.add(sendButton, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        connectButton.addActionListener(e -> connectToServer());
        messageField.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
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

    private void connectToServer() {
        try {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            log("System:", "Attempting to connect to " + ip + ":" + port + "..");
            socket = new Socket(ip, port);
            log("System:", "Connected to server!");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connectButton.setEnabled(false);
            ipField.setEnabled(false);
            portField.setEnabled(false);
            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            messageField.setText("");
            messageField.requestFocus();

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        log("Server:", "Received: \"" + msg + "\"");
                    }
                    log("Error:", "Connection lost.");
                    messageField.setEnabled(false);
                    sendButton.setEnabled(false);
                } catch (IOException ex) {
                    log("Error:", "Connection lost.");
                    messageField.setEnabled(false);
                    sendButton.setEnabled(false);
                }
            }).start();
        } catch (Exception ex) {
            log("Error:", ex.getMessage());
        }
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            out.println(msg);
            log("Client:", "Sending: \"" + msg + "\"");
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
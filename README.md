# Simple-TCP-Chat-App
Simple TCP Chat Application using Java Swing for Advanced Java Programming course individual assignment 
This is a basic bidirectional chat application built using Java Swing for the graphical user interface and TCP sockets for network communication. It demonstrates fundamental concepts from network programming, such as establishing client-server connections, sending/receiving messages via streams, and handling GUI events. The app supports a single client connection and logs messages with timestamps.

## Features

- Server Side: Listens on a specified port (default 8000), accepts a client connection, displays logs, and allows sending messages via an input field and "Send" button.
- Client Side: Connects to the server via IP and port, displays logs, and sends messages similarly.
- Bidirectional Communication: Messages can be sent from both ends independently.
- Logging: Timestamps, system events (e.g., connections, disconnections), and message directions (e.g., "Client: Sending: ...").
- Error Handling: Displays connection lost errors and disables inputs on disconnect.
- GUI Elements: Text areas for logs, fields for port/IP, buttons for start/connect/send.

## Screenshots
### Server GUI
  - <img width="487" height="493" alt="image" src="https://github.com/user-attachments/assets/c029ce36-749d-4bdb-bf0e-97274c847fc0" />

### Client GUI
  - <img width="487" height="493" alt="client" src="https://github.com/user-attachments/assets/264e8d82-e2af-4f00-991b-95a0a80e255c" />

## Requirements

- Java 8 or higher (uses standard libraries: java.net, java.io, javax.swing, etc.)
- No external dependencies

## Installation

1. Save the code files: Server.java and Client.java.
2. Compile them using javac Server.java and javac Client.java.
3. Run with java Server and java Client.

## Usage

1. Launch the server application (java Server).
2. Enter a port (default: 8000) and click "Start". The server will listen for connections.
3. Launch the client application (java Client).
4. Enter the server IP (default: localhost) and port, then click "Connect".
5.Once connected, type messages in the input field and press Enter or click "Send" on either side.
6. Close the windows to disconnect and stop the apps.

package dai.lab.smtp;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Represents an SMTP client for sending emails via a specified SMTP server.
 * This class handles the connection to the server, sending SMTP commands,
 * and sending email messages.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class SMTPClient {

    private String serverAddress;
    private int port;
    private Socket smtpSocket;

    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * Constructor to initialize the SMTP client with server address and port.
     * 
     * @param serverAddress The address of the SMTP server.
     * @param port The port to connect to on the SMTP server.
     */
    public SMTPClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    /**
     * Establishes a connection to the SMTP server and initializes the reader and writer streams.
     * 
     * @throws IOException If an I/O error occurs while connecting to the server.
     */
    public void connectToServer() throws IOException {
        smtpSocket = new Socket(serverAddress, port);
        reader = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(smtpSocket.getOutputStream()));
    }

    /**
     * Sends a command to the SMTP server.
     * 
     * @param command The SMTP command to be sent.
     * @param writer The writer stream to send the command.
     * @throws IOException If an I/O error occurs while sending the command.
     */
    public void sendCommand(String command, BufferedWriter writer) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
    }

    /**
     * Reads a response from the SMTP server.
     * 
     * @param reader The reader stream to read the response.
     * @return The response from the SMTP server.
     * @throws IOException If an I/O error occurs while reading the response.
     */
    public String readResponse(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    /**
     * Sends an email to the specified recipients via the SMTP server.
     * 
     * @param sender The sender's email address.
     * @param receivers A list of recipient email addresses.
     * @param message The email message to be sent.
     * @throws IOException If an I/O error occurs while sending the email.
     */
    public void sendEmail(String sender, List<String> receivers, Mail message) throws IOException {
        // Send SMTP commands
        readResponse(reader);
        sendCommand("HELO localhost", writer);
        readResponse(reader);
    
        sendCommand("MAIL FROM:<" + sender + ">", writer);
        readResponse(reader);
    
        // Add each recipient to the 'RCPT TO' field
        for (String recipient : receivers) {
            sendCommand("RCPT TO:<" + recipient + ">", writer);
            readResponse(reader);
        }
    
        sendCommand("DATA", writer);
        readResponse(reader);
    
        // Send From: and To: headers
        writer.write("From: <" + sender + ">\r\n");
        writer.write("To: " + String.join(", ", receivers) + "\r\n"); // All recipients in one To:
        writer.write("Subject: " + message.getSubject() + "\r\n");
        writer.write("\r\n");
        writer.write(message.getBody() + "\r\n");
        writer.write(".\r\n");
        writer.flush();
        readResponse(reader);
    
        sendCommand("RSET", writer);
    }

    /**
     * Closes the connection to the SMTP server, closing the reader, writer, and socket.
     * 
     * @throws IOException If an I/O error occurs while closing the connection.
     */
    public void closeConnection() throws IOException {
        if (writer != null) writer.close();
        if (reader != null) reader.close();
        if (smtpSocket != null && !smtpSocket.isClosed()) {
            smtpSocket.close();
        }
    }
}

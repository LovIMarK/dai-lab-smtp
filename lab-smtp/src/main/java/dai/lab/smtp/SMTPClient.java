package dai.lab.smtp;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class SMTPClient {

    private String serverAddress;
    private int port;
    private Socket smtpSocket;

    private BufferedReader reader;
    private BufferedWriter writer;

    public SMTPClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void connectToServer() throws IOException {
        smtpSocket = new Socket(serverAddress, port);
        reader = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(smtpSocket.getOutputStream()));
    }

    public void sendCommand(String command, BufferedWriter writer) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
    }

    public String readResponse(BufferedReader reader) throws IOException {
        return reader.readLine();
    }


    public void sendEmail(String sender, List<String> receivers, Mail message) throws IOException {
        // Envoyer les commandes SMTP
        readResponse(reader);
        sendCommand("HELO localhost", writer);
        readResponse(reader);

        sendCommand("MAIL FROM:<" + sender + ">", writer);
        readResponse(reader);

        for (String recipient : receivers) {
            sendCommand("RCPT TO:<" + recipient + ">", writer);
            readResponse(reader);
        }

        sendCommand("DATA", writer);
        readResponse(reader);

        // Envoi du sujet et du corps du message
        writer.write("Subject: " + message.getSubject() + "\r\n");
        writer.write("\r\n");
        writer.write(message.getBody() + "\r\n");
        writer.write(".\r\n");
        writer.flush();
        readResponse(reader);

        sendCommand("RSET", writer);
    }

    public void closeConnection() throws IOException {
        if (writer != null) writer.close();
        if (reader != null) reader.close();
        if (smtpSocket != null && !smtpSocket.isClosed()) {
            smtpSocket.close();
        }
    }

}

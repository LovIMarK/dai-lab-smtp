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
    
        // Ajouter chaque destinataire dans le champ 'RCPT TO'
        for (String recipient : receivers) {
            sendCommand("RCPT TO:<" + recipient + ">", writer);
            readResponse(reader);
        }
    
        sendCommand("DATA", writer);
        readResponse(reader);
    
        // Envoi des en-têtes From: et To:
        writer.write("From: <" + sender + ">\r\n");
        writer.write("To: " + String.join(", ", receivers) + "\r\n"); // Tous les destinataires dans un seul To:
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
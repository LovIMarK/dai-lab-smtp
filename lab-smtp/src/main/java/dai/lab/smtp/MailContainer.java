package dai.lab.smtp;

import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Manages a static list of Mail objects, including methods to load, retrieve, and manage emails.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class MailContainer {
    private static ArrayList<Mail> mailList = new ArrayList<>(); // Static list of mails

    /**
     * Loads all mails from a JSON file and adds them to the static mail list.
     * 
     * @param filePath The path of the file containing the emails.
     */
    public static void loadMails(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("Invalid file path.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Load JSON from the file
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Extract the list of mails under the "emails" key
            JsonNode emailsNode = rootNode.path("emails");

            // Check if the "emails" key is present and is an array
            if (emailsNode.isArray()) {
                // Deserialize the list of mails
                List<Mail> loadedMails = objectMapper.readValue(emailsNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Mail.class));

                // Add the loaded mails to the static mailList
                mailList.addAll(loadedMails);

                // Print the loaded mails
                for (Mail mail : loadedMails) {
                    System.out.println("Subject: " + mail.getSubject());
                    System.out.println("Body: " + mail.getBody());
                }
            } else {
                System.err.println("The 'emails' key is not present or is not an array.");
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * Retrieves a random mail from the static list.
     * 
     * @return A random Mail object.
     */
    public static Mail getRandomMail() {
        if (mailList.isEmpty()) {
            throw new IllegalStateException("No mails available. Please load the mails first.");
        }
        Random random = new Random();
        return mailList.get(random.nextInt(mailList.size()));
    }

    /**
     * Returns the complete list of mails.
     * 
     * @return The list of all mails.
     */
    public static ArrayList<Mail> getAllMails() {
        return new ArrayList<>(mailList);
    }
}

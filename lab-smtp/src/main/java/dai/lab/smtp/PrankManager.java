package dai.lab.smtp;

import java.util.ArrayList;

/**
 * Manages the prank campaign by coordinating the configuration, victim handling,
 * email sending, and SMTP connection.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class PrankManager {
    private SMTPClient client;

    /**
     * Initializes the prank campaign by loading configurations, validating the group size,
     * connecting to the SMTP server, and sending prank emails.
     */
    public PrankManager() {

        ConfigurationManager.loadConfiguration();
        
        // Validate the number of victims per group
        if (!(ConfigurationManager.getGroupSize() >= 2 && ConfigurationManager.getGroupSize() <= 5)) {
            System.err.println("The number of victims per group must be between 2 and 5.");
            return;
        }

        // Initialize SMTP client
        this.client = new SMTPClient(ConfigurationManager.getIpAddress(), ConfigurationManager.getPort());

        try {
            // Load configuration files
            VictimContainer.loadVictims(ConfigurationManager.getVictimsPath());
            MailContainer.loadMails(ConfigurationManager.getMailMsgsPath());

            // Retrieve victims
            ArrayList<String> victims = VictimContainer.getVictims();
            if (victims.isEmpty()) {
                System.err.println("No victims found. Please check your victims file.");
                return;
            }

            // Connect to SMTP server
            this.client.connectToServer();

            // Divide victims into groups and send emails
            for (int i = 0; i < victims.size(); i += ConfigurationManager.getGroupSize()) {
                System.out.println("Processing group " + (i / ConfigurationManager.getGroupSize() + 1)); // Debug

                // Create a group
                int end = Math.min(i + ConfigurationManager.getGroupSize(), victims.size());
                ArrayList<String> group = new ArrayList<>(victims.subList(i, end));

                String sender = group.get(0); // First member is the sender
                ArrayList<String> victimsGroup = new ArrayList<>(group.subList(1, group.size())); // Others are recipients

                Mail randomMail = MailContainer.getRandomMail(); // Choose a random mail
                System.out.println("Sending email from " + sender + " to " + victimsGroup); // Debug

                // Send the email
                this.client.sendEmail(sender, victimsGroup, randomMail);
            }

        } catch (Exception e) {
            System.err.println("Error during prank campaign: " + e.getMessage());
        } finally {
            // Close SMTP connection
            try {
                this.client.closeConnection();
            } catch (Exception e) {
                System.err.println("Error closing SMTP connection: " + e.getMessage());
            }
            System.out.println("SMTP session closed.");
        }
    }
}

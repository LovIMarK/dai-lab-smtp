package dai.lab.smtp;

import java.util.ArrayList;

public class PrankManager {
    private SMTPClient client;

    public PrankManager() {
        // Validation du nombre de victimes par groupe
        if (!(ConfigurationManager.n >= 2 && ConfigurationManager.n <= 5)) {
            System.err.println("The number of victims per group must be between 2 and 5.");
            return;
        }

        // Initialisation du client SMTP
        this.client = new SMTPClient(ConfigurationManager.ipAddress, ConfigurationManager.port);

        try {
            // Chargement des fichiers de configuration
            VictimContainer.loadVictims(ConfigurationManager.victimsPath);
            MailContainer.loadMails(ConfigurationManager.mailMsgsPath);

            // Récupération des victimes
            ArrayList<String> victims = VictimContainer.getVictims();
            if (victims.isEmpty()) {
                System.err.println("No victims found. Please check your victims file.");
                return;
            }

            // Connexion au serveur SMTP
            this.client.connectToServer();

            // Division des victimes en groupes et envoi des emails
            for (int i = 0; i < victims.size(); i += ConfigurationManager.n) {
                System.out.println("Processing group " + (i / ConfigurationManager.n + 1)); // Debug

                // Création d'un groupe
                int end = Math.min(i + ConfigurationManager.n, victims.size());
                ArrayList<String> group = new ArrayList<>(victims.subList(i, end));

                String sender = group.get(0); // Premier membre est l'expéditeur
                ArrayList<String> victimsGroup = new ArrayList<>(group.subList(1, group.size())); // Les autres sont les destinataires

                Mail randomMail = MailContainer.getRandomMail(); // Choisir un mail aléatoire
                System.out.println("Sending email from " + sender + " to " + victimsGroup); // Debug

                // Envoi de l'email
                this.client.sendEmail(sender, victimsGroup, randomMail);
            }

        } catch (Exception e) {
            System.err.println("Error during prank campaign: " + e.getMessage());
        } finally {
            // Fermeture de la connexion SMTP
            try {
                this.client.closeConnection();
            } catch (Exception e) {
                System.err.println("Error closing SMTP connection: " + e.getMessage());
            }
            System.out.println("SMTP session closed.");
        }
    }
}
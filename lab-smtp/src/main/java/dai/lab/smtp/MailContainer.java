package dai.lab.smtp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MailContainer {
    private static ArrayList<Mail> mailList = new ArrayList<>(); // Liste statique de mails

    /*
    Charge tous les mails d'un fichier et les ajoute à la liste statique.
    @param filePath : chemin du fichier contenant les mails.
    */
    public static void loadMails(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("Chemin du fichier invalide.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Charger le JSON à partir du fichier
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Extraire la liste des mails sous la clé "emails"
            JsonNode emailsNode = rootNode.path("emails");

            // Vérifier si la clé "emails" est présente et est un tableau
            if (emailsNode.isArray()) {
                // Désérialiser la liste de mails
                List<Mail> loadedMails = objectMapper.readValue(emailsNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Mail.class));

                // Ajouter les mails chargés à la liste statique mailList
                mailList.addAll(loadedMails);

                // Afficher les mails chargés
                for (Mail mail : loadedMails) {
                    System.out.println("Subject: " + mail.getSubject());
                    System.out.println("Body: " + mail.getBody());
                }
            } else {
                System.err.println("La clé 'emails' n'est pas présente ou n'est pas un tableau.");
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    /*
    Récupère un mail aléatoire de la liste statique.
    @return : un objet Mail aléatoire.
    */
    public static Mail getRandomMail() {
        if (mailList.isEmpty()) {
            throw new IllegalStateException("Aucun mail disponible. Chargez les mails d'abord.");
        }
        Random random = new Random();
        return mailList.get(random.nextInt(mailList.size()));
    }

    /*
    Retourne la liste complète des mails.
    @return : liste des mails.
    */
    public static ArrayList<Mail> getAllMails() {
        return new ArrayList<>(mailList);
    }
}

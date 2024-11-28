package dai.lab.smtp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

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

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder subjectBuilder = new StringBuilder();
            StringBuilder bodyBuilder = new StringBuilder();
            boolean isBody = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Subject:")) {
                    if (subjectBuilder.length() > 0 || bodyBuilder.length() > 0) {
                        // Ajoute le mail actuel à la liste avant de commencer un nouveau
                        mailList.add(new Mail(subjectBuilder.toString(), bodyBuilder.toString()));
                        subjectBuilder.setLength(0);
                        bodyBuilder.setLength(0);
                    }
                    subjectBuilder.append(line.substring(8).trim());
                    isBody = false;
                } else if (line.startsWith("Body:")) {
                    isBody = true;
                } else if (isBody) {
                    bodyBuilder.append(line).append("\n");
                }
            }

            // Ajoute le dernier mail après la boucle
            if (subjectBuilder.length() > 0 || bodyBuilder.length() > 0) {
                mailList.add(new Mail(subjectBuilder.toString(), bodyBuilder.toString()));
            }

        } catch (Exception e) {
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

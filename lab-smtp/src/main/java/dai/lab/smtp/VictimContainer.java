package dai.lab.smtp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VictimContainer {
    public static ArrayList<String> victims = new ArrayList<String>();

    /*
    Getter for the list of victims.
    @return : the list of valid victim email addresses.
    */
    public static ArrayList<String> getVictims() {
        return new ArrayList<String>(victims);
    }

    /*
    Validates the format of an email address.
    @param userMail : the email address to validate.
    @return : true if the email is valid, false otherwise.
    */
    private static boolean validateVictim(String userMail) {
        if (userMail == null || userMail.isEmpty()) {
            return false;
        }

        // Expression régulière pour valider les emails
        String emailRegex = "^[\\w._%+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$";

        // Retourne vrai si l'email correspond à l'expression régulière, sinon faux
        return userMail.matches(emailRegex);
    }

    /*
    Loads a list of valid email addresses from a JSON file.
    @param filePath : the path of the file containing victim email addresses.
    @return : a list of valid victim email addresses or null if the file path is invalid.
    */
    public static void loadVictims(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Charger le JSON à partir du fichier
            VictimList victimList = objectMapper.readValue(new File(filePath), VictimList.class);

            victims = new ArrayList<>();
            // Ajouter chaque e-mail valide à la liste
            for (String email : victimList.getVictims()) {
                if (validateVictim(email)) {
                    victims.add(email);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // Classe interne pour représenter la structure JSON
    public static class VictimList {
        private ArrayList<String> victims;

        public ArrayList<String> getVictims() {
            return victims;
        }

        public void setVictims(ArrayList<String> victims) {
            this.victims = victims;
        }
    }
}

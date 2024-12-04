package dai.lab.smtp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A container class for managing the list of victims (email addresses).
 * It validates email formats and loads a list of victims from a JSON file.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class VictimContainer {

    public static ArrayList<String> victims = new ArrayList<String>();

    /**
     * Retrieves the list of valid victim email addresses.
     * 
     * @return A list of valid victim email addresses.
     */
    public static ArrayList<String> getVictims() {
        return new ArrayList<String>(victims);
    }

    /**
     * Validates the format of an email address.
     * 
     * @param userMail The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    private static boolean validateVictim(String userMail) {
        if (userMail == null || userMail.isEmpty()) {
            return false;
        }

        // Regular expression to validate emails
        String emailRegex = "^[\\w._%+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$";

        return userMail.matches(emailRegex);
    }

    /**
     * Loads a list of valid email addresses from a JSON file.
     * 
     * @param filePath The path of the file containing victim email addresses.
     */
    public static void loadVictims(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Load JSON from the file
            VictimList victimList = objectMapper.readValue(new File(filePath), VictimList.class);

            victims = new ArrayList<>();
            // Add each valid email to the list
            for (String email : victimList.getVictims()) {
                if (validateVictim(email)) {
                    victims.add(email);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * A nested class to represent the structure of the JSON data for victims.
     */
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

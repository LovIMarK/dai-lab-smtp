package dai.lab.smtp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * The ConfigurationManager class handles loading and storing configuration data 
 * from a JSON file. It provides access to various configuration attributes.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class ConfigurationManager {
    private static String ipAddress;
    private static int port;
    private static String victimsPath;
    private static String mailMsgsPath;
    private static int groupSize;

    private static final String CONFIG_FILE_PATH = "lab-smtp/resources/config.json";

    /**
     * Loads configuration data from the JSON file and initializes the static attributes.
     * This method is called to read the configuration file and populate the class fields.
     */
    public static void loadConfiguration() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Configuration config = objectMapper.readValue(new File(CONFIG_FILE_PATH), Configuration.class);

            ipAddress = config.getIpAddress();
            port = config.getPort();
            victimsPath = config.getVictimsPath();
            mailMsgsPath = config.getMailMsgsPath();
            groupSize = config.getGroupSize();

            if (groupSize < 2 || groupSize > 5) {
                throw new IllegalArgumentException("Group size must be between 2 and 5. Current value: " + groupSize);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading the configuration file: " + e.getMessage());
        }
    }

    /**
     * Retrieves the IP address for the server.
     * 
     * @return The IP address as a string.
     */
    public static String getIpAddress() {
        return ipAddress;
    }

    /**
     * Retrieves the port number for the server.
     * 
     * @return The port number.
     */
    public static int getPort() {
        return port;
    }

    /**
     * Retrieves the path to the victims file.
     * 
     * @return The path to the victims file as a string.
     */
    public static String getVictimsPath() {
        return victimsPath;
    }

    /**
     * Retrieves the path to the mail messages file.
     * 
     * @return The path to the mail messages file as a string.
     */
    public static String getMailMsgsPath() {
        return mailMsgsPath;
    }

    /**
     * Retrieves the group size configuration.
     * 
     * @return The group size as an integer.
     */
    public static int getGroupSize() {
        return groupSize;
    }

    /**
     * Represents the structure of the configuration file.
     */
    private static class Configuration {
        private String ipAddress;
        private int port;
        private String victimsPath;
        private String mailMsgsPath;
        private int groupSize;

        public String getIpAddress() {
            return ipAddress;
        }

        public int getPort() {
            return port;
        }

        public String getVictimsPath() {
            return victimsPath;
        }

        public String getMailMsgsPath() {
            return mailMsgsPath;
        }

        public int getGroupSize() {
            return groupSize;
        }
    }
}

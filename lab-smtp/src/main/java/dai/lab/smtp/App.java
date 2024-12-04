package dai.lab.smtp;

/**
 * The App class is the entry point of the application.
 * It initializes the PrankManager class to start the prank operations.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class App {
    
    /**
     * Main method to launch the application.
     * It creates an instance of the PrankManager class to start the process.
     *
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args)
    {
        // Create a new instance of PrankManager to start the prank process
        new PrankManager();
    }
}

package dai.lab.smtp;

/**
 * Represents an email with a subject and body.
 * Provides getters for the email's subject and body.
 * 
 * @author Samuel Fernandez
 * @author Lovink Mark
 */
public class Mail {
    private String subject;
    private String body;

    /**
     * Default constructor for the Mail class.
     */
    public Mail() {}

    /**
     * Constructor to initialize the email with a subject and body.
     * 
     * @param subject The subject of the email.
     * @param body The body of the email.
     */
    public Mail(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    /**
     * Getter for the subject of the email.
     * 
     * @return The subject of the email as a string.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Getter for the body of the email.
     * 
     * @return The body of the email as a string.
     */
    public String getBody() {
        return body;
    }

    /**
     * Provides a string representation of the email.
     * 
     * @return A string containing the subject and body of the email.
     */
    @Override
    public String toString() {
        return "Subject: " + subject + "\nBody: " + body;
    }
}

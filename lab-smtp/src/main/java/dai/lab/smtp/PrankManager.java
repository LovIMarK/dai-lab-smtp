package dai.lab.smtp;

import java.util.ArrayList;

//TODO: CORRIGER BUG LISTE VIDE
public class PrankManager 
{
    private SMTPClient client;

    public PrankManager()
    {
        if(!(ConfigurationManager.n > 2 && ConfigurationManager.n < 6))
        {
            System.err.println("The number of victims per groups must be between 2 and 5.");
            return;
        }

        this.client = new SMTPClient(ConfigurationManager.ipAddress, ConfigurationManager.port);

        try {

            this.client.connectToServer();

            VictimContainer.loadVictim(ConfigurationManager.victimsPath);
            MailContainer.loadMails(ConfigurationManager.mailMsgsPath);
            

            ArrayList<String> victims = VictimContainer.getVictims();

            
        for (int i = 0; i < victims.size(); i += ConfigurationManager.n) {
            int end = Math.min(i + ConfigurationManager.n, victims.size());
            ArrayList<String> group = new ArrayList<>(victims.subList(i, end));

            String sender = group.get(0);

            ArrayList<String> victimsGroup = new ArrayList<>(group.subList(1, group.size()));

            Mail randomMail = MailContainer.getRandomMail();

            try {
                this.client.sendEmail(sender, victimsGroup, randomMail);
            } catch (Exception e) {
                System.err.println(e);
            }

        }

            this.client.closeConnection();
        
        } catch (Exception e) {

            System.err.println(e);

        }
    
    }
}

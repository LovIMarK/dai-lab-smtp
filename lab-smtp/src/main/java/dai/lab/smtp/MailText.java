package dai.lab.smtp;

import java.util.ArrayList;

public class MailText 
{
    private static ArrayList<String> mails;
    private Mail mail;

    public MailText(String filePath)
    {
        if(mails == null)
            mails = loadMessages(filePath);

        
    }

    public Mail getMail()
    {
        return this.mail;
    }

    public static ArrayList<String> loadMessages(String filePath)
    {
        return null;
    }

    class Mail
    {
        private String header;
        private String body;

        public Mail(String header, String body)
        {
            this.header = header;
            this.body = body;
        }

        public String getHeader()
        {
            return this.header;
        }

        public String getBody()
        {
            return this.body;
        }
    }
    
}

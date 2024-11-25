package dai.lab.smtp;

 /*
    Classe interne pour représenter un email
    */
    public class Mail {
        private String subject; // Sujet de l'email
        private String body;    // Corps de l'email

        /*
        Constructeur privé de la classe interne
        */
        public Mail(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }

        /*
        Getter pour le sujet
        */
        public String getSubject() {
            return subject;
        }

        /*
        Getter pour le corps
        */
        public String getBody() {
            return body;
        }

        @Override
        public String toString() {
            return "Sujet: " + subject + "\nCorps: " + body;
        }
    }

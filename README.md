# Implementation of an SMTP Client for Sending Jokes via Email 

## Introduction 

This project is a TCP client application written in Java that uses the SMTP protocol to send jokes via email to a group of recipients. The program allows you to configure victims, messages, and the number of groups from configuration files.


---


# How to Run the Project 
**Prerequisites:**  
- **Java Development Kit (JDK):**  Ensure you have Java 8 or a newer version installed on your system.
 
- **Docker Installed:**  Ensure Docker is installed and running on your system.
 
- **SMTP Server (via Docker):**  The SMTP server will be launched using Docker. Ensure Docker is configured correctly.

### Steps to Launch the Project 

#### 1. Clone or Download the Project 

Start by cloning the repository or downloading the project files to your local machine.

#### 2. Launch the SMTP Server 

Use Docker to start the SMTP server with this command:


```bash
docker run -d --name smtp-server -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

This launches a MailHog SMTP server with:
 
- **SMTP port:**  1025
 
- **Web interface:**  [http://localhost:8025](http://localhost:8025/) 
Verify the server is running by accessing the web interface in your browser.

#### 3. Configure the Project 
Update the `config.json` file located in `lab-smtp/resources/` to adjust parameters: 
- **Group Size:**  Set `groupSize` (between 2 and 5).
 
- **SMTP Server:**  Use `127.0.0.1` for `ipAddress` and `1025` for `port`.

#### 4. Customize JSON Files 
Edit the following files in `lab-smtp/resources/` to suit your needs: 
- `victims.json`: Add or modify email addresses for victims.
 
- `mailMsgs.json`: Define custom email subjects and bodies.

#### 5. Compile the Project 

Navigate to the project root and compile the source files:


```bash
javac -d bin src/dai/lab/smtp/*.java
```
This places the compiled files in the `bin` directory.
#### 6. Run the Application 
Run the main application from the `bin` directory:

```bash
java dai.lab.smtp.App
```

#### 7. Monitor the Results 
The application divides victims into groups, sends prank emails, and logs progress in the console. View sent emails in the MailHog web interface at [http://localhost:8025](http://localhost:8025/) .
### Troubleshooting 
 
- **Invalid `groupSize`:**  Ensure `groupSize` in `config.json` is between 2 and 5.
 
- **Docker Issues:**  Verify Docker is installed and running, and ensure the MailHog container started correctly.
 
- **JSON Errors:**  Validate the syntax of `victims.json` and `mailMsgs.json` with a JSON validator.

Feel free to experiment with the JSON files to customize group sizes, victim emails, or email messages.


---


## Class Diagram 

Below is a class diagram describing the main components and their relationships.

![UML](lab-smtp\figures\DAI_SMTP_UML.png)


---


## Implementation Description 

### Main Classes and Their Responsibilities 
 
1. **`ConfigurationManager`** 
The `ConfigurationManager` class is responsible for loading and managing configuration data from a JSON file. It exposes methods for accessing essential parameters such as IP address, port, file paths for victims and messages, as well as the group size. 
  - **Responsibilities:**  
    - Read configurations from a JSON file located in `lab-smtp/resources/`.

    - Validate configurations, including group size, which must be between 2 and 5.

    - Provide centralized, static access to configuration parameters for the rest of the application.
 
  - **Key Implementation Points:**  
    - Uses **Jackson**  library for mapping JSON data to Java objects.

    - Robust error handling: any issue related to loading the file or data format triggers an exception with a clear message.

    - Checks constraints like group size to avoid inconsistencies at the configuration stage.
 
  - **Expected JSON Structure for Configuration:** 

```json
{
  "ipAddress": "127.0.0.1",
  "port": 1025,
  "victimsPath": "lab-smtp/resources/victims.txt",
  "mailMsgsPath": "lab-smtp/resources/messages.txt",
  "groupSize": 3
}
```
 
  - **Example Usage in the Application:** 

```java
ConfigurationManager.loadConfiguration();
String serverIp = ConfigurationManager.getIpAddress();
int serverPort = ConfigurationManager.getPort();
```
 
  - **Link with Other Classes:**  
    - `PrankManager` uses the paths to the victims and messages files to load data for creating groups.
 
    - `SMTPClient` relies on IP address and port parameters to establish a connection to the SMTP server.


---

 
1. **`Mail`** 
The `Mail` class represents an email with a subject and body. It provides methods to access these details and display them as strings. 
  - **Responsibilities:** 
    - Encapsulate email data: subject and content.

    - Provide an easy way to retrieve this information through getters.

    - Offer a clear text representation of the email for debugging or logging.
 
  - **Key Implementation Points:** 
    - Flexible constructors: a default constructor and another to directly initialize the subject and body.
 
    - `toString` method to display an email in a readable format, useful for logs or testing features.
 
  - **Example Usage in the Application:** 

```java
Mail email = new Mail("Subject Example", "This is the body of the email.");
System.out.println(email);
// Output:
// Subject: Subject Example
// Body: This is the body of the email.
```
 
  - **Link with Other Classes:**  
    - `PrankManager` generates instances of `Mail` from message files to assign to groups.
 
    - `SMTPClient` uses `Mail` objects to construct messages for sending via the SMTP protocol.


---

 
1. **`MailContainer`** 
The `MailContainer` class manages a static list of emails (`Mail`). It provides methods to load, retrieve, and access emails stored in a JSON file. 
  - **Responsibilities:** 
    - Load a list of emails from a specified JSON file.

    - Provide centralized access to the list of emails via static methods.

    - Allow random retrieval of an email for various uses, such as automatically generating scenarios.
 
  - **Key Implementation Points:**  
    - Uses **Jackson**  to parse JSON files and transform data into `Mail` objects.

    - Error handling to ensure the file is valid and contains expected information.
 
    - Maintains a centralized list (`mailList`) to avoid unnecessary duplicates.
 
  - **Example Usage in the Application:** 

```java
// Load emails from a file
MailContainer.loadMails("lab-smtp/resources/mails.json");

// Retrieve a random email
Mail randomMail = MailContainer.getRandomMail();
System.out.println(randomMail);

// Retrieve all emails
ArrayList<Mail> allMails = MailContainer.getAllMails();
System.out.println("Number of loaded mails: " + allMails.size());
```
 
  - **Link with Other Classes:**  
    - `PrankManager` uses `MailContainer` to select random emails and assign them to victim groups.
 
    - The `Mail` class is closely linked, with each instance representing an email handled by `MailContainer`.
 
  - **Important Note:**  
    - The `getRandomMail` method ensures a random distribution among loaded emails.
 
    - Emails must be properly structured in the JSON file under an `"emails"` key.


---

 
1. **`PrankManager`** 
The `PrankManager` class is responsible for managing the entire email prank campaign. It coordinates configurations, victim processing, email sending, and communication with the SMTP server. 
  - **Responsibilities:** 
    - Load and validate configurations.

    - Split victims into groups and manage email addresses.
 
    - Send emails via an SMTP client (`SMTPClient`).

    - Manage the SMTP connection and close it after sending.
 
  - **Key Implementation Points:** 
    - The class starts by loading configurations (config file, victims, and messages).

    - Victims are then split into defined-sized groups, and for each group, an email is sent from the first member to the others.
 
    - A random message is selected using `MailContainer.getRandomMail()`.

    - An SMTP client is used to establish a connection, send emails, and close the session properly.
 
  - **Example Usage in the Application:** 

```java
// Initialize and launch the prank campaign
PrankManager prankManager = new PrankManager();
```
 
  - **Important Points:** 
    - Group size validation is performed during initialization.

    - A group is formed with the first member as the sender and others as recipients.

    - If an error occurs, an error message is displayed, and the SMTP connection is properly closed.
 
  - **Example Dialogue with the SMTP Server:** 

```scss
Sending email from alice@example.com to [bob@example.com, claire@example.com]
CONNECT TO SMTP SERVER (localhost:1025)
MAIL FROM: alice@example.com
RCPT TO: bob@example.com
DATA
Subject: Fake Subject
Body: This is a fake message.
.
QUIT
```
 
  - **Note:**  
    - The class relies on components like `VictimContainer` for managing victims and `MailContainer` for messages.
 
    - The `sendEmail` method of the `SMTPClient` class is responsible for interaction with the SMTP server.


---
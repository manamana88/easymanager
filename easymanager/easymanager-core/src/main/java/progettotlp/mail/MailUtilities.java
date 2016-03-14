/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.mail;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author sys0
 */
public class MailUtilities {

    public static void main(String args[]) throws Exception{
        Properties props = System.getProperties();


        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.crtaglio.com");

        System.out.println("PREV AUT");
        // Get session
        Session session = Session.getInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                String username = "fatture@crtaglio.com";
                String password = "fatture";
                return new PasswordAuthentication(username, password);
        }
        });

        System.out.println("POST AUT");
        // Define message
        MimeMessage message =
          new MimeMessage(session);
        message.setFrom(
          new InternetAddress("fatture@crtaglio.com"));
        message.addRecipient(
          Message.RecipientType.TO,
          new InternetAddress("ilariazappacosta85@gmail.com"));
        message.setSubject(
          "Ciao Ilaria");
        //message.setText("bbbbbbb");

        // create the message part
        MimeBodyPart messageBodyPart =
          new MimeBodyPart();

        //fill message
        messageBodyPart.setText("prrrrrrrrrr :-p");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source =
          new FileDataSource(new File("/home/vincenzo/Scrivania/dealKondor"));
        messageBodyPart.setDataHandler(
          new DataHandler(source));
        messageBodyPart.setFileName("dealKondor");
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        message.setContent(multipart);

        // Send the message

        System.out.println("PREV SEND");
        Transport.send( message );
        System.out.println("POST SEND");
    }


}

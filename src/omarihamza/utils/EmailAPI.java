package omarihamza.utils;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class EmailAPI {


    public static void sendEmail(String email, String password, String title, String body, ArrayList<String> recipients) throws AddressException {
        String host = "outlook.office365.com";

        Address[] to = new Address[recipients.size()];

        for (int i = 0; i < recipients.size(); i++) {
            to[i] = new InternetAddress(recipients.get(i));
        }

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");


        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipients(Message.RecipientType.TO, to);
            message.setSubject(title);
            message.setText(body);

            //send the message
            Transport.send(message);

            Utils.showPopup("Success", "Email sent successfully to " + recipients.size() + " recipients.", Alert.AlertType.INFORMATION);

        } catch (MessagingException e) {
            e.printStackTrace();
            Utils.showPopup("Error", "Email was not sent.", Alert.AlertType.ERROR);

        }
    }

}

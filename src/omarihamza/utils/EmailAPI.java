package omarihamza.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import omarihamza.callbacks.RefreshHistoryCallback;
import omarihamza.models.AppSettings;
import omarihamza.models.Group;
import omarihamza.models.MessageType;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class EmailAPI {


    public static void sendEmail(Group group, String email, String password, String title, String body, ArrayList<String> recipients, RefreshHistoryCallback historyCallback, Stage mStage) throws AddressException {


        AppSettings appSettings = FileUtils.loadSettings();

        Address[] to = new Address[recipients.size()];

        for (int i = 0; i < recipients.size(); i++) {
            to[i] = new InternetAddress(recipients.get(i));
        }

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", appSettings.getHost());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", appSettings.getPort());


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


            group.getMessages().add(new omarihamza.models.Message(title, body, MessageType.Email));
            FileUtils.updateGroup(group);
            historyCallback.refreshHistory();

            mStage.close();

        } catch (MessagingException e) {
            mStage.close();
            e.printStackTrace();
            Utils.showPopup("Error", "Email was not sent.\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}

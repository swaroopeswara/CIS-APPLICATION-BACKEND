package com.dw.ngms.cis.email;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by swaroop on 2019/05/17.
 */
public class EmailTest {

    public static void main(String[] args) throws AddressException {

        final String username = "smtp@dataworld.co.za";
        final String password = "Dw#$SMTP";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.dataworld.co.za");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("smtp@dataworld.co.za"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("swaroopeswara@gmail.com, swaroopragava23@gmail.com")
            );
            message.setSubject("Testing Gmail TLS");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

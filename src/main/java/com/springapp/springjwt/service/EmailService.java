package com.springapp.springjwt.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static com.springapp.springjwt.constant.EmailConstant.*;

@Service
public class EmailService {

    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
        Message message = createEmail(firstName,password,email,"new_password");
        SMTPTransport smtpTransport = (SMTPTransport) getGetEmailSession().getTransport(SIMPLE_MAIL_TRANSPORT_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message,message.getAllRecipients());
        smtpTransport.close();
    }

    public void sendNewActivationTokenEmail(String firstName, String subject, String email) throws MessagingException {
        Message message = createEmail(firstName,subject,email, "newActivationToken");
        SMTPTransport smtpTransport = (SMTPTransport) getGetEmailSession().getTransport(SIMPLE_MAIL_TRANSPORT_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message,message.getAllRecipients());
        smtpTransport.close();
    }

        private Message createEmail(String firstName, String subject, String email, String type) throws MessagingException {
        Message message = new MimeMessage(getGetEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email,false));
        message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(CC_EMAIL,false));
        message.setSubject(EMAIL_SUBJECT);
        if (type.compareTo("new_password") == 0){
            message.setText("Hello" + firstName + "\n \n  Your new account password is: " + subject + "\n \n The Support Team");

        }else {
            message.setText("Hello" + firstName + "\n \n  Please open the link to activate your account " + CONFIRMATION_URL + subject + "\n \n The Support Team");

        }
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getGetEmailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);

        return Session.getInstance(properties,null);
    }


}

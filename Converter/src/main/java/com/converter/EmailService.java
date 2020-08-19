package com.converter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {
	
    public void emailClient(String EMAIL_ADDRESS_RECIPIENT, String SUBJECT, String EMAIL_TEXT)  {
    	
    	final String username = "madagasakaria@gmail.com";
        final String password = "library123456789library";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("madagasakaria@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_ADDRESS_RECIPIENT)
            );
            message.setSubject(SUBJECT);
            message.setText(EMAIL_TEXT);

            Transport.send(message);

            System.out.println("Poslano!");

        } catch (MessagingException e) {
        		System.out.println("Unauthorized!");
        }
    }


  }

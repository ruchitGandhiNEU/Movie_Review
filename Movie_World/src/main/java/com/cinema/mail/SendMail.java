/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.mail;

import java.io.UnsupportedEncodingException;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
public class SendMail {

    private String body = "";

    public void sendRegistrationMail(String toEmail, String username, String firstname) {
        try {
            this.body = " Hello Mr " + firstname + " \n\n" + " Welcome to Movie World. \n\n" + " Your username is : " + username + ".\n\n Regards,\n MovieWorld";
            sendEmail(toEmail, "Welcome to MovieWorld", this.body);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

     public static void sendEmail(String to, String subject, String textBody) throws UnsupportedEncodingException{
     // Recipient's email ID needs to be mentioned.
        

        // Sender's email ID needs to be mentioned
        String from = "aedhealthassist@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("aedhealthassist@gmail.com", "Asd@1234");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from,"info@MovieWorld.com", "UTF8"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(textBody);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


	}
}

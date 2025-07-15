/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class EmailUtil {

    private final int LIMIT_MINUS = 10;
    private static final String FROM_EMAIL = "binbinhl113@gmail.com";
    private static final String APP_PASSWORD = "awso ntpu jbuh alwc"; // không dùng mật khẩu thật

    public String generateToken() {
        return UUID.randomUUID().toString();
    }
    
    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUS);
    }
    
    public boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }
    
    public static void send(String to, String subject, String content) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    } 
    
    public boolean sendResetEmail(String to, String link, String name) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
        
        Session session = Session.getInstance(props, auth);
        
        MimeMessage msg = new MimeMessage(session);
        
        try {
            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.setFrom(FROM_EMAIL);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Reset Password", "UTF-8");
            String content = "<h1>Hello"+name+"</h1>"+"<p>Click the link to reset password "
                    + "<a href="+link+">Click here</a></p>";
            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);
            System.out.println("Send successfully");
            return true;
        } catch (Exception e) {
            System.out.println("Send error");
            System.out.println(e);
            return false;
        }
    }
}

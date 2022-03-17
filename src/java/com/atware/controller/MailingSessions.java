/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.atware.xml.JDBCXmlReader;
/**
 *
 * @author melarga.coulibaly
 */
public class MailingSessions {
    private Session mailSession;
    
    public MailingSessions(){
    }
    
   public void sendMessage2(String email, String subject,String bodyMessage, String fileAttached) throws MessagingException {


        try {
            Properties props = System.getProperties();//new Properties();
            Session session = null; 
            final String username = JDBCXmlReader.getSmtpfrom();
            final String password = JDBCXmlReader.getSmtpuecret();
            final String hostSmtp = JDBCXmlReader.getSmtphost();    
            String PortSmtp =JDBCXmlReader.getSmtpport(); 
            String authSmtp =JDBCXmlReader.getSmtpAuth(); 
            String starttlsSmtp =JDBCXmlReader.getSmtpsecurity();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth",authSmtp);
            props.put("mail.smtp.starttls.enable", starttlsSmtp);
            props.put("mail.smtp.host",hostSmtp );
            props.put("mail.smtp.port",PortSmtp);
            props.put("mail.debug", "false"); 
            props.setProperty("mail.smtp.sendpartial", "true");
            session = Session.getInstance(props, new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(username, password);
               }
            });
            if (session != null) {
                   Transport transport=null;
                    try {
                        transport = session.getTransport("smtp");
                    } catch (NoSuchProviderException ex) {
                        System.out.println("ImpossiblegetTransport");
                        ex.printStackTrace();
                    }
    

                transport.connect(hostSmtp, username, password);

                
            // create a message
                MimeMessage message = new MimeMessage(mailSession);
                message.setRecipient(RecipientType.TO, new InternetAddress(email));

                message.setSubject(subject);

                // create and fill the first message part
                MimeBodyPart textpart = new MimeBodyPart();
                textpart.setText(bodyMessage,"utf-8");

                MimeBodyPart htmlpart = new MimeBodyPart();
                htmlpart.setContent(bodyMessage, "text/html; charset=utf-8");

                // create the second message part
                MimeBodyPart mbp2 = new MimeBodyPart();

                // attach the file to the message
                FileDataSource fds = new FileDataSource(fileAttached);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());

                // create the Multipart and add its parts to it
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(textpart);
                mp.addBodyPart(htmlpart);
                mp.addBodyPart(mbp2);

                // add the Multipart to the message
                message.setContent(mp);

                // set the Date: header
                message.setSentDate(new Date());

                // send the message
                transport.send(message);
                transport.close();
         }
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
        
    }
   public void sendMessage3(String email,String subject, String bodyMessage, String fileAttached) throws MessagingException {


        try {
            // create a message
            MimeMessage message = new MimeMessage(mailSession);
            message.setRecipient(RecipientType.TO, new InternetAddress(email));
            
            message.setSubject("MBanking");

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(bodyMessage);

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            FileDataSource fds = new FileDataSource(fileAttached);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            message.setContent(mp);

            // set the Date: header
            message.setSentDate(new Date());

            // send the message
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
   public void sendMessage(String email,String subject, String bodyMessage, String fileAttached) throws MessagingException {

        Transport transport = null;
        try {
            // create a message
            MimeMessage message = new MimeMessage(mailSession);
            message.setRecipient(RecipientType.TO, new InternetAddress(email));

            message.setSubject("MBanking");

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(bodyMessage);

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            FileDataSource fds = new FileDataSource(fileAttached);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            message.setContent(mp);

            // set the Date: header
            message.setSentDate(new Date());

            System.out.println("Valeur de session : " + mailSession.getClass());
            transport = mailSession.getTransport("smtp");
            System.out.println("Valeur de transport " + transport.getClass());
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());

////            // send the message
////            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        } finally {

            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException logOrIgnore) {
                }
            }
        }
    }
    
}

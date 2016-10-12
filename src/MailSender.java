import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailSender
{
   public static void sendConfirmationMail(String username, String address, String password, String uuid, int ldHCode) {

	   String sender = "-----";
	   Properties properties = System.getProperties();
	   
	   properties.put("mail.transport.protocol", "smtp");
	   properties.put("mail.smtp.host", "smtp.gmail.com");
	   properties.put("mail.smtp.port", "25");
	   properties.put("mail.smtp.starttls.enable", "true");
	   properties.put("mail.smtp.starttls.required", "true");
	   properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	   properties.put("mail.smtp.auth", "true");
	   
	   Authenticator authenticator = new Authenticator() {
	   protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("--------", "----");
		}
	   };
	   
	   Session session = Session.getInstance(properties, authenticator);
	   //session.setDebug(true);
	   
	   try{
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(sender));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
		msg.setSubject("SQL Demo: Registration confirmation");
		msg.setText("Congratulations! You have registered at SQL Demo. \n" +
		"Your login is: " + username + "\nand your password is: " + password +"\n\n"
		+ "Click the link below to complete your registration.\n\n"
		+ "http://localhost:8080/jdbcMVC/userconfirm?name=" + username + "&id=" + uuid + "&ldHCode=" + ldHCode
		);
		
		//użyć odpowiedniej klasy z API Javy? np. URI.addQueryString()...
		
		
		Transport.send(msg);
		System.out.println("Print: mail sent successfully!");
		
	   }catch (MessagingException mex) {
	   System.out.println("Print: mail error!");
       mex.printStackTrace();
      }
	   
   }

   
}
package hirondelle.predict.util;

import hirondelle.predict.pub.register.Register;
import hirondelle.predict.util.exception.MailException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// TODO CRIAR TESTES
/**
 * Class responsible for sending customized mails 
 * on the Predict application.
 * 
 * @author Bruno Moura
 */
public final class MailFacade {

	public static void sendWelcomeMail(Register aRegister)
		throws MailException {
		
		if(aRegister == null) throw new MailException("Register instance must not be null!");

		StringBuilder welcomeMessage = new StringBuilder("Olá ");
		welcomeMessage.append(aRegister.getScreenName().getRawString()).append(",");
		welcomeMessage.append(System.lineSeparator()).append(System.lineSeparator());
		
		welcomeMessage.append("Seja bem-vindo ao Share your predictions. Faça suas previsões até ");
		
		Calendar currentTime = Calendar.getInstance();
		currentTime.add(Calendar.DAY_OF_MONTH, 5);
		
		DateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
		welcomeMessage.append(simpleFormat.format(currentTime.getTime()));
		welcomeMessage.append(" e ganhe mais pontos!");
		welcomeMessage.append(System.lineSeparator()).append(System.lineSeparator());
		
		sendMail(aRegister.getEmail().getRawString(), "Share your Predictions – Welcome", welcomeMessage.toString());
	}
	
	public static void sendRegistersMarketingMail(int totalRegisters)
		throws MailException {
		
		StringBuilder message = new StringBuilder("Olá Equipe de Marketing, ");
		message.append(System.lineSeparator()).append(System.lineSeparator());

		message.append("Segue abaixo o seu relatório de novos cadastros no sistema Share Your Predictions.");
		message.append(System.lineSeparator()).append(System.lineSeparator());

		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		message.append("Os dados foram coletados no ");
		message.append(dateFormat.format(new Date()));
		message.append(" e o total de novos registros é de ");
		message.append(totalRegisters).append(".");

		message.append(System.lineSeparator()).append(System.lineSeparator());

		message.append("Equipe Share Your Predictions.");

		sendMail("inf328-lab02-marketing@yopmail.com", "Overview - New Registers Amount", message.toString());
	}
	
	private static void sendMail(String to, String subject, String message) 
		throws MailException {

		if(to == null || to.equals("")) throw new MailException("To e-mail must not be null nor blank.");
		if(subject == null || subject.equals("")) throw new MailException("Subject must not be null nor blank.");
		if(message == null || message.equals("")) throw new MailException("Message must not be null nor blank.");

		// Sender's email ID needs to be mentioned
		final String from = "grupo10.inf328@gmail.com";

		final String password = "grupo_10-2014";

		// Assuming you are sending email from localhost
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", host);

		// Get the Session object.
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			MimeMessage mail = new MimeMessage(session);

			// Set From: header field of the header.
			mail.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			mail.setSubject(subject);

			// Now set the actual message
			mail.setText(message);

			// Send message
			Transport.send(mail);
		} catch (MessagingException mex) {
			System.out.println(">> [MailFacade] Problemas no envio de email para: " + to + " de: " + from + " com assunto: " + subject + ".");
			mex.printStackTrace();
			throw new MailException("Oh no! We had some problems sending the email. Try again later!", mex);
		}
	}
}

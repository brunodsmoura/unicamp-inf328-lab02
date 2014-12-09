package aspects;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

public aspect MailBehaviouAspect {
	
	pointcut throwMessagingException(Message message) : 
		execution(static void *.send(..)) &&
		args(message);
	
	before(Message message) throws MessagingException : throwMessagingException(message){
		System.out.println("\n\n>>> ASPECTO ATIVADO::. INTERCEPTAR THROW MESSAGING EXCEPTION.");

		if(message != null){
			Address[] recipients = message.getRecipients(RecipientType.TO);
			boolean hasExceptionMail = false;

			for(Address recipient : recipients){
				if(InternetAddress.class.isAssignableFrom(recipient.getClass())
					&& ((InternetAddress)recipient).getAddress().equals("messaging.exception@yopmail.com")){
					hasExceptionMail = true;
					break;
				}
			}

			if(hasExceptionMail){
				throw new MessagingException();
			}
		}
	}
}
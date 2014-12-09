package aspects;

import hirondelle.predict.pub.register.Register;

public aspect ByPassMailFacadeValuesAspect {

	pointcut interceptarWelcomeEmail(Register aRegister) : 
		execution(static void *.sendWelcomeMail(..)) &&
		args(aRegister);

	void around(Register aRegister) : interceptarWelcomeEmail(aRegister){
		if(aRegister != null){
			System.out.println("\n\n>>> ASPECTO ATIVADO::. INTERCEPTAR ENVIO WELCOME EMAIL (NULLABLE).\n\n");

			boolean hasEmailProcurado = aRegister.getEmail() != null && 
					aRegister.getEmail().getRawString().equals("nullable.register@yopmail.com");

			Register auxiliar = (hasEmailProcurado) ? null : aRegister;
			proceed(auxiliar);
		}
	}
	
	pointcut interceptarSendEmail(String to, String subject, String message) : 
		execution(static void *.sendMail(..)) &&
		args(to, subject, message);
	
	void around(String to, String subject, String message): interceptarSendEmail(to, subject, message){
		System.out.println("\n\n>>> ASPECTO ATIVADO::. INTERCEPTAR ENVIO EMAIL (NULLABLE).");
		
		if(to != null){
			if(to.equals("nullable.receiver@yopmail.com")){
				proceed(null, subject, message);
				return;
			}

			if(to.equals("blank.receiver@yopmail.com")){
				proceed("", subject, message);
				return;
			}

			if(to.equals("nullable.subject@yopmail.com")){
				proceed(to, null, message);
				return;
			}

			if(to.equals("blank.subject@yopmail.com")){
				proceed(to, "", message);
				return;
			}

			if(to.equals("nullable.message@yopmail.com")){
				proceed(to, subject, null);
				return;
			}

			if(to.equals("blank.message@yopmail.com")){
				proceed(to, subject, "");
				return;
			}
		}
		
		proceed(to, subject, "");
	}

}
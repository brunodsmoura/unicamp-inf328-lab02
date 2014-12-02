package aspects;

import hirondelle.predict.pub.register.Register;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.security.SafeText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public aspect CreateDAOAspect {

	pointcut interceptarRegistroUsuario(SafeText fPassword) :
		execution(* hirondelle.predict.pub.register.Register.validateState(..)) &&
		args(fPassword);

	after(SafeText fPassword) returning() throws ModelCtorException : interceptarRegistroUsuario(fPassword){
		SafeText currentPassword = fPassword;
		if(currentPassword != null){
			String messageDoesNotFollowPolicy = "The password informed does not follow the security policy";
			String messageNotAllowedPassword = "This password cannot be used. Please contact our system administrator.";
			
			String rawPassword = currentPassword.getRawString();

			if(rawPassword.length() < 8){
				System.out.println("\n\n>>> ASPECTO ATIVADO::. SENHA COM MENOS DE 8 CARACTERES\n\n");
				throwPasswordException(messageDoesNotFollowPolicy);
			}

			Pattern patternLetraMaiusculaMinuscula = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
			if(!patternLetraMaiusculaMinuscula.matcher(rawPassword).find()){
				System.out.println("\n\n>>> ASPECTO ATIVADO::. SENHA N�O POSSUI UMA LETRA MAIUSCULA OU MINUSCULA\n\n");
				throwPasswordException(messageDoesNotFollowPolicy);
			}

			Pattern patternCaracteresEspeciais = Pattern.compile("[*.$!]");
			if(!patternCaracteresEspeciais.matcher(rawPassword).find()){
				System.out.println("\n\n>>> ASPECTO ATIVADO::. SENHA N�O POSSUI OS CARACTERES ESPECIAIS *.$!.\n\n");
				throwPasswordException(messageDoesNotFollowPolicy);
			}

			Pattern patternAoMenosDoisNumeros = Pattern.compile("\\d{2,}");
			if(!patternAoMenosDoisNumeros.matcher(rawPassword).find()){
				System.out.println("\n\n>>> ASPECTO ATIVADO::. SENHA N�O POSSUI AO MENOS 2 NUMEROS\n\n");
				throwPasswordException(messageDoesNotFollowPolicy);
			}

			List<String> prohibitedPasswords = new ArrayList<String>();
			prohibitedPasswords.add("12344321a*");
			prohibitedPasswords.add("12344321A*");
			prohibitedPasswords.add("12345678.a");
			
			if(prohibitedPasswords.contains(rawPassword)){
				System.out.println("\n\n>>> ASPECTO ATIVADO::. SENHA BLOQUEADA\n\n");
				throwPasswordException(messageNotAllowedPassword);
			}
		}
	}
	
	private void throwPasswordException(String message)
		throws ModelCtorException{
		if(message == null || message.equals("")) return;
		
		ModelCtorException passwordException = new ModelCtorException();
		passwordException.add(message);
		throw passwordException;
	}

	pointcut interceptarInformacaoLogin(Register aRegister) : 
		call(public SafeText getLoginName()) &&
		target(aRegister);

	SafeText around(Register aRegister) : interceptarInformacaoLogin(aRegister){
		SafeText loginName = null;
		if(aRegister != null){
			System.out.println("\n\n>>> ASPECTO ATIVADO::. INTERCEPTAR INFORMACAO LOGIN.\n\n");

			loginName = proceed(aRegister);
			boolean hasEmailProcurado = aRegister.getEmail() != null && 
											aRegister.getEmail().getRawString().equals("falha@panico.com.br");

			if(hasEmailProcurado){
				loginName = new SafeText("NULL");
			}
		}
		
		return loginName;
	}
}
package aspects;

import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;

public aspect ByPassCaptchaValidationAspect {

	public String captcha = null;

	// pointcut to capture captcha parameter on register
	pointcut getRegisterCaptcha(SafeText aLoginName, SafeText aScreenName,
			SafeText aPassword, SafeText aPasswordConfirm, SafeText aEmail,
			SafeText aCaptchaChallenge, SafeText aCaptchaResponse, Id aIpAddress) : execution(*..*.Register.new(..)) && args(aLoginName, 
					   aScreenName, 
					   aPassword, 
					   aPasswordConfirm, 
					   aEmail, 
					   aCaptchaChallenge,
					   aCaptchaResponse,
					   aIpAddress);

	Object around(SafeText aLoginName, SafeText aScreenName,
			SafeText aPassword, SafeText aPasswordConfirm, SafeText aEmail,
			SafeText aCaptchaChallenge, SafeText aCaptchaResponse, Id aIpAddress) : getRegisterCaptcha(aLoginName, 
					   aScreenName, aPassword, aPasswordConfirm, aEmail, aCaptchaChallenge, aCaptchaResponse, aIpAddress){
		if (aCaptchaResponse != null) {
			captcha = aCaptchaResponse.getRawString(); // set parameter
		}
		return proceed(aLoginName, aScreenName, aPassword, aPasswordConfirm,
				aEmail, aCaptchaChallenge, aCaptchaResponse, aIpAddress);

	}

	// pointcut to capture captcha parameter on Reset password
	pointcut getResetPasswordCaptcha(SafeText aEmail, SafeText aNonce,
			SafeText aPassword, SafeText aPasswordConfirm,
			SafeText aCaptchaChallenge, SafeText aCaptchaResponse, Id aIpAddress) : execution(*..*.ResetPassword.new(..)) && args(aEmail,
			    	    aNonce,
			    	    aPassword, 
			    	    aPasswordConfirm, 
			    	    aCaptchaChallenge,
			    	    aCaptchaResponse,
			    	    aIpAddress);

	Object around(SafeText aEmail, SafeText aNonce, SafeText aPassword,
			SafeText aPasswordConfirm, SafeText aCaptchaChallenge,
			SafeText aCaptchaResponse, Id aIpAddress) : getResetPasswordCaptcha(aEmail,
			    	    aNonce,
			    	    aPassword, 
			    	    aPasswordConfirm, 
			    	    aCaptchaChallenge,
			    	    aCaptchaResponse,
			    	    aIpAddress){
		if (aCaptchaResponse != null) {
			captcha = aCaptchaResponse.getRawString(); // set parameter
		}
		return proceed(aEmail, aNonce, aPassword, aPasswordConfirm,
				aCaptchaChallenge, aCaptchaResponse, aIpAddress);

	}
	
	
	// pointcut to capture captcha parameter on Reset password
		pointcut getLostPasswordCaptcha(SafeText aEmail, 
			    SafeText aCaptchaChallenge,
			    SafeText aCaptchaResponse,
			    Id aIpAddress) : execution(*..*.LostPassword.new(..)) && args(aEmail, 
			    	    aCaptchaChallenge,
			    	    aCaptchaResponse,
			    	    aIpAddress);

		Object around(SafeText aEmail, 
			    SafeText aCaptchaChallenge,
			    SafeText aCaptchaResponse,
			    Id aIpAddress) : getLostPasswordCaptcha(aEmail, 
			    	    aCaptchaChallenge,
			    	    aCaptchaResponse,
			    	    aIpAddress){
			if (aCaptchaResponse != null) {
				captcha = aCaptchaResponse.getRawString(); // set parameter
			}
			return proceed(aEmail, 
		    	    aCaptchaChallenge,
		    	    aCaptchaResponse,
		    	    aIpAddress);

		}
	
	

	// pointcut to skip captcha verification if the value TESTE_SELENIUM is passed
	pointcut disableCaptcha() : execution(boolean *..*.*.isCaptchaValid());

	Object around() : disableCaptcha() {
		if (captcha != null && captcha.equals("TESTE_SELENIUM")) {
			return true;
		} else {
			return proceed();
		}
	}

}

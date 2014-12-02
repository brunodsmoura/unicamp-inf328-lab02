package hirondelle.predict.tests;

import hirondelle.predict.config.Configuration;
import hirondelle.predict.database.DatabaseHelper;
import hirondelle.predict.pages.Pages;
import hirondelle.predict.webdriver.SeleniumProvider;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class ResetPasswordTest extends TestBase{
	public static final String VALID_EMAIL = "blah4@blah.com";
	public static final String LOGIN_NAME = "testeD";
	public static final String ORIGINAL_PASSWORD_HASH = "51abb9636078defbf888d8457a7c76f85c8f114c";
	public static final String NEW_PASSWORD = "teste123";
	public static final String NONCE = "TESTE";
	
	public static final String INVALID_NONCE = "XXX";
	public static final String INVALID_PASSWORD = "XXX";
	public static final String INVALID_EMAIL = "XXX";
	public static final String NONEXISTENT_EMAIL = "XXX@XXX.COM";
	public static final String INVALID_CAPTCHA = "XXX";
	
	
	@Before
	public void beforeTest(){
		dbHelper.resetUserPasswordWithNonce(LOGIN_NAME, ORIGINAL_PASSWORD_HASH, NONCE); //reset to original password
		pages.resetPassword().openWithNonce(NONCE); //return to reset password page
	}
	
	@After
	public void afterTest(){
		SeleniumProvider.saveScreenshotByTestName(this.getClass().getSimpleName(), testName.getMethodName());
	}
	
	//TODO verify if the password was changed successfully by login with the new password 

	@Test
	public void reset_withValidFields_ShouldResetPasswordAndShowASuccessMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertEquals("Please log in with your new password.", 
				pages.resetPassword().value("message"));
	}
	
	@Test
	public void reset_withInvalidEmail_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", INVALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Valid email address is required."));
	}
	
	@Test
	public void reset_withoutEmail_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", "");
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Valid email address is required."));
	}
	
	@Test
	public void reset_withNonRegisteredEmail_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", NONEXISTENT_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Please try again. Password reset is available only for 15 minutes after the email is sent."));
	}
	
	@Test
	public void reset_withoutCaptcha_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", "");
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Please type in the fuzzy characters (CAPTCHA)"));
	}
	
	
	@Test
	public void reset_withInvalidCaptcha_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", INVALID_CAPTCHA);
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("CAPTCHA invalid. Please type in the fuzzy characters (CAPTCHA) again."));
	}
	
	
	@Test
	public void reset_withInvalidPassword_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", INVALID_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", INVALID_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Password is required, minimum 6 characters."));
	}
	
	
	@Test
	public void reset_withoutPassword_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", "");
		pages.resetPassword().type("PasswordConfirm", "");
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Password is required, minimum 6 characters."));
	}
	
	@Test
	public void reset_withoutPasswordConfirmation_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", "");
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Password confirmation is required, must match the password supplied above."));
	}
	
	
	@Test
	public void reset_withInvalidPasswordConfirmation_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", INVALID_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Password confirmation doesn't match the original password."));
	}
	
	
	@Test
	public void reset_withoutNonce_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().open();
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Nonce is not present."));
	}
	
	
	@Test
	public void reset_withInvalidNonce_ShouldNotResetPasswordAndShowAnErrorMessage() {
		pages.resetPassword().openWithNonce(INVALID_NONCE);
		pages.resetPassword().type("Email", VALID_EMAIL);
		pages.resetPassword().type("Password", NEW_PASSWORD);
		pages.resetPassword().type("PasswordConfirm", NEW_PASSWORD);
		pages.resetPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.resetPassword().clickResetButton();
		Assert.assertTrue(pages.resetPassword().values("error").contains("Please try again. Password reset is available only for 15 minutes after the email is sent."));
	}
	
}

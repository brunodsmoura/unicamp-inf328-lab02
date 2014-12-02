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

public class LostPasswordTest extends TestBase {
	public static final String VALID_EMAIL = "blah4@blah.com";
	
	public static final String INVALID_EMAIL = "XXX";
	public static final String INVALID_CAPTCHA = "XXX";
	public static final String NONEXISTENT_EMAIL = "XXX@XXX.COM";
	
	
	@Before
	public void beforeTest(){
		pages.lostPassword().open(); //return to lost password page
	}
	
	@After
	public void afterTest(){
		SeleniumProvider.saveScreenshotByTestName(this.getClass().getSimpleName(), testName.getMethodName());
	}

	
	//TODO verify if the email was sent and the reset link is valid and the email body is valid too
	
	
	@Test
	public void reset_withValidFields_ShouldShowASuccessMessage() {
		pages.lostPassword().type("Email", VALID_EMAIL);
		pages.lostPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.lostPassword().clickResetButton();
		Assert.assertEquals("An email has been sent to that email address. The email contains a special link to let you reset your password.", 
				pages.lostPassword().value("message"));
	}
	
	@Test
	public void reset_withInvalidEmail_ShouldShowAnErrorMessage() {
		pages.lostPassword().type("Email", INVALID_EMAIL);
		pages.lostPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.lostPassword().clickResetButton();
		Assert.assertTrue(pages.lostPassword().values("error").contains("Valid email address is required."));
	}
	
	@Test
	public void reset_withoutEmail_ShouldShowAnErrorMessage() {
		pages.lostPassword().type("Email", "");
		pages.lostPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.lostPassword().clickResetButton();
		Assert.assertTrue(pages.lostPassword().values("error").contains("Valid email address is required."));
	}
	
	@Test
	public void reset_withNonRegisteredEmail_ShouldShowAnErrorMessage() {
		pages.lostPassword().type("Email", NONEXISTENT_EMAIL);
		pages.lostPassword().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.lostPassword().clickResetButton();
		Assert.assertTrue(pages.lostPassword().values("error").contains("That email address doesn't belong to any registered user. Please check the email address."));
	}
	
		
	@Test
	public void reset_withoutCaptcha_ShouldShowAnErrorMessage() {
		pages.lostPassword().type("Email", VALID_EMAIL);
		pages.lostPassword().type("recaptcha_response_field", "");
		pages.lostPassword().clickResetButton();
		Assert.assertTrue(pages.lostPassword().values("error").contains("Please type in the fuzzy characters (CAPTCHA)"));
	}
	
	
	@Test
	public void reset_withInvalidCaptcha_ShouldShowAnErrorMessage() {
		pages.lostPassword().type("Email", VALID_EMAIL);
		pages.lostPassword().type("recaptcha_response_field", INVALID_CAPTCHA);
		pages.lostPassword().clickResetButton();
		Assert.assertTrue(pages.lostPassword().values("error").contains("CAPTCHA invalid. Please type in the fuzzy characters (CAPTCHA) again."));
	}
}

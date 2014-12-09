package hirondelle.predict.tests;

import hirondelle.predict.config.Configuration;
import hirondelle.predict.database.DatabaseHelper;
import hirondelle.predict.pages.Pages;
import hirondelle.predict.webdriver.SeleniumProvider;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class RegisterTest extends TestBase{
	
	public static final String VALID_USERNAME = "RegisterTest";
	public static final String VALID_SCREENNAME = "Register Test";
	public static final String VALID_EMAIL = "inf328@yopmail.com";
	public static final String VALID_PASSWORD = "IC.Inf328!";

	
	public static final String INVALID_USERNAME = "XXX";
	public static final String INVALID_SCREENNAME = "XXX";
	public static final String INVALID_EMAIL = "XXX";
	public static final String INVALID_PASSWORD = "XXX";
	public static final String INVALID_CAPTCHA = "XXX";
	
	
	@Before
	public void beforeTest(){
		pages.registerPage().open(); //return to register page
		cleanUp();
	}
	
	@After
	public void afterTest(){
		SeleniumProvider.saveScreenshotByTestName(this.getClass().getSimpleName(), testName.getMethodName());
		cleanUp();
	}
	
	public void cleanUp(){
		dbHelper.deleteUser(VALID_USERNAME);
		dbHelper.deleteUserByEmail(VALID_EMAIL);
	}

	@Test
	public void register_withValidFields_ShouldRegisterThatUserAndShowASuccessMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertEquals("Thank you! It works! A message was sent to e-mail " + VALID_EMAIL + "!", pages.registerPage().value("message"));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_withInvalidLoginName_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", INVALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Login Name is required, minimum 6 characters."));
		Assert.assertEquals(false, dbHelper.checkUser(INVALID_USERNAME));
	}
	
	@Test
	public void register_withoutLoginName_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", "");
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Login Name is required, minimum 6 characters."));
		Assert.assertEquals(false, dbHelper.checkUserByEmail(INVALID_USERNAME));
	}
	
	
	@Test
	public void register_withInvalidScreenName_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", INVALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Screen Name is required, minimum 6 characters."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_withoutScreenName_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", "");
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Screen Name is required, minimum 6 characters."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_withInvalidEmail_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", INVALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Valid email address is required (in case you forget your password)."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_withoutEmail_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Valid email address is required (in case you forget your password)."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	
	@Test
	public void register_withInvalidPassword_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", INVALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", INVALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Password is required, minimum 6 characters."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	
	@Test
	public void register_withoutPassword_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", "");
		pages.registerPage().type("PasswordConfirm", "");
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Password is required, minimum 6 characters."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_withoutPasswordConfirmation_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", "");
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Password confirmation is required, must match the password supplied above."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	
	@Test
	public void register_withInvalidPasswordConfirmation_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", INVALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Password confirmation doesn't match the original password."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	
	@Test
	public void register_withoutCaptcha_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", "");
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Please type in the fuzzy characters (CAPTCHA)"));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	
	@Test
	public void register_withInvalidCaptcha_ShouldNotRegisterThatUserAndShowAnErrorMessage() {
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", VALID_EMAIL);
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", INVALID_CAPTCHA);
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("CAPTCHA invalid. Please type in the fuzzy characters (CAPTCHA) again."));
		Assert.assertEquals(false, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_WithValidFields_NullableEntry_WelcomeMail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "nullable.register@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Register instance must not be null!"));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_WithValidFields_NullableReceiverEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "nullable.receiver@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("To e-mail must not be null nor blank."));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_WithValidFields_BlankReceiverEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "blank.receiver@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("To e-mail must not be null nor blank."));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_WithValidFields_NullableSubjectEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "nullable.subject@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Subject must not be null nor blank."));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_WithValidFields_BlankSubjectEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "blank.subject@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Subject must not be null nor blank."));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}

	@Test
	public void register_WithValidFields_NullableMessageEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "nullable.message@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Message must not be null nor blank."));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Test
	public void register_WithValidFields_BlankMessageEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "blank.message@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Message must not be null nor blank."));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}
	
	@Ignore("Corrigir comportamento do pointcut MailBehaviourAspect.")
	@Test
	public void register_WithValidFields_MessagingExceptionEmail(){
		pages.registerPage().type("LoginName", VALID_USERNAME);
		pages.registerPage().type("ScreenName", VALID_SCREENNAME);
		pages.registerPage().type("Email", "messaging.exception@yopmail.com");
		pages.registerPage().type("Password", VALID_PASSWORD);
		pages.registerPage().type("PasswordConfirm", VALID_PASSWORD);
		pages.registerPage().type("recaptcha_response_field", Configuration.getProperty("captchaByPass"));
		pages.registerPage().clickRegisterButton();
		Assert.assertTrue(pages.registerPage().values("error").contains("Oh no! We had some problems sending the email. Try again later!"));
		Assert.assertEquals(true, dbHelper.checkUser(VALID_USERNAME));
	}

}
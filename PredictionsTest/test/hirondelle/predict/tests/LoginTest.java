package hirondelle.predict.tests;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import hirondelle.predict.database.DatabaseHelper;
import hirondelle.predict.pages.Pages;
import hirondelle.predict.webdriver.SeleniumProvider;

public class LoginTest extends TestBase{

	public static final String VALID_USERNAME = "testeD";
	public static final String VALID_PASSWORD = "testtest";
	public static final String INVALID_USERNAME = "teste1";
	public static final String INVALID_PASSWORD = "test1";
	
	
	@Before
	public void beforeTest(){
		pages.loginPage().open(); //return to login page
	}
	
	@After
	public void afterTest(){
		SeleniumProvider.saveScreenshotByTestName(this.getClass().getSimpleName(), testName.getMethodName());
	}
	

	
	@Test
	public void login_withExistentUser_shouldLogin(){
		pages.loginPage().login(VALID_USERNAME, VALID_PASSWORD);
		Assert.assertEquals("Predictions - Your Lists", pages.loginPage().getTitle());
		SeleniumProvider.saveScreenshotByTestName(this.getClass().getSimpleName(), testName.getMethodName() + "_Logged");
		pages.loginPage().logoff();
	}
	
	@Test
	public void login_withExistentUserButInvalidPassword_shouldNotLogin(){
		pages.loginPage().login(VALID_USERNAME, INVALID_PASSWORD);
		Assert.assertTrue(pages.loginPage().containsMessage("Please try again."));
		Assert.assertFalse(pages.loginPage().getTitle().equals("Predictions - Your Lists"));
	}
	
	@Test
	public void login_withNoNExistentUser_shouldNotLogin(){
		pages.loginPage().login(INVALID_USERNAME, INVALID_PASSWORD);
		Assert.assertTrue(pages.loginPage().containsMessage("Please try again."));
		Assert.assertFalse(pages.loginPage().getTitle().equals("Predictions - Your Lists"));
	}
	
	
	
}

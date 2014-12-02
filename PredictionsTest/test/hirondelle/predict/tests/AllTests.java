package hirondelle.predict.tests;

import hirondelle.predict.webdriver.SeleniumProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	LoginTest.class,
	LostPasswordTest.class,
	RegisterTest.class,
	ResetPasswordTest.class
	})
public class AllTests extends TestBase{
	
	@BeforeClass
	public static void beforeSuite(){
		SUITE = true;
	}
	
	@AfterClass
	public static void afterSuite(){
		SUITE = false;
		SeleniumProvider.end();
	}

	
	
}

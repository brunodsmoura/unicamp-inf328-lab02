package hirondelle.predict.tests;

import hirondelle.predict.database.DatabaseHelper;
import hirondelle.predict.pages.Pages;
import hirondelle.predict.webdriver.SeleniumProvider;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.TestName;

public class TestBase {
	protected static Boolean SUITE = Boolean.FALSE;
	
	public static Pages pages = new Pages();
	public static DatabaseHelper dbHelper = new DatabaseHelper();
	
	@Rule 
	public TestName testName = new TestName();
	
	public static void shouldFinalize(){
		if(!SUITE){
			dbHelper.close();
			SeleniumProvider.end();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass(){
		shouldFinalize();
	}
	

}

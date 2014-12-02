package hirondelle.predict.webdriver;

import hirondelle.predict.config.Configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class SeleniumProvider  {

	private static WebDriver driver;
	private static SeleniumHelper helper;
	
	public static WebDriver getCurrentDriver() {
		if (driver == null) {
			initialize();
		}
		return driver;
	}
	
	public static SeleniumHelper getHelper() {
		if (helper == null) {
			initialize();
		}
		return helper;
	}

	public static void initialize() {
		driver = DriverFactory.getDriver();
		helper = new SeleniumHelper(driver);
	}
	
	public static void end() {
		driver.close();
	}
	
	public WebDriver get() {
		return driver;
	}

	public static boolean saveScreenshotTo(String path) {
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(path));
			return true;
		}
		catch(IOException ioe) {
			System.err.println(ioe);
			return false;
		}
	}
	
	public static boolean saveScreenshotByTestName(String testCase, String testName) {
		return saveScreenshotTo(Configuration.getProperty("pathToScreenshots") + testCase + "-" + testName + ".jpg");
	}
	
}

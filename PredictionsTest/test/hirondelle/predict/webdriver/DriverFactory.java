package hirondelle.predict.webdriver;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import hirondelle.predict.config.Configuration;

enum DriverFactory {
	
	CHROME {
		protected WebDriver createDriver() {
			
			if(Configuration.getProperty("useBrowserSpecificPath").equals("TRUE")){
				System.setProperty("webdriver.chrome.driver", Configuration.getProperty("browserBynaryPath"));
			} 
			return new ChromeDriver();
		}
	},
		
	FIREFOX {
		protected WebDriver createDriver() {
			if(Configuration.getProperty("useBrowserSpecificPath").equals("TRUE")){
				System.setProperty("webdriver.firefox.bin",Configuration.getProperty("browserBynaryPath"));
			} 
    		return new FirefoxDriver();
		}
	},
		
	IE {
		protected WebDriver createDriver() {
			if(Configuration.getProperty("useBrowserSpecificPath").equals("TRUE")){
				System.setProperty("webdriver.ie.driver", Configuration.getProperty("browserBynaryPath"));
			} 
			return new InternetExplorerDriver();
		}
	};

	protected abstract WebDriver createDriver();

	public static WebDriver getDriver() {
		try {
			return valueOf(Configuration.getProperty("browserType")).createDriver();
		}
		catch(IllegalArgumentException iae) {
			return null;
		}
	}
	
}

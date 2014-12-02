package hirondelle.predict.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import hirondelle.predict.config.Configuration;
import hirondelle.predict.webdriver.condition.ElementIsFound;
import hirondelle.predict.webdriver.condition.Timer;

public class SeleniumHelper {

	private WebDriver driver;

	public SeleniumHelper(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void openUrl(String url) {
		driver.get(Configuration.getProperty("baseURL") + url);
	}

	public WebElement findElement(By element) {
		new Timer(20000).waitUntil(new ElementIsFound(element));
		return driver.findElement(element);
	}
	
	public List<WebElement> findElements(By element) {
		new Timer(20000).waitUntil(new ElementIsFound(element));
		return driver.findElements(element);
	}
	
	public String getTitle() {
		return driver.getTitle();
	}
	
}

package hirondelle.predict.webdriver.condition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import hirondelle.predict.webdriver.SeleniumProvider;

public class ElementIsFound implements Condition {
	
	public String test = "";

	private By locator;
	private WebDriver driver;

	public ElementIsFound(By locator) {
		this.locator = locator;
		this.driver = SeleniumProvider.getCurrentDriver();
	}

	public boolean isSatisfied() {
		try {
			driver.findElement(locator);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String describe() {
		return "Waiting for element" + locator.toString();
	}

}

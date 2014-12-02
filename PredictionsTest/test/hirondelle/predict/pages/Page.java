package hirondelle.predict.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import hirondelle.predict.webdriver.SeleniumHelper;
import hirondelle.predict.webdriver.SeleniumProvider;

public abstract class Page {
	
	protected SeleniumHelper helper;

	public Page() {
		helper = SeleniumProvider.getHelper();
	}
	
	protected void click(WebElement element) {
		element.click();
	}

	protected void type(WebElement element, String string) {
		element.sendKeys(string);
	}
	
	protected void clear(WebElement element) {
		element.clear();
	}
	
	protected WebElement getInputByValue(String inputValue) {
		return helper.findElement(By.xpath("//input[@value='" + inputValue + "']"));
	}
	
	protected WebElement getInputByTitle(String inputTitle) {
		return helper.findElement(By.xpath("//input[@title='" + inputTitle + "']"));
	}
	
	protected WebElement getInputByName(String inputName) {
		return helper.findElement(By.xpath("//input[@name='" + inputName + "']"));
	}
	
	protected String getValueByClassName(String className) {
		return helper.findElements(By.className(className)).get(0).getText();
	}
	
	protected List<String> getValuesByClassName(String className) {
		List<String> values = new ArrayList<String>();
		for(WebElement webElement : helper.findElements(By.className(className))){
			values.add(webElement.getText());
		}
		return values;
	}
}

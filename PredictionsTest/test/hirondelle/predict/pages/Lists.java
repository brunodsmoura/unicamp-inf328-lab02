package hirondelle.predict.pages;

import org.openqa.selenium.By;

public class Lists extends Page {
	
	private By pageTitle = By.xpath("//h2");
	private By titleField = By.xpath("//input[@name='Title']");
	private By addButton = By.xpath("//input[@type='submit' and @value='Add/    Edit']");
	private String tableList = "//table[@class='report' and @title='Prediction Lists']";
	private By logoutButton = By.xpath("//input[@value='Log Out']");
	private By errorMessages = By.xpath("//p[@class='display-messages']");
	
	public void addTitle(String title) {
		type(helper.findElement(titleField), title);
		click(helper.findElement(addButton));
	}

	public String getTableInfoByPosition(int line, int column) {
		return helper.findElement(
				By.xpath(tableList + "//tr[" + (line + 1) + "]/td[" + column + "]")).getText();
	}

	public String getScreenName() {
		return helper.findElement(pageTitle).getText();
	}

	public String getLogoutButton() {
		return helper.findElement(logoutButton).getAttribute("value");
	}

	public void clearTitleField() {
		clear(helper.findElement(titleField));
	}

	public void clickLogoutButton() {
		click(helper.findElement(logoutButton));
	}

	public String getMessageError() {
		return helper.findElement(errorMessages).getText();
	}

}

package hirondelle.predict.pages;

import org.openqa.selenium.By;

public class Login extends Page {
	
	private By userName = By.name("j_username");
	private By password = By.name("j_password");
	private By loginLink = By.xpath("//a[text()='Login']");
	private By login = By.cssSelector("input[value='Login']");
	
	public void open() {
		helper.openUrl("/predict/main/lists/PredictionListAction.list");
		helper.getDriver().manage().window().maximize();
	}

	public void login(String user, String passwd) {
		type(helper.findElement(userName), user);
		type(helper.findElement(password), passwd);
		click(helper.findElement(login));
	}

	public String getLoggedinUserName() {
		return helper.findElement(By.className("profile-link")).getText();
	}
	
	public boolean containsMessage(String message) {
		return helper.findElement(By.cssSelector("table")).getText().contains(message);
	}
	
	public String getTitle() {
		return helper.getTitle();
	}
	
	public void logoff(){
		click(helper.findElement(By.cssSelector("input[value='Log Out']")));
	}

}

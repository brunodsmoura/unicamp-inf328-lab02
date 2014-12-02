package hirondelle.predict.pages;

import java.util.List;

public class ResetPassword extends Page {
	public void open() {
		helper.openUrl("/predict/pub/resetpassword/ResetPasswordAction.show");
		helper.getDriver().manage().window().maximize();
	}
	
	public void openWithNonce(String nonce) {
		helper.openUrl("/predict/pub/resetpassword/ResetPasswordAction.show?Nonce=" + nonce);
		helper.getDriver().manage().window().maximize();
	}

	public void type(String fieldName, String text) {
		type(getInputByName(fieldName), text);
	}

	public void clickResetButton() {
		click(getInputByValue("Reset Password"));
	}
	
	public String value(String className){
		return getValueByClassName(className);
	}
	
	public List<String> values(String className){
		return getValuesByClassName(className);
	}
}

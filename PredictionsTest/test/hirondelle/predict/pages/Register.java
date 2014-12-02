package hirondelle.predict.pages;

import java.util.List;


public class Register extends Page {
	
	
	public void open() {
		helper.openUrl("/predict/pub/register/RegisterAction.show");
		helper.getDriver().manage().window().maximize();
	}

	public void type(String fieldName, String text) {
		type(getInputByName(fieldName), text);
	}

	public void clickRegisterButton() {
		click(getInputByValue("Register"));
	}
	
	public String value(String className){
		return getValueByClassName(className);
	}
	
	public List<String> values(String className){
		return getValuesByClassName(className);
	}
	

}

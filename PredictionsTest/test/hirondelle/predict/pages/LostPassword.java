package hirondelle.predict.pages;

import java.util.List;

public class LostPassword extends Page{
	public void open() {
		helper.openUrl("/predict/pub/lostpassword/LostPasswordAction.show");
		helper.getDriver().manage().window().maximize();
	}

	public void type(String fieldName, String text) {
		type(getInputByName(fieldName), text);
	}

	public void clickResetButton() {
		click(getInputByValue("Reset My Password"));
	}
	
	public String value(String className){
		return getValueByClassName(className);
	}
	
	public List<String> values(String className){
		return getValuesByClassName(className);
	}
}

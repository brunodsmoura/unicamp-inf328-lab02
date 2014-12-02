package hirondelle.predict.webdriver.condition;

public interface Condition {

	public String test = "";

	public boolean isSatisfied();

	public String describe();

}

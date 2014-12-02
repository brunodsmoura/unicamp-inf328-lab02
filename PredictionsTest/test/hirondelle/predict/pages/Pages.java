package hirondelle.predict.pages;

public class Pages {
	
	public Home homePage() {
		return new Home();
	}
	
	public Lists listsPage() {
		return new Lists();
	}

	public Login loginPage() {
		return new Login();
	}
	
	public Register registerPage() {
		return new Register();
	}
	
	public LostPassword lostPassword() {
		return new LostPassword();
	}
	
	public ResetPassword resetPassword() {
		return new ResetPassword();
	}

}

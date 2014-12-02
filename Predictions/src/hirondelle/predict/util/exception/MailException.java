
package hirondelle.predict.util.exception;

/**
 * Class responsible for dealing with mail exceptions.
 * 
 * @author Bruno Moura
 */
public class MailException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MailException(String message){ super(message); }
	public MailException(String message, Throwable cause){ super(message, cause); }

}

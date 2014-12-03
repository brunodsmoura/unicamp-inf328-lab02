package hirondelle.predict.pub.register.observer;

import hirondelle.predict.util.exception.MailException;

/**
 * 
 * @author Bruno Moura
 */
public interface RegisterObserver {

    void update(NewRegisterObservable observable) throws MailException;

}
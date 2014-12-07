package hirondelle.predict.pub.register.observer;

import hirondelle.predict.util.MailFacade;
import hirondelle.predict.util.exception.MailException;

/**
 * 
 * @author Bruno Moura
 */
public class MarketingObserver implements RegisterObserver {

	@Override
	public boolean equals(Object object) {
		return (object instanceof MarketingObserver);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public void update(NewRegisterObservable observable) throws MailException {
		if(observable == null) 
			throw new IllegalArgumentException("Observable must not be null");

		final int totalNewRegisters = observable.getTotalNewRegisters();
		if(totalNewRegisters % 5 != 0) return;

		MailFacade.sendRegistersMarketingMail(totalNewRegisters);
	}

}
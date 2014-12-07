package hirondelle.predict.pub.register.observer;

import hirondelle.predict.util.exception.MailException;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Bruno Moura
 */
public class NewRegisterObservable {

	private final Set<RegisterObserver> observers;
	private int totalNewRegisters;
	
	private static NewRegisterObservable instance = null;

	private NewRegisterObservable(){ 
		this.observers = new HashSet<RegisterObserver>();
		this.totalNewRegisters = 0;
	}

	public static NewRegisterObservable getInstance(){
		if(instance == null) instance = new NewRegisterObservable();
		return instance;
	}

	public int getTotalNewRegisters(){
		return totalNewRegisters;
	}

	public void addObserver(RegisterObserver observer){
		if(observer == null) return;
		
		this.observers.add(observer);
	}

	public void removeObserver(RegisterObserver observer){
		if(observer == null) return;
		
		this.observers.remove(observer);
	}

	public void incrementTotalNewRegisters() throws MailException {
		this.totalNewRegisters++;
		this.notifyObservers();
	}

	public void notifyObservers() throws MailException {
		if(this.observers.isEmpty()) return;

		for(RegisterObserver observer : observers){
			observer.update(this);
		}
	}
	
	@Override
	public String toString() {
		return String.format("NewRegisterObservable[totalNewRegisters: %d]", totalNewRegisters);
	}

}
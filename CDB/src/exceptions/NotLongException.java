package exceptions;

public class NotLongException extends Exception {

	public NotLongException() {
		super();
	}
	
	public NotLongException(Exception compoundException) {
		super(compoundException);
	}
	
}

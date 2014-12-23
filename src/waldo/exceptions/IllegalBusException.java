package waldo.exceptions;

/**
 * Represents a problem with a Bus definition
 */

public class IllegalBusException extends RuntimeException {
	
	public IllegalBusException(String msg) {
		super(msg);
	}

}

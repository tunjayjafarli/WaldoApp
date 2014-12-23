package waldo.exceptions;

/**
 * Represents a problem with Waldo Service
 */

public class WaldoException extends RuntimeException {
	
	public WaldoException(String msg) {
		super(msg);
	}

}

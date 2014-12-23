package waldo.exceptions;

/**
 * Represents a problem with a Bus Stop representation
 */

public class IllegalBusStopException extends RuntimeException {

	public IllegalBusStopException(String msg) {
		super(msg);
	}
}

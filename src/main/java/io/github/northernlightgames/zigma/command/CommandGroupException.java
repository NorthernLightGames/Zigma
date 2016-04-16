package io.github.northernlightgames.zigma.command;

public class CommandGroupException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4275151040621897693L;

	public CommandGroupException() {
		
	}

	public CommandGroupException(String message) {
		super(message);
	}

	public CommandGroupException(Throwable cause) {
		super(cause);
	}

	public CommandGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandGroupException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

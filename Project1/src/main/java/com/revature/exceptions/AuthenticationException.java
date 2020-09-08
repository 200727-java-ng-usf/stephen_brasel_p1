package com.revature.exceptions;

/**
 * Thrown when a potential entity is not registered with the system,
 * i.e., a user tries to login to a server they have not registered with.
 */
public class AuthenticationException extends RuntimeException {

	/**
	 * creates this exception and prints "User authentication failed!"
	 * to the screen.
	 */
	public AuthenticationException() {
		super("User authentication failed!");
	}

	/**
	 * creates this exception and prints message to the screen.
	 * @param message
	 */
	public AuthenticationException(String message) {
		super(message);
	}
}

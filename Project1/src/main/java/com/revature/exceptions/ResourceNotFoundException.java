package com.revature.exceptions;

/**
 * Thrown when a Resource was trying to be found in the repository and wasn't.
 */
public class ResourceNotFoundException extends Exception{

	/**
	 * Creates this exception and prints s to the screen.
	 */
	public ResourceNotFoundException() {
		super("No resources found using the specified criteria.");
	}
}

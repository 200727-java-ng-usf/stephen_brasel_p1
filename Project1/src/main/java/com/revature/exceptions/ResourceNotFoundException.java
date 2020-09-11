package com.revature.exceptions;

public class ResourceNotFoundException extends Exception{

	public ResourceNotFoundException() {
		super("No resources found using the specified criteria.");
	}
}

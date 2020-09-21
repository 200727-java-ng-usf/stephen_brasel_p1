package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a Controller that handles Profile logic.
 */
public class ProfileController {
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return profile-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a <code>{@link String}</code> URI for profile-related pages.
	 */
	public static String profile(HttpServletRequest req){
		return "partials/profile.html";
	}
}

package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a Controller that handles Login logic.
 */
public class LoginController {
//	private static final UserService userService = new UserService();

	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return login-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a <code>{@link String}</code> URI for login-related pages.
	 */
	public static String login(HttpServletRequest req) {
		return "partials/login.html";
	}
}

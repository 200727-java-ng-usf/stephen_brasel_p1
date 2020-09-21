package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a Controller that hangles User-related logic
 */
public class UsersController {
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return user-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a  <code>{@link String}</code> URI for user-related pages.
	 */
	public static String users(HttpServletRequest req){
		return "partials/users.html";
	}
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return user-updating-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a  <code>{@link String}</code> URI for user-updating-related pages.
	 */
	public static String update(HttpServletRequest req){
		return "partials/user_update.html";
	}
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return user-registration-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a  <code>{@link String}</code> URI for user-registration-related pages.
	 */
	public static String register(HttpServletRequest req){
		return "partials/user_register.html";
	}
}

package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a Controller that handles Homepage logic.
 */
public class HomeController {
	/**
	 * Parses the request. If a user is logged in, returns the home page. Else, returns the login page.
	 * @param req an <code>{@link HttpServletRequest}</code> to be parsed.
	 * @return a <code>{@link String}</code> URI for home-related pages.
	 */
	public static String home(HttpServletRequest req){
		String principal = (String) req.getSession().getAttribute("principal");
		if(principal == null || principal.equals("")){
			return "partials/login.html";
		}
		return "partials/home.html";
//		return "/html/home.html";
	}
}

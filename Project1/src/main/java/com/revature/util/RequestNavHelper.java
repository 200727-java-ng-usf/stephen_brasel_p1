package com.revature.util;

import com.revature.controllers.HomeController;
import com.revature.controllers.LoginController;
import com.revature.servlets.NavServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Assists the <code>{@link NavServlet}</code> in presenting users with the appropriate page for their role.
 */
public class RequestNavHelper {

	/**
	 * Processes a navigation-bar-related request.
	 * @param req the <code>{@link HttpServletRequest}</code> to process
	 * @return the appropriate navbar html
	 */
	public static String process(HttpServletRequest req){
		System.out.println("Processing: " + req.getRequestURI());
		switch (req.getRequestURI()){
			case "/revabursement/main.nav":
			case "/main.nav":
				return "partials/navbars/main.html";
			case "/revabursement/admin.nav":
			case "/admin.nav":
				return "partials/navbars/admin.html";
			case "/revabursement/employee.nav":
			case "/employee.nav":
				return "partials/navbars/employee.html";
			case "/revabursement/manager.nav":
			case "/manager.nav":
				return "partials/navbars/manager.html";

			default:
				return null;
		}
	}
}

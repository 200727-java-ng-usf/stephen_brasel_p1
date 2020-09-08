package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Controllers handle the business logic of an endpoint
 */
public class LoginController {

	public static String login(HttpServletRequest req){

		/*
		You may want to implement route guarding here for your endpoints
		You could use filters (middleware) to do this.

		For example, you may want to make sure only ADMINS can access admin endpoints.
		 */

		//ensure that the method is a POST http method, else send them back to the login page.
		if(!req.getMethod().equals("POST")){
			return "html/badlogin.html";
		}

		String username = req.getParameter(("username"));
		String password = req.getParameter(("password"));

		if(!(username.equals("cheese") && password.equals("louise"))){
			return "/api/wrongCredits";
		} else{
			/*
			for YOUR project, you won't actually hardcore "cheese" and "louise" you'll go to

			 */
			req.getSession().setAttribute("loggedUsername", username);
			req.getSession().setAttribute("loggedPassword", password);
			return "/api/home";
		}

//		return "/html/login.html";
	}
}

package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.Credentials;
import com.revature.models.AppUser;
import com.revature.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controllers handle the business logic of an endpoint
 */
public class LoginController {
//	private static final UserService userService = new UserService();

	public static String login(HttpServletRequest req) {

		return "partials/login.html";
//		/*
//		You may want to implement route guarding here for your endpoints
//		You could use filters (middleware) to do this.
//
//		For example, you may want to make sure only ADMINS can access admin endpoints.
//		 */
//
//		//ensure that the method is a POST http method, else send them back to the login page.
//		if(!req.getMethod().equals("POST")){
//			return "partials/login.html";
//		}
//		ObjectMapper mapper = new ObjectMapper();
////
////		System.out.println(req.getRequestURI());
//		Credentials creds = new Credentials(req.getParameter("username"), req.getParameter("password"));
//		AppUser authUser = userService.authenticate(creds);
//
//		if (authUser != null) {
//			HttpSession session = req.getSession();
//			session.setAttribute("user-role", authUser.getRole().toString());
//			return "/partials/home.html";
//		} else {
//			return "partials/login.html";
//		}

//		return "/html/login.html";
	}
}

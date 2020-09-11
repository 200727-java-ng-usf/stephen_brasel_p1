package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class HomeController {
	public static String home(HttpServletRequest req){
		String principal = (String) req.getSession().getAttribute("principal");
		if(principal == null || principal.equals("")){
			return "partials/login.html";
		}
		return "partials/home.html";
//		return "/html/home.html";
	}
}

package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class RegisterController {
	public static String register(HttpServletRequest req){
		return "partials/user_register.html";
//		return "/html/home.html";
	}
}

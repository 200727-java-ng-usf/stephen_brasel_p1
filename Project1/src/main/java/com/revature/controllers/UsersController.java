package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class UsersController {
	public static String users(HttpServletRequest req){
		return "partials/users.html";
	}
	public static String update(HttpServletRequest req){
		return "partials/user_update.html";
	}
	public static String register(HttpServletRequest req){
		return "partials/user_register.html";
	}
}

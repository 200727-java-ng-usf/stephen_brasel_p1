package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class UsersController {
	public static String users(HttpServletRequest req){
		return "partials/users.html";
	}
}

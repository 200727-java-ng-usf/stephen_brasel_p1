package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class ProfileController {
	public static String profile(HttpServletRequest req){
		return "partials/profile.html";
	}
}

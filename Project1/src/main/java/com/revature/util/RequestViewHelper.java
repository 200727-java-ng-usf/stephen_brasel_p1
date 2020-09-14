package com.revature.util;

import com.revature.controllers.HomeController;
import com.revature.controllers.LoginController;
import com.revature.controllers.RegisterController;

import javax.servlet.http.HttpServletRequest;

public class RequestViewHelper {

	public static String process(HttpServletRequest req){
		switch (req.getRequestURI()){
			case "/revabursement/login.view":
			case "/login.view":
				return LoginController.login(req);
//				return "partials/login.html";

			case "/revabursement/user_register.view":
			case "/user_register.view":
				return RegisterController.register(req);
//				return "partials/user_register.html";

			case "/revabursement/home.view":
			case "/home.view":
				return HomeController.home(req);
//				return "partials/home.html";

			default:
				return null;
		}
	}
}

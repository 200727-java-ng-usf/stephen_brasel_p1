package com.revature.util;

import com.revature.controllers.HomeController;
import com.revature.controllers.LoginController;

import javax.servlet.http.HttpServletRequest;

public class RequestNavHelper {

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

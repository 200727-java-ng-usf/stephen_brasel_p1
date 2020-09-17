package com.revature.util;

import com.revature.controllers.*;
import org.hibernate.sql.Update;

import javax.servlet.http.HttpServletRequest;

public class RequestViewHelper {

	public static String process(HttpServletRequest req){
		switch (req.getRequestURI()){
			case "/revabursement/login.view":
			case "/login.view":
				return LoginController.login(req);
//				return "partials/login.html";

			case "/revabursement/home.view":
			case "/home.view":
				return HomeController.home(req);
//				return "partials/home.html";

			case "/revabursement/profile.view":
			case "/profile.view":
				return ProfileController.profile(req);
//				return "partials/profile.html";

			case "/revabursement/user_register.view":
			case "/user_register.view":
				return UsersController.register(req);
//				return "partials/user_register.html";
			case "/revabursement/user_update.view":
			case "/user_update.view":
				return UsersController.update(req);

			case "/revabursement/users.view":
			case "/users.view":
			case "/revabursement/user_view.view":
			case "/user_view.view":
			case "/revabursement/user_delete.view":
			case "/user_delete.view":
				return UsersController.users(req);
//				return "partials/users.html";

			case "/revabursement/reimbursement.view":
			case "/reimbursement.view":
			case "revabursement/reimbursement_create.view":
			case "reimbursement_create.view":
			case "revabursement/reimbursement_update.view":
			case "reimbursement_update.view":
			case "revabursement/reimbursement_view.view":
			case "reimbursement_view.view":
				return ReimbursementController.reimburse(req);
//				return "partials/reimbursements.html";

			default:
				return null;
		}
	}
}

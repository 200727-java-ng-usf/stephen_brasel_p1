package com.revature.util;

import com.revature.controllers.*;
import com.revature.servlets.ViewServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Assists the <code>{@link ViewServlet}</code> in directing users to the appropriate page.
 */
public class RequestViewHelper {

	/**
	 * Processes a view-related request.
	 * @param req the <code>{@link HttpServletRequest}</code> to process
	 * @return the appropriate web page html
	 */
	public static String process(HttpServletRequest req){
		System.out.println("Processing:" + req.getRequestURI());
		switch (req.getRequestURI()){
			case "/revabursement/login.view":
			case "/login.view":
				return LoginController.login(req);

			case "/revabursement/home.view":
			case "/home.view":
				return HomeController.home(req);

			case "/revabursement/profile.view":
			case "/profile.view":
				return ProfileController.profile(req);

			case "/revabursement/user_register.view":
			case "/user_register.view":
				return UsersController.register(req);
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

			case "/revabursement/reimbursement_create.view":
			case "/reimbursement_create.view":
				return ReimbursementController.create(req);
			case "/revabursement/reimbursement_update.view":
			case "/reimbursement_update.view":
				return ReimbursementController.update(req);
			case "/revabursement/reimbursement.view":
			case "/reimbursement.view":
			case "/revabursement/reimbursement_view.view":
			case "/reimbursement_view.view":
			case "/revabursement/reimbursement_delete.view":
			case "/reimbursement_delete.view":
				return ReimbursementController.reimburse(req);

			default:
				return null;
		}
	}
}

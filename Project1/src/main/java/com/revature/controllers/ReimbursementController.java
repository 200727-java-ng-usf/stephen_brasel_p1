package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class ReimbursementController {
	public static String reimburse(HttpServletRequest req){
		return "partials/reimbursements.html";
	}
	public static String create(HttpServletRequest req){
		return "partials/reimbursement_create.html";
	}
	public static String update(HttpServletRequest req){
		return "partials/reimbursement_update.html";
	}
}

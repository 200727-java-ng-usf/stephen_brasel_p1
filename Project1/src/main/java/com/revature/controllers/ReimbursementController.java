package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class ReimbursementController {
	public static String reimburse(HttpServletRequest req){
		return "partials/reimbursements.html";
	}
	public static String submit(HttpServletRequest req){
		return "partials/reimbursement_submit.html";
	}
	public static String update(HttpServletRequest req){
		return "partials/reimbursement_update.html";
	}
}

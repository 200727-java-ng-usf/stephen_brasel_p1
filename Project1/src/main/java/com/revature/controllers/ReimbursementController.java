package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a Controller that handles Reimbursement logic.
 */
public class ReimbursementController {
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return reimbursement-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a <code>{@link String}</code> URI for reimbursement-related pages.
	 */
	public static String reimburse(HttpServletRequest req){
		return "partials/reimbursements.html";
	}
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return reimbursement-creation-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a <code>{@link String}</code> URI for reimbursement-creation-related pages.
	 */
	public static String create(HttpServletRequest req){
		return "partials/reimbursement_create.html";
	}
	/**
	 * Parses an <code>{@link HttpServletRequest}</code> to return reimbursement-updating-related pages
	 * @param req and <code>{@link HttpServletRequest}</code> to be parsed
	 * @return a <code>{@link String}</code> URI for reimbursement-updating-related pages.
	 */
	public static String update(HttpServletRequest req){
		return "partials/reimbursement_update.html";
	}
}

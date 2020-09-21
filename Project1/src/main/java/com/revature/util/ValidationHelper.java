package com.revature.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.AppUser;
import com.revature.services.UserService;
import com.revature.servlets.ValidationServlet;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Assists the <code>{@link ValidationServlet}</code> in the validation process.
 */
public class ValidationHelper {

	/**
	 * An instance of the <code>{@link UserService}</code> class
	 * This handles all services related to <code>{@link AppUser}</code>.
	 */
	private final UserService userService = new UserService();

	/**
	 * Processes a validation-related request.
	 * @param req the <code>{@link HttpServletRequest}</code> to process
	 * @return true if the process completed successfully.
	 * 		This method processes email and username availability,
	 * 		If they're available, returns true.
	 */
	public boolean process(HttpServletRequest req) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		switch (req.getRequestURI()) {
			case "/revabursement/email.validate":
				String email = mapper.readValue(req.getInputStream(), String.class);
				return userService.isEmailAvailable(email);

			case "/revabursement/username.validate":
				String username = mapper.readValue(req.getInputStream(), String.class);
				return userService.isUsernameAvailable(username);

			default:
				return false;
		}

	}
}
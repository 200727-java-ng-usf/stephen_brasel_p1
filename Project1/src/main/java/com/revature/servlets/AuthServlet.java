package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;
import com.revature.dtos.ErrorResponse;
import com.revature.dtos.Principal;
import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.models.AppUser;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Serves all authentication-related RESTful states.
 */
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
	/**
	 * An instance of the <code>{@link UserService}</code> class
	 * This handles all services related to <code>{@link AppUser}</code>s.
	 */
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
		resp.setStatus(204);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter respWriter = resp.getWriter();
		resp.setContentType("application/json");

		try{

			// Use Jackson to read the request body and map the provided JSON to a Java POJO
			Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);
			AppUser authUser = userService.authenticate(creds);
			Principal principal = new Principal(authUser);


			HttpSession session = req.getSession();
			session.setAttribute("principal", principal.stringify());

			String principalJSON = mapper.writeValueAsString(principal);
			respWriter.write(principalJSON);

			resp.setStatus(200);

		} catch (MismatchedInputException | InvalidRequestException e){
			resp.setStatus(400); // 400 = BAD REQUEST

			ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed credentials object found in request");
			respWriter.write(mapper.writeValueAsString(err));

		} catch (AuthenticationException e){
			resp.setStatus(401); // 400 = BAD REQUEST

			ErrorResponse err = new ErrorResponse(401, e.getMessage());
			respWriter.write(mapper.writeValueAsString(err));

		} catch(Exception e) {

			e.printStackTrace();
			resp.setStatus(500); // 500 = INTERNAL SERVER ERRORS

			ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad...");
			respWriter.write(mapper.writeValueAsString(err));
		}

	}
}

package com.revature.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.ErrorResponse;
import com.revature.dtos.Principal;
import com.revature.dtos.UserDto;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.AppUser;
import com.revature.models.Role;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter respWriter = resp.getWriter();
		resp.setContentType("application/json");

		ErrorResponse err = authorize(req, new ArrayList<>(Collections.singletonList("Admin")));
		if (err.getStatus() != 202) {
			respWriter.write(mapper.writeValueAsString(err));
			resp.setStatus(err.getStatus());
			return; // necessary so that we do not continue with the rest of this method's logic.
		}
		try {
			String idParam = req.getParameter("id");
			if (idParam != null) {
				int id = Integer.parseInt(idParam);
				AppUser user = userService.getUserById(id);
				String userJSON = mapper.writeValueAsString(user);
				respWriter.write(userJSON);
				resp.setStatus(200);
			} else {
				List<AppUser> users = userService.getAllUser();
				String usersJSON = mapper.writeValueAsString(users);
				respWriter.write(usersJSON);
				resp.setStatus(200); // not required, 200 by default so long as no exceptions/errors are thrown
			}
		} catch (ResourceNotFoundException rnfe) {
			resp.setStatus(404);

			err = new ErrorResponse(404, rnfe.getMessage());
			respWriter.write(mapper.writeValueAsString(err));
		} catch (NumberFormatException | InvalidRequestException e) {
			resp.setStatus(400); // BAD REQUEST

			err = new ErrorResponse(400, "Malformed user id parameter value provided");
			respWriter.write(mapper.writeValueAsString(err));
		} catch (Exception e) {

			e.printStackTrace();
			resp.setStatus(500); // 500 = INTERNAL SERVER ERRORS

			err = new ErrorResponse(500, " It's not you, it's us. Our bad...");
			respWriter.write(mapper.writeValueAsString(err));
		}
	}

	private static ErrorResponse authorize(HttpServletRequest req, ArrayList<String> RolesAccepted) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String principalJSON = (String) req.getSession().getAttribute("principal");
		System.out.println(principalJSON);

		if (principalJSON == null) {
			ErrorResponse err = new ErrorResponse(401, "No principal object found on request. ");
			return err;
		}
		Principal principal = mapper.readValue(principalJSON, Principal.class);


		if (RolesAccepted.stream().noneMatch(role -> principal.getRole().equalsIgnoreCase(role))) {
			ErrorResponse err = new ErrorResponse(403, "Forbidding: Your role does not permit you to access this endpoint. ");
			return err;
		}
		return new ErrorResponse(202, "Authorized.");
	}

	/**
	 * Used to handle incoming requests to register new users for the application
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");

		ObjectMapper mapper = new ObjectMapper();
		PrintWriter respWriter = resp.getWriter();

		try {

			UserDto userDto = mapper.readValue(req.getInputStream(), UserDto.class);
			System.out.println(userDto);
			AppUser newUser = new AppUser(
					userDto.getFirstName(),
					userDto.getLastName(),
					userDto.getUsername(),
					userDto.getPassword(),
					userDto.getEmail()
			);
			System.out.println(newUser);
			newUser.setRole(Role.getByName(userDto.getRole()));
			System.out.println(newUser);
			userService.register(newUser);
			System.out.println(newUser);
			String newUserJSON = mapper.writeValueAsString(newUser);
			respWriter.write(newUserJSON);
			resp.setStatus(201); // 201 = CREATED
		} catch (MismatchedInputException me) {
			resp.setStatus(400); // 400 = BAD REQUEST

			ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed user object found in request");
			respWriter.write(mapper.writeValueAsString(err));

		} catch (Exception e) {

			e.printStackTrace();
			resp.setStatus(500); // 500 = INTERNAL SERVER ERRORS

			ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad...");
			respWriter.write(mapper.writeValueAsString(err));
		}

	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");

		ObjectMapper mapper = new ObjectMapper();
		PrintWriter respWriter = resp.getWriter();

		try {
			String activeParam = req.getHeader("activate");
			System.out.println(activeParam);
			if (activeParam != null) {
				if (Boolean.parseBoolean(activeParam)) {
					System.out.println(Boolean.parseBoolean(activeParam));
					Principal principal = mapper.readValue(req.getInputStream(), Principal.class);
					boolean result = userService.activateUserById(principal.getId());
					if (!result) {
						throw new ResourcePersistenceException();
					} else {
						resp.setStatus(204); // 204
					}
				}
			} else {

				UserDto userDto = mapper.readValue(req.getInputStream(), UserDto.class);
				System.out.println(userDto);
				AppUser updatedUser = new AppUser(
						userDto.getId(),
						userDto.getFirstName(),
						userDto.getLastName(),
						userDto.getUsername(),
						userDto.getPassword(),
						userDto.getEmail(),
						Role.getByName(userDto.getRole())
				);
				System.out.println(updatedUser);
//				userService.updateUser(updatedUser);
				System.out.println(updatedUser);
				String updatedUserJSON = mapper.writeValueAsString(updatedUser);
				respWriter.write(updatedUserJSON);
				resp.setStatus(200); // 200 = SUCCESS
			}
		} catch (MismatchedInputException me) {
			me.printStackTrace();
			resp.setStatus(400); // 400 = BAD REQUEST

			ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed user object found in request\n" + me);
			respWriter.write(mapper.writeValueAsString(err));

		} catch (Exception e) {

			e.printStackTrace();
			resp.setStatus(500); // 500 = INTERNAL SERVER ERRORS

			ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad...");
			respWriter.write(mapper.writeValueAsString(err));
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");

		ObjectMapper mapper = new ObjectMapper();
		PrintWriter respWriter = resp.getWriter();

		try {
			Principal principal = mapper.readValue(req.getInputStream(), Principal.class);
			boolean result = userService.deactivateUserById(principal.getId());
			if (!result) {
				throw new ResourcePersistenceException();
			} else {
				resp.setStatus(204); // 204
			}
		} catch (MismatchedInputException me) {
			me.printStackTrace();
			resp.setStatus(400); // 400 = BAD REQUEST

			ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed user object found in request\n" + me);
			respWriter.write(mapper.writeValueAsString(err));

		} catch (Exception e) {

			e.printStackTrace();
			resp.setStatus(500); // 500 = INTERNAL SERVER ERRORS

			ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad...");
			respWriter.write(mapper.writeValueAsString(err));
		}
	}
}

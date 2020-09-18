package com.revature.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.*;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.*;
import com.revature.services.ReimbursementService;
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
import java.util.List;

@WebServlet("/reimbursements/*")
public class ReimbursementServlet extends HttpServlet {

	private final ReimbursementService reimbursementService = new ReimbursementService();
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		PrintWriter respWriter = resp.getWriter();
		ErrorResponse err;
		resp.setContentType("application/json");

		try {
			//Always be able to get your own reimbursements.
			String authorParam = req.getParameter("author");
			String idParam = req.getParameter("id");
			String principalJSON = (String) req.getSession().getAttribute("principal");
			System.out.println(principalJSON);
			if (principalJSON == null) {
				err = new ErrorResponse(401, "No principal object found on request. ");
				respWriter.write(mapper.writeValueAsString(err));
				return;
			}
			Principal principal = mapper.readValue(principalJSON, Principal.class);
			err = authorize(req, new ArrayList<>(Arrays.asList("Admin", "Manager")));
			if (err.getStatus() == 202) { // authorized
				if (authorParam != null) {
					// GET ALL REIMBURSEMENTS FOR ONE PERSON
					int id = Integer.parseInt(authorParam);
					List<Reimbursement> reimbursement = reimbursementService.getReimbursementByAuthor(id);
					String userJSON = mapper.writeValueAsString(reimbursement);
					respWriter.write(userJSON);
					resp.setStatus(200);
				} else if (idParam != null) {
					// GET ONE REIMBURSEMENT BY IT'S ID
					int id = Integer.parseInt(idParam);
					Reimbursement reimbursement = reimbursementService.getReimbursementById(id);
					String userJSON = mapper.writeValueAsString(reimbursement);
					respWriter.write(userJSON);
					resp.setStatus(200);
				} else {
					// GET ALL THE REIMBURSEMENTS
					List<Reimbursement> reimbursements = reimbursementService.getAllReimbursements();
					String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
					respWriter.write(reimbursementsJSON);
					resp.setStatus(200); // not required, 200 by default so long as no exceptions/errors are thrown
				}
			} else {
				if (authorParam != null || idParam != null) {
					if (principal.getId() == Integer.parseInt(authorParam)) {
						// GET ALL REIMBURSEMENTS FOR ME
						int id = Integer.parseInt(authorParam);
						List<Reimbursement> reimbursement = reimbursementService.getReimbursementByAuthor(id);
						String userJSON = mapper.writeValueAsString(reimbursement);
						respWriter.write(userJSON);
						resp.setStatus(200);
					} else if (principal.getId() == Integer.parseInt(idParam)) {
						// GET ONE OF MY REIMBURSEMENTS
						int id = Integer.parseInt(idParam);
						List<Reimbursement> reimbursement = reimbursementService.getReimbursementByAuthor(id);
						String userJSON = mapper.writeValueAsString(reimbursement);
						respWriter.write(userJSON);
						resp.setStatus(200);
					}
				} else {
					respWriter.write(mapper.writeValueAsString(err));
					resp.setStatus(err.getStatus());
				}
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
			ErrorResponse err = new ErrorResponse(403, "Forbidden: Your role does not permit you to access this endpoint. ");
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

			ReimbDto reimbDto = mapper.readValue(req.getInputStream(), ReimbDto.class);
			System.out.println(reimbDto);
			Reimbursement newReimbursement = new Reimbursement(
					reimbDto.getAmount(),
					reimbDto.getDescription(),
					reimbDto.getReceiptURI(),
					userService.getUserByUsername(reimbDto.getAuthor()),
					ReimbursementType.getByName(reimbDto.getType())
			);
			System.out.println(newReimbursement);
			reimbursementService.submit(newReimbursement);
			System.out.println(newReimbursement);
			String newReimbursementJSON = mapper.writeValueAsString(newReimbursement);
			respWriter.write(newReimbursementJSON);
			resp.setStatus(201); // 201 = CREATED
		} catch (MismatchedInputException me) {
			resp.setStatus(400); // 400 = BAD REQUEST

			ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed reimbursement object found in request");
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
		ErrorResponse err;
		String principalJSON = (String) req.getSession().getAttribute("principal");
		System.out.println(principalJSON);
		if (principalJSON == null) {
			err = new ErrorResponse(401, "No principal object found on request. ");
			respWriter.write(mapper.writeValueAsString(err));
			return;
		}
		Principal principal = mapper.readValue(principalJSON, Principal.class);

		try {
			ReimbursementDto upReDt = mapper.readValue(req.getInputStream(), ReimbursementDto.class);
			System.out.println(upReDt);
			Reimbursement updatedReimbursement = new Reimbursement(
					upReDt.getId(),
					upReDt.getAmount(),
					upReDt.getSubmitted(),
					upReDt.getResolved(),
					upReDt.getDescription(),
					upReDt.getReceiptURI(),
					userService.getUserByUsername(upReDt.getAuthor()),
					null,
					ReimbursementStatus.getByName(upReDt.getReimb_status_id()),
					ReimbursementType.getByName(upReDt.getReimb_type_id())
			);
			System.out.println(updatedReimbursement);
			if (ReimbursementStatus.PENDING != updatedReimbursement.getReimb_status_id()) {
				AppUser resolver = userService.getUserById(principal.getId());
				updatedReimbursement.setResolver(resolver);
				reimbursementService.updateReimbursement(updatedReimbursement);
				System.out.println(updatedReimbursement);
				String updatedReimbursementJSON = mapper.writeValueAsString(updatedReimbursement);
				respWriter.write(updatedReimbursementJSON);
				resp.setStatus(200); // 200 = SUCCESS
			} else if (Role.getByName(principal.getRole()) == Role.EMPLOYEE) {
				reimbursementService.updateReimbursement(updatedReimbursement);
				System.out.println(updatedReimbursement);
				String updatedReimbursementJSON = mapper.writeValueAsString(updatedReimbursement);
				respWriter.write(updatedReimbursementJSON);
				resp.setStatus(200); // 200 = SUCCESS
			}
		} catch (MismatchedInputException me) {
			me.printStackTrace();
			resp.setStatus(400); // 400 = BAD REQUEST

			err = new ErrorResponse(400, "Bad Request: Malformed user object found in request\n" + me);
			respWriter.write(mapper.writeValueAsString(err));

		} catch (Exception e) {

			e.printStackTrace();
			resp.setStatus(500); // 500 = INTERNAL SERVER ERRORS

			err = new ErrorResponse(500, "It's not you, it's us. Our bad...");
			respWriter.write(mapper.writeValueAsString(err));
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");

		ObjectMapper mapper = new ObjectMapper();
		PrintWriter respWriter = resp.getWriter();

		try {
			IntegerDto id = mapper.readValue(req.getInputStream(), IntegerDto.class);
			boolean result = reimbursementService.deleteReimbursementById(id.getId());
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

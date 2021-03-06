package com.revature.servlets;

import com.revature.util.RequestNavHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Serves all navigation-bar-related RESTful states.
 */
@WebServlet("*.nav")
public class NavServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(RequestNavHelper.process(req)).forward(req, resp);
	}

}

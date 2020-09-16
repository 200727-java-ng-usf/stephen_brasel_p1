package com.revature.servlets;

import com.revature.util.RequestViewHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.view")
public class ViewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String processedRequest = RequestViewHelper.process(req);
		RequestDispatcher requestDispatcher = req.getRequestDispatcher(processedRequest);
		requestDispatcher.forward(req, resp);
	}

}

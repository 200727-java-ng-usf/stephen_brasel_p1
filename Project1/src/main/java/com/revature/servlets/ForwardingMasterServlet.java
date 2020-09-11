package com.revature.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
		urlPatterns = {"/api/*", "/MasterServlet"},
		loadOnStartup = 0)
public class ForwardingMasterServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(com.revature.servlets.RequestHelper.process(req)).forward(req,resp);
//		resp.getWriter().write("in get");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(com.revature.servlets.RequestHelper.process(req)).forward(req,resp);
//		resp.getWriter().write("in post");
	}
}

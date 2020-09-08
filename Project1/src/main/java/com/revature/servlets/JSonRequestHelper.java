package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JSonRequestHelper {

	public static void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println(req.getRequestURI());

		//prune ending of URI string

//		switch (req.getRequestURI()){
//			case "/revabursement/json/dannyboy":
//				DannyBoyController.dannyFinder(req,resp);
//				break;
//			case "/revabursement/json/mars":
//				MarsController.marsFinder(req,resp);
//				break;
//			default:
//				SuperVillain nullVillain = new SuperVillain(null, null, 0);
//				resp.getWriter().write(new ObjectMapper().writeValueAsString(nullVillain));
//				break;
//		}
	}
}

package com.revature.servlets;

import com.revature.controllers.HomeController;
import com.revature.controllers.LoginController;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper {

	/*

	JSON means JavaScript Object Notation
	- a JSON is a data format used to transfer data in a platform independet way
	- a JSON consists of key-value pairs (when it is an object)
	-	>an array IS a valid JSON so the "key-value pair" comment may not
			make as much sense in this case

	JSON =/= JavaScript Object

	(background: in JS	{} is an object
						[] is an array
						"" is a string

	examples of JSON
	{
		"child1":	{
						"grandchild1": 35,
						"grandchild2": "barnacles",
						"grandchild3": "hello"
					},
		"child2": 50
	}

	ANOTHER example of a JSON
	[35, "barnacles", {"grandchild2": 77}]
		BASICALLY, an array is a valid JSON

	example of XML
	<xml>
		<child>
			<grandhchild> 35</grandhchild>
			<grandhchild> Barnacles</grandhchild>
			<grandhchild> Hello</grandhchild>
		</child>
		<child> 50</child>
	</xml>
	 */

	public static String process(HttpServletRequest req){

		String ret = ("THIS is the current URI active: " + req.getRequestURI());

		switch(req.getRequestURI()){
			case "/revabursement/html/login": case "/revabursement/api/login":
				System.out.println("in login case");
//				return "/html/login.html";
				return LoginController.login(req);
			case "/revabursement/html/home": case "/revabursement/api/home":
				System.out.println("in home case");
//				NOT modularized
//				return "/html/home.html";

				//modularized
				return HomeController.home(req);
			default:
				System.out.println("in default");
				return "/html/badlogin.html";
		}
//		System.out.println(ret);
//		return "/html/home.html";
	}
}

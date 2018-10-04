package com.avaya.AAAMIADBWebService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/*
 * This class is needed if you are trying to write a JAX-RS based web service. You can enhance this as needed for the application.
 * 
 * For applications which provide call related features only and web service is not required, remove this class.
 * 
 */

@Path("/myResource")
public class MyResource 
{
	@GET
	public String doGet() 
	{
		return "Hello World";
	}
}
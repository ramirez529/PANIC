package com.avaya.AAAMIADBWebService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
/*
 * This class is needed if you are trying to write a JAX-RS based web service. You can enhance this as needed for the application.
 * 
 * For applications which provide call related features only and web service is not required, remove this class.
 * 
 */
@ApplicationPath("/ws")
public class MyApplication extends Application 
{
	//Don't need to implement getClasses
}

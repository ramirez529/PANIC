package com.avaya.AAAMIADBWebService;

import com.avaya.collaboration.call.Call;
import com.avaya.collaboration.call.CallListenerAbstract;
import com.avaya.collaboration.call.TheCallListener;
import com.avaya.collaboration.util.logger.Logger;

/*
 * This class is needed if an application with call features is written.
 * If you have an application which is doing only HTTP related operations, remove this class from the project.
 * 
 * For HTTP only application, also remove the sip.xml from src/main/java/webapp/WEB-INF and blank out details from
 * CARRule.xml. Look at the files for more details.
 * 
 */
@TheCallListener
public class MyCallListener extends CallListenerAbstract 
{
    private static Logger logger = Logger.getLogger(MyCallListener.class);
   
	public MyCallListener() 
	{
	}
	
	@Override
	public final void callIntercepted(final Call call) 
	{
        logger.fine("Entered callIntercepted.");
        call.allow();
	}
}

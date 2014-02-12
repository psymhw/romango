package services;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import data.User;

/*
 * This interceptor provides authentication for the secure actions of the application.
 * It does two things.  First, it checks the session scope map to see if there's user 
 * object present, which indicates that the current user is already logged in.  If this
 * object is not present, the interceptor alters the workflow of the request by returning 
 * a login control string that causes the request to forward to the login page.
 * 
 * If the user object is present in the session map, then the interceptor injects the user
 * object into the action by calling the setUser method, and then allows the processing of 
 * the request to continue.  
 */

public class AuthenticationInterceptor implements Interceptor {

	public void destroy() {	}

	public void init() {	}

	public String intercept( ActionInvocation actionInvocation ) throws Exception 
	{

		Map session = actionInvocation.getInvocationContext().getSession();
		
		String userName = (String) session.get( "logged-in" );
		
		
		if (userName == null) 
		{
		  //System.out.println("Interceptor, username is null");
		  return Action.LOGIN;
		} 
		else 
		{
		    Action action = ( Action ) actionInvocation.getAction();
		   // System.out.println("Logged in: interceptor");
		    return actionInvocation.invoke();
		}

	}
	
	
}

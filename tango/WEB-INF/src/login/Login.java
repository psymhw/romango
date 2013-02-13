package login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.WebErrorOutput;

import db.MemberDb;

public class Login extends HttpServlet 
{
	boolean debug=false;
	public void service(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException
    {
	  if (debug) System.out.println("LOGIN SERVLET");
	  String errorMessage="";
	  boolean success=true;
	  String server=request.getServerName();
   	  if (server==null) server="remote";
      String mode=(String)request.getParameter("mode");
      HttpSession session = request.getSession();
      String user = (String)session.getAttribute("user");
      
      if (user!=null)
      {
    	if (debug)  System.out.println("LOGIN SERVLET - USER NOT NULL");
    	session.removeAttribute("user");
    	session.removeAttribute("UserData");
    	String nextJSP = "/events";
	    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	    dispatcher.forward(request,response);
	    return;
      }
      else
      {
    	if (debug)  System.out.println("LOGIN SERVLET - USER IS NULL");
    	String username=(String)request.getParameter("username");
    	String password=(String)request.getParameter("password");
    	
    	if (debug)   System.out.println("LOGIN SERVLET - username: "+username);
    	if (debug)   System.out.println("LOGIN SERVLET - password: "+password);
    	if (username==null) {success=false; errorMessage+="<br>* Username required.";} else if (username.length()<2) {success=false;errorMessage+="<br>* Username required.";}
    	if (password==null) {success=false; errorMessage+="<br>* Password required."; } else if (password.length()<2) {success=false;errorMessage+="<br>* Password required.";}
    	MemberDb mdb = null;
    	if (success)
    	{
    	  if (debug)  System.out.println("LOGIN SERVLET - FORM VARS OK");
    	  try
    	  {
    	    mdb = MemberDb.getMember(username, server);
    	  }  catch (Exception e) {
    		  if (debug)  System.out.println("LOGIN SERVLET - MEMBER LOOKUP EXCEPTION");
    		  response.setContentType("text/html");
    	      ServletOutputStream out = response.getOutputStream();

    	      response.setContentType("text/html");
    	      new WebErrorOutput(e, out);
    	      return;
    	  }
    	  
    	  if (mdb==null) { success=false; errorMessage+="<br>* Username not found."; }
    	  else if (mdb.getPassword()==null) { success=false; errorMessage+="<br>* No password in member record."; } // this should not happen
    	  else if (!mdb.getPassword().equals(password)) { success=false; errorMessage+="<br>* incorrect password."; }
    	  if (mdb.getActive()==0) { success=false; errorMessage+="<br>* your account is suspended."; }
    	  
    	  if (success)
    	  {
    		 if (debug)   System.out.println("LOGIN SERVLET - LOGIN SUCCESSFUL");
    		 session.setAttribute("user", username);
    		 session.setAttribute("UserData", mdb);
    	     String nextJSP = "/events";
    		 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
    		 dispatcher.forward(request,response);  
    		 return;
    	  }
    	   	  
    	  
    	}
    	 if (debug) System.out.println("LOGIN SERVLET - LOGIN FAILED");
    	 session.removeAttribute("user");  // just making sure
    	 session.removeAttribute("UserData");
    	 request.setAttribute("ErrorMessage", errorMessage);
		 String nextJSP = "/login/loginErrorMain.jsp";
 		 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
 		 dispatcher.forward(request,response);  
 		 return;
      }
      
    }
}

package instructors;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import model.WebErrorOutput;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import serv.Tester;


import db.MemberDb;


public class Instructors extends HttpServlet 
{
  private static final long serialVersionUID = 1L;
  
  
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
	String server=request.getServerName();
	if (server==null) server="remote";
	String mode = (String)request.getParameter("mode");
	if (mode==null) mode="list";
	if ("list".equals(mode))
	{
	  ArrayList members=null;
	  try
	  {	
        members = MemberDb.getMembers("instructor = 1 and active = 1 order by last_name", server);
	  } catch (Exception e)
	  {
	    response.setContentType("text/html");
  	    ServletOutputStream out = response.getOutputStream();
  	    response.setContentType("text/html");
  	    new WebErrorOutput(e, out);
  	    return; 
	  }
	  
	  request.setAttribute("Instructors", members);
	  String nextJSP = "/instructors/instructorsMain.jsp";
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
      dispatcher.forward(request,response);
      return;
	}
	
	// Security check
	HttpSession session = request.getSession();
	MemberDb ud = (MemberDb)session.getAttribute("UserData");
	int uid=0;
	if (ud!=null) uid=ud.getId();
	String passedId = (String)request.getParameter("id");
	int id = -1;
	try { id=Integer.parseInt(passedId); } catch(Exception e) {}
	if (!(uid==id)) 
	{
	  response.setContentType("text/html");
	  ServletOutputStream out = response.getOutputStream();
	  response.setContentType("text/html");
	  out.println("Instructor edit: Access Denied");
	  return;
	}
	
	// Anything below here had to pass the security check
	
	if ("edit".equals(mode))
	{
		
		MemberDb mdb =null; 
    	try {
    		mdb=MemberDb.getMember(uid, server);
    	  } catch (Exception e) {
    		  response.setContentType("text/html");
    	      ServletOutputStream out = response.getOutputStream();

    	      response.setContentType("text/html");
    	      new WebErrorOutput(e, out);
    	      return;
    	  }
    	if (mdb!=null)
    	{	
    	  mdb.setMode("update");
    	}
    	request.setAttribute("MemberDb", mdb);
    	String nextJSP = "/instructors/memberEditMain.jsp";
	    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	    dispatcher.forward(request,response);
	    return;
	}
	
	if ("update".equals(mode))
    {
  	 
  	  
  	  try {
  		  // instructors can't change everything
  		    MemberDb mdb = MemberDb.getMember(uid, server);
  		    mdb.setLast_name(request.getParameter("last_name"));
  		    mdb.setFirst_name(request.getParameter("first_name"));
  		    mdb.setEmail(request.getParameter("email"));
  		    mdb.setPassword(request.getParameter("password"));
  		    mdb.setDescription(getSqlString(request.getParameter("description")));
  		    mdb.setCaption(getSqlString(request.getParameter("caption")));
  	        mdb.update(server);
  	        request.setAttribute("MemberDb", mdb);
  	        String nextJSP = "/instructors?mode=list";
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	        dispatcher.forward(request,response);
	        return;
  	  } catch (Exception e) {
  		  response.setContentType("text/html");
  	      ServletOutputStream out = response.getOutputStream();

  	      response.setContentType("text/html");
  	      new WebErrorOutput(e, out);
  	      return;
  	  }
  	 
    }
  }
  
  private static String getSqlString(String inStr)
  {
  	if (inStr==null) return null;
  	return inStr.replace("'", "\\'");
  }
} 


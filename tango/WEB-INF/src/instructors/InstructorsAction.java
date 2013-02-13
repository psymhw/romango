package instructors;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.WebErrorOutput;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.MemberDb;

//import db.ImageRefDb;
//import db.NotesDb;

public class InstructorsAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
  HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward list = mapping.findForward("list");   
    ActionForward listRedirect = mapping.findForward("listRedirect");  
    ActionForward edit = mapping.findForward("edit");   
    ActionForward accessDenied = mapping.findForward("accessDenied");   
    ActionForward unsupportedForward = mapping.findForward("unsupportedForward");   
    
   // String server=request.getServerName();
	//if (server==null) server="remote";
	String mode = (String)request.getParameter("mode");
	if (mode==null) mode="list";
	if ("list".equals(mode))
	{
	  ArrayList members=null;
	  try
	  {	
        members = MemberDb.getMembers("instructor = 1 and active = 1 order by last_name", null);
	  } catch (Exception e)
	  {
	    e.printStackTrace();
	  }
	  
	 request.setAttribute("Instructors", members);
	 // String nextJSP = "/instructors/instructorsMain.jsp";
     // RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
     // dispatcher.forward(request,response);
      return list;
	}
	
	// Security check
	HttpSession session = request.getSession();
	MemberDb ud = (MemberDb)session.getAttribute("UserData");
	int uid=0;
	if (ud!=null) uid=ud.getId();
	String passedId = (String)request.getParameter("id");
	int id = -1;
	try { id=Integer.parseInt(passedId); } catch(Exception e) {}
	if (ud.getAdmin()!=1) 
	{	
	  if (!(uid==id)) return accessDenied;
	}
	// Anything below here had to pass the security check
	
	if ("edit".equals(mode))
	{
		
		MemberDb mdb =null; 
    	try {
    		mdb=MemberDb.getMember(id, (String)null);
    	  } catch (Exception e) {
    		  e.printStackTrace();
    	  }
    	if (mdb!=null)
    	{	
    	  mdb.setMode("update");
    	}
    	request.setAttribute("MemberDb", mdb);
    	//String nextJSP = "/instructors/memberEditMain.jsp";
	   // RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	   // dispatcher.forward(request,response);
	    return edit;
	}
	
	if ("update".equals(mode))
    {
  	 
  	  
  	  try {
  		  // instructors can't change everything
  		    MemberDb mdb = MemberDb.getMember(id, (String)null);
  		    mdb.setLast_name(request.getParameter("last_name"));
  		    mdb.setFirst_name(request.getParameter("first_name"));
  		    mdb.setEmail(request.getParameter("email"));
  		    mdb.setPassword(request.getParameter("password"));
  		    mdb.setDescription(getSqlString(request.getParameter("description")));
  		    mdb.setCaption(getSqlString(request.getParameter("caption")));
  	        mdb.update((String)null);
  	      
	        return listRedirect;
  	  } catch (Exception e) {
  		  e.printStackTrace();
  	  }
  	 
    }
	
	return unsupportedForward;
  }
  
  private static String getSqlString(String inStr)
  {
  	if (inStr==null) return null;
  	return inStr.replace("'", "\\'");
  }
} 


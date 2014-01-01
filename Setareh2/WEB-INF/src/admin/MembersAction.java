package admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

//import model.WebErrorOutput;

import db.MemberDb;



public class MembersAction extends Action
{
	private static final long serialVersionUID = 2537503576794692389L;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
	  ActionForward list = mapping.findForward("list");   
	  ActionForward edit = mapping.findForward("edit");  
	  ActionForward show = mapping.findForward("show"); 
	  ActionForward unsupportedForward = mapping.findForward("unsupportedForward");
	  
	  ActionForward accessDenied = mapping.findForward("accessDenied");   
	  
	  
	  String server=request.getServerName();
	  if (server==null) server="remote";
	  HttpSession session = request.getSession();
	  String error="";
	  MemberDb userData = (MemberDb)session.getAttribute("UserData");
	  boolean auth=true;
	  if (userData==null) { auth=false; error="<br>* UserData not found"; }
	  else if (userData.getAdmin()==0) { auth=false; error+="<br>* User not an admin"; }
	  if (!auth)
	  {
	     return accessDenied;
	  }
	  
	  
	  
      String mode=(String)request.getParameter("mode");
      if (mode==null) mode="list";
      
      //System.out.println("admin MwmbersAction: mode: "+mode);
      
      if ("list".equals(mode))
      {  
    	  ArrayList members = null;
    	  try {
    		   members = listMembers();
    		  request.setAttribute("MemberList", members);
      	  } catch (Exception e) {
      		 e.printStackTrace();
      	  }
      	return list;
      }
      
     
      
      if ("newMember".equals(mode))
      {
    	MemberDb mdb = new MemberDb();
    	mdb.setMode("insert");
    	
    	request.setAttribute("MemberDb", mdb);
    	//String nextJSP = "/admin/members/memberEditMain.jsp";
	   // RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	   // dispatcher.forward(request,response);
	    return edit;
      }
      if ("edit".equals(mode))
      {
    	MemberDb mdb =null; 
    	try {
    		int tint =0;
    		try { tint=Integer.parseInt(request.getParameter("id")); } catch(Exception e) {}
    		mdb=MemberDb.getMember(tint, server);
    	  } catch (Exception e) {
    		 e.printStackTrace();
    	  }
    	if (mdb!=null)
    	{	
    	  mdb.setMode("update");
    	}
    	request.setAttribute("MemberDb", mdb);
    	//String nextJSP = "/admin/members/memberEditMain.jsp";
	   // RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	   // dispatcher.forward(request,response);
	    return edit;
      }
      
      if ("show".equals(mode))
      {
    	MemberDb mdb =null; 
    	try {
    		int tint =0;
    		try { tint=Integer.parseInt(request.getParameter("id")); } catch(Exception e) {}
    		mdb=MemberDb.getMember(tint, server);
    	  } catch (Exception e) {
    		 e.printStackTrace();
    	  }
    	if (mdb!=null)
    	{	
    	  mdb.setMode("update");
    	}
    	request.setAttribute("MemberDb", mdb);
    	//String nextJSP = "/admin/members/memberShowMain.jsp";
	    //RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	   // dispatcher.forward(request,response);
	    return show;
      }
      
      if ("insert".equals(mode))
      {
    	  MemberDb mdb = new MemberDb(request);
    		  
    	 
    	  
    	  try {
    	    mdb.insert(server);
    	    ArrayList members = null;
      	  try {
      		   members = listMembers();
      		  request.setAttribute("MemberList", members);
        	  } catch (Exception e) {
        		 e.printStackTrace();
        	  }
        	return list;
    	  } catch (Exception e) {
    		 e.printStackTrace();
    	  }
    	  
      }
      
      if ("update".equals(mode))
      {
    	  MemberDb mdb = new MemberDb(request);
    	 
    	 
    	  
    	  try {
    	    mdb.update(server);
    	    ArrayList members = null;
      	  try {
      		   members = listMembers();
      		  request.setAttribute("MemberList", members);
        	  } catch (Exception e) {
        		 e.printStackTrace();
        	  }
        	return list;
    	  } catch (Exception e) {
    		  e.printStackTrace();
    	  }
    	 
      }
      
      return unsupportedForward;
      
      /*
      response.setContentType("text/html");
      ServletOutputStream out = response.getOutputStream();

      response.setContentType("text/html");
      */
    }
	
	public ArrayList listMembers() throws Exception
	{
		// String server=request.getServerName();
	   	//  if (server==null) server="remote";
		ArrayList members = null;
  	  
    	    members = MemberDb.getMembers(null, null);
    	 return members;
    //	request.setAttribute("MemberList", members);
   //   String nextJSP = "/admin/members/memberListMain.jsp";
	//    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	//    dispatcher.forward(request,response);
	}
	/*
	public void listEmails(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		 String server=request.getServerName();
	   	  if (server==null) server="remote";
		ArrayList members = null;
  	  
    	    members = MemberDb.getMembers(null, server);
    	 
    	request.setAttribute("MemberList", members);
      String nextJSP = "/admin/members/emailListBody.jsp";
	    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
	    dispatcher.forward(request,response);
	}
	*/
	public void outMember(MemberDb mdb)
	{
		System.out.println("id: "+mdb.getId());
  	  System.out.println("first_name: "+mdb.getFirst_name());
  	  System.out.println("last_name: "+mdb.getLast_name());
  	  System.out.println("email: "+mdb.getEmail());
  	  System.out.println("location_id: "+mdb.getLocation_id());
  	  System.out.println("password: "+mdb.getPassword());
  	  System.out.println("description: "+mdb.getDescription());
  	  System.out.println("admin: "+mdb.getAdminChecked());
  	  System.out.println("host: "+mdb.getHostChecked());
  	  System.out.println("instructor: "+mdb.getInstructorChecked());
	}

}

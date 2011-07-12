package test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TestAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
	  
	ActionForward list = mapping.findForward("list");   
	ActionForward show = mapping.findForward("show"); 
	
	String mode = (String)request.getParameter("mode");
	if (mode==null) mode="list";
	
	if ("list".equals(mode)) return list;
	else return show;
  }
	
}

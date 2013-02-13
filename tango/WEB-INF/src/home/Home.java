package home;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import serv.Tester;




public class Home extends HttpServlet 
{
  private static final long serialVersionUID = 1L;
  
  
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
	       
			   String someObject = "MODE 1: Some Object String";
			   request.setAttribute("SomeObject", someObject);
			   String nextJSP = "/home/home.jsp";
			   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			   dispatcher.forward(request,response);
    
  }
  
 
} 


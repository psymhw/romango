package serv;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;




public class Commander extends HttpServlet 
{
  private static final long serialVersionUID = 1L;
  
  
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
	  String mode;
	 
	  response.setContentType("text/html");
	  ServletOutputStream out = response.getOutputStream();
	 
	 response.setContentType("text/html");

	 Date startupTime = (Date)this.getServletContext().getAttribute("StartupTime");
     mode = request.getParameter("mode");
     if (mode==null) mode="home";
     String server=request.getServerName();
     if (server==null) server="remote";
    Tester tester = new Tester(out, mode, server, startupTime);
    
  }
  
 
} 


package admin;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class InitServlet extends HttpServlet 
{
  private static final long serialVersionUID = 1274527954615535502L;
  //Hashtable<String, String> acctHash = new Hashtable<String, String>();
 // Hashtable<String, String> trackHash = new Hashtable<String, String>();
 
  
  public void init(ServletConfig config) throws ServletException
  {
     super.init(config);
     InitAction ia = new InitAction();
     ia.doInit(this.getServletContext());
     System.out.println("RickDb InitServlet: setting up accounts and tracks");
  }
  
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
            
     try
     {
      
     } catch (Exception e) { e.printStackTrace();  }
   }
  
 
}

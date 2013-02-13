package serv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.util.LabelValueBean;

import db.MediaDb;

public class InitServlet extends HttpServlet 
{
 	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException
	{
	  super.init(config);
	 
	 this.getServletContext().setAttribute("StartupTime", new Date());
	 ServletContext context = this.getServletContext();
	 
	 System.out.println("\n****** TANGO ******\n");
	 setCategories(context);
	}
	
    public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
    {
      String server=request.getServerName();
   	  if (server==null) server="remote";
   	  System.out.println("init servlet - server: "+server);
     
	
	
    }
    
    void setCategories(ServletContext context)
	{
    	 PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		String category=null;
		ArrayList <LabelValueBean>categories = new ArrayList<LabelValueBean>();
		categories.add(new LabelValueBean("-- All --","-- All --"));
		try
		{
			Connection db = broker.serviceConnectionManager().getConnection();
			Statement stmt = db.createStatement();    
		  ResultSet rs = stmt.executeQuery("select distinct(category) from media"); 
		  if (rs != null) { while (rs.next()) { category=rs.getString("category"); categories.add(new LabelValueBean(category, category)); } }
		   rs.close();
		   stmt.close();
		} catch (Exception e) {	e.printStackTrace();}
		context.setAttribute("Categories", categories);
		System.out.println("init servlet - Categories set ");
		//context.setAttribute("MediaPages", getMediaPages(broker));
		
		broker.close();
	}
    
    
}

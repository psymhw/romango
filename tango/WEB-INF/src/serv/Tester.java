package serv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import model.WebErrorOutput;

import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;


import db.MysqlConnection;

public class Tester 
{
  
  public Tester(ServletOutputStream out, String mode, String runLocation, Date startupTime)
  {
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss z");
	  try {
		     out.println("<html>");
		     out.println("<body>");
		    
			 out.println("<h2>Test Servlet...</h2><br>");
			 out.println("<b>Run Location:</b> "+runLocation+". <b>Startup Time:</b> "+df.format(startupTime)+"<br><br>");
		 if ("mysqlConnect".equals(mode)) mysqlConnect(out, mode, runLocation);
			 else home(out, mode);
		    
			 out.println("</body>");
			 out.println("</html>");
		     } catch (Exception e) { new WebErrorOutput(e, out); }
		     
  }
  
  void home(ServletOutputStream out, String mode)throws Exception
  {
    
    out.println("mode = "+mode);
  }
	
 
  
  
  void mysqlConnect(ServletOutputStream out, String mode, String runLocation) throws Exception
  {
	  
	  MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	  Connection con = mysqlConnection.connect();

	    
	     
	      if(!con.isClosed())
	      {
	        out.println("Successfully connected to " +
	          "MySQL server using TCP/IP... InstructorCount = "+ getInstructorCount(con));
	      }
	      else
	    	  out.println("FAILED connect to " +
	          "MySQL server using TCP/IP...");

	      mysqlConnection.close();
	   

  }
  
  long getInstructorCount(Connection db) throws Exception
  {
    long tlong=0;
    Statement stmt = db.createStatement(); 
    
    String sql = "select * from instructor";
    
    ResultSet rs = stmt.executeQuery(sql);
    if (rs != null) { while (rs.next()) { 
    	tlong++;
    	//tlong=rs.getLong("page_id"); 
    	} }
    rs.close();
    stmt.close();
    
    return tlong;
  }
}

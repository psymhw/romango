package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;

public class MysqlConnection 
{
  Connection con = null;
  String runLocation;
  
  public MysqlConnection(String runLocation)
  {
	  this.runLocation=runLocation;
  }
  public Connection connect() throws Exception
  {  
	  String db="jdbc:mysql://localhost:3306/etango?autoReconnect=true";
	//  if ("localhost".equals(runLocation)) db="jdbc:mysql://localhost/etango";
	//  else db="jdbc:mysql://etango.db.4163272.hostedresource.com/etango";
    
		   Class.forName("com.mysql.jdbc.Driver").newInstance();
		//    con = DriverManager.getConnection(db, "etango", "tangX123");
		   con = DriverManager.getConnection(db, "rroman", "smegmafish7A");
		    return con;
		 
       
	  }
  
  public void close()
  {
	  try {
	  con.close();
	  } catch(Exception e) { e.printStackTrace(); }
  }
	  
}

package tangodj.tangodj2;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDatabase 
{
	private boolean exists=false;
	static Connection connection;
	public static final String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL ="jdbc:derby:tango_db;create=true";

	
	public CreateDatabase() throws ClassNotFoundException, SQLException, IOException
	{
	  File test = new File("tango_db");
	  if (test!=null)
	  {
		if (test.exists()) exists=true;
	  }
	  
	  if (exists) System.out.println("database found");
	  else
	  {
		Class.forName(DRIVER);
		 connection = DriverManager.getConnection(JDBC_URL);
	    connection.createStatement().execute("create table tracks(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"path varchar(300), " +
	    		"pathHash varchar(32), " +
	    		"title varchar(100), " +
	    		"artist varchar(40), " +
	    		"album varchar(100), " +
	    		"genre varchar(40), " +
	    		"comment varchar(100) " +
	    		")");
	    
	    connection.createStatement().execute("CREATE UNIQUE INDEX pathInd ON TRACKS(PATHHASH)");
	    connection.close();
	    System.out.println("database initiallized");
	  }
	}
	
	public static void main(String[] args) 
	{
	  try
	  {
		 
	    new CreateDatabase();
	  } catch (Exception e) { e.printStackTrace(); }
	}

}

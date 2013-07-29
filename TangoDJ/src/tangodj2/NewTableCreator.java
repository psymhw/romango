package tangodj2;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NewTableCreator 
{
	private boolean exists=false;
	static Connection connection;
	public static final String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL ="jdbc:derby:tango_db;create=true";

	
	public NewTableCreator() throws ClassNotFoundException, SQLException, IOException
	{
	  
		Class.forName(DRIVER);
		 connection = DriverManager.getConnection(JDBC_URL);
		 
		 /*
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
	    */
	    connection.createStatement().execute("CREATE UNIQUE INDEX pathInd ON TRACKS(PATHHASH)");
	    
	    connection.createStatement().execute("create table playlists(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"name varchar(100), " +
	    		"location varchar(100), " +
	    		"incept TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
	    		")");
	    
	    connection.close();
	    System.out.println("table created");
	  
	}
	
	public static void main(String[] args) 
	{
	  try
	  {
		 
	    new NewTableCreator();
	  } catch (Exception e) { e.printStackTrace(); }
	}

}

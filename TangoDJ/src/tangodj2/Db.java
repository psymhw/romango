package tangodj2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db 
{
	public static Connection connection;
	public static final String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL ="jdbc:derby:tango_db;create=true";
	public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=false";

	public Db()throws SQLException, ClassNotFoundException
	{
		//connect();
	
	}
	
	public static void connect()  throws SQLException, ClassNotFoundException
	{
		connect(false);
	}
	
	public static void connect(boolean create)  throws SQLException, ClassNotFoundException
	{
	  Class.forName(DRIVER);
	  if (create) connection = DriverManager.getConnection(JDBC_URL);
	  else DriverManager.getConnection(JDBC_URL2);
	}
	
	public static void disconnect() throws SQLException
	{
		connection.close();
	}
}

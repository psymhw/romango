package tangodj2.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class TrackViewer {

	public static final String SQL_STATEMENT = "select * from tracks";
	public static void main(String[] args) throws SQLException
	{
		String title;
		String album;
		String path;
		Connection connection = DriverManager.getConnection("jdbc:derby:tango_db;create=true");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(SQL_STATEMENT);
		int counter=0; 
		
		while(resultSet.next())
		{
			title=resultSet.getString("title");
			album=resultSet.getString("album");
			path= resultSet.getString("path");
		    System.out.println(counter+": "+album+" ** "+title+" ** "+path);
		    counter++;
		  
		 // for(int x=1; x<=columnCount; x++)
		//	{
		//	  str=resultSet.getString(x);
		//	  if (str.length()>19) str=str.substring(0, 19);
		//	  System.out.format("%20s", str+" | ");
		//	}
		}
		if (statement!=null) statement.close();
		if (connection!=null) connection.close();
	}

}

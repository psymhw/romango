package tangodj2.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import tangodj2.Hasher;

public class TrackChecker {

	public static final String SQL_STATEMENT = "select * from tracks";
	public static void main(String[] args) throws SQLException
	{
		String str;
		Connection connection = DriverManager.getConnection("jdbc:derby:tango_db;create=true");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(SQL_STATEMENT);
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int columnCount =  resultSetMetaData.getColumnCount();
		for(int x=1; x<=columnCount; x++)
		{
		  System.out.format("%20s", resultSetMetaData.getColumnName(x)+" | ");
		}
		
		File file;
		Hasher hasher = new Hasher();
		String filedHash="";
		String result="";
		
		while(resultSet.next())
		{
			str=resultSet.getString("path");
			filedHash=resultSet.getString("pathHash");
			String hash = hasher.getMd5Hash(str.getBytes());
			if (hash.equals(filedHash)) result="OK"; else result="BOGUS";
			
		   file = new File(str);
		   
		   if (file.exists())
		   System.out.println("OK: "+hash+" : "+filedHash+" : "+result);
		   else
			   System.out.println("NOT FOUND: "+str);
		  
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

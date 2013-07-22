package test.derby;

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
		String str;
		Connection connection = DriverManager.getConnection(TrackLoader.JDBC_URL2);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(SQL_STATEMENT);
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int columnCount =  resultSetMetaData.getColumnCount();
		for(int x=1; x<=columnCount; x++)
		{
		  System.out.format("%20s", resultSetMetaData.getColumnName(x)+" | ");
		}
		while(resultSet.next())
		{
			str=resultSet.getString("path");
		    System.out.println("path hash: "+str.hashCode());
		  
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

package test.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryDB 
{
	public static final String SQL_STATEMENT = "select * from channels";
	public static void main(String[] args) throws SQLException
	{
		Connection connection = DriverManager.getConnection(CreateDB.JDBC_URL);
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
		  System.out.println("");
		  for(int x=1; x<=columnCount; x++)
			{
			  System.out.format("%20s", resultSet.getString(x)+" | ");
			}
		}
		if (statement!=null) statement.close();
		if (connection!=null) connection.close();
	}

}

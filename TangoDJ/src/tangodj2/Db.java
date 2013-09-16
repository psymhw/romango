package tangodj2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tangodj2.PlaylistTree.PlaylistTreeItem;

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
	  else connection = DriverManager.getConnection(JDBC_URL2);
	}
	
	public static PlaylistTreeItem getPlaylist(int id)
	{
	  String name;	
	  String location;
	  Date incept;
	  PlaylistTreeItem playlistTreeItem=null;
	  try
	  {
	    connect();
	 	Statement statement = connection.createStatement();
	 	ResultSet resultSet = statement.executeQuery("select * from playlists where id = "+id);
	 	if(resultSet.next())
	 	{
	 	  name=resultSet.getString("name");
	 	  location = resultSet.getString("location");
	 	  incept = resultSet.getDate("incept");
	 	  id = resultSet.getInt("id");
	 	  playlistTreeItem = new PlaylistTreeItem(id, name);
	 	}
	 	if (statement!=null) statement.close();
	 	disconnect();
	  } catch (Exception e) { e.printStackTrace();}
	  System.out.println("DB - "+playlistTreeItem.toString());
	  return playlistTreeItem;
	}
	
	public static void disconnect() throws SQLException
	{
		if (connection!=null) connection.close();
	}
}

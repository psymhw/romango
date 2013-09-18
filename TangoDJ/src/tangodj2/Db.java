package tangodj2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tangodj2.PlaylistTree.PlaylistTree;
import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;

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
	 	if (resultSet!=null) resultSet.close();
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

	public static int insertTanda(String artist, int styleId, int position) throws SQLException, ClassNotFoundException
	{
		 connect();
		 String sql="insert into tandas (artist, styleId, playlistId, position) values('"+artist+"', "+styleId+","+SharedValues.currentPlaylist+", "+position+")";
		// System.out.println("sql: "+sql);
		 connection.createStatement().execute(sql);
		 
		 int maxid=0;
         sql="select max(id) maxid from tandas";
         System.out.println("sql: "+sql);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql);
         if(resultSet.next())
 	 	 {
           maxid= resultSet.getInt("maxid");
 	 	 }
      	 if (resultSet!=null) resultSet.close();
	 	 if (statement!=null) statement.close();
		 disconnect();
		 // System.out.println("maxid: "+maxid);
	     return maxid;
	}

	
	public static ArrayList<TandaTreeItem> getTandaTreeItems(int playlistId)  throws SQLException, ClassNotFoundException
	{
	  ArrayList<TandaTreeItem> tandaTreeItems = new ArrayList<TandaTreeItem>();
	  connect();
	  String sql="select * from tandas where playlistId="+playlistId+" order by position";
	  connection.createStatement().execute(sql);
	 
      System.out.println("sql: "+sql);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      
      TandaTreeItem tti;
      while (resultSet.next())
      {
    	  tti = new TandaTreeItem(resultSet.getString("artist"), resultSet.getInt("styleId"), resultSet.getInt("position"), false);
    	  int id = resultSet.getInt("id"); 
    	  tti.setDbId(id);
    	  tti.setPosition(PlaylistTree.getTandaCounter());
    	  tandaTreeItems.add(tti);
      }
   	  if (resultSet!=null) resultSet.close();
 	  if (statement!=null) statement.close();
	  disconnect();
	  return tandaTreeItems;
	}
}

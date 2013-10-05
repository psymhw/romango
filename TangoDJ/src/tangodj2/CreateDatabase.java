package tangodj2;

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
	
	public boolean exists()
	{
		 File test = new File("tango_db");
		  if (test!=null) 
		  { 
			  if (test.exists()) 
			  {
				  System.out.println("database found");
				  return true; 
			  }
		  }
		  return false;
		}
	
	public void create() throws ClassNotFoundException, SQLException
	{
		connect();
	  createTracksTable();
		createPlaylistsTable();
		createStateTable();
		createTandasTable();
		createCortinasTable();
		disconnect();
	   //Db.disconnect();
	  System.out.println("database initiallized");	
	}
	
	
	
	private void disconnect() throws SQLException
	{
		connection.close();
	}
	private void connect()  throws SQLException, ClassNotFoundException
	{
	  Class.forName(DRIVER);
	  connection = DriverManager.getConnection(JDBC_URL);	
	}
	
	
	
	private void createPlaylistsTable() throws SQLException
	{
		connection.createStatement().execute("create table playlists(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"name varchar(100), " +
	    		"location varchar(100), " +
	    		"incept TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
	    		")");	
		connection.createStatement().execute("insert into playlists (name, location) values('Test Playlist', 'Eugene')");
		System.out.println("Playlists table created");

	}
	
	private void createStateTable() throws SQLException
	{
		connection.createStatement().execute("create table state(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"name varchar(50), " +
	    		"value varchar(50) " +
	    		")");	
		System.out.println("State table created");
	}
	
	private void createTracksTable() throws SQLException
	{
		connection.createStatement().execute("create table tracks(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"path varchar(300), " +
	    		"pathHash varchar(32), " +
	    		"title varchar(100), " +
	    		"artist varchar(40), " +
	    		"album varchar(100), " +
	    		"duration integer, " +
	    		"track_year varchar(4), " +
	    		"playlist_count integer, " +
	    		"rating float, " +
	    		"delay integer, " +
	    		"genre varchar(40), " +
	    		"cleanup integer, " +
	    		"comment varchar(100) " +
	    		")");
	    
	    connection.createStatement().execute("CREATE UNIQUE INDEX pathInd ON TRACKS(PATHHASH)");
	    System.out.println("Tracks table created");
	}
	
	private void createTandasTable() throws SQLException
	{
		String sql="create table tandas(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"playlistId INTEGER, " +
	    		"position INTEGER, " +
	    		"artist varchar(40), " +
	    		"styleId INTEGER, " +
	    		"trackHash_0 varchar(32), " +
	    		"trackHash_1 varchar(32), " +
	    		"trackHash_2 varchar(32), " +
	    		"trackHash_3 varchar(32), " +
	    		"trackHash_4 varchar(32), " +
	    		"trackHash_5 varchar(32), " +
	    		"trackHash_6 varchar(32), " +
	    		"trackHash_7 varchar(32), " +
	    		"trackHash_8 varchar(32), " +
	    		"trackHash_9 varchar(32), " +
	    		"cortinaId INTEGER " +
	    		")";
		//System.out.println(sql);
		connection.createStatement().execute(sql);	
		System.out.println("Tandas table created");
	}
	
	public void createCortinasTable() throws SQLException
	{
	  String sql="create table cortinas(" +
        "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
        "start INTEGER, " +
        "stop INTEGER, " +
        "fadein INTEGER, " +
        "fadeout INTEGER, " +
        "track_count INTEGER, " +
        "delay INTEGER, " +
        "comment varchar(60), " +
        "title varchar(60), " +
        "hash varchar(32) " +
        ")";
  //System.out.println(sql);
  connection.createStatement().execute(sql);  
  System.out.println("Cortinas table created");
	}
	
	public static void main(String[] args) 
	{
	  try
	  {
	    new CreateDatabase();
	  } catch (Exception e) { e.printStackTrace(); }
	}

}

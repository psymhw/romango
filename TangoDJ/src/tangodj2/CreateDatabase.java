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
	  createListsTable();
	  createListMembersTable();
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
	    		"level integer, " +
	    		"parent integer, " +
	    		"folder integer, " +
	    		"position integer, " +
	    		"incept TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
	    		")");	
		connection.createStatement().execute("insert into playlists (name, location, level, parent, folder, position) values('Playlists', '', 0, 0, 1, 0)");
		connection.createStatement().execute("insert into playlists (name, location, level, parent, folder, position) values('Milongas', '',  1, 1, 1, 0)");
		connection.createStatement().execute("insert into playlists (name, location, level, parent, folder, position) values('My 1st Playlist', '',  2, 2, 0, 0)");

		connection.createStatement().execute("insert into playlists (name, location, level, parent, folder, position) values('Practicas', '', 1, 1, 1, 1)");

		System.out.println("Playlists table created");
	}
	
	private void createListsTable() throws SQLException
  {
    connection.createStatement().execute("create table lists(" +
          "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
          "name varchar(100), " +
          "incept TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
          ")"); 
    System.out.println("Lists table created");
  }
  
	 private void createListMembersTable() throws SQLException
	  {
	    connection.createStatement().execute("create table listmembers(" +
	          "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	          "pathHash varchar(32), " +
	          "track_id integer " +
	          ")"); 
	    System.out.println("List Members table created");
	  }
	
	private void createStateTable() throws SQLException
	{
		connection.createStatement().execute("create table state(" +
	    		"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
	    		"name varchar(50), " +
	    		"value varchar(50) " +
	    		")");	
		connection.createStatement().execute("insert into state (name) values('TangoFolder')");
		connection.createStatement().execute("insert into state (name) values('CleanupFolder')");
		connection.createStatement().execute("insert into state (name, value) values('CurrentPlaylist', '3')");

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
	    		"leader varchar(40), " +
	    		"album varchar(100), " +
	    		"duration integer, " +
	    		"track_year varchar(4), " +
	    		"playlist_count integer, " +
	    		"track_no integer, " +
	    		"rating varchar(5), " +
	    		"bpm varchar(3), " +
	    		"delay integer, " +
	    		"genre varchar(40), " +
	    		"style varchar(20), " +
	    		"singer varchar(40), " +
	    		"cleanup integer, " +
	    		"adjectives varchar(100), " +
	    		"comment varchar(100) " +
	    		")");
	    
	    connection.createStatement().execute("CREATE UNIQUE INDEX pathInd ON TRACKS(PATHHASH)");
	    connection.createStatement().execute("CREATE INDEX loadInd ON TRACKS(cleanup,artist,album,track_no)");
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
	    		"cortinaId INTEGER, " +
	    		"tanda_disable INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t0 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t1 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t2 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t3 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t4 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t5 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t6 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t7 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t8 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_t9 INTEGER NOT NULL DEFAULT 0, "+
	    		"disable_cortina INTEGER NOT NULL DEFAULT 0"+
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
        "premade INTEGER, " +
        "original_duration INTEGER, " +
        "comment varchar(60), " +
        "theme varchar(40), " +
        "title varchar(60), " +
        "artist varchar(40), " +
        "album varchar(100), " +
        "path varchar(300), " +
        "hash varchar(32) " +
        ")";
  //System.out.println(sql);
  connection.createStatement().execute(sql);  
  System.out.println("Cortinas table created");
	}
	
//	public static void main(String[] args) 
	//{
	  //try
	 // {
	  //  new CreateDatabase();
	 // } catch (Exception e) { e.printStackTrace(); }
//	}

}

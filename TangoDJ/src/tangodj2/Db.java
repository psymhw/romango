package tangodj2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.ObservableList;

import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.cleanup.CleanupTrack;
import tangodj2.cortina.Cortina;
import tangodj2.cortina.CortinaTrack;
import tangodj2.tango.TangoTrack;

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
	
	public static void loadAllTracks(int type)
	{
		
	    String title;
	    String artist;
	    String album;
	    String genre;
	    String comment;
	    String pathHash;
	    String path;
	    String track_year;
	    int cortina;
	    int duration=0;
	    
	    try
	    {
			connect();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from tracks where cortina = "+type+"order by artist, album, title");
			while(resultSet.next())
			{
			  title=resultSet.getString("title");
			  artist = resultSet.getString("artist");
			  album = resultSet.getString("album");
			  genre = resultSet.getString("genre");
			  comment = resultSet.getString("comment");
			  track_year = resultSet.getString("track_year");
			  //System.out.println("track_year: "+track_year);
			  pathHash = resultSet.getString("pathHash");
			  path = resultSet.getString("path");
			  duration=resultSet.getInt("duration");
			  cortina=resultSet.getInt("cortina");
			 // SharedValues.allTracksData.add(new Track(title, artist, album, genre, comment, pathHash, path, duration, cortina, track_year));
			  //System.out.println("added: "+title);
			}
			if (resultSet!=null) resultSet.close();
			if (statement!=null) statement.close();
			disconnect();
	    } catch (Exception e) { e.printStackTrace();}
	  }
	
	public static void loadTangoTracks(ObservableList<TangoTrack> tangoTracksData)
  {
    String title;
    String artist;
    String album;
    String genre;
    String comment;
    String pathHash;
    String path;
    String track_year;
    int cleanup;
    int duration=0;
      
    tangoTracksData.clear();
      
    try
    {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from tracks where cleanup = 0 order by artist, album, title");
      while(resultSet.next())
      {
        title=resultSet.getString("title");
        artist = resultSet.getString("artist");
        album = resultSet.getString("album");
        genre = resultSet.getString("genre");
        comment = resultSet.getString("comment");
        track_year = resultSet.getString("track_year");
        //System.out.println("track_year: "+track_year);
        pathHash = resultSet.getString("pathHash");
        path = resultSet.getString("path");
        duration=resultSet.getInt("duration");
        cleanup=resultSet.getInt("cleanup");
        tangoTracksData.add(new TangoTrack(title, artist, album, genre, comment, pathHash, path, duration, cleanup, track_year));
        //System.out.println("added: "+title);
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
    } catch (Exception e) { e.printStackTrace();}
  }
	
	public static void loadCleanupTracks(ObservableList<CleanupTrack> cleanupTracksData)
  {
    String title;
    String artist;
    String album;
    String genre;
    String comment;
    String pathHash;
    String path;
    String track_year;
    int cleanup;
    int duration=0;
      
    cleanupTracksData.clear();
      
    try
    {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from tracks where cleanup = 1 order by artist, album, title");
      while(resultSet.next())
      {
        title=resultSet.getString("title");
        artist = resultSet.getString("artist");
        album = resultSet.getString("album");
        genre = resultSet.getString("genre");
        comment = resultSet.getString("comment");
        track_year = resultSet.getString("track_year");
        //System.out.println("track_year: "+track_year);
        pathHash = resultSet.getString("pathHash");
        path = resultSet.getString("path");
        duration=resultSet.getInt("duration");
        cleanup=resultSet.getInt("cleanup");
        cleanupTracksData.add(new CleanupTrack(title, artist, album, genre, comment, pathHash, path, duration, cleanup, track_year));
        //System.out.println("added: "+title);
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
    } catch (Exception e) { e.printStackTrace();}
  }
  
	
	public static void loadCortinaTracks(ObservableList<CortinaTrack> cortinaTracksData)
  {
      cortinaTracksData.clear();
      try
      {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from cortinas " +
      		"order by title, start");
      CortinaTrack cortinaTrack;
      while(resultSet.next())
      {
        cortinaTrack= new CortinaTrack(resultSet.getInt("id"), 
                                       resultSet.getInt("start"),
                                       resultSet.getInt("stop"), 
                                       resultSet.getInt("fadein"), 
                                       resultSet.getInt("fadeout"), 
                                       resultSet.getInt("delay"), 
                                       resultSet.getInt("original_duration"), 
                                       resultSet.getString("title"),
                                       resultSet.getString("hash"));
           
        cortinaTracksData.add(cortinaTrack);
        
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
      //System.out.println("Cortina Data: "+cortinaTracksData.size());
      } catch (Exception e) { e.printStackTrace();}
    }
	
	public static CortinaTrack getCortinaTrack(int id)
  {
	  CortinaTrack cortinaTrack=null;
      try
      {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from cortinas where id = "+id+ 
          " order by title, start");
      
      while(resultSet.next())
      {
        cortinaTrack= new CortinaTrack(resultSet.getInt("id"), 
                                       resultSet.getInt("start"),
                                       resultSet.getInt("stop"), 
                                       resultSet.getInt("fadein"), 
                                       resultSet.getInt("fadeout"), 
                                       resultSet.getInt("delay"), 
                                       resultSet.getInt("original_duration"), 
                                       resultSet.getString("title"),
                                       resultSet.getString("hash"));
           
        
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
      } catch (Exception e) { e.printStackTrace();}
      if (cortinaTrack!=null)
      {
        cortinaTrack.setTrackMeta(getTrackInfo(cortinaTrack.getPathHash()));
        
      }
      //System.out.println("Db - Cortina Hash: "+cortinaTrack.getPathHash());
      //
      return cortinaTrack;
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
	
	public static PlaylistTreeItem getPlaylist(int id) throws SQLException, ClassNotFoundException
	{
	  String name;	
	  String location;
	  Date incept;
	  PlaylistTreeItem playlistTreeItem=null;
	  
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
	  
	 //System.out.println("DB - "+playlistTreeItem.toString());
	  
	  return playlistTreeItem;
	}
	
	public static TrackMeta getTrackInfo(String pathHash )
	{
	  TrackMeta trackMeta=null;
	  try
	  {
	    connect();
	 	Statement statement = connection.createStatement();
	 	ResultSet resultSet = statement.executeQuery("select * from tracks where pathHash = '"+pathHash+"'");
	 	if(resultSet.next())
	 	{
	 	
	 	  trackMeta = new TrackMeta(resultSet.getString("title"),
	 			                    resultSet.getString("artist"),
	 			                    resultSet.getString("album"),
	 			                   resultSet.getString("comment"),
	 			                   resultSet.getString("genre"),
	 			                  resultSet.getString("pathHash"),
	 			                 resultSet.getString("path"),
	 			                 resultSet.getString("track_year")
	 			  );
	 	  trackMeta.duration= resultSet.getInt("duration");
	 	  trackMeta.cleanup=resultSet.getInt("cleanup");
	 	}
	 	if (resultSet!=null) resultSet.close();
	 	if (statement!=null) statement.close();
	 	
	 	disconnect();
	  } catch (Exception e) { e.printStackTrace();}
	  return trackMeta;
	}
	
	public static void disconnect() throws SQLException
	{
		if (connection!=null) connection.close();
	}

	public static int insertTanda(String artist, int styleId, int position) throws SQLException, ClassNotFoundException
	{
		 connect();
		 String sql="insert into tandas (artist, styleId, playlistId, position, cortinaId) values('"+sqlReadyString(artist)+"', "+styleId+","+SharedValues.currentPlaylist+", "+position+", -1)";
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
	public static String sqlReadyString(String inStr)
	{
	   String returnStr = inStr.replace("'","''");
	   returnStr = returnStr.replace("��","");
	   returnStr = returnStr.replace("\\","\\\\");
	   
	   char tChar=0;
	   returnStr = removeChar(returnStr, tChar);
	   
	   return returnStr;
	}
	
	public static String removeChar(String s, char c) 
    {
	  String r = "";
	  for (int i = 0; i < s.length(); i ++) 
	  {
	    if (s.charAt(i) != c) r += s.charAt(i);
	  }
	  return r;
    }
	
	
	
	public static ArrayList<TandaTreeItem> getTandaTreeItems(int playlistId)  throws SQLException, ClassNotFoundException
	{
	  ArrayList<TandaTreeItem> tandaTreeItems = new ArrayList<TandaTreeItem>();
	  connect();
	  String sql="select * from tandas where playlistId="+playlistId+" order by position";
	  connection.createStatement().execute(sql);
	 
     // System.out.println("sql: "+sql);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      
      TandaTreeItem tandaTreeItem;
      while (resultSet.next())
      {
    	  tandaTreeItem = new TandaTreeItem(resultSet.getString("artist"), resultSet.getInt("styleId"));
    	  tandaTreeItem.setDbId(resultSet.getInt("id"));
    	  tandaTreeItem.setCortinaId(resultSet.getInt("cortinaId"));
    	 
    	  // GET THE TRACKS 
    	  for(int i=0; i<10; i++)
    	  {
    	 	  String trackName="trackHash_"+i;
    		  String trackHash=resultSet.getString(trackName);
    		//System.out.println("Db - trackHash Found: "+i+" - "+trackHash);
    		  if (trackHash!=null) tandaTreeItem.loadTrack(trackHash);
    	  }
    	  
    	  // GET THE CORTINA
    	  tandaTreeItem.loadCortina(tandaTreeItem.getCortinaId());
    	  
    	  tandaTreeItems.add(tandaTreeItem);
      }
   	  if (resultSet!=null) resultSet.close();
 	  if (statement!=null) statement.close();
	  disconnect();
	  return tandaTreeItems;
	}

	public static void updateTandaTracks(TandaTreeItem tandaTreeItem) throws SQLException, ClassNotFoundException
	{
	   StringBuffer sql = new StringBuffer("update tandas set ");
	   String trackName;
	  // int trackNumber=tandaTreeItem.getNumberOfTracks();
	   
	   PlaylistTreeItem playlistTreeItem = (PlaylistTreeItem)tandaTreeItem.getParent();
	   int position = playlistTreeItem.getTandaPosition(tandaTreeItem);
	   
	   ArrayList<String> trackHashCodes = tandaTreeItem.getTrackHashCodes();
	   System.out.println("Db - trackHashCodes size: "+trackHashCodes.size());
	   Iterator<String> it = trackHashCodes.iterator();
	   int counter=0;
	   while(it.hasNext())
	   {
		  trackName="trackHash_"+counter;
		  String hashCode=it.next();
		  sql.append(trackName+" = '"+hashCode+"', ");
	     counter++;
	   }
	   for(int i=counter; i<10; i++)
	   {
		   trackName="trackHash_"+i;
		   sql.append(trackName+" = null, ");
	   }
	   sql.append(" position = "+position);
	   sql.append(", cortinaId = "+tandaTreeItem.getCortinaId());
	   
	   sql.append(" where id = "+tandaTreeItem.getDbId());
	  System.out.println(sql);
	   
	   connect();
	   connection.createStatement().execute(sql.toString());
	   disconnect();
	}
	
	public static void updateTandaPositions(PlaylistTreeItem playlistTreeItem)  throws SQLException, ClassNotFoundException
	{
	  Iterator it = playlistTreeItem.getChildren().iterator();
	  TandaTreeItem tandaTreeItem;
	  int counter=0;
	  while(it.hasNext())
	  {
		tandaTreeItem = (TandaTreeItem)it.next();
		updateTandaPosition(tandaTreeItem, counter);
		counter++;
	  }
	}
	
	public static void updateTandaPosition(TandaTreeItem tandaTreeItem, int position) throws SQLException, ClassNotFoundException
	{
	   StringBuffer sql = new StringBuffer("update tandas set");
	   sql.append(" position = "+position);
	   sql.append(" where id = "+tandaTreeItem.getDbId());
	 //  System.out.println(sql);
	   
	   connect();
	   connection.createStatement().execute(sql.toString());
	   disconnect();
	}
	
	public static void deleteTanda(TandaTreeItem tandaTreeItem) throws SQLException, ClassNotFoundException
	{
	   StringBuffer sql = new StringBuffer("delete from tandas");
	   sql.append(" where id = "+tandaTreeItem.getDbId());
	 //  System.out.println(sql);
	   
	   connect();
	   connection.createStatement().execute(sql.toString());
	   disconnect();
	}

  public static int insertCortina(Cortina cortina) throws SQLException, ClassNotFoundException
  {
    connect();
    String sql="insert into cortinas (start, stop, fadein, fadeout, delay, original_duration, title, hash) values("
    +cortina.start+", "
    +cortina.stop+", "
    +cortina.fadein+", "
    +cortina.fadeout+", "
     +cortina.delay+", "
    +cortina.original_duration+", '"
    +sqlReadyString(cortina.title)+"', '"
    +cortina.hash+"')";
   // System.out.println("sql: "+sql);
    connection.createStatement().execute(sql);
    
    int maxid=0;
    sql="select max(id) maxid from cortinas";
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
   
    return maxid;
  }
	
}

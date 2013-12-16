package tangodj2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.allPlaylistsTree.AllPlaylistsBaseItem;
import tangodj2.allPlaylistsTree.AllPlaylistsFolderItem;
import tangodj2.allPlaylistsTree.AllPlaylistsPlaylistItem;
import tangodj2.cleanup.CleanupTable;
import tangodj2.cleanup.CleanupTrack;
import tangodj2.cortina.Cortina;
import tangodj2.cortina.CortinaTrack;
import tangodj2.tango.TangoTable;
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
	
	/*
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
	*/
	
	public static void loadTangoTracks()
  {
	  loadTangoTracks(null);
  }
	public static void loadTangoTracks(String search)
  {
	  
    TangoTable.tangoTracksData.clear();
    
    
    String sql;
    
    if (search==null)
      sql= "select * from tracks where cleanup = 0 order by artist, album, track_no";
    else
    {  
      search=search.toLowerCase();
      sql = "select * from tracks where cleanup = 0 and (lower(title) like '%"+search+"%' or lower(leader) like '%"+search+"%') order by artist, album, track_no";
    }
      TrackDb trackDb;
      
    try
    {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      while(resultSet.next())
      {
        trackDb = getTrackDb(resultSet);
        TangoTable.tangoTracksData.add(new TangoTrack(trackDb));
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
    } catch (Exception e) { 
      System.out.println("Db - loadSql: "+sql);
      e.printStackTrace();
    }
  }
	
	
	public static void loadCleanupTracks()
  {
	  loadCleanupTracks(null);
  }
	
	
	public static void loadCleanupTracks(String search)
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
      
    CleanupTable.cleanupTracksData.clear();
    
String sql;
    
    if (search==null)
      sql= "select * from tracks where cleanup = 1 order by artist, album, track_no";
    else
    {  
      search=search.toLowerCase();
      sql = "select * from tracks where cleanup = 1 and (lower(title) like '%"+search+"%' or lower(artist) like '%"+search+"%') order by artist, album, track_no";
    }
      
    try
    {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
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
        CleanupTable.cleanupTracksData.add(new CleanupTrack(title, artist, album, genre, comment, pathHash, path, duration, cleanup, track_year));
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
      		"order by artist, album");
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
                                       resultSet.getString("hash"),
                                       resultSet.getString("path"),
                                       resultSet.getString("album"),
                                       resultSet.getString("artist"),
                                       resultSet.getInt("premade"));
           
        cortinaTracksData.add(cortinaTrack);
        
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
      //System.out.println("Cortina Data: "+cortinaTracksData.size());
      } catch (Exception e) { e.printStackTrace();}
    }
	
  public static Preferences getPreferences() throws Exception
  {
	  Preferences prefs = new Preferences();
      try
      {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from state ");
      while(resultSet.next())
      {
         if ("TangoFolder".equals(resultSet.getString("name"))) 
        	 prefs.tangoFolder=resultSet.getString("value");
         if ("CleanupFolder".equals(resultSet.getString("name"))) 
        	 prefs.cleanupFolder=resultSet.getString("value");
         if ("CurrentPlaylist".equals(resultSet.getString("name"))) 
        	 prefs.currentPlaylist=Integer.parseInt(resultSet.getString("value"));
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      
     
      disconnect();
      //System.out.println("Cortina Data: "+cortinaTracksData.size());
      } catch (Exception e) { e.printStackTrace();}
	  
      if (prefs.cleanupFolder==null)
    	  prefs.cleanupFolder="C:\\";
      if (prefs.cleanupFolder.length()==0)
    	  prefs.cleanupFolder="C:\\";
      if (prefs.tangoFolder==null)
    	  prefs.tangoFolder="C:\\";
      if (prefs.tangoFolder.length()==0)
    	  prefs.tangoFolder="C:\\";
	  return prefs;
  }
  
  public static void updateTangoTableColumnVisible(String colName, boolean visible)
  {
    String strBool="false";
    if (visible) strBool="true";
    try 
    {
      connect();
      connection.createStatement().execute("update state set value = '"+strBool+"' where name = '"+colName+"'");
      disconnect(); 
    } catch (Exception e) { e.printStackTrace(); }
  }
  
  public static boolean trackExists(String pathHash)
  {
    boolean exists=false;
   
    try 
    {
      
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from tracks where pathHash = '"+pathHash+"'");
      if (resultSet.next())
      {
        exists = true;
      }
           
    } catch (Exception e) { e.printStackTrace(); }
    return exists;
  }
  
  public static void updateTangoTableColumnWidth(String colName, double width)
  {
    String strDouble=""+(int)width;
    try 
    {
      connect();
      connection.createStatement().execute("update state set value = '"+strDouble+"' where name = '"+colName+"'");
      disconnect(); 
    } catch (Exception e) { e.printStackTrace(); }
  }
  
  public static boolean getTangoTableColumnVisible(String colName, boolean defaultVisibility) 
  {
    String strBool="true";
    boolean returnValue=true;
    
    try
	{
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from state where name = '"+colName+"'");
      if (resultSet.next())
      {
        strBool = resultSet.getString("value");
        if (resultSet!=null) resultSet.close();
        if (statement!=null) statement.close();
        if ("true".equals(strBool))  returnValue= true;
        else returnValue= false;
      }
      else
      {
        if (defaultVisibility) strBool="true"; else strBool="false";
        connection.createStatement().execute("insert into state (name, value) values('"+colName+"', '"+strBool+"')");
        returnValue= true;
      }
      disconnect();
	} catch (Exception e) { e.printStackTrace(); }
    return returnValue;
  }
  
  public static double getTangoTableColumnWidth(String colName, double defaultWidth) 
  {
    String strDouble=""+(int)defaultWidth;
    double retVal=defaultWidth;
    
    try
    {
      connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from state where name = '"+colName+"'");
      if (resultSet.next())
      {
        strDouble = resultSet.getString("value");
        if (resultSet!=null) resultSet.close();
        if (statement!=null) statement.close();
        try { retVal=Double.parseDouble(strDouble); } catch(Exception e) {}
      }
      else
      {
        connection.createStatement()
         .execute("insert into state (name, value) values('"+colName+"', '"+strDouble+"')");
      }
    } catch (Exception e) { e.printStackTrace(); }
    return retVal;
  }
  
  public static void updatePreferences(Preferences prefs)
  {
	  StringBuffer sql = new StringBuffer("update state set value = '"
			  +prefs.tangoFolder+"' where name='TangoFolder'");
	   try {
	   connect();
	   //System.out.println("Db update prefs sql: "+sql);
	   connection.createStatement().execute(sql.toString());
	   
	   sql = new StringBuffer("update state set value = '"
				  +prefs.cleanupFolder+"' where name='CleanupFolder'");
	   connection.createStatement().execute(sql.toString());
	   
	   sql = new StringBuffer("update state set value = '"
				  +prefs.currentPlaylist+"' where name='CurrentPlaylist'");
	   connection.createStatement().execute(sql.toString());
	   disconnect(); 
	   } catch (Exception e) { e.printStackTrace(); }
  }
  public static void saveCurrentPlaylist(int id)
  {
	try 
	{
	  connect();
	   
	  String  sql = "update state set value = '"
				  +id+"' where name='CurrentPlaylist'";
	   connection.createStatement().execute(sql);
	   disconnect(); 
	 } catch (Exception e) { e.printStackTrace(); }
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
                                       resultSet.getString("hash"),
                                       resultSet.getString("path"),
                                       resultSet.getString("album"),
                                       resultSet.getString("artist"),
                                       resultSet.getInt("premade")
            
            );
           
        
      }
      if (resultSet!=null) resultSet.close();
      if (statement!=null) statement.close();
      disconnect();
      } catch (Exception e) { e.printStackTrace();}
      if (cortinaTrack!=null)
      {
        cortinaTrack.setTrackDb(getTrackInfo(cortinaTrack.getPathHash()));
        
      }
      //System.out.println("Db - Cortina Hash: "+cortinaTrack.getPathHash());
      //
      return cortinaTrack;
    }
  
	
	private static void connect()  throws SQLException, ClassNotFoundException
	{
		connect(false);
	}
	
	private static void connect(boolean create)  throws SQLException, ClassNotFoundException
	{
	 // Class.forName(DRIVER);
	 // if (create) connection = DriverManager.getConnection(JDBC_URL);
	//  else connection = DriverManager.getConnection(JDBC_URL2);
	}
	
	public static void databaseConnect(boolean create)  throws SQLException, ClassNotFoundException
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
	  
	  //connect();
	  try {
	 	Statement statement = connection.createStatement();
	 	ResultSet resultSet = statement.executeQuery("select * from playlists where id = "+id);
	 	if(resultSet.next())
	 	{
	 	  name=resultSet.getString("name");
	 	  location = resultSet.getString("location");
	 	  incept = resultSet.getDate("incept");
	 	  id = resultSet.getInt("id");
	 	  playlistTreeItem = new PlaylistTreeItem(id, name);
	 	  playlistTreeItem.setTreeType("playlist");
	 	}
	 	if (resultSet!=null) resultSet.close();
	 	if (statement!=null) statement.close();
	  } catch (Exception e) { e.printStackTrace(); }
	 	
	 	//disconnect();
	  
	 //System.out.println("DB - "+playlistTreeItem.toString());
	  
	  return playlistTreeItem;
	}
	
	
	public static CleanupTrack getCleanupTrack(String pathHash)
	{
	  TrackDb tm = getTrackInfo(pathHash);
	  CleanupTrack cleanupTrack = new CleanupTrack(tm.title, tm.artist, tm.album, tm.genre, tm.comment, 
	      tm.pathHash, tm.path, tm.duration, 0, "");
	  return cleanupTrack;
	}
	
	public static void updateTrack(TrackDb trackDb)
	{
	  sqlReadyTrackInfo(trackDb);
	  String sql = "update tracks set " 
	               +" title = '"+trackDb.title+"', "
	               +" album = '"+trackDb.album+"', "
	               +" leader = '"+trackDb.leader+"', "
	               +" artist = '"+trackDb.artist+"', "
	               +" track_year = '"+trackDb.track_year+"', "
	               +" comment = '"+trackDb.comment+"', "
	               +" singer = '"+trackDb.singer+"', "
	               +" genre = '"+trackDb.genre+"', "
	               +" rating = '"+trackDb.rating+"', "
	               +" adjectives = '"+trackDb.adjectives+"', "
	               +" style = '"+trackDb.style+"', "
	               +" bpm = '"+trackDb.bpm+"', "
	               +" delay = "+trackDb.delay
	               +" where pathHash =  '"+trackDb.pathHash+"'";
     try 
     {
       connect();
       System.out.println("Db update track sql: "+sql);
       connection.createStatement().execute(sql);
       disconnect();
     } catch (Exception e) { e.printStackTrace();}
   }
	
	public static TrackDb getTrackInfo(String pathHash)
	{
	  TrackDb trackDb=null;
	  try
	  {
	    connect();
	 	  Statement statement = connection.createStatement();
	 	  ResultSet resultSet = statement.executeQuery("select * from tracks where pathHash = '"+pathHash+"'");
	 	  if(resultSet.next())
	 	  {
	 	    trackDb=getTrackDb(resultSet);
	 	  }
	 	  if (resultSet!=null) resultSet.close();
	 	  if (statement!=null) statement.close();
	 	  disconnect();
	  } catch (Exception e) { e.printStackTrace();}
	  return trackDb;
	}
	
	private static TrackDb getTrackDb(ResultSet resultSet) throws Exception
	{
	  TrackDb trackDb = new TrackDb(resultSet.getString("title"),
    
	  resultSet.getString("artist"),
    resultSet.getString("album"),
    resultSet.getString("comment"),
    resultSet.getString("genre"),
    resultSet.getString("pathHash"),
    resultSet.getString("path"),
    resultSet.getString("track_year"));
    trackDb.duration= resultSet.getInt("duration");
    trackDb.cleanup=resultSet.getInt("cleanup");
    trackDb.singer=resultSet.getString("singer");
    trackDb.style=resultSet.getString("style");
    trackDb.adjectives=resultSet.getString("adjectives");
    trackDb.rating=resultSet.getString("rating");
    trackDb.leader=resultSet.getString("leader");
    trackDb.bpm=resultSet.getString("bpm");
    trackDb.track_no= resultSet.getInt("track_no");
    trackDb.delay= resultSet.getInt("delay");

    return trackDb;
	}
	
	private static void disconnect() throws SQLException
	{
	//	if (connection!=null) connection.close();
	//	connection=null;
	}
	
	public static void databaseDisconnect() throws SQLException
  {
    if (connection!=null) connection.close();
    connection=null;
  }

	public static int insertTanda(String artist, int styleId, int position) throws SQLException, ClassNotFoundException
	{
		 connect();
		 String sql="insert into tandas (artist, styleId, playlistId, position, cortinaId) values('"+sqlReadyString(artist)+"', "+styleId+","+TangoDJ2.prefs.currentPlaylist+", "+position+", -1)";
		// System.out.println("Db insertTanda sql: "+sql);
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
	
	private static int getMaxId(String file) throws Exception
	{
		int maxid=0;
        String sql="select max(id) maxid from "+file;
        System.out.println("sql: "+sql);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if(resultSet.next())
	 	 {
          maxid= resultSet.getInt("maxid");
	 	 }
     	 if (resultSet!=null) resultSet.close();
	 	 if (statement!=null) statement.close();
	 	 return maxid;
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
	
	static void sqlReadyTrackInfo(TrackDb trackDb)
	 {
	   
	   trackDb.title    = sqlReadyString(trackDb.title, 100);
	   trackDb.artist   = sqlReadyString(trackDb.artist, 40);
	   trackDb.album    = sqlReadyString(trackDb.album, 100);
	   trackDb.comment  = sqlReadyString(trackDb.comment, 100);
	   trackDb.path     = sqlReadyString(trackDb.path);
	   trackDb.genre     = sqlReadyString(trackDb.genre, 40);
	   trackDb.leader    = sqlReadyString(trackDb.leader);
	   //trackDb.path = new File(trackDb.path).toURI().toString();
	   trackDb.track_year     = sqlReadyString(trackDb.track_year, 4);
	  
	 }
	 
	 public static String sqlReadyString(String inStr, int maxLength)
	 {
	   String retStr = sqlReadyString(inStr);
	   if (retStr.length()>=maxLength) retStr=retStr.substring(0, maxLength-1);
	   return retStr;
	 }
	 
	 public static String sqlReadyString(String inStr)
	 {
	    if (inStr==null) return "";
	    String returnStr = inStr.replace("'","''");
	    returnStr = returnStr.replace("ÿþ","");
	   // returnStr = returnStr.replace("\\","\\\\");
	    //System.out.println(inStr);
	    char tChar=0;
	    returnStr = removeChar(returnStr, tChar);
	    
	    return returnStr;
	 }
	
	public static void execute(String sql)
	{
	  try
    {
      connection.createStatement().execute(sql);
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
	}
	
	public static void disableTanda(boolean set, int id)
	{
	  int value=0;
	  if (set==true) value=1;
	  String sql="update tandas set tanda_disable = "+value
	      +", disable_t0 = "+value
	      +", disable_t1 = "+value
	      +", disable_t2 = "+value
	      +", disable_t3 = "+value
	      +", disable_t4 = "+value
	      +", disable_t5 = "+value
	      +", disable_t6 = "+value
	      +", disable_t7 = "+value
	      +", disable_t8 = "+value
	      +", disable_t9 = "+value
	      +", disable_cortina = "+value
	      +" where id="+id;
	  try {
    connection.createStatement().execute(sql);
	  } catch (Exception e) {e.printStackTrace(); }
	}
	
	public static void disableTrack(boolean set, int tandaId, int trackInTanda)
	{
	  int value=0;
    if (set==true) value=1;
	  
	  String sql="update tandas set disable_t"+(trackInTanda-1)+" = "+value +" where id="+tandaId;
	 // System.out.println("Db - trackInTanda: "+sql);
	  try {
	    connection.createStatement().execute(sql);
	    } catch (Exception e) {e.printStackTrace(); }
	}
	
	public static ArrayList<TandaTreeItem> getTandaTreeItems(int playlistId)  throws SQLException, ClassNotFoundException
	{
	  ArrayList<TandaTreeItem> tandaTreeItems = new ArrayList<TandaTreeItem>();
	  connect();
	  String sql="select * from tandas where playlistId="+playlistId+" order by position";
	 // connection.createStatement().execute(sql);
	 
     // System.out.println("sql: "+sql);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      
      TandaTreeItem tandaTreeItem;
      while (resultSet.next())
      {
    	  tandaTreeItem = new TandaTreeItem(resultSet.getString("artist"), resultSet.getInt("styleId"));
    	  tandaTreeItem.setDbId(resultSet.getInt("id"));
    	  tandaTreeItem.setCortinaId(resultSet.getInt("cortinaId"));
    	  int tanda_disable=resultSet.getInt("tanda_disable");
    	  tandaTreeItem.setDisabled(tanda_disable);
    
    	  // GET THE TRACKS 
    	  for(int i=0; i<10; i++)
    	  {
    	 	  String trackName="trackHash_"+i;
    	 	  String disableName="disable_t"+i;
    		  String trackHash=resultSet.getString(trackName);
    		  int track_disable=resultSet.getInt(disableName);
    		  //if (tanda_disable==1) track_disable=1; // If the tanda is disables, so are all the tracks, regardless of their db setting
    		  
    		  // LOAD THE TRACKS INTO THE TANDA
    		  if (trackHash!=null) tandaTreeItem.loadTrack(trackHash, track_disable);
    	  		  
    	  }
    	  
    	  
    	  // GET THE CORTINA
    	  int cortina_disable = resultSet.getInt("disable_cortina");
    	 // if (tanda_disable==1) cortina_disable=1; // If the tanda is disables, so is the cortina, regardless of its db setting
    	  tandaTreeItem.loadCortina(tandaTreeItem.getCortinaId(), cortina_disable);
    	  tandaTreeItem.setExpanded(false);
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
	   //System.out.println("Db - trackHashCodes size: "+trackHashCodes.size());
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
	 // System.out.println(sql);
	   
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

  public static void updateCortina(Cortina cortina) throws SQLException, ClassNotFoundException
  {
    connect();
    String sql="update cortinas set"
              +" start = " +cortina.start
              +", stop = "  +cortina.stop
              +", fadein = "+cortina.fadein
              +", fadeout = "+cortina.fadeout
              +", delay = "+cortina.delay
              +", title = '"+cortina.title
              +"' where id = "+cortina.id;
    System.out.println("sql: "+sql);
    connection.createStatement().execute(sql);
    
    disconnect();
  }
  
  public static void updatePremadeCortina(Cortina cortina) throws SQLException, ClassNotFoundException
  {
    connect();
    String sql="update cortinas set"
              +" title = '"+cortina.title
              +"' where id = "+cortina.id;
    System.out.println("sql: "+sql);
    connection.createStatement().execute(sql);
    
    disconnect();
  }
  public static int insertCortina(Cortina cortina) throws SQLException, ClassNotFoundException
  {
    connect();
    String sql="insert into cortinas (start, stop, fadein, fadeout, delay, original_duration, title, hash, path, album, artist, premade) values("
    +cortina.start+", "
    +cortina.stop+", "
    +cortina.fadein+", "
    +cortina.fadeout+", "
     +cortina.delay+", "
    +cortina.original_duration+", '"
    +sqlReadyString(cortina.title)+"', '"
    +cortina.hash+"', '"
    +cortina.path+"', '"
    +cortina.album+"', '"
    +cortina.artist+"', "
    +cortina.premade+")";
    System.out.println("sql: "+sql);
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
  
  public static int insertPremadeCortina(TrackDb trackDb) 
  {
    //connect();
    int maxid=0;
    String sql="insert into cortinas (start, stop, fadein, fadeout, delay, original_duration, title, hash, path, album, artist, premade) values("
    +0+", "
    +0+", "
    +0+", "
    +0+", "
    +0+", "
    +trackDb.duration+", '"
    +sqlReadyString(trackDb.title)+"', '"
    +trackDb.pathHash+"', '"
    +trackDb.path+"', '"
    +trackDb.album+"', '"
    +trackDb.artist+"', "
    +1+")";
    System.out.println("sql: "+sql);
    try {
    connection.createStatement().execute(sql);
    
   
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
    
    //disconnect();
   
    } catch (Exception e) { e.printStackTrace(); }
    return maxid;
  }
  
  public static AllPlaylistsBaseItem getAllPlaylists()
  {
	  AllPlaylistsBaseItem root=null;
	  try
	  {
	    connect();
	    root = getPlaylistItem(1);
	    getPlaylistsRecursive(root);
	    disconnect();
	  } catch (Exception e) {e.printStackTrace();}
	  return root;
  }

private static AllPlaylistsBaseItem getPlaylistItem(int i) throws Exception
{
	String sql = "select * from playlists where id = "+i;
	Statement statement = connection.createStatement();
 	ResultSet resultSet = statement.executeQuery(sql);
 	
 	String name="";
 	int folder=0;
 	AllPlaylistsBaseItem apfi=null;
  	if(resultSet.next())
 	{
 	  name=resultSet.getString("name");
 	  folder=resultSet.getInt("folder");
 	  if (folder==1)
 	     apfi = new AllPlaylistsFolderItem(name);
 	  else apfi = new AllPlaylistsPlaylistItem(name);
 	  apfi.setId(resultSet.getInt("id"));
 	  apfi.setLevel(resultSet.getInt("level"));
 	  apfi.setParentId(resultSet.getInt("parent"));
 	  apfi.setPosition(resultSet.getInt("position"));
 	}
 	if (resultSet!=null) resultSet.close();
 	if (statement!=null) statement.close();
	return apfi;
}

private static AllPlaylistsBaseItem getPlaylistsRecursive(AllPlaylistsBaseItem parent) throws Exception
{
	String sql = "select * from playlists where parent = "+parent.getId()
			+" order by position";
	Statement statement = connection.createStatement();
 	ResultSet resultSet = statement.executeQuery(sql);
 	
 	String name="";
 	int folder=0;
 	int id=0;
 	AllPlaylistsBaseItem apfi=null;
  	while (resultSet.next())
 	{
 	  name=resultSet.getString("name");
 	  folder = resultSet.getInt("folder");
 	  id = resultSet.getInt("id");
 	  if (folder==0)
 		  apfi = new AllPlaylistsPlaylistItem(name);
 	  else
 	    apfi = new AllPlaylistsFolderItem(name);
 	  apfi.setId(id);
 	  apfi.setLevel(resultSet.getInt("level"));
 	  apfi.setParentId(resultSet.getInt("parent"));
 	  apfi.setPosition(resultSet.getInt("position"));
 	  parent.getChildren().add(apfi);
 	  getPlaylistsRecursive(apfi);
 	}
 	if (resultSet!=null) resultSet.close();
 	if (statement!=null) statement.close();
	return apfi;
}

public static AllPlaylistsBaseItem insertPlaylistsItem(String name, String location,  int parent, int level, int position, int folder) 
{
	AllPlaylistsBaseItem apbi=null;
  try
  {
    connect();
    String sql = "insert into playlists (name, location, parent, level, position, folder) "
    +"values('"+name+"', '"
    +location+"', "
    +parent+", "
    +level+", "
    +position+"," +
    +folder+")";
    //System.out.println("sql: "+sql);
     connection.createStatement().execute(sql);
     int id = getMaxId("playlists");
     apbi = getPlaylistItem(id);
     disconnect();
	} catch (Exception e) { e.printStackTrace(); }
  return apbi;
}

public static void insertTrack(TrackDb trackDb, int type) 
{
 int cleanup=0; 
 if (type==SharedValues.CLEANUP) cleanup=1;
    
 String sql="insert into tracks(cleanup, path, pathHash, title, leader, artist, album, duration, track_no, genre, comment, style, track_year) "
     +"values ("+cleanup+", '"+trackDb.path
             +"', '"+trackDb.pathHash
             +"', '"+trackDb.title
             +"', '"+trackDb.leader
             +"', '"+trackDb.artist
             +"', '"+trackDb.album
             +"', "+trackDb.duration
             +", "+trackDb.track_no
             +", '"+trackDb.genre
             +"', '"+trackDb.comment
             +"', '"+trackDb.style
             +"', '"+trackDb.track_year
             +"')";
    
 
 TangoDJ2.feedback.setText("Adding: "+trackDb.album+", "+trackDb.title);
  try 
  {
    if (Db.connection==null)
    System.out.println("TrackLoader3 - connection is null");
    
    if (isSet(trackDb.title)) 
    {
      Db.connection.createStatement().execute(sql);
    }
  } catch (SQLIntegrityConstraintViolationException v) 
  { 
    TangoDJ2.feedback.setText("DUPLICATE"); 
  }
  catch (Exception e) { e.printStackTrace(); }
}

private static boolean isSet(String inStr)
{
   if (inStr==null) return false;
   if (inStr.length()==0) return false;
   return true;
}
	
}

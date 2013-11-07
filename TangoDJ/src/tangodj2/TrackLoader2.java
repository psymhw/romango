package tangodj2;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj2.cleanup.CleanupTable;
import tangodj2.tango.TangoTable;

public class TrackLoader2 
{
 public static final String DRIVER2 ="org.apache.derby.jdbc.EmbeddedDriver";
 public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=false";
 static Connection connection;
  private Timeline timeline;
  int seconds=0;
  int timelineCycles=0;
  
  static int errors=0;
  static int counter=0;
  private static Hasher hasher = new Hasher();
  private static List<TrackMeta> trackInfo = new ArrayList<TrackMeta>();
  public boolean isTango=true;
  TangoTable tangoTable;
  CleanupTable cleanupTable;
	
  public TrackLoader2()
  {
	seconds=0;
	timeline = new Timeline();
	timeline.setCycleCount(Timeline.INDEFINITE);
	
	
	KeyFrame keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
	{
	  public void handle(Event event) 
	  {
	    seconds++;
	    timelineCycles++;
	    if (metaLoaded()) 
	    {
	      timeline.stop();
		    System.out.println("timeline stopped. Cycles: "+timelineCycles);
		    // listTrackInfo();
		    sqlReadyTrackInfo();
		    insertRecords();
		    if (isTango) { tangoTable.reloadData(); }
		    else cleanupTable.reloadData();
		  }
		  if (seconds>=30) 
		  {
		    timeline.stop();
		    System.out.println("ERROR: timeline stopped. Cycles: "+timelineCycles);
		    listTrackInfo();
		  }
	  }});
			      
	  timeline.getKeyFrames().add(keyFrame);
  }
  
  private boolean metaLoaded()
  {
	  TrackMeta trackMeta;
	   int counter=0;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   String tStr;
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.duration==0) return false;
		 if (trackMeta.metaComplete==false) return false;
	  }  
	   return true;
  }
  
  public void process(String inPath, boolean singleFile, boolean isTango) throws ClassNotFoundException, IOException, SQLException
  {
	  this.isTango=isTango;
    trackInfo.clear();
    
    if (singleFile)
    {
      File f = new File(inPath);
      processSingleFile(f.toPath());
    }
    else
    {
	    FileVisitor<Path> fileProcessor = new ProcessFile();
	    Files.walkFileTree(Paths.get(inPath), fileProcessor);
    }	      
	  if (errors==0) System.out.println(" No Errors\n");
		else System.out.println(errors+" Errors\n");
	   
	  getMetaData();
	  timelineCycles=0;
	  timeline.playFromStart();
  }
	 
  
  void getMetaData()
  {
    TrackMeta trackMeta;
	  int counter=0;
	  Iterator<TrackMeta> it = trackInfo.iterator();
	  String tStr;
	  while(it.hasNext())
	  {
	    trackMeta=it.next();
	    new MediaMetaGetter(trackMeta);
    }
  }
  
  
	 void listTrackInfo()
	 {
	   TrackMeta trackMeta;
	   int counter=0;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 System.out.println((counter++)+") title: "+trackMeta.title);
		 System.out.println("  artist: "+trackMeta.artist);
		 System.out.println("  length: "+trackMeta.duration);
	  }
			
	 }
	 
	 private boolean isSet(String inStr)
		{
		   if (inStr==null) return false;
		   if (inStr.length()==0) return false;
		   return true;
		}
	 void sqlReadyTrackInfo()
	 {
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 trackMeta.title    = sqlReadyString(trackMeta.title);
		 trackMeta.artist   = sqlReadyString(trackMeta.artist);
		 trackMeta.album    = sqlReadyString(trackMeta.album);
		 trackMeta.comment  = sqlReadyString(trackMeta.comment);
		 trackMeta.path     = sqlReadyString(trackMeta.path);
		 trackMeta.genre     = sqlReadyString(trackMeta.genre);
		 //trackMeta.path = new File(trackMeta.path).toURI().toString();
		 trackMeta.track_year     = sqlReadyString(trackMeta.track_year);
	  }
			
	 }
	 
	 void insertRecords() 
	 {
		int cleanup=0;  // TODO set this on load
		if (!isTango) cleanup=1;
	try{
		//Db.connect();
	   connection = DriverManager.getConnection(JDBC_URL2);
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 String sql="insert into tracks(cleanup, path, pathHash, title, artist, album, duration, genre, comment, style, track_year) "
		 		+"values ("+cleanup+", '"+trackMeta.path
	              +"', '"+trackMeta.pathHash
	              +"', '"+trackMeta.title
	              +"', '"+trackMeta.artist
	              +"', '"+trackMeta.album
	              +"', "+trackMeta.duration
	              +", '"+trackMeta.genre
	              +"', '"+trackMeta.comment
	              +"', 'Tango"
	              +"', '"+trackMeta.track_year
	              +"')";
		//System.out.println("Db: "+sql);
		 try {
		   // TODO java.sql.SQLIntegrityConstraintViolationException needs to be handled here
		 if (isSet(trackMeta.title)) connection.createStatement().execute(sql);
		 } catch (Exception ex) { System.out.println("SQL ERROR: "+sql); 
		 ex.printStackTrace(); } 
	  }
		connection.close();	
	 // Db.disconnect();
	} catch (Exception e) { e.printStackTrace(); }
}
	  

	 void processDirectory()
	 {
	   FileVisitor<Path> fileProcessor = new ProcessFile();
	 }
	 
	private static final class ProcessFile extends SimpleFileVisitor<Path> 
	{
	  public FileVisitResult visitFile(Path path, BasicFileAttributes aAttrs) throws IOException 
	  {
	    return processSingleFile(path);
	  }
		    
	  public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException 
	  {
		System.out.println("Processing directory:" + aDir);
		return FileVisitResult.CONTINUE;
	  }
	}
		    
	
	
	public static String sqlReadyString(String inStr)
	{
	   String returnStr = inStr.replace("'","''");
	   returnStr = returnStr.replace("ÿþ","");
	  // returnStr = returnStr.replace("\\","\\\\");
	   
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
	
    public static FileVisitResult processSingleFile(Path path)
    {
      String pathStr = path.toString().trim().toLowerCase();
      String pathStr2="";
	  String title="";
	  String artist="";
	  String album="";
	  String comment="";
	  String genre="";
	  String track_year="";
	  
	  String pathHash="";
	  MP3File mp3 = null;
	  AbstractID3v2 tag;
	  AbstractID3v1 tag2;
    
      if (pathStr.endsWith(".mp3")) 
      {
        File file = path.toFile();
        pathStr2=path.toString();
        
        try { mp3= new MP3File(file); } catch (Exception e) { errors++; System.out.println(counter+" Could not create MP3File class: "+pathStr+"\n"); counter++; return FileVisitResult.CONTINUE;  }
        try { tag = mp3.getID3v2Tag();  } catch (Exception e2) {  errors++; System.out.println(counter+" Could not get ID3v2 tag: "+pathStr+"\n"); counter++; return FileVisitResult.CONTINUE;  }
       
        
        title=tag.getSongTitle();
        comment= tag.getSongComment();
        genre=tag.getSongGenre();
        artist=tag.getLeadArtist();
  	    album=tag.getAlbumTitle();
  	    track_year=tag.getYearReleased();
  	    //System.out.println(track_year);
  	 	          
  	    pathStr2=path.toString();
  			    	  
		pathHash = hasher.getMd5Hash(pathStr2.getBytes());
		  
		int duration = 0;
		boolean success=false;
		  
		trackInfo.add(new TrackMeta(title, artist, album, comment, genre, pathHash, pathStr2, track_year));
		  
		counter++;
      }
      else
      {
  	    System.out.println(counter+" Not an MP3 file: "+pathStr+"\n");
      }
      return FileVisitResult.CONTINUE;
    }

    public void setTangoTable(TangoTable tangoTable)
    {
      this.tangoTable = tangoTable;
    }
    
    public void setCleanupTable(CleanupTable cleanupTable)
    {
      this.cleanupTable = cleanupTable;
    }
}

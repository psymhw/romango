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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj2.Hasher;

public class TrackLoader2 
{
  public static final String DRIVER2 ="org.apache.derby.jdbc.EmbeddedDriver";
  public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=false";
  static Connection connection;
 
  static int errors=0;
  static int counter=0;
  private static Hasher hasher = new Hasher();
  private static List<TrackMeta> trackInfo = new ArrayList<TrackMeta>();
 // private boolean needMissingMeta=true;
 // private static SimpleBooleanProperty needMissingMeta = new SimpleBooleanProperty(true);
	
  public TrackLoader2()
  {
	  SharedValues.loadMonitor.set(0); //initiallize counter
		ChangeListener changeListener = new ChangeListener() 
		{
		  public void changed(ObservableValue observable, Object oldValue, Object newValue) 
		  {
			int oldInt = (int)oldValue;
			int newInt = (int)newValue;
			if (newInt<oldInt) 
			{	  
			  if (newInt==0)
			  {
				System.out.println("Assignment Complete.");
				listTrackInfo();
				cleanupTrackInfo();
				insertRecords();
				AllTracksTab.reloadData();
			  }
			}
		  }};
			 
		  SharedValues.loadMonitor.addListener(changeListener);
  }
  
  
  public void process(String inPath) throws ClassNotFoundException, IOException, SQLException
  {
	Class.forName(DRIVER2);
	trackInfo.clear();
	SharedValues.loadMonitor.set(0);
	/*
	 * The SharedValues.loadMonitor is an integer that increments for each mp3 file found.
	 * Most of the info is set in a TrackMeta and added to a list: trackInfo but...
	 * the track length (duration) is set in a lazy fashion by the TimeGetter class
	 * which finds the appropriate TrackMeta in the list, assign the time and decrments the
	 * SharedValues.loadMonitor value. 
	 * 
	 * A change listener on SharedValues.loadMonitor (directly below) detects the value has decreased to 0
	 * which means all of the times are assigned and I can now use the list to add records to the DB.
	 */
	
	
	
	 
	  FileVisitor<Path> fileProcessor = new ProcessFile();

	  Files.walkFileTree(Paths.get(inPath), fileProcessor);
		      
	   if (errors==0) System.out.println(" No Errors\n");
		      else System.out.println(errors+" Errors\n");
	   
	   assignTimes();
		}
	 
  
  void assignTimes()
	 {
	   TrackMeta trackMeta;
	   int counter=0;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   String tStr;
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 SharedValues.loadMonitor.set(SharedValues.loadMonitor.get()+1);
		 new TimeGetter(trackMeta);
		 if (!isSet(trackMeta.title)) { new MediaMetaGetter(trackMeta); }
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
	 void cleanupTrackInfo()
	 {
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 trackMeta.title    = cleanString(trackMeta.title);
		 trackMeta.artist   = cleanString(trackMeta.artist);
		 trackMeta.album    = cleanString(trackMeta.album);
		 trackMeta.comment  = cleanString(trackMeta.comment);
		 trackMeta.path      = cleanString(trackMeta.path);
	  }
			
	 }
	 
	 void insertRecords() 
	 {
	try{	 
	   connection = DriverManager.getConnection(JDBC_URL2);
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 String sql="insert into tracks(path, pathHash, title, artist, album, duration, genre, comment) "
		 		+"values ('"+trackMeta.path
	              +"', '"+trackMeta.pathHash
	              +"', '"+trackMeta.title
	              +"', '"+trackMeta.artist
	              +"', '"+trackMeta.album
	              +"', "+trackMeta.duration
	              +", '"+trackMeta.genre
	              +"', '"+trackMeta.comment
	              +"')";
		 try {
		 if (isSet(trackMeta.title)) connection.createStatement().execute(sql);
		 } catch (Exception ex) { System.out.println("SQL ERROR: "+sql); } 
	  }
		connection.close();	
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
		      //System.out.println(""+counter+": Processing file:" + path);
		      String pathStr = path.toString().trim().toLowerCase();
		      String pathStr2="";
			  String title="";
			  String artist="";
			  String album="";
			  String comment="";
			  String genre="";
			  
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
		    	 	          
		    	  pathStr2=path.toString();
		    	  pathStr2=pathStr2.replace("'","''");
		    	 
		        	 
		    	  
		    	  /*
				  title = title.replace("'","''");
				  artist= artist.replace("'","''");
				  album=album.replace("'","''");
				  comment=comment.replace("'","''");
				  
				  pathStr2=path.toString();
				  pathStr2=pathStr2.replace("'","''");
				  
				  title = title.replace("ÿþ","");
				  artist = artist.replace("ÿþ","");
				  album = album.replace("ÿþ","");
				  
				  
				  char tChar=0;
				  title = removeChar(title, tChar);
				  artist = removeChar(artist, tChar);
				  album = removeChar(album, tChar);
				 */
		    	  
				  pathHash = hasher.getMd5Hash(pathStr2.getBytes());
				  
				  int duration = 0;
				  boolean success=false;
				  
				  trackInfo.add(new TrackMeta(title, artist, album, comment, genre, pathHash, pathStr2));
				  
				//  new TimeGetter(pathStr2, trackInfo, pathHash);	
				 
				 
				 	    		  
		   	  
		    	//	 System.out.println(" inserting: "+sql);
		    	//  SharedValues.loadMonitor.set(SharedValues.loadMonitor.get()+1);
		          counter++;
		       }
		      else
		      {
		    	  System.out.println(counter+" Not an MP3 file: "+pathStr+"\n");
		      }
		      return FileVisitResult.CONTINUE;
		    }
		    
		    public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException 
		    {
		      System.out.println("Processing directory:" + aDir);
		      return FileVisitResult.CONTINUE;
		    }
		}
		    
	public static String cleanString(String inStr)
	{
	   String returnStr = inStr.replace("'","''");
	   returnStr = returnStr.replace("ÿþ","");
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
		    
	
}

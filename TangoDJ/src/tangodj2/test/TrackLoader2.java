package tangodj2.test;

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

	 boolean changed=false;
	 String str="NOT SET";
	 static int errors=0;
	 static int counter=0;
	 private static Hasher hasher = new Hasher();
	 static List<TrackMeta> trackInfo = new ArrayList<TrackMeta>();
	
	 public TrackLoader2(String inPath) throws ClassNotFoundException, IOException, SQLException
		{
		 Class.forName(DRIVER2);
		 
		 SharedValues.loadMonitor.set(0); //initiallize counter
		 ChangeListener changeListener = new ChangeListener() 
		  {
		    public void changed(ObservableValue observable, Object oldValue, Object newValue) 
			{
			  int oldInt = (int)oldValue;
			  int newInt = (int)newValue;
			 /// System.out.println("loadMonitorChange");
			  //if (newInt>oldInt) System.out.println("going up: "+ newInt);
			  if (newInt<oldInt) 
			  {	  
				  //System.out.println("going down: "+newInt);
				  if (newInt==0)
				  {
					  System.out.println("READY");
				  
				  listTrackInfo();
				//  insertRecords();
				  }
			  }
			}
	      };
	      SharedValues.loadMonitor.addListener(changeListener);
			 FileVisitor<Path> fileProcessor = new ProcessFile();

		      Files.walkFileTree(Paths.get(inPath), fileProcessor);
		      
		      if (errors==0) System.out.println(" No Errors\n");
		      else System.out.println(errors+" Errors\n");
		}
	 
	 void listTrackInfo()
	 {
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 System.out.println(trackMeta.duration+" "+trackMeta.title);
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
		 connection.createStatement().execute(sql);
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
		      String pathStr3="";
			  String title="";
			  String artist="";
			  String album="";
			  String comment="";
			  String genre="";
			  
			  String pathHash="";
			  MP3File mp3 = null;
			  String sql="";
			  AbstractID3v2 tag;
			  AbstractID3v1 tag2;
		      
		      if (pathStr.endsWith(".mp3")) 
		      {
		          File file = path.toFile();
		          
		          try { mp3= new MP3File(file); } catch (Exception e) { errors++; System.out.println(counter+" Could not create MP3File class: "+pathStr+"\n"); return FileVisitResult.CONTINUE;  }
		          try { tag = mp3.getID3v2Tag();  } catch (Exception e2) {  errors++; System.out.println(counter+" Could not get ID3v2 tag: "+pathStr+"\n"); return FileVisitResult.CONTINUE;  }
		          
		          
		          title=tag.getSongTitle();
		          comment= tag.getSongComment();
		          genre=tag.getSongGenre();
		          artist=tag.getLeadArtist();
		    	  album=tag.getAlbumTitle();
		    	 
		    	  
		    	  if (title.trim().length()==0)
		    	  {
		    		  errors++; System.out.println(counter+" No title in MP# file for: "+pathStr+"\n"); 
		    		  return FileVisitResult.CONTINUE; 
		    	  }
		          
				  title = title.replace("'","''");
				  artist= artist.replace("'","''");
				  album=album.replace("'","''");
				  
				  pathStr2=path.toString();
				  pathStr2=pathStr2.replace("'","''");
				  
				  title = title.replace("ÿþ","");
				  artist = artist.replace("ÿþ","");
				  album = album.replace("ÿþ","");
				  
				  char tChar=0;
				  title = removeChar(title, tChar);
				  artist = removeChar(artist, tChar);
				  album = removeChar(album, tChar);
				  
				  pathHash = hasher.getMd5Hash(pathStr2.getBytes());
				  
				  int duration = 0;
				  boolean success=false;
				  
				  trackInfo.add(new TrackMeta(title, artist, album, comment, genre, pathHash, pathStr2));
				  
				  new TimeGetter(pathStr2, trackInfo, pathHash);	
				 
				 
				 	    		  
		   	  
		    	//	 System.out.println(" inserting: "+sql);
		    	  SharedValues.loadMonitor.set(SharedValues.loadMonitor.get()+1);
		          counter++;
		       }
		      else
		      {
		    	  System.out.println(counter+" Not an MP3 file: "+pathStr+"\n");
		      }
		      return FileVisitResult.CONTINUE;
		    }
		    
		    @Override  public FileVisitResult preVisitDirectory(
		      Path aDir, BasicFileAttributes aAttrs
		    ) throws IOException {
		      System.out.println("Processing directory:" + aDir);
		      return FileVisitResult.CONTINUE;
		    }
		}
		    public static String removeChar(String s, char c) {

		    	   String r = "";

		    	   for (int i = 0; i < s.length(); i ++) {
		    	      if (s.charAt(i) != c) r += s.charAt(i);
		    	   }

		    	   return r;
		    	}
		    
	
}

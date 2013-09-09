package tangodj2;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
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
import java.util.Iterator;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.FrameBodyPOPM;
import org.farng.mp3.id3.ID3v2_2Frame;


public class TrackLoader 
{
	public static final String DRIVER2 ="org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=false";
	static int counter=1;
	static Connection connection;
	static FileWriter fstream;
	static BufferedWriter out;
	static int errors=0;
	private static Hasher hasher = new Hasher();
	
	public TrackLoader(String inPath) throws ClassNotFoundException, SQLException, IOException
	{
		Class.forName(DRIVER2);
		 FileVisitor<Path> fileProcessor = new ProcessFile();
	      connection = DriverManager.getConnection(JDBC_URL2);

	      fstream = new FileWriter("load.log");
	      out = new BufferedWriter(fstream);
	      out.write(" Processing Directory: "+inPath+"\n");
          
	      Files.walkFileTree(Paths.get(inPath), fileProcessor);
	      
	      connection.close();
	      if (errors==0) out.write(" No Errors\n");
	      else out.write(errors+" Errors\n");
	    	  
	      out.close();
	      System.out.println("OK");  
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
		          
		          
		          try { mp3= new MP3File(file); } catch (Exception e) { errors++; out.write(counter+" Could not create MP3File class: "+pathStr+"\n"); return FileVisitResult.CONTINUE;  }
		          try { tag = mp3.getID3v2Tag();  } catch (Exception e2) {  errors++; out.write(counter+" Could not get ID3v2 tag: "+pathStr+"\n"); return FileVisitResult.CONTINUE;  }
		          
		          
		          title=tag.getSongTitle();
		          comment= tag.getSongComment();
		          genre=tag.getSongGenre();
		          artist=tag.getLeadArtist();
		    	  album=tag.getAlbumTitle();
		    	 
		    	  
		    	  if (title.trim().length()==0)
		    	  {
		    		  errors++; out.write(counter+" No title in MP# file for: "+pathStr+"\n"); 
		    		  return FileVisitResult.CONTINUE; 
		    	  }
		          
		          //int emp =  mp3.getEmphasis();
		          //System.out.println("emp: "+emp);
		          
		         // FrameBodyPOPM popularityFrame = (FrameBodyPOPM)tag.getFrame("POPM").getBody();
		          
		         
		          /*
		          
		          System.out.println("tag.getFrameCount()="+tag.getFrameCount());
		          Iterator frameIt = tag.getFrameIterator();
		         		          
		          while (frameIt.hasNext())
		          {
		              ID3v2_2Frame frame = (ID3v2_2Frame)frameIt.next();
		              String farmeId = frame.getIdentifier();
		              boolean isValid = ID3v2_2Frame.isValidID3v2FrameIdentifier(farmeId);
		              System.out.println(farmeId + " is valid=" + isValid);
		             
		              System.out.println("frame.getBody().getIdentifier()="+frame.getBody().getIdentifier());
		              System.out.println("frame.body="+frame.getBody().toString());
		          }
		          */
		          
		    	
		    	  
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

			
				  
				  //try {
				 // Media  media = new Media(new File(pathStr2).toURI().toString());
		    	 // System.out.println("length: "+media.getDuration());
				//  } catch (Exception exx) { System.out.println("Trouble w media;");}
		    	 
				  
		    	  try { 
		    		  
		    		  sql="insert into tracks(path, pathHash, title, artist, album, genre, comment) values ('"+pathStr2+
		    				                                                     "', '"+pathHash+
		    				                                                     "', '"+title+
		    				                                                     "', '"+artist
		    				                                                     +"', '"+album
		    				                                                     +"', '"+genre
		    				                                                     +"', '"+comment
		    				                                                     +"')";
		    		  
		    		  connection.createStatement().execute(sql);
		    		//  System.out.println(" inserting: "+sql);
		    	      } catch (SQLException e3) 
		    	        { 
		    		      out.write(counter+" Could not write to database: "+pathStr+"\n"); 
		    		      out.write(counter+" "+e3.getMessage()+"\n");
		    		      out.write(counter+" SQL: "+sql+"\n");
		    		      errors++; 
		    		      return FileVisitResult.CONTINUE;  
		    		    }
		          counter++;
		       }
		      else
		      {
		    	// out.write(counter+" Not an MP3 file: "+pathStr+"\n");
		      }
		      return FileVisitResult.CONTINUE;
		    }
		    
		    @Override  public FileVisitResult preVisitDirectory(
		      Path aDir, BasicFileAttributes aAttrs
		    ) throws IOException {
		      System.out.println("Processing directory:" + aDir);
		      return FileVisitResult.CONTINUE;
		    }
		    
		    public static String removeChar(String s, char c) {

		    	   String r = "";

		    	   for (int i = 0; i < s.length(); i ++) {
		    	      if (s.charAt(i) != c) r += s.charAt(i);
		    	   }

		    	   return r;
		    	}
		  }
}

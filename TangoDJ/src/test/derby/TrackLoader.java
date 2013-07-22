package test.derby;

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

import org.farng.mp3.MP3File;



public class TrackLoader 
{
	public static final String DRIVER2 ="org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=true";
	static int counter=1;
	static Connection connection;
	
	
		public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException
		{
		  Class.forName(DRIVER2);
		  
		  String ROOT = "C:\\music\\tango\\Garcia";
		  FileVisitor<Path> fileProcessor = new ProcessFile();
		  
		    
	      connection = DriverManager.getConnection(JDBC_URL2);
	      connection.createStatement().execute("create table tracks(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), path varchar(200), title varchar(100), artist varchar(40))");
	    //  connection.createStatement().execute("create table tracks(id integer, path varchar(200))");
		  
	      Files.walkFileTree(Paths.get(ROOT), fileProcessor);
	      
	      connection.close();
	      System.out.println("OK");  
	    		
		}

	
		private static final class ProcessFile extends SimpleFileVisitor<Path> {
		    @Override public FileVisitResult visitFile(
		      Path path, BasicFileAttributes aAttrs
		    ) throws IOException {
		      System.out.println(""+counter+": Processing file:" + path);
		      
		      
		      String pathStr = path.toString().trim().toLowerCase();
		      
		      if (pathStr.endsWith(".mp3")) 
		      {
		        try 
		        {
		          File file = path.toFile();
				  String title="";
				  String artist="";
		    	  MP3File mp3= new MP3File(file);
		    	  title=mp3.getID3v2Tag().getSongTitle();
		    	  artist=mp3.getID3v2Tag().getLeadArtist();
				  connection.createStatement().execute("insert into tracks(path, title, artist) values ('"+path.toString()+"', '"+title+"', '"+artist+"')");
			    } catch (Exception e) { e.printStackTrace(); }
		        counter++;
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
}

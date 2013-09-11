package tangodj2.test;

import java.io.File;

import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;


public class MP3TagCheck extends Application
{
	static Stage primaryStage;
	File file;
	public static void main(String[] args) 
	{
		 launch(args);
	}
	public void start(Stage stage) 
	{
		  primaryStage=stage;
		  Group root = new Group();
	      Scene scene = new Scene(root, 950, 550, Color.WHITE);
	      Button btn = new Button();
	      btn.setText("Open FileChooser'");
	      btn.setOnAction(new EventHandler<ActionEvent>() {
	 
	          @Override
	          public void handle(ActionEvent event) {
	              FileChooser fileChooser = new FileChooser();
	 
	              //Set extension filter
	              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
	              fileChooser.getExtensionFilters().add(extFilter);
	              fileChooser.setInitialDirectory(new File("C:\\music\\tango"));
	             
	              //Show open file dialog
	              file = fileChooser.showOpenDialog(null);
	             
	              check(file.getPath());
	          }
	      });
			 root.getChildren().add(btn);
		      primaryStage.setScene(scene);
		      primaryStage.show();
			 
	}
	public void check(String pathStr)
	{
		int errors=0;
		int counter=0;
	      String pathStr2="";
		  String title="";
		  String artist="";
		  String album="";
		  String comment="";
		  String genre="";
		  
		  String pathHash="";
		  MP3File mp3 = null;
		  AbstractID3v2 tag;
		//  AbstractID3v1 tag;
	      
	      if (pathStr.endsWith(".mp3")) 
	      {
	          
	    	  File file = new File(pathStr);
	    	  
	          try { mp3= new MP3File(file); } catch (Exception e) { errors++; System.out.println(counter+" Could not create MP3File class: "+pathStr+"\n"); counter++; return;  }
	          try { tag = mp3.getID3v2Tag();  } catch (Exception e2) {  errors++; System.out.println(counter+" Could not get ID3v2 tag: "+pathStr+"\n"); counter++; return;   }
	        //  try { tag = mp3.getID3v1Tag();  } catch (Exception e2) {  errors++; System.out.println(counter+" Could not get ID3v2 tag: "+pathStr+"\n"); counter++; return;   }
		        
	         
	          
	          title=tag.getSongTitle();
	          comment= tag.getSongComment();
	          genre=tag.getSongGenre();
	          artist=tag.getLeadArtist();
	    	  album=tag.getAlbumTitle();
	    	  
	    	  System.out.println("tag: "+tag.getIdentifier());
	    	  System.out.println("title: "+title);
	    	  System.out.println("artist: "+artist);
	    	  System.out.println("genre: "+genre);
	    	  System.out.println("album: "+album);
	    	  System.out.println("comment: "+comment);
	    	
	    	  
	    	  Media media = new Media(file.toURI().toString());
	          media.getMetadata().addListener(
	        		  
	        		  new MapChangeListener<String, Object>() {
	            @Override
	            public void onChanged(Change<? extends String, ? extends Object> ch) {
	              if (ch.wasAdded()) {
	                handleMetadata(ch.getKey(), ch.getValueAdded());
	              }
	            }
	          }
	        		  
	        		  );

	          MediaPlayer mediaPlayer = new MediaPlayer(media);
	    	  
	    	  if (title.trim().length()==0)
	    	  {
	    		  errors++; System.out.println(counter+" No title in MP3 file for: "+pathStr+"\n"); 
	    		  return; 
	    	  }
	          
	    	  pathStr2=pathStr;
	    	  pathStr2=pathStr2.replace("'","''");
	    	  
	    	//  pathStr2 = cleanString(pathStr2);
	    	  title    = cleanString(title);
	    	  artist   = cleanString(artist);
	    	  album    = cleanString(album);
	    	  comment  = cleanString(comment);
	    	  
	    	  
	    	 
	    	  
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
			  
			  */
			  char tChar=0;
			  title = removeChar(title, tChar);
			  artist = removeChar(artist, tChar);
			  album = removeChar(album, tChar);
			 
	    	  
			  
			  int duration = 0;
			  boolean success=false;
			  
	   	  
	    	//	 System.out.println(" inserting: "+sql);
	    	  SharedValues.loadMonitor.set(SharedValues.loadMonitor.get()+1);
	          counter++;
	       }
	      else
	      {
	    	  System.out.println(counter+" Not an MP3 file: "+pathStr+"\n");
	      }	
	}
	
	private void handleMetadata(String key, Object value) 
	{
		System.out.println(key+" : "+value.toString());
		/*
	    if (key.equals("album")) {
	    	System.out.println("album: "+value.toString());
	    } else if (key.equals("artist")) {
	    	System.out.println("artist: "+value.toString());
	    } if (key.equals("title")) {
	    	System.out.println("title: "+value.toString());
	    } if (key.equals("year")) {
	    
	    }
	    */
	  }
	
	public static String cleanString(String inStr)
	{
	   String returnStr = inStr.replace("'","''");
	   returnStr = returnStr.replace("ÿþ","");
	   returnStr = returnStr.replace("\\","\\\\");
	   
	  // char tChar=0;
	  // returnStr = removeChar(returnStr, tChar);
	   
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

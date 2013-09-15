package tangodj2.test;

import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;


public class TreeTest extends Application
{
  static Stage primaryStage;
  File file;
  TreeItem<Text> canaro;
  TreeItem<Text> newitem;
  
  public static void main(String[] args) {  launch(args);}
  
  public void start(Stage stage) 
  {
	primaryStage=stage;
	Group rootGroup = new Group();
	VBox vbox = new VBox();
	rootGroup.getChildren().add(vbox);
	Scene scene = new Scene(rootGroup, 950, 550, Color.WHITE);
	primaryStage.setScene(scene);
	
	Button btn = new Button();
	btn.setText("Add Item");
	btn.setOnAction(new EventHandler<ActionEvent>() 
	{
      public void handle(ActionEvent event) 
      {
    	  newitem = new TreeItem<Text>(new Text("New Item"));
    	  canaro.getChildren().add(newitem);
      }
    });
	      
	Button btn2 = new Button();
	btn2.setText("Remove Item");
	btn2.setOnAction(new EventHandler<ActionEvent>() 
	{
      public void handle(ActionEvent event) 
      {
    	  canaro.getChildren().remove(newitem);
      }
    });
	      
	TreeItem<Text> treeRoot = new TreeItem<Text>(new Text("Test Playlist"));
	treeRoot.setExpanded(true);
	
	canaro = new TreeItem<Text>(new Text("Canaro - VALS"));

	Text track1_text = new Text("Track 1");
	track1_text.setFont(Font.font("Serif", 20));
	TreeItem<Text> track1 = new TreeItem<Text>(track1_text);
	
	canaro.getChildren().add(track1);
	canaro.getChildren().add(new TreeItem<Text>(new Text("Track 2")));
	treeRoot.getChildren().addAll(canaro, 
	          new TreeItem<Text>(new Text("Di Sarli - Tango")),
	          new TreeItem<Text>(new Text("Biagi - Milonga"))
	      );
	//TreeViewWithItems treeView = new TreeViewWithItems(treeRoot);
	TreeView treeView = new TreeView(treeRoot);

	//treeView.setShowRoot(false);
	
	
	
	vbox.getChildren().add(btn);
	vbox.getChildren().add(btn2);
	vbox.getChildren().add(treeView);
	
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

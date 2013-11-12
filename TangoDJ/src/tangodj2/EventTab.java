package tangodj2;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;



import tangodj2.infoWindow.InfoWindow2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class EventTab extends Tab
{
  public	Playlist playlist=null;
  private Player player;
  private Button infoWindowButton = new Button("Info Window");
  HBox hbox =  new HBox();
  VBox eventControls = new VBox();
	
  public EventTab()
  {
    this.setText("Event");
	  try 
	  {
	    playlist = new Playlist(TangoDJ2.prefs.currentPlaylist);
	  } catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
	
	  playlist.getTreeView().setPrefWidth(500);
	
    hbox.setPadding(new Insets(10, 10, 10, 10));
	  hbox.setSpacing(20);
	  hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
    hbox.getChildren().add(playlist.getTreeView());
    
    eventControls.setPadding(new Insets(10, 10, 10, 10));
    eventControls.setSpacing(10);
    
    eventControls.getChildren().add(infoWindowButton);
    hbox.getChildren().add(eventControls);
    setContent(hbox);
    setupListeners();
    
    try
    {
      URL url = getClass().getResource("/resources/sounds/tada.mp3");
      File f = new File (url.toURI());
     // System.out.println("resource: "+f.exists()); 
      Media media = new Media(f.toURI().toString());
      MediaPlayer mp = new MediaPlayer(media);
      Equalizer eq = new Equalizer(mp);
      setEqualizer(eq);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
 
  public void setPlayer(Player player)
  {
    this.player=player;
  }
  
  public void setEqualizer(Equalizer eq)
  {
   if (eventControls.getChildren().size()>1) eventControls.getChildren().remove(1);
   eventControls.getChildren().add(eq.getPane());
  }
 
  private void setupListeners()
  {
	  infoWindowButton.setOnAction(new EventHandler<ActionEvent>() 
	  {
	    public void handle(ActionEvent actionEvent) 
	   {
	     if (player.infoWindow==null)  player.infoWindow=new InfoWindow2(playlist, new ProgressBar());	
	   }
   });
	  
	 // PLAYLIST FOCUS LISTENER
	 ChangeListener playlistFocusListener = new ChangeListener() 
	 {
	   public void changed(ObservableValue observable, Object oldValue, Object newValue) 
	   {
	     player.setPlayMode(Player.PLAYMODE_PLAYLIST);
	     player.setFeaturesMode(Player.PLAYLIST);
	   }};   
	   playlist.playlistFocus.addListener(playlistFocusListener);
   }
  
   public void changePlaylist(int playlistId)
   {
	   try 
	   {
	     playlist = new Playlist(playlistId);
	     hbox.getChildren().remove(0);
	     hbox.getChildren().add(0, playlist.getTreeView());
	  } catch (Exception e) {e.printStackTrace();};
 } 
}

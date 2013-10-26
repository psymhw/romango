package tangodj2;

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

public class EventTab extends Tab
{
  public	Playlist playlist=null;
  private Player player;
  private Button infoWindowButton = new Button("Info Window");
 
	
 public EventTab(Player player)
 {
   this.player=player;
   this.setText("Event");
	try 
	{
	  playlist = new Playlist();
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
	
	playlist.getTreeView().setPrefWidth(500);
	HBox hbox =  new HBox();
    hbox.setPadding(new Insets(10, 10, 10, 10));
	hbox.setSpacing(20);
	 // vbox.getChildren().add(vbox);
	hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
    hbox.getChildren().add(playlist.getTreeView());
  //  vbox.setHgrow(playlist.getTreeView(), Priority.ALWAYS);
    
    VBox eventControls = new VBox();
    eventControls.setPadding(new Insets(10, 10, 10, 10));
    eventControls.setSpacing(10);
   // eventControls.getChildren().add(new Button("Info Window"));
    eventControls.getChildren().add(infoWindowButton);
    hbox.getChildren().add(eventControls);
    setContent(hbox);
    setupListeners();
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
	      }
	    };   
	    playlist.playlistFocus.addListener(playlistFocusListener);
 }
}

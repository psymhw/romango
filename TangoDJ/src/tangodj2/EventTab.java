package tangodj2;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import tangodj2.clock.AnalogueClock;

public class EventTab extends Tab
{
  public	static Playlist playlist=null;
  private Player player;
  
 
  HBox hbox =  new HBox();
  HBox treeBox =  new HBox();
  VBox eventControlsBox = new VBox();
  TreeView treeView;
  EventTab eventTab;
  GridPane infoGrid = new GridPane();
  final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
  final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
  final String BRAND_NAME   = "Tango DJ";
  final double CLOCK_RADIUS = 70;
  Label playlistTimeVal; 
  Label remainingTimeVal;
  final URL tree_stylesheet = getClass().getResource("PlaylistTree/tree.css");

	
  public EventTab()
  {
    this.setText("Event");
    eventTab=this;
	  
	  playlist = new Playlist(TangoDJ2.prefs.currentPlaylist);
	  System.out.println("TangoDJ2 - new Playlist for event tab");
	
	  playlist.getTreeView().setPrefWidth(500);
	
    hbox.setPadding(new Insets(10, 10, 10, 10));
	  hbox.setSpacing(5);
	  hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
	  treeView = playlist.getTreeView();
	  treeBox.getStylesheets().add(tree_stylesheet.toString());
	  treeBox.getChildren().add(treeView);
    hbox.getChildren().add(treeBox);
    
    eventControlsBox.setPadding(new Insets(10, 10, 10, 10));
    eventControlsBox.setSpacing(10);
    
    hbox.getChildren().add(eventControlsBox);
    
    infoGrid.setHgap(5);
    ColumnConstraints col0 = new ColumnConstraints();
    col0.setHalignment(HPos.RIGHT);
    infoGrid.getColumnConstraints().add(col0);
    
    Font labelFont=new Font("Arial", 14);
    
    Label playlistTimeLabel = new Label("Total Time");
    Label remainingTimeLabel = new Label("Time Left");
    
    playlistTimeLabel.setFont(labelFont);
    remainingTimeLabel.setFont(labelFont);
    
    playlistTimeVal = new Label(formatIntoMMSS(playlist.totalPlaylistTimeProperty.get()));
    remainingTimeVal = new Label(formatIntoMMSS(playlist.totalPlaylistTimeProperty.get()));
    
    playlistTimeVal.setFont(labelFont);
    remainingTimeVal.setFont(labelFont);
    
    
    infoGrid.add(playlistTimeLabel, col[0], row[0]);
    infoGrid.add(playlistTimeVal,   col[1], row[0]);
    
    infoGrid.add(remainingTimeLabel, col[0], row[1]);
    infoGrid.add(remainingTimeVal,   col[1], row[1]);
    
    VBox rightPanel = new VBox();
    rightPanel.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work

    
    
    rightPanel.getChildren().add(infoGrid);
    
    final AnalogueClock analogueClock = new AnalogueClock(BRAND_NAME, CLOCK_RADIUS);
    rightPanel.getChildren().add(analogueClock);
    
    hbox.getChildren().add(rightPanel);
    
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
      setEqualizer(eq.getPane());
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    
    
  }
 
  private AnalogueClock makeAnalogueClock() 
  {
    final AnalogueClock analogueClock = new AnalogueClock(BRAND_NAME, CLOCK_RADIUS);
    return analogueClock;
  }
  
  public void setPlayer(Player player)
  {
    this.player=player;
  }
  
  public void reloadPlaylist()
  {
    //int playNext=playlist.getNextTrack();
    treeBox.getChildren().remove(treeView);
    playlist = new Playlist(TangoDJ2.prefs.currentPlaylist);
    playlist.getTreeView().setPrefWidth(500);
    //playlist.setNextTrack(playNext);
    treeView = playlist.getTreeView();
    treeBox.getChildren().add(treeView);
  }
  
  public void setEqualizer(Pane eqPane)
  {
    if (eventControlsBox.getChildren().size()>0)  eventControlsBox.getChildren().remove(0);
   eventControlsBox.getChildren().add(eqPane);
  }
 
  private void setupListeners()
  {
	  
	  
	  
	  
	  /*
	   * Now set in setActiveTab
	 // PLAYLIST FOCUS LISTENER
	 ChangeListener playlistFocusListener = new ChangeListener() 
	 {
	   public void changed(ObservableValue observable, Object oldValue, Object newValue) 
	   {
	     player.setPlayMode(Player.PLAYMODE_PLAYLIST);
	     //player.setMode(Player.PLAYLIST);
	   }
	 };   
	 playlist.playlistFocus.addListener(playlistFocusListener);
	*/ 
	  
// PLAYER PLAYING LISTENER
  ChangeListener playingListener = new ChangeListener() 
  {
    public void changed(ObservableValue observable, Object oldValue, Object newValue) 
    {
      eventTab.setDisable(Player.playing.get());
    }
  };   
  Player.playing.addListener(playingListener);
	   
// TOTAL PLAYLIST TIME LISTENER
 ChangeListener totalPlaylistTimeListener = new ChangeListener() 
 {
   public void changed(ObservableValue observable, Object oldValue, Object newValue) 
   {
     playlistTimeVal.setText(formatIntoMMSS((double)newValue));
   }
 };   
 Playlist.totalPlaylistTimeProperty.addListener(totalPlaylistTimeListener);
 
//REMAINING PLAYLIST TIME LISTENER
ChangeListener remainingPlaylistTimeListener = new ChangeListener() 
{
 public void changed(ObservableValue observable, Object oldValue, Object newValue) 
 {
   remainingTimeVal.setText(formatIntoMMSS((double)newValue));
 }
};   
Playlist.timeLeftProperty.addListener(remainingPlaylistTimeListener);
  
	   
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
   
   
   static String formatIntoMMSS(double millisIn)
   {
     millisIn=millisIn/1000;
     int hours = (int)millisIn / 3600,
     remainder = (int)millisIn % 3600,
     minutes = remainder / 60,
     seconds = remainder % 60;
     DecimalFormat sec = new DecimalFormat( "00" );
     DecimalFormat min = new DecimalFormat( "##" );
     DecimalFormat hr = new DecimalFormat( "##" );
   //return ( (minutes < 10 ? "0" : "") + minutes
   //+ ":" + (seconds< 10 ? "0" : "") + seconds );
   
   return hr.format(hours)+":"+min.format(minutes)+":"+sec.format(seconds);

   }
   
}

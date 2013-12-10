package tangodj2;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tangodj2.clock.AnalogueClock;

public class EventTab extends Tab
{
  public	static Playlist playlist=null;
  private Player player;
 
  HBox leftBox =  new HBox();
  VBox centerVBox = new VBox();
  TreeView treeView;
  
  EventTab eventTab;
  final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
  final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
  final String BRAND_NAME   = "Tango DJ";
  final double CLOCK_RADIUS = 70;
  Label playlistTimeVal; 
  Label remainingPlaylistTimeVal;
  
  Label tandaTotalTime = new Label("0");
  Label tandaRemainTime= new Label("0");
  
  Label playingStyle = new Label("");
  Label playingTitle = new Label("");
  Label playingArtist = new Label("");
  Label playingAlbum = new Label("");
  Label playingSinger = new Label("");
  Label playingComment = new Label("");
  
  /*
  Label selectedStyle = new Label("");
  Label selectedTitle = new Label("");
  Label selectedArtist = new Label("");
  Label selectedAlbum = new Label("");
  Label selectedSinger = new Label("");
  Label selectedComment = new Label("");
  */
  
  final URL tree_stylesheet = getClass().getResource("PlaylistTree/tree.css");
  PlaylistTrack playlistTrack;

	
  public EventTab()
  {
    this.setText("Event");
    eventTab=this;
	  
	  playlist = new Playlist(TangoDJ2.prefs.currentPlaylist);
	  playlistTrack = playlist.getFirstPlaylistTrack();
	  
	  //System.out.println("TangoDJ2 - new Playlist for event tab");
	
	  playlist.getTreeView().setPrefWidth(500);
	
	  treeView = playlist.getTreeView();
	  leftBox.getStylesheets().add(tree_stylesheet.toString());
	  leftBox.getChildren().add(treeView);
	  
    centerVBox.setPadding(new Insets(10, 10, 10, 10));
    centerVBox.setSpacing(10);
    centerVBox.setAlignment(Pos.CENTER);
    
    StackPane rightPanel = new StackPane();
    VBox rightPanelVBox = new VBox();
    
    rightPanel.setPadding(new Insets(5, 5, 5, 5));
    
    final AnalogueClock analogueClock = new AnalogueClock(BRAND_NAME, CLOCK_RADIUS);
    centerVBox.getChildren().add(analogueClock);
    
    rightPanelVBox.setAlignment(Pos.TOP_RIGHT);
    rightPanelVBox.setPadding(new Insets(5));
    
    
    // PLAYING TRACK INFO
    TitleGridPane2 playingTrackInfo = new TitleGridPane2("Playing");
    playingTrackInfo.addRow("Orchestra", playingStyle);
    playingTrackInfo.addRow("Title", playingTitle);
  //  playingTrackInfo.addRow("Orchestra", playingArtist);
    playingTrackInfo.addRow("Album", playingAlbum);
    playingTrackInfo.addRow("Singer", playingSinger);
    playingTrackInfo.addRow("Comment", playingComment);
    rightPanelVBox.getChildren().add(playingTrackInfo);
    
    /*
 // SELECTED TRACK INFO
    TitleGridPane2 selectedTrackInfo = new TitleGridPane2("Selected");
    selectedTrackInfo.addRow("Orchestra", selectedStyle);
    selectedTrackInfo.addRow("Title", selectedTitle);
  //  selectedTrackInfo.addRow("Orchestra", selectedArtist);
    selectedTrackInfo.addRow("Album", selectedAlbum);
    selectedTrackInfo.addRow("Singer", selectedSinger);
    selectedTrackInfo.addRow("Comment", selectedComment);
    rightPanelVBox.getChildren().add(selectedTrackInfo);
    */
    
    // PLAYLIST INFO PANEL
    playlistTimeVal = new Label(formatIntoMMSS(playlist.getTotalPlaylistTime()));
    remainingPlaylistTimeVal = new Label(formatIntoMMSS(playlist.getTotalPlaylistTime()));
    TitleGridPane2 playlistInfo = new TitleGridPane2("Playlist");
    playlistInfo.addTimeRow("Total Time", playlistTimeVal);
    playlistInfo.addTimeRow("Remaining Time", remainingPlaylistTimeVal);
    rightPanelVBox.getChildren().add(playlistInfo);
    
    // TANDA INFO PANEL
    TitleGridPane2 tandaInfo = new TitleGridPane2("Tanda");
    if (playlistTrack!=null)
    {
      tandaTotalTime = new Label(formatIntoMMSS(playlistTrack.tandaInfo.tandaDuration));
      tandaRemainTime = new Label(formatIntoMMSS(playlistTrack.tandaInfo.tandaDuration));
    }
    tandaInfo.addTimeRow("Total Time", tandaTotalTime);
    tandaInfo.addTimeRow("Remaining Time", tandaRemainTime);
    rightPanelVBox.getChildren().add(tandaInfo);
    
    rightPanel.getChildren().add(rightPanelVBox);
    
    
    SplitPane sp = new SplitPane();
    sp.setStyle("-fx-background-color: plum;");
    
    
    sp.getItems().addAll(leftBox, centerVBox, rightPanel);
    sp.setDividerPositions(0.3f, 0.5f, 0.9f);
    
    setContent(sp);
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
 
  /*
  private AnalogueClock makeAnalogueClock() 
  {
    final AnalogueClock analogueClock = new AnalogueClock(BRAND_NAME, CLOCK_RADIUS);
    return analogueClock;
  }
  */
  public void setPlayer(Player player)
  {
    this.player=player;
  }
  
  public void reloadPlaylist()
  {
    //int playNext=playlist.getNextTrack();
    leftBox.getChildren().remove(treeView);
    playlist = new Playlist(TangoDJ2.prefs.currentPlaylist);
    playlist.getTreeView().setPrefWidth(500);
    //playlist.setNextTrack(playNext);
    treeView = playlist.getTreeView();
    leftBox.getChildren().add(treeView);
  }
  
  public void setEqualizer(Pane eqPane)
  {
    if (centerVBox.getChildren().size()>1)  centerVBox.getChildren().remove(1);
   centerVBox.getChildren().add(eqPane);
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
  
  // PLAYLIST FOCUS LISTENER
  ChangeListener playlistFocusListener = new ChangeListener() 
  {
    public void changed(ObservableValue observable, Object oldValue, Object newValue) 
    {
      update();
    }
  };   
  playlist.playlistFocus.addListener(playlistFocusListener);
	 
 /* 
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
   remainingPlaylistTimeVal.setText(formatIntoMMSS((double)newValue));
 }
};   
Playlist.timeLeftProperty.addListener(remainingPlaylistTimeListener);
 */ 
	   
   }
  
  
   public void changePlaylist(int playlistId)
   {
	   try 
	   {
	     playlist = new Playlist(playlistId);
	     leftBox.getChildren().remove(0);
	     leftBox.getChildren().add(0, playlist.getTreeView());
	  } catch (Exception e) {e.printStackTrace();};
 } 
   
   
   private String formatIntoMMSS(double millisIn)
   {
     millisIn=millisIn/1000;
     
     // interesting way to declare integers (variables) below
     int hours = (int)millisIn / 3600,  remainder = (int)millisIn % 3600,  minutes = remainder / 60,  seconds = remainder % 60;
     
     DecimalFormat sec = new DecimalFormat( "00" );
     DecimalFormat min = new DecimalFormat( "##" );
     DecimalFormat hr = new DecimalFormat( "##" );
     
     if (hours>0) return hr.format(hours)+":"+min.format(minutes)+":"+sec.format(seconds);
     else if (minutes>0) return min.format(minutes)+":"+sec.format(seconds);
     else return sec.format(seconds);

   }
   
   public void update()
   {
     // playlist
     this.playlistTrack=playlist.getPlayingPlaylistTrack();
     playlistTimeVal.setText(formatIntoMMSS(playlist.getTotalPlaylistTime())); // in case we changed playlist
     
     // tanda
     tandaTotalTime.setText(formatIntoMMSS(playlistTrack.tandaInfo.tandaDuration));
     
     if (Player.playing.get())
     {
       // playing track
       playingStyle.setText(playlistTrack.artist+" - "+playlistTrack.style);
       playingTitle.setText(playlistTrack.title);
       playingArtist.setText(playlistTrack.artist);
       playingAlbum.setText(playlistTrack.album);
       playingSinger.setText(playlistTrack.singer);
       playingComment.setText(playlistTrack.comment);
     }
     else
     {
       playingStyle.setText("");
       playingTitle.setText("");
       playingArtist.setText("");
       playingAlbum.setText("");
       playingSinger.setText("");
       playingComment.setText("");
     }
     
     /*
     PlaylistTrack selectedTrack = playlist.getSelectedPlaylistTrack();
     if (selectedTrack!=null)
     {  
       selectedStyle.setText(selectedTrack.artist+" - "+selectedTrack.style);
       selectedTitle.setText(selectedTrack.title);
       selectedArtist.setText(selectedTrack.artist);
       selectedAlbum.setText(selectedTrack.album);
       selectedSinger.setText(selectedTrack.singer);
       selectedComment.setText(selectedTrack.comment);
     }
     */
   }
   
   public void updateProgress(double currentTrackTime)
   {
     double playlistProgress=playlist.getTotalPlaylistTime()-(playlistTrack.startTimeInPlaylist+currentTrackTime);
     remainingPlaylistTimeVal.setText(formatIntoMMSS(playlistProgress));
     
     double tandaProgress=playlistTrack.tandaInfo.tandaDuration-(playlistTrack.startTimeInTanda+currentTrackTime);
     tandaRemainTime.setText(formatIntoMMSS(tandaProgress));
    // System.out.println("EventTab - UPDATE PROGRESS");
   }
   
}

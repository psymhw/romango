package tangodj2;

import java.io.File;

import tangodj2.cleanup.CleanupTrack;
import tangodj2.cortina.Cortina;
import tangodj2.cortina.CortinaTable;
import tangodj2.cortina.CortinaTrack;
import tangodj2.infoWindow.InfoWindow2;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Player 
{
    int playerRegHeight=65;
    int playerLargeHeight=150;
   
    //private Duration duration;
    private Slider timeSlider;
    private Slider cortinaTimeSlider;
    private Label playTimeLabel;
    private Label cortinaPlayTimeLabel;
    private Slider volumeSlider;
    private HBox mediaBar;
    private MediaPlayer mediaPlayer;
    
    
   // SimpleIntegerProperty cortinaLengthProperty = new SimpleIntegerProperty();
    
    Button playButton=null;
    Button stopButton=null;
    Button skipButton=null;
    
    Button previousButton=null;
    Button saveCortinaButton=null;
    Button setStartButton = new Button("*");
    Button setStopButton = new Button("*");
  //  Button dummyButton = new Button(" ");

    public static SimpleBooleanProperty playing=new SimpleBooleanProperty(false);
    private Equalizer eq;
    public final static int PLAYLIST_CREATE=1;
    public final static int PLAYLIST=2;
    public final static int EVENT_PLAYLIST=3;
    public final static int CORTINA_CREATE=4;
    private Playlist playlist=null;
    //int nextPlaylistTrack=0;
    EventTab eventTab;
    VBox vbox = new VBox();
    int mode = PLAYLIST_CREATE;
    GridPane cortinaCreatControls;
    Label startPositionLabel=null;
    Label endPositionLabel=null;
  //  Label playPositionLabel=null;
    Label cortinaLengthLabel=null;
    
    Label currentTrackTitleLabel = new Label("");
    
   // Duration currentTrackTime = new Duration(0);
    //private Duration currentTrackDuration = new Duration(0); //??
    String currentTrackHash="";
    String currentTrackTitle="";
    private int currentCortinaId=-1;
    private int premade=0;
   
    
    private Slider startPositionSlider=null;
    private Slider endPositionSlider=null;
   // private Slider playPositionSlider=null;
    
    
    CheckBox fadeInCheckBox = new CheckBox("Fade In");
    CheckBox fadeOutCheckBox = new CheckBox("Fade Out");
    CheckBox delayCheckBox = new CheckBox("3 Sec delay at end");
    private boolean fadeOut=false;
    double holdVolume=.7;
   // Duration stopTime;
    
    final static int PLAYLIST_BUILD_TANGO_TABLE=10;
    final static int PLAYLIST_BUILD_CLEANUP_TABLE=11;
    final static int PLAYLIST_BUILD_CORTINA_TABLE=12;
    final static int PLAYLIST_BUILD_TANDA_TABLE=15;
    final static int CORTINA_CREATE_CLEANUP_TABLE=13;
    final static int CORTINA_CREATE_CORTINA_TABLE=14;
  
    private static final Duration FADE_DURATION = Duration.seconds(3.0);
    private boolean advancedControls=false;
   // private Duration cortinaStart;
   // private Duration cortinaEnd;
   // private Duration cortinaLength;
    private boolean cortina=false;
    
    public final static int PLAYMODE_SINGLE_TRACK = 20;
    final static int PLAYMODE_PLAYLIST = 21;
    final static int PLAYMODE_CORTINA_SINGLE_TRACK = 22;
    final static int PLAYMODE_CLEANUP_TO_CORTINA_TRACK = 23;
    final static int PLAYMODE_PREMADE_CORTINA_SINGLE_TRACK = 24;
    final static int PLAYMODE_TANDA = 25;
    
    public final static int PLAYLIST_BUILDER_TAB = 0;
    public final static int EVENT_TAB = 1;
    public final static int PLAYLIST_CHOICE_TAB = 2;
    public final static int CORTINA_CREATE_TAB = 3;
    public final static int FAVORITES_TAB = 4;
    
    
    private int playMode=0;
    CleanupTrack currentCleanupTrack=null;
    TextField cortinaTitleOverride = new TextField("");
    Label cortinaOriginalTitle = new Label("");
    
    final private static int INSERT = 0;
    final private static int UPDATE = 1;
    int cortinaMode=INSERT;
    private int active_tab=0;
    private long currentTandaId=0;
    private int currentTandaIndex=-1;
    
   public void setCurrentTandaIndex(int currentTandaIndex) {
		this.currentTandaIndex = currentTandaIndex;
	}

public void setCurrentTandaId(long currentTandaId) {
		this.currentTandaId = currentTandaId;
	}

	// SimpleStringProperty source = new SimpleStringProperty();
    public static InfoWindow2 infoWindow;

    public Player(EventTab eventTab) 
    {
      this.eventTab=eventTab;
     
      HBox currentTrackBox = new HBox();
      currentTrackBox.setPadding(new Insets(5, 0, 5, 20));
      currentTrackTitleLabel.setFont(new Font("Arial", 18));
     // currentTrackTitleLabel.setStyle("-fx-background-color:rgba(250, 69, 208,.45);");
      currentTrackTitleLabel.setStyle("-fx-background-color: #bfc2c7;");
     // currentTrackTitleLabel.setMinHeight(25);
    //  currentTrackTitleLabel.setMaxHeight(25);
    //  currentTrackTitleLabel.setPrefHeight(25);
      
     
      currentTrackBox.getChildren().add(currentTrackTitleLabel);
    
      
      //vbox.setStyle("-fx-background-color: DAE6F3; -fx-border-color: RED; -fx-border-style: SOLID; -fx-border-width: 2px;");     
      vbox.setStyle("-fx-background-color: #bfc2c7;");
      
     // setupListeners();
      mediaBar = new HBox();
      mediaBar.setSpacing(5);
     // mediaBar.setStyle("-fx-background-color: #bfc2c7;");
      mediaBar.setAlignment(Pos.CENTER);
      mediaBar.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 1px;");   

      int top=7;
      int bottom=5;
      int right=5;
      int left=5;
      mediaBar.setPadding(new Insets(top, right, bottom, left));

      setupButtons();
      
      mediaBar.getChildren().add(previousButton);
      mediaBar.getChildren().add(stopButton);
      mediaBar.getChildren().add(playButton);
      mediaBar.getChildren().add(skipButton);
      Label spacer = new Label("   ");
      mediaBar.getChildren().add(spacer);
   
      Label timeLabel = new Label("Time: ");
      mediaBar.getChildren().add(timeLabel);
 
      timeSlider = new Slider(0, 100, 0);
      timeSlider.setMajorTickUnit(10);
      timeSlider.setShowTickMarks(true);
      
      cortinaTimeSlider = new Slider(0, 100, 0);
      //cortinaTimeSlider.setMajorTickUnit(30);
      //cortinaTimeSlider.setShowTickMarks(true);
      
      HBox.setHgrow(timeSlider, Priority.ALWAYS);
      timeSlider.setMinWidth(50);
      timeSlider.setMaxWidth(Double.MAX_VALUE);
     // timeSlider.valueProperty().bindBidirectional(currentTimeValue);
      
       mediaBar.getChildren().add(timeSlider);

       playTimeLabel = new Label();
       playTimeLabel.setPrefWidth(130);
       playTimeLabel.setMinWidth(50);
       mediaBar.getChildren().add(playTimeLabel);

       Label volumeLabel = new Label("Vol: ");
       mediaBar.getChildren().add(volumeLabel);

       volumeSlider = new Slider(0, 1, 0);
       volumeSlider.setPrefWidth(70);
       volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
       volumeSlider.setMinWidth(30);
       //volumeSlider.setValue(50);
       volumeSlider.setValue(.7);
       
       /*
       volumeSlider.valueProperty().addListener(new InvalidationListener() 
       {
         public void invalidated(Observable ov) 
         {
           if (volumeSlider.isValueChanging()) 
           {
            // System.out.println("Playes, volumeSlider invalidation");
              mediaPlayer.setVolume(volumeSlider.getValue() );
           }
         }
       });
      */
       mediaBar.getChildren().add(volumeSlider);
       
       
       timeSlider.valueProperty().addListener(new InvalidationListener() 
       {
         public void invalidated(Observable ov) 
         {
           if (timeSlider.isValueChanging()) 
           {
             // multiply duration by percentage calculated by slider position
              mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(timeSlider.getValue() / 100.0));
           }
         }
       });
       
       
       vbox.getChildren().add(currentTrackBox);
       vbox.getChildren().add(mediaBar);
      // VBox.setVgrow(currentTrackTitleLabel, Priority.ALWAYS);
       
       setupCortinaControls();
      // setRegHeight();
      // setDefaultTrack();
       
       
    }

    private void setupButtons()
    {
      previousButton = new Button("<<");
      previousButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          stopPlaying();
          
          if (playMode==PLAYMODE_PLAYLIST)
          {
            playlist.skip();
            playPreviousTrack();
          }
          if (playMode==PLAYMODE_TANDA)
          {
             currentTandaIndex--;
             playTanda();
          }
        }
      });
      
      skipButton = new Button(">>");
      skipButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          if (active_tab==EVENT_TAB)
          {
            skipButton.setDisable(true);
            final Timeline fadeOutTimeline = new Timeline(
              new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), 0.0)));
            fadeOutTimeline.setOnFinished(new EventHandler() 
            {
              public void handle(Event arg0)
              {
                stopPlaying();
              
                 if (playlist.getPlayingTrack()==playlist.getNextTrack()) 
                   playlist.setNextTrack(playlist.getNextTrack()+1);
                 playlist.skip();
                playPlaylist();
                skipButton.setDisable(false);
              }
            });
            fadeOutTimeline.play();
          }
          else
          {
            stopPlaying();
            
            if (playMode==PLAYMODE_PLAYLIST)
            {
              if (playlist.getPlayingTrack()==playlist.getNextTrack()) 
              playlist.setNextTrack(playlist.getNextTrack()+1);
              playlist.skip();
              playPlaylist(); 
            }
            
            if (playMode==PLAYMODE_TANDA)
            {
               currentTandaIndex++;
               playTanda();
            }
          }
        
        }
      });
      
      playButton = new Button(">");
      stopButton = new Button("Stop");
      stopButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          stopPlaying();
          playlist.stopPlaying();
        }
      });
      
      stopButton.setDisable(true);

      playButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
        	
        	if (playMode==PLAYMODE_TANDA)
            {
              if (mediaPlayer == null) 
              {  
                playTanda();
              }
              else
              {  
                Status status = mediaPlayer.getStatus();
                if (status == Status.PLAYING) pauseTrack();
                else if (status == Status.PAUSED) resumeTrack();
                else playTanda();
              }
            }
          if (playMode==PLAYMODE_SINGLE_TRACK)
          {
            if (mediaPlayer == null) 
            {  
              playTrack();
            }
            else
            {  
              Status status = mediaPlayer.getStatus();
              if (status == Status.PLAYING) pauseTrack();
              else if (status == Status.PAUSED) resumeTrack();
              else playTrack();
            }
          }
          
          if (playMode==PLAYMODE_PLAYLIST)
          {
            if (mediaPlayer == null) 
            {  
              playPlaylist();
            }
            else
            {
              Status status = mediaPlayer.getStatus();
              if (status == Status.PLAYING) pauseTrack();
              else if (status == Status.PAUSED) resumeTrack();
              else playPlaylist();
            }
          }
          
          if (playMode==PLAYMODE_CORTINA_SINGLE_TRACK)
          {
           // System.out.println("Player, PLAY_CORTINA");
            playCortina();
          }
          
          if (playMode==PLAYMODE_PREMADE_CORTINA_SINGLE_TRACK)
          {
            System.out.println("Player, PLAY_PREMADE_CORTINA");
            playPremadeCortina();
          }
          
          if (playMode==PLAYMODE_CLEANUP_TO_CORTINA_TRACK)
          {
           // System.out.println("Player, PLAY_CORTINA");
            playCleanupToCortinaTrack();
          }
          
        }
      });
    }
    
    
    public void showAdvancedControls(boolean show)
    {
      
      if (show)
      {
        advancedControls=true;
        vbox.getChildren().add(cortinaCreatControls);
       // setLargeHeight();
       // System.out.println("Player - vbox size: "+vbox.getChildren().size());
      }
      else 
      {
        advancedControls=false;
        vbox.getChildren().remove(cortinaCreatControls);
        //setRegHeight();
       // System.out.println("Player - vbox size*: "+vbox.getChildren().size());
      }
    }
    
    
    
    GridPane setupCortinaControls()
    {
      final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
      final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
      cortinaCreatControls = new GridPane();
      cortinaCreatControls.setPadding(new Insets(10, 10, 10, 10));
      cortinaCreatControls.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 1px;"); // border doesn't work
      cortinaCreatControls.setHgap(15);
      cortinaPlayTimeLabel = new Label();
      cortinaPlayTimeLabel.setPrefWidth(130);
      cortinaPlayTimeLabel.setMinWidth(50);
      cortinaTitleOverride.setPrefWidth(250);
     
      saveCortinaButton = new Button("Save Cortina");
      
      saveCortinaButton.setOnMouseClicked(new EventHandler<MouseEvent>() 
      {
        public void handle(MouseEvent event) 
        { 
          if (cortinaMode==INSERT)
          {
            insertCortina();
          }
          else  if (cortinaMode==UPDATE) 
          {  
            if (premade==1) updatePremadeCortina();
            else
            updateCortina();
          }
        } 
      });
      
      setStartButton.setPrefWidth(30);
      setStopButton.setPrefWidth(30);
     // dummyButton.setPrefWidth(30);
      
      startPositionSlider = new Slider(0, 1, 0);
      endPositionSlider = new Slider(0, 1, 1);
    //  playPositionSlider = new Slider(0, 1, 0);
      
     // fadeInCheckBox.setSelected(true);
     // fadeOutCheckBox.setSelected(true);
      delayCheckBox.setSelected(false);
      
      startPositionLabel = new Label("00:00");
      startPositionLabel.setPrefWidth(40);
      // System.out.println("Default Track Duration2: "+formatTime(currentTrackDuration));
      endPositionLabel = new Label("");
  //    playPositionLabel = new Label(Double.toString(playPositionSlider.getValue()));
      cortinaLengthLabel=new Label("0:00");
      cortinaLengthLabel.setFont(new Font("Arial", 16));
      
      cortinaCreatControls.add(new Text("Start Position"), col[0], row[0]);
      cortinaCreatControls.add(new Text("End Position"),   col[0], row[1]);
      cortinaCreatControls.add(new Text("Cortina Length"), col[0], row[2]);
      
      cortinaCreatControls.add(startPositionLabel,         col[1], row[0]);
      cortinaCreatControls.add(endPositionLabel,           col[1], row[1]);
     // cortinaCreatControls.add(cortinaLengthLabel,          col[1], row[2]);
      
      cortinaCreatControls.add(startPositionSlider,        col[2], row[0]);
      cortinaCreatControls.add(endPositionSlider,          col[2], row[1]);
      cortinaCreatControls.add(cortinaLengthLabel,         col[2], row[2]);
      
      cortinaCreatControls.add(setStartButton,             col[3], row[0]);
      cortinaCreatControls.add(setStopButton,              col[3], row[1]);
     // cortinaCreatControls.add(dummyButton,                col[3], row[2]);
      
      cortinaCreatControls.add(fadeInCheckBox,             col[4], row[0]);
      cortinaCreatControls.add(fadeOutCheckBox,            col[4], row[1]);
      cortinaCreatControls.add(delayCheckBox,              col[4], row[2]);
      
      cortinaCreatControls.add(new Text("Cortina Time:"),  col[5], row[0]);
      cortinaCreatControls.add(new Text("Cortina Title:"), col[5], row[1]);
      cortinaCreatControls.add(new Text("Original Title:"),col[5], row[2]);
      
      cortinaCreatControls.add(cortinaTimeSlider           ,col[6], row[0]);
      cortinaCreatControls.add(cortinaTitleOverride        ,col[6], row[1]);
      cortinaCreatControls.add(cortinaOriginalTitle        ,col[6], row[2]);
      
      cortinaCreatControls.add(cortinaPlayTimeLabel        ,col[7], row[0]);
      cortinaCreatControls.add(saveCortinaButton           ,col[7], row[1]);
      
      startPositionSlider.valueProperty().addListener(new ChangeListener<Number>() 
      {
          public void changed(ObservableValue<? extends Number> arg0,  Number arg1, Number arg2) 
          {
            //System.out.println("startSlidertPos: "+arg1+", "+arg2);
            double positionMillis = getPositionMillis(arg2.doubleValue(), currentCleanupTrack.getIntDuration()*1000);
            //  System.out.println("Player - positionMillis: "+positionMillis);
            startPositionLabel.setText(formatTime(new Duration(positionMillis/1000)));
            updateCortinaLengthLabel();
            
          }
      });
      
     setStartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event)  
        {
          startPositionSlider.setValue(timeSlider.getValue()/100);
        }
     });
      
     
      endPositionSlider.valueProperty().addListener(new ChangeListener<Number>() 
      {
          public void changed(ObservableValue<? extends Number> arg0,
                  Number arg1, Number arg2) 
          {
            double positionMillis = getPositionMillis(arg2.doubleValue(), currentCleanupTrack.getIntDuration()*1000);
            //System.out.println("Player - positionMillis: "+positionMillis);
            endPositionLabel.setText(formatTime(new Duration(positionMillis/1000)));
            updateCortinaLengthLabel();
          }
      });
      
      setStopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event)  
        {
          endPositionSlider.setValue(timeSlider.getValue()/100);
        }
     });
      
      setCortinaControlsActive(false);
      
      return cortinaCreatControls;
    }
    
    public void setCortinaControlsActive(boolean b)
    {
      startPositionSlider.setDisable(!b);
      endPositionSlider.setDisable(!b);
      setStartButton.setDisable(!b);
      setStopButton.setDisable(!b);
      delayCheckBox.setDisable(!b);
      fadeInCheckBox.setDisable(!b);
      fadeOutCheckBox.setDisable(!b); 
      saveCortinaButton.setDisable(!b);
      cortinaTimeSlider.setDisable(!b);
      cortinaTitleOverride.setDisable(!b);
      setPlayerControlsActive(b);
    }
    
    public void setPremadeCortinaControlsActive(boolean b)
    {
      startPositionSlider.setDisable(b);
      endPositionSlider.setDisable(b);
      setStartButton.setDisable(!b);
      setStopButton.setDisable(!b);
      delayCheckBox.setDisable(b);
      fadeInCheckBox.setDisable(b);
      fadeOutCheckBox.setDisable(b); 
      saveCortinaButton.setDisable(!b);
      cortinaTimeSlider.setDisable(b);
      cortinaTitleOverride.setDisable(!b);
      setPlayerControlsActive(b);
    }
    
    public void setPlayerControlsActive(boolean b)
    {
      timeSlider.setDisable(!b);
      playButton.setDisable(!b);
      skipButton.setDisable(!b);
      previousButton.setDisable(!b);
      stopButton.setDisable(!b);
    }

    protected double getPositionMillis(double sliderPos, int totalLength)
    {
      // TODO Auto-generated method stub
      return sliderPos*totalLength;
    }

    private void updateCortina()
    {
      Cortina cortina = new Cortina();
      cortina.id=currentCortinaId;
      
      
      
      cortina.start=(int)(getPositionMillis(startPositionSlider.getValue(), currentCleanupTrack.getIntDuration()));
      cortina.stop=(int)(getPositionMillis(endPositionSlider.getValue(), currentCleanupTrack.getIntDuration()));
      if (fadeInCheckBox.isSelected()) cortina.fadein=1; else cortina.fadein=0;
      if (fadeOutCheckBox.isSelected()) cortina.fadeout=1; else cortina.fadeout=0;
      if (delayCheckBox.isSelected()) cortina.delay=1; else cortina.delay=0;
      cortina.hash=currentCleanupTrack.getPathHash();
      cortina.path=currentCleanupTrack.getPath().get();
      cortina.album=currentCleanupTrack.getAlbum();
      cortina.artist=currentCleanupTrack.getArtist();
      cortina.premade=0;
      cortina.original_duration=currentCleanupTrack.getIntDuration();
     // String cortinaTimes = " ("+cortinaLength.getText()+") "
     //                          +startPositionLabel.getText()+" - "
     //                          +endPositionLabel.getText();
      if (cortinaTitleOverride.getLength()>60)
      cortina.title=cortinaTitleOverride.getText().substring(0, 60);
      else cortina.title=cortinaTitleOverride.getText();
     try {
           Db.updateCortina(cortina);
           CortinaTable.reloadData();
     } catch (Exception e) { e.printStackTrace(); }
     
     setCortinaControlsActive(false);
    }
    
    private void updatePremadeCortina()
    {
      Cortina cortina = new Cortina();
      cortina.id=currentCortinaId;
      if (cortinaTitleOverride.getLength()>60)
        cortina.title=cortinaTitleOverride.getText().substring(0, 60);
        else cortina.title=cortinaTitleOverride.getText();
     
     try {
           Db.updatePremadeCortina(cortina);
           CortinaTable.reloadData();
     } catch (Exception e) { e.printStackTrace(); }
     
     setPremadeCortinaControlsActive(false);
    }
    
    private void insertCortina()
    {
      Cortina cortina = new Cortina();
      cortina.start=(int)(getPositionMillis(startPositionSlider.getValue(), currentCleanupTrack.getIntDuration()));
      cortina.stop=(int)(getPositionMillis(endPositionSlider.getValue(), currentCleanupTrack.getIntDuration()));
      if (fadeInCheckBox.isSelected()) cortina.fadein=1; else cortina.fadein=0;
      if (fadeOutCheckBox.isSelected()) cortina.fadeout=1; else cortina.fadeout=0;
      if (delayCheckBox.isSelected()) cortina.delay=1; else cortina.delay=0;
      cortina.hash=currentCleanupTrack.getPathHash();
      cortina.path=currentCleanupTrack.getPath().get();
      cortina.album=currentCleanupTrack.getAlbum();
      cortina.artist=currentCleanupTrack.getArtist();
      cortina.premade=0;
      cortina.original_duration=currentCleanupTrack.getIntDuration();
     // String cortinaTimes = " ("+cortinaLength.getText()+") "
     //                          +startPositionLabel.getText()+" - "
     //                          +endPositionLabel.getText();
      if (cortinaTitleOverride.getLength()>60)
      cortina.title=cortinaTitleOverride.getText().substring(0, 60);
      else cortina.title=cortinaTitleOverride.getText();
     try {
           int cortinaId=Db.insertCortina(cortina);
           CortinaTable.addTrack(cortinaId);
     } catch (Exception e) { e.printStackTrace(); }
     
      cortinaTitleOverride.setText("");
    }
    
    public void setActiveTab(int active_tab)
    {
      this.active_tab=active_tab;
      if (active_tab==EVENT_TAB) { setPlaylist(EventTab.playlist); setPlayMode(Player.PLAYMODE_PLAYLIST); }
      else if (active_tab==PLAYLIST_BUILDER_TAB) setPlaylist(PlaylistBuilderTab.playlist);
      
      if (active_tab==CORTINA_CREATE_TAB) showAdvancedControls(true);
      else showAdvancedControls(false);
    }
    
    public int getActiveTab()
    {
	 return active_tab;
    }
    
    
    public VBox get()
    {
      return vbox;
    }
    
    private void pauseTrack()
    {
      mediaPlayer.pause();
      playButton.setText(">");
    }
    
    private void resumeTrack()
    {
      mediaPlayer.play();
    }
    
    
    
    public void playPreviousTrack()
    {
      playlist.setPrevious();
      playPlaylist();
    }
    
    public void playPlaylistDelayed(final PlaylistTrack playlistTrack)
    {
      currentTrackTitleLabel.setText("Delay 3");
      final Timeline timeline = new Timeline();
      timeline.setCycleCount(3);
      
      KeyFrame keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
      {
        int seconds=3;
        public void handle(Event event) 
        {
           currentTrackTitleLabel.setText("Delay "+seconds);
           seconds--;
         }});
              
        timeline.setOnFinished(new EventHandler() 
        {
          public void handle(Event arg0)
          {
            playlistTrack.baseTreeItem.setPlayingImage(false);
            if (playlist.getPlayingTrack()==playlist.getNextTrack()) 
                playlist.setNextTrack(playlist.getNextTrack()+1);
            playPlaylist();
          }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }
    
    
    
    public void playCleanupToCortinaTrack()
    {
      String sourcePath=null;
      cortina=false;
      
      if (currentTrackHash==null) return;
      TrackDb trackDb=Db.getTrackInfo(currentTrackHash);
      sourcePath=trackDb.path;
      currentTrackTitle=trackDb.title;
      
      playing.set(true);
      playButton.setText("||");
      stopButton.setDisable(false);


      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
   
      mediaPlayer.setVolume(holdVolume);
      mediaPlayer.setStartTime(new Duration(currentCleanupTrack.getIntDuration()*startPositionSlider.getValue()));
      mediaPlayer.setStopTime(new Duration(currentCleanupTrack.getIntDuration()*endPositionSlider.getValue()));
   
      // FADE IN
      if (fadeInCheckBox.isSelected()) fadeIn();
      // FADE OUT
      if (fadeOutCheckBox.isSelected()) 
        fadeOut=true; 
        else fadeOut=false;
     

     // eq = new Equalizer(mediaPlayer);
     // eventTab.setEqualizer(eq.getGridPane());
      
      // IMPORTANT
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) 
        {
          updateCortinaValues();
        }
      });
   
     
      mediaPlayer.setOnPaused(new Runnable() 
      {
        public void run() 
        {
         // System.out.println("Playlist - mediaPlayer.setOnPaused");
          //System.out.println("onPaused");
          //playButton.setText(">");
        }
    });

    mediaPlayer.setOnReady(new Runnable() 
    {
      public void run() 
      {
       // System.out.println("Playlist - mediaPlayer.setOnReady");
       // currentTrackDuration = mediaPlayer.getMedia().getDuration();
        updateCortinaValues();
      }
    });

    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
         stopPlaying();
         return;
      }
    });
  }
    
    
    
    public void playTrack()
    {
      String sourcePath=null;
      cortina=false;
      System.out.println("Player - playTrack()");
      if (currentTrackHash==null) return;
      TrackDb trackDb=Db.getTrackInfo(currentTrackHash);
      sourcePath=trackDb.path;
      currentTrackTitle=trackDb.title;
      
      playing.set(true);
      playButton.setText("||");
      stopButton.setDisable(false);


      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(holdVolume);
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
      

      //eq = new Equalizer(mediaPlayer);
      //eventTab.setEqualizer(eq);
      
      // IMPORTANT
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) 
        {
          updateValues();
        }
      });
   
     
      mediaPlayer.setOnPaused(new Runnable() 
      {
        public void run() 
        {
         // System.out.println("Playlist - mediaPlayer.setOnPaused");
          //System.out.println("onPaused");
          //playButton.setText(">");
        }
    });

    mediaPlayer.setOnReady(new Runnable() 
    {
      public void run() 
      {
       // System.out.println("Playlist - mediaPlayer.setOnReady");
       // currentTrackDuration = mediaPlayer.getMedia().getDuration();
        updateValues();
      }
    });

    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
         stopPlaying();
         return;
      }
    });
  }
    
    public void playPlaylist()
    {
      String sourcePath=null;
      fadeOut=false;
      volumeSlider.setValue(holdVolume);
      
      int trackToPlay=playlist.getNextTrack();
      //System.out.println("Player - track to play: "+trackToPlay);
      final PlaylistTrack playlistTrack=playlist.getTrack(trackToPlay);
      if (playlistTrack==null) return;
      playlist.setPlayingTrack(playlist.getNextTrack());
      if (!playlistTrack.tandaInfo.tandaTreeItem.isExpanded()) playlistTrack.tandaInfo.tandaTreeItem.setExpanded(true);
      
     // playlistTrack.baseTreeItem.setPlayingImage(true);
     // playlistTrack.playing=true;
      playing.set(true);
      if (active_tab==EVENT_TAB)
      {
        if (infoWindow!=null) infoWindow.update();
        eventTab.update();
      }
      
      cortina = playlistTrack.cortina;
      premade = playlistTrack.premade;
      sourcePath=playlistTrack.path;
      
     
      playButton.setText("||");
      stopButton.setDisable(false);

      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      
      if (cortina)
      {
       if (premade==0) 
       {  
    	   mediaPlayer.setStartTime(new Duration(playlistTrack.startValue));
    	   mediaPlayer.setStopTime(new Duration(playlistTrack.stopValue));
         // FADE IN
         if (playlistTrack.fadein==1) fadeIn();
         // FADE OUT
         if (playlistTrack.fadeout==1) fadeOut=true; else fadeOut=false;
         System.out.println("Player - fadeOut: "+fadeOut);
       }
      }
       
      setCurrentTrackTitle(playlistTrack.title);
      
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());

      if (active_tab==EVENT_TAB)
      {
        eq = new Equalizer(mediaPlayer);
        eventTab.setEqualizer(eq.getPane());
      }
     
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) { updateValues(); } });
     
      mediaPlayer.setOnPaused(new Runnable() 
      {
        public void run() { playButton.setText(">"); } });

      mediaPlayer.setOnReady(new Runnable() 
      {
        public void run() { updateValues(); } });
    
      mediaPlayer.setOnStopped(new Runnable() 
      {
        public void run() 
       {
          playlistTrack.baseTreeItem.setPlayingImage(false);
          playlistTrack.playing=false;
        }
     });
      
    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
        stopPlaying();
       
        
        if (playlistTrack.delay) playPlaylistDelayed(playlistTrack);
        else 
        {  
          playlistTrack.baseTreeItem.setPlayingImage(false);
          if (playlist.getPlayingTrack()==playlist.getNextTrack()) 
              playlist.setNextTrack(playlist.getNextTrack()+1);
          playPlaylist();
        }
      }
    });
  }
    
    public void playTanda()
    {
      String sourcePath=null;
      fadeOut=false;
      volumeSlider.setValue(holdVolume);
      
      TandaDb tandaDb = Db.getTanda(currentTandaId);
      
      playing.set(true);
         
      TrackDb trackDb=null;
      cortina=false;
      premade=0;
      switch (currentTandaIndex)
      {
        case 0: trackDb = Db.getTrackInfo(tandaDb.getTrackHash_0());  break;
        case 1: trackDb = Db.getTrackInfo(tandaDb.getTrackHash_1());  break;
        case 2:	trackDb = Db.getTrackInfo(tandaDb.getTrackHash_2());  break;
        case 3:	trackDb = Db.getTrackInfo(tandaDb.getTrackHash_3());  break;
        case 4:	trackDb = Db.getTrackInfo(tandaDb.getTrackHash_4());  break;
        case 5: trackDb = Db.getTrackInfo(tandaDb.getTrackHash_5());  break;
        case 6: trackDb = Db.getTrackInfo(tandaDb.getTrackHash_6());  break;
        case 7:	trackDb = Db.getTrackInfo(tandaDb.getTrackHash_7());  break;
        case 8:	trackDb = Db.getTrackInfo(tandaDb.getTrackHash_8());  break;
        case 9:	trackDb = Db.getTrackInfo(tandaDb.getTrackHash_9());  break;
      }
      
      CortinaTrack cortinaTrack=null;
      if (trackDb==null) 
      {
    	if (tandaDb.getCortinaId()>0)  
    	{
    	  cortinaTrack=Db.getCortinaTrack((int)tandaDb.getCortinaId());
    	  trackDb = cortinaTrack.getTrackDb();
    	  cortina=true;
    	  premade=cortinaTrack.getPremade();
    	}
      }
      
      if (trackDb==null)
      {
    	 currentTandaIndex=0; 
    	 return;
      }
      
      sourcePath=trackDb.path;
     
      playButton.setText("||");
      stopButton.setDisable(false);

      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      
      
      if (cortina)
      {
       if (premade==0) 
       {  
    	   mediaPlayer.setStartTime(new Duration(cortinaTrack.getStartValue()));
    	   mediaPlayer.setStopTime(new Duration(cortinaTrack.getStopValue()));
         // FADE IN
         if (cortinaTrack.getFadein()==1) fadeIn();
         // FADE OUT
         if (cortinaTrack.getFadeout()==1) fadeOut=true; else fadeOut=false;
         System.out.println("Player - fadeOut: "+fadeOut);
       }
      }
       
      setCurrentTrackTitle(trackDb.title);
      
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());

          
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) { updateValues(); } });
     
      mediaPlayer.setOnPaused(new Runnable() 
      {
        public void run() { playButton.setText(">"); } });

      mediaPlayer.setOnReady(new Runnable() 
      {
        public void run() { updateValues(); } });
    
      mediaPlayer.setOnStopped(new Runnable() 
      {
        public void run() 
       {
          //playlistTrack.baseTreeItem.setPlayingImage(false);
         // playlistTrack.playing=false;
        }
     });
      
    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
        stopPlaying();
        if (cortina) 
        { 
          cortina=false; 
          currentTandaIndex=0; 
          return;  
        }
        currentTandaIndex++;
        playTanda();
      }
    });
  }
    
    private void stopPlaying()
    {
      if (active_tab==EVENT_TAB)
      {
        eq.clear();
      }
      stopButton.setDisable(true);
      playButton.setText(">");
      playing.set(false);
      timeSlider.setValue(0);
     // mediaPlayer.setAudioSpectrumListener(null);
      mediaPlayer.dispose();
     
      
    }   
    
   public void playCortina()
   {
      String sourcePath=null;
      volumeSlider.setValue(holdVolume);
      cortina=true;
      
      CortinaTrack cortinaTrack = Db.getCortinaTrack(currentCortinaId);
     // TrackDb trackDb = Db.getTrackInfo(cortinaTrack.getPathHash()); // TODO CortinaTrack should already have this
      sourcePath=cortinaTrack.getPath();
     
      playing.set(true);
      playButton.setText("||");
      stopButton.setDisable(false);
      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
      mediaPlayer.setStartTime(new Duration(cortinaTrack.getStartValue()));
      mediaPlayer.setStopTime(new Duration(cortinaTrack.getStopValue()));
      System.out.println("Player - total duration: "+mediaPlayer.getTotalDuration());
     
      // FADE IN
      if (cortinaTrack.getFadein()==1) fadeIn();
      // FADE OUT
      if (cortinaTrack.getFadeout()==1) fadeOut=true; else fadeOut=false;
      

    //eq = new Equalizer(mediaPlayer);
     // eventTab.setEqualizer(eq.getGridPane());
      
      
      
     
      // IMPORTANT
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) 
        {
          updateCortinaValues();
        }
      });
   

    mediaPlayer.setOnReady(new Runnable() 
    {
      public void run() 
      {
       // System.out.println("Playlist - mediaPlayer.setOnReady");
       // currentTrackDuration = mediaPlayer.getMedia().getDuration();
        updateCortinaValues();
      }
    });
    

    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
          stopButton.setDisable(true);
          playButton.setText(">");
          timeSlider.setValue(0);
          mediaPlayer.stop();
         // cortinaTrack.baseTreeItem.setPlayingImage(false);
          return;
      }
    });
    
    
  }
   
   public void playPremadeCortina()
   {
      String sourcePath=null;
      volumeSlider.setValue(holdVolume);
      cortina=true;
      
      CortinaTrack cortinaTrack = Db.getCortinaTrack(currentCortinaId);
     // TrackDb trackDb = Db.getTrackInfo(cortinaTrack.getPathHash()); // TODO CortinaTrack should already have this
      sourcePath=cortinaTrack.getPath();
     
      playing.set(true);
      playButton.setText("||");
      stopButton.setDisable(false);
      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
      //mediaPlayer.setStartTime(new Duration(cortinaTrack.getStartValue()));
      //mediaPlayer.setStopTime(new Duration(cortinaTrack.getStopValue()));
     
     
      // FADE IN
      //if (cortinaTrack.getFadein()==1) fadeIn();
      // FADE OUT
      //if (cortinaTrack.getFadeout()==1) fadeOut=true; else fadeOut=false;
      fadeOut=false;

    //eq = new Equalizer(mediaPlayer);
     // eventTab.setEqualizer(eq.getGridPane());
      
      
      
     
      // IMPORTANT
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) 
        {
         // updateCortinaValues();
          updateValues();
        }
      });
   

    mediaPlayer.setOnReady(new Runnable() 
    {
      public void run() 
      {
       // System.out.println("Playlist - mediaPlayer.setOnReady");
       // currentTrackDuration = mediaPlayer.getMedia().getDuration();
      //  updateCortinaValues();
        updateValues();
        System.out.println("Player - total durationx: "+mediaPlayer.getTotalDuration());
      }
    });
    

    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
          stopButton.setDisable(true);
          playButton.setText(">");
          timeSlider.setValue(0);
          mediaPlayer.stop();
         // cortinaTrack.baseTreeItem.setPlayingImage(false);
          return;
      }
    });
    
    
  }
   
   
    private void fadeIn()
    {
      holdVolume=mediaPlayer.getVolume();
      mediaPlayer.setVolume(0);
      final Timeline fadeInTimeline = new Timeline(
          new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), holdVolume)));
      fadeInTimeline.play();
    }

    /*
    public void playCortina()
    {
      String sourcePath=null;
      fadeOut=false;
      volumeSlider.setValue(holdVolume);
      double start=0;
      double stop=0;
      int originalDuration=0;
      double startFraction=0;
      double stopFraction=0;
      cortina=true;
      
      CortinaTrack cortinaTrack = Db.getCortinaTrack(currentCortinaId);
      
      start=cortinaTrack.getStartValue();
      stop = cortinaTrack.getStopValue();
      originalDuration = cortinaTrack.getOriginal_duration();
      startFraction = start/originalDuration;
      stopFraction = stop/originalDuration;
      
      
      startPositionSlider.setValue(startFraction);
      endPositionSlider.setValue(stopFraction);
         
      currentTrackDuration = new Duration(originalDuration);
      setCurrentTrackTitle(cortinaTrack.getTitle());
          
      cortinaStart = currentTrackDuration.multiply(startFraction);
      cortinaEnd = currentTrackDuration.multiply(stopFraction);
      startPositionLabel.setText(formatTime(cortinaStart)); 
      endPositionLabel.setText(formatTime(cortinaEnd));
      cortinaLength = cortinaEnd.subtract(cortinaStart);
        //  Duration remainingCortinaTime=endDuration.subtract(startDuration).subtract(currentTrackTime.subtract(startDuration));
      cortinaLengthLabel.setText(formatTime(cortinaLength));
        //  updateUIValues();
       
      TrackDb trackDb = Db.getTrackInfo(cortinaTrack.getPathHash()); // TODO CortinaTrack should already have this
      sourcePath=trackDb.path;
     
      
      System.out.println("Player, playCortina -            start: "+start);
      System.out.println("Player, playCortina -             stop: "+stop);
      System.out.println("Player, playCortina - originalDuration: "+originalDuration);
      System.out.println("Player, playCortina -    startFraction: "+startFraction);
      System.out.println("Player, playCortina -     stopFraction: "+stopFraction);
      System.out.println("Player, playCortina -     cortinaStart: "+cortinaStart.toMillis());
      System.out.println("Player, playCortina -       cortinaEnd: "+cortinaEnd.toMillis());

      
      System.out.println("Player - trackDb - path: "+trackDb.path);
      playing=true;
      playButton.setText("||");
      stopButton.setDisable(false);
     // 
      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      
     // mediaPlayer.setVolume(0);
      
      holdVolume = volumeSlider.getValue();
      if (cortina) { if (fadeInCheckBox.isSelected()) volumeSlider.setValue(0);}
      
      final Timeline fadeInTimeline = new Timeline(
        new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), holdVolume)));
      
      mediaPlayer.setVolume(volumeSlider.getValue());
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
      
      if (fadeInCheckBox.isSelected()) fadeInTimeline.play();
      if (fadeOutCheckBox.isSelected()) fadeOut=true;
     
     // System.out.println("Total Time: "+totalTrackTime);
      
      
      
      Duration startTime=currentTrackDuration.multiply(startFraction);
      stopTime=currentTrackDuration.multiply(stopFraction);
      
      
       // System.out.println("Start Time: "+ startTime);
     mediaPlayer.setStartTime(startTime);
     mediaPlayer.setStopTime(stopTime);
      
      System.out.println("Player, playCortina -  Media startTime: "+startTime);
      System.out.println("Player, playCortina -   Media stopTime: "+stopTime);
      
    

      eq = new Equalizer(mediaPlayer);
      equalizerTab.setContent(eq.getGridPane());
      
      // IMPORTANT
      mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
      {
        public void invalidated(Observable ov) 
        {
          updateValues();
        }
      });
   
     
      mediaPlayer.setOnPaused(new Runnable() 
      {
        public void run() 
        {
         // System.out.println("Playlist - mediaPlayer.setOnPaused");
          //System.out.println("onPaused");
          //playButton.setText(">");
        }
    });

    mediaPlayer.setOnReady(new Runnable() 
    {
      public void run() 
      {
       // System.out.println("Playlist - mediaPlayer.setOnReady");
        currentTrackDuration = mediaPlayer.getMedia().getDuration();
        updateValues();
      }
    });

    mediaPlayer.setOnEndOfMedia(new Runnable() 
    {
      public void run() 
      {
          stopButton.setDisable(true);
          playButton.setText(">");
          timeSlider.setValue(0);
          mediaPlayer.stop();
         // cortinaTrack.baseTreeItem.setPlayingImage(false);
          return;
      }
    });
    
    
  }
    */
    private MediaPlayer createMediaPlayer(String path, boolean autoPlay)
    {
      final String thisPath=path;
      Media  media = new Media(path);
      final MediaPlayer mp = new MediaPlayer(media);
     // mp.getTotalDuration();
      mp.setOnError(new Runnable() 
      {
        public void run() 
        {
          System.out.println("Media error occurred: " + mp.getError());
          System.out.println("path: " + thisPath);
        }
      });
      mp.setAutoPlay(autoPlay);
      
      
      return mp;
    }
    
    protected void updateCortinaValues() 
    {
      //System.out.println("Player: update values - currentTime: "+currentTrackTime);
      if (playTimeLabel != null && timeSlider != null && volumeSlider != null) 
      {
         Platform.runLater(new Runnable() 
         {
           public void run() 
           {
        	   Duration fullTrackPosition =  mediaPlayer.getCurrentTime();
             Duration cortinaTrackPosition = fullTrackPosition.subtract(mediaPlayer.getStartTime());
             Double cortinaLength = mediaPlayer.getTotalDuration().toMillis();
               
             if (!timeSlider.isValueChanging()) 
             {
               timeSlider.setValue(fullTrackPosition.divide(currentCleanupTrack.getIntDuration()).toMillis()* 100.0);
               cortinaTimeSlider.setValue(cortinaTrackPosition.divide(cortinaLength).toMillis()* 100.0);
               playTimeLabel.setText(formatTime(fullTrackPosition, new Duration(currentCleanupTrack.getIntDuration())));
               cortinaPlayTimeLabel.setText(formatTime(cortinaTrackPosition, new Duration(cortinaLength)));
             }
             
             if (fadeOut==true)
             {
              // if (currentTrackTime.toSeconds()>=stopTime.subtract(new Duration(5000)).toSeconds())
            	if (fullTrackPosition.toSeconds()>=mediaPlayer.getStopTime().subtract(new Duration(5000)).toSeconds())
               {
                 fadeOut=false;
                 holdVolume = volumeSlider.getValue();
                 System.out.println("Player - Fade Out");
                 final Timeline fadeOutTimeline = new Timeline(
                     new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), 0.0)));
                 
                  fadeOutTimeline.play();
               }
             }
            /*
             if (!volumeSlider.isValueChanging()) 
             {
               volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume()* 100));
             }
            */
           }
         });
      }
    }

    protected void updateValues() 
    {
      //System.out.println("Player: update values - currentTime: "+currentTrackTime);
      if (playTimeLabel != null && timeSlider != null && volumeSlider != null) 
      {
         Platform.runLater(new Runnable() 
         {
           public void run() 
           {
             Duration trackPosition =  mediaPlayer.getCurrentTime();
             Double trackLength = mediaPlayer.getTotalDuration().toMillis();
               
             if (!timeSlider.isValueChanging()) 
             {
               if (cortina)
               {
            	   Duration fullTrackPosition =  mediaPlayer.getCurrentTime();
            	   Duration cortinaTrackPosition = fullTrackPosition.subtract(mediaPlayer.getStartTime());
                 Double cortinaLength = mediaPlayer.getTotalDuration().toMillis();
                 timeSlider.setValue(cortinaTrackPosition.divide(cortinaLength).toMillis()* 100.0);
                 playTimeLabel.setText(formatTime(cortinaTrackPosition, new Duration(cortinaLength)));
                 if (active_tab==EVENT_TAB) eventTab.updateProgress(trackPosition.toMillis());
               }
               else
               {
                 timeSlider.setValue(trackPosition.divide(trackLength).toMillis()* 100.0);
                 playTimeLabel.setText(formatTime(trackPosition, new Duration(trackLength)));
                 if (infoWindow!=null) infoWindow.updateProgress(trackPosition.toMillis());  
                 if (active_tab==EVENT_TAB) eventTab.updateProgress(trackPosition.toMillis());
               }
             }
             
             if (fadeOut==true)
             {
              // if (currentTrackTime.toSeconds()>=stopTime.subtract(new Duration(5000)).toSeconds())
              if (trackPosition.toSeconds()>=mediaPlayer.getStopTime().subtract(new Duration(5000)).toSeconds())
               {
                 fadeOut=false;
                 System.out.println("Player - Fade Out");
                 final Timeline fadeOutTimeline = new Timeline(
                     new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), 0.0)));
                  fadeOutTimeline.play();
               }
             }
            /*
             if (!volumeSlider.isValueChanging()) 
             {
               volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume()* 100));
             }
            */
           }
         });
      }
    }
    
  private static String formatTime(Duration elapsed, Duration duration) 
  {
    int intElapsed = (int) Math.floor(elapsed.toSeconds());
    int elapsedHours = intElapsed / (60 * 60);
    if (elapsedHours > 0) 
    {
      intElapsed -= elapsedHours * 60 * 60;
    }
    int elapsedMinutes = intElapsed / 60;
    int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

    if (duration.greaterThan(Duration.ZERO)) 
    {
      int intDuration = (int) Math.floor(duration.toSeconds());
      int durationHours = intDuration / (60 * 60);
      if (durationHours > 0) { intDuration -= durationHours * 60 * 60; }
      int durationMinutes = intDuration / 60;
      int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
      if (durationHours > 0) 
      {
        return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
                      durationHours, durationMinutes, durationSeconds);
       } 
       else 
       {
         return String.format("%02d:%02d/%02d:%02d",
                      elapsedMinutes, elapsedSeconds, durationMinutes,
                      durationSeconds);
       }
     } 
     else 
     {
       if (elapsedHours > 0) 
       {
         return String.format("%d:%02d:%02d", elapsedHours,
                      elapsedMinutes, elapsedSeconds);
       } 
       else 
       {
         return String.format("%02d:%02d", elapsedMinutes,
                      elapsedSeconds);
       }
     }
   }
  
  private static String formatTime(Duration duration) 
  {
    if (duration.greaterThan(Duration.ZERO)) 
    {
      int intDuration = (int) Math.floor(duration.toSeconds());
      int durationHours = intDuration / (60 * 60);
      if (durationHours > 0) { intDuration -= durationHours * 60 * 60; }
      int durationMinutes = intDuration / 60;
      int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
      if (durationHours > 0) 
      {
        return String.format("%d:%02d:%02d", 
                      durationHours, durationMinutes, durationSeconds);
       } 
       else 
       {
         return String.format("%02d:%02d",
                     durationMinutes,
                      durationSeconds);
       }
     }
    else return "00:00";
    
     
   }

  public String getCurrentTrackHash()
  {
    return currentTrackHash;
  }

  public void setCurrentTrackHash(String currentTrackHash)
  {
    this.currentTrackHash = currentTrackHash;
  }

  public String getCurrentTrackTitle()
  {
    return currentTrackTitle;
  }

  public void setCurrentTrackTitle(String currentTrackTitle)
  {
    this.currentTrackTitle = currentTrackTitle;
    currentTrackTitleLabel.setText(currentTrackTitle);
  }

  public int getPlayMode()
  {
    return playMode;
  }

  public void setPlayMode(int playMode)
  {
    this.playMode = playMode;
    
    //System.out.println("Player - playMode: "+playMode);
    
    if (playMode==PLAYMODE_SINGLE_TRACK)
    {
      playButton.setDisable(false);
      skipButton.setDisable(true);
      previousButton.setDisable(true);
    }
    
    if (playMode==PLAYMODE_CORTINA_SINGLE_TRACK)
    {
    	playButton.setDisable(false);
      skipButton.setDisable(true);
      previousButton.setDisable(true);
    }
    
    if (playMode==PLAYMODE_PLAYLIST)
    {
    	playButton.setDisable(false);
      skipButton.setDisable(false);
      previousButton.setDisable(false);
      timeSlider.setDisable(false);
    }
    
    if (playMode==PLAYMODE_TANDA)
    {
    	playButton.setDisable(false);
       skipButton.setDisable(false);
       previousButton.setDisable(false);
       timeSlider.setDisable(false);
    }
    
    if (playMode==PLAYMODE_PREMADE_CORTINA_SINGLE_TRACK)
    {
      System.out.println("Player - premade cortina play controls");
      playButton.setDisable(false);
      skipButton.setDisable(false);
      previousButton.setDisable(false);
      timeSlider.setDisable(false);
    }
    
  }

  public void setCortinaEditControls(CortinaTrack cortinaTrack)
  {
    cortinaMode=UPDATE;
    setCurrentCortinaId(cortinaTrack.getId());
    premade=0;
    setCurrentTrackTitle(cortinaTrack.getTitle());  
    currentCleanupTrack=Db.getCleanupTrack(cortinaTrack.getPathHash());
    
    int originalLength=0;
    double startFraction=0;
    double stopFraction=0;
    
    originalLength = cortinaTrack.getOriginal_duration();
    startFraction = (double)cortinaTrack.getStartValue()/originalLength;
    stopFraction = (double)cortinaTrack.getStopValue()/originalLength;
    
    Duration originalDuration = new Duration(originalLength);
    Duration cortinaStart = originalDuration.multiply(startFraction);
    Duration cortinaEnd = originalDuration.multiply(stopFraction);
    Duration cortinaLength = cortinaEnd.subtract(cortinaStart);

    if (cortinaTrack.getFadein()==1) fadeInCheckBox.setSelected(true);
    else fadeInCheckBox.setSelected(false);
    
    if (cortinaTrack.getFadeout()==1) fadeOutCheckBox.setSelected(true);
    else fadeOutCheckBox.setSelected(false);
    
    if (cortinaTrack.getDelay()==1) delayCheckBox.setSelected(true);
    else delayCheckBox.setSelected(false);
    
    startPositionSlider.setValue(startFraction);
    //startPositionSlider.setDisable(true);
    endPositionSlider.setValue(stopFraction);
   // endPositionSlider.setDisable(true);
    startPositionLabel.setText(formatTime(cortinaStart)); 
    endPositionLabel.setText(formatTime(cortinaEnd));
    cortinaLengthLabel.setText(formatTime(cortinaLength));
    
    saveCortinaButton.setText("Update Cortina");
    cortinaOriginalTitle.setText(currentCleanupTrack.getTitle());
    cortinaTitleOverride.setText(cortinaTrack.getTitle());
    
    cortinaPlayTimeLabel.setText(formatTime(new Duration(0), cortinaLength));
    updateCortinaLengthLabel();
  }
  
  public void setPremadeCortinaEditControls(CortinaTrack cortinaTrack)
  {
    setCurrentCortinaId(cortinaTrack.getId());
    premade=1;
    cortinaTitleOverride.setText(cortinaTrack.getTitle());
    
    cortinaMode=UPDATE;
    setCurrentTrackTitle(cortinaTrack.getTitle());  
    
    int originalLength=0;
    
    originalLength = cortinaTrack.getOriginal_duration();
    
    
    saveCortinaButton.setText("Update Cortina");
    cortinaTitleOverride.setText(cortinaTrack.getTitle());
    
  }
  
  public void setCortinaNewControls(CleanupTrack cleanupTrack)
  {
    cortinaMode=INSERT;
    setCurrentCortinaId(-1);
    currentCleanupTrack=cleanupTrack;
    setCurrentTrackTitle(cleanupTrack.getTitle());  
    int originalLength=0;
    double startFraction=0;
    double stopFraction=0;
    
    originalLength = cleanupTrack.getIntDuration();
    startFraction = 0;
    stopFraction = 1;
    
    Duration originalDuration = new Duration(originalLength);
    Duration cortinaEnd = originalDuration;
    
    startPositionSlider.setValue(startFraction);
    endPositionSlider.setValue(stopFraction);
    startPositionLabel.setText(formatTime(new Duration(0))); 
    endPositionLabel.setText(formatTime(cortinaEnd));
    
    cortinaOriginalTitle.setText(currentCleanupTrack.getTitle());
    cortinaTitleOverride.setText(currentCleanupTrack.getTitle());
    updateCortinaLengthLabel();
    saveCortinaButton.setText("Save Cortina");
    cortinaPlayTimeLabel.setText("00:00");
    fadeInCheckBox.setSelected(false);
    fadeOutCheckBox.setSelected(false);
    delayCheckBox.setSelected(false);
  }
  
  
  public int getCurrentCortinaId()
  {
    return currentCortinaId;
  }

  public void setCurrentCortinaId(int currentCortinaId)
  {
    this.currentCortinaId = currentCortinaId;
  }

public Playlist getPlaylist() {
	return playlist;
}

public void setPlaylist(Playlist playlist) {
	this.playlist = playlist;
}
  
  private void updateCortinaLengthLabel()
  {
    int startTimeMillis = (int)(startPositionSlider.getValue()*currentCleanupTrack.getIntDuration());
    int stopTimeMillis = (int)(endPositionSlider.getValue()*currentCleanupTrack.getIntDuration());
    //System.out.println("start: "+startTimeMillis);
   // System.out.println("stop: "+stopTimeMillis);
    int cortinaLengthMillis = stopTimeMillis-startTimeMillis;
    cortinaLengthLabel.setText(formatTime(new Duration(cortinaLengthMillis)));
  }
  
  public void changePlaylist(int playlistId)
  {
	  try {
	  playlist = new Playlist(playlistId);
	  } catch (Exception e) {e.printStackTrace();};
  }
  
  /*
  public void setLargeHeight()
  {
    //vbox.setMinHeight(playerLargeHeight);
   // vbox.setMaxHeight(playerLargeHeight);
  //  vbox.setPrefHeight(playerLargeHeight);
  }
  
  public void setRegHeight()
  {
   // vbox.setMinHeight(playerRegHeight);
   // vbox.setMaxHeight(playerRegHeight);
   // vbox.setPrefHeight(playerRegHeight);
  }
  */
 }
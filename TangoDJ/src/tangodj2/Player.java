package tangodj2;

import java.io.File;

import tangodj2.cortina.Cortina;
import tangodj2.cortina.CortinaTable;
import tangodj2.cortina.CortinaTrack;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
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
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Player 
{
    private MediaView mediaView = new MediaView();
    private boolean atEndOfMedia = false;
    //private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private HBox mediaBar;
    private MediaPlayer mediaPlayer;
    Button playButton=null;
   Button stopButton=null;
     Button skipButton=null;
     Button previousButton=null;
    private boolean playing=false;
    private Equalizer eq;
    public final static int PLAYLIST_CREATE=1;
    public final static int PLAYLIST=2;
    public final static int EVENT_PLAYLIST=3;
    public final static int CORTINA_CREATE=4;
    private final Playlist playlist;
    //int nextPlaylistTrack=0;
    Tab equalizerTab;
    VBox vbox = new VBox();
    int mode = PLAYLIST_CREATE;
    GridPane cortinaCreatControls;
    Label startPositionValue=null;
    Label endPositionValue=null;
    Label playPositionValue=null;
    Label cortinaLengthLabel=null;
    
    Label currentTrackTitleLabel = new Label("");
    Duration currentTrackTime = new Duration(0);
    private Duration currentTrackDuration = new Duration(0); //??
    String currentTrackHash="";
    String currentTrackTitle="";
    private int currentCortinaId=-1;
   
    
    private Slider startPositionSlider=null;
    private Slider endPositionSlider=null;
    private Slider playPositionSlider=null;
    
    
    CheckBox fadeInCheckBox = new CheckBox("Fade In");
    CheckBox fadeOutCheckBox = new CheckBox("Fade Out");
    CheckBox delayCheckBox = new CheckBox("3 Sec delay at end");
    private boolean fadeOut=false;
    double holdVolume=.7;
    Duration stopTime;
    
    final static int PLAYLIST_BUILD_TANGO_TABLE=10;
    final static int PLAYLIST_BUILD_CLEANUP_TABLE=11;
    final static int PLAYLIST_BUILD_CORTINA_TABLE=12;
    final static int CORTINA_CREATE_CLEANUP_TABLE=13;
    final static int CORTINA_CREATE_CORTINA_TABLE=14;
  
    private static final Duration FADE_DURATION = Duration.seconds(3.0);
    private boolean advancedControls=false;
    private Duration cortinaStart;
    private Duration cortinaEnd;
    private Duration cortinaLength;
    private boolean cortina=false;
    
    final static int PLAYMODE_SINGLE_TRACK = 20;
    final static int PLAYMODE_PLAYLIST = 21;
    final static int CORTINA_SINGLE_TRACK = 22;
    
    private int playMode=0;
    
    
   // SimpleStringProperty source = new SimpleStringProperty();
   

    public Player(Playlist playlist, Tab equalizerTab) 
    {
      this.playlist=playlist;
      this.equalizerTab=equalizerTab;
     
      
      currentTrackTitleLabel.setFont(new Font("Arial", 18));
      currentTrackTitleLabel.setStyle("-fx-background-color: #bfc2c7;");
      
     // setupListeners();
      mediaBar = new HBox();
      mediaBar.setSpacing(5);
      mediaBar.setStyle("-fx-background-color: #bfc2c7;");
      mediaBar.setAlignment(Pos.CENTER);
      mediaBar.setPadding(new Insets(5, 10, 5, 10));
      BorderPane.setAlignment(mediaBar, Pos.CENTER);

      setupButtons();
      
      mediaBar.getChildren().add(previousButton);
      mediaBar.getChildren().add(stopButton);
      mediaBar.getChildren().add(playButton);
      mediaBar.getChildren().add(skipButton);
      Label spacer = new Label("   ");
      mediaBar.getChildren().add(spacer);

      Label timeLabel = new Label("Time: ");
      mediaBar.getChildren().add(timeLabel);
 
      timeSlider = new Slider();
      HBox.setHgrow(timeSlider, Priority.ALWAYS);
      timeSlider.setMinWidth(50);
      timeSlider.setMaxWidth(Double.MAX_VALUE);
      
       mediaBar.getChildren().add(timeSlider);

       playTime = new Label();
       playTime.setPrefWidth(130);
       playTime.setMinWidth(50);
       mediaBar.getChildren().add(playTime);

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
              mediaPlayer.seek(currentTrackDuration.multiply(timeSlider.getValue() / 100.0));
           }
         }
       });
       vbox.setStyle("-fx-background-color: #bfc2c7;");
       
       vbox.getChildren().add(currentTrackTitleLabel);
       vbox.getChildren().add(mediaBar);
       setupCortinaControls();
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
          playPreviousTrack();
        }
      });
      
      skipButton = new Button(">>");
      skipButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          stopPlaying();
       //   playTrack();
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
          if (playMode==PLAYMODE_SINGLE_TRACK)
          {
            if (mediaPlayer == null) playTrack(); //playTrackDelay(5);
            else
            {  
              Status status = mediaPlayer.getStatus();
             // System.out.println("Player, status: "+status.toString());
           
              if (status == Status.PLAYING) pauseTrack();
              else if (status == Status.PAUSED) resumeTrack();
              else playTrack();
            }
          }
          
          if (playMode==PLAYMODE_PLAYLIST)
          {
            playPlaylist();
          }
          
          if (playMode==CORTINA_SINGLE_TRACK)
          {
            System.out.println("Player, PLAY_CORTINA");
            playCortina();
          }
          
        }
      });
    }
    
    /*
    public void setDefaultTrack()
    {
      TrackMeta trackMeta=null;
      Track firstTrack=null;
      String trackHash=null;
      //System.out.println("Player - Set Default track");
      if (SharedValues.allTracksData.size()==0) 
      {
        System.out.println("Player - No Tracks");
        return;
      }
      firstTrack = SharedValues.allTracksData.get(0);
      trackHash=firstTrack.getPathHash();
      
      
      if (mode==CORTINA_CREATE)
      {
        //System.out.println("Player - CORTINA_CREATE");
        if (SharedValues.selectedCleanupPathHash.get()==null)
          SharedValues.selectedCleanupPathHash.set(trackHash);
        else trackHash=SharedValues.selectedCleanupPathHash.get();
        
      }
      if (mode==PLAYLIST_CREATE)
      {
        //System.out.println("Player - PLAYLIST_CREATE");
        if (SharedValues.selectedTangoPathHash.get()==null) 
          SharedValues.selectedTangoPathHash.set(trackHash);
        else trackHash=SharedValues.selectedTangoPathHash.get();
      }
          
      trackMeta = Db.getTrackInfo(trackHash);
      File file = new File(trackMeta.path);
      int totalTrackTime=trackMeta.duration;
      currentTrackDuration = new Duration(totalTrackTime*1000);
     // System.out.println("Default Track Duration: "+formatTime(currentTrackDuration));
      currentTrackTitleLabel.setText(trackMeta.title);
      updateUIValues(trackMeta.pathHash);
    }
*/
    public void showAdvancedControls(boolean show)
    {
      
      if (show)
      {
        advancedControls=true;
        vbox.getChildren().add(cortinaCreatControls);
      }
      else 
      {
        advancedControls=false;
        vbox.getChildren().remove(cortinaCreatControls);
      }
    }
    
    
    
    GridPane setupCortinaControls()
    {
      final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
      final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
      cortinaCreatControls = new GridPane();
      cortinaCreatControls.setPadding(new Insets(10, 10, 10, 10));
      cortinaCreatControls.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
      cortinaCreatControls.setHgap(15);
      
      Button setStartButton = new Button("*");
      Button setStopButton = new Button("*");
      Button dummyButton = new Button(" ");
      Button saveCortinaButton = new Button("Save Cortina");
      
      saveCortinaButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event)  
        {
          insertCortina();
        }

        
     });
      
      setStartButton.setPrefWidth(30);
      setStopButton.setPrefWidth(30);
      dummyButton.setPrefWidth(30);
      
      startPositionSlider = new Slider(0, 1, 0);
      endPositionSlider = new Slider(0, 1, 1);
      playPositionSlider = new Slider(0, 1, 0);
      
      fadeInCheckBox.setSelected(true);
      fadeOutCheckBox.setSelected(true);
      delayCheckBox.setSelected(false);
      
      startPositionValue = new Label("00:00");
      // System.out.println("Default Track Duration2: "+formatTime(currentTrackDuration));
      endPositionValue = new Label(formatTime(currentTrackDuration));
      playPositionValue = new Label(Double.toString(playPositionSlider.getValue()));
      cortinaLengthLabel=new Label("0:00");
    
      
      cortinaCreatControls.add(new Text("Start Position"), col[0], row[0]);
      cortinaCreatControls.add(new Text("End Position"),   col[0], row[1]);
      cortinaCreatControls.add(new Text("Play Position"),  col[0], row[2]);
      
      
      cortinaCreatControls.add(startPositionValue,         col[1], row[0]);
      cortinaCreatControls.add(endPositionValue,           col[1], row[1]);
      cortinaCreatControls.add(playPositionValue,          col[1], row[2]);
      
      cortinaCreatControls.add(startPositionSlider,        col[2], row[0]);
      cortinaCreatControls.add(endPositionSlider,          col[2], row[1]);
      cortinaCreatControls.add(playPositionSlider,         col[2], row[2]);
      
      cortinaCreatControls.add(setStartButton,             col[3], row[0]);
      cortinaCreatControls.add(setStopButton,              col[3], row[1]);
      cortinaCreatControls.add(dummyButton,                col[3], row[2]);
      
      cortinaCreatControls.add(fadeInCheckBox,             col[4], row[0]);
      cortinaCreatControls.add(fadeOutCheckBox,            col[4], row[1]);
      cortinaCreatControls.add(delayCheckBox,              col[4], row[2]);
      
      cortinaCreatControls.add(new Text("Cortna Length: "),col[5], row[0]);
      cortinaCreatControls.add(cortinaLengthLabel         ,col[6], row[0]);
      cortinaCreatControls.add(saveCortinaButton          ,col[7], row[1]);
      
      
      
      
      startPositionSlider.valueProperty().addListener(new ChangeListener<Number>() 
      {
          public void changed(ObservableValue<? extends Number> arg0,
                  Number arg1, Number arg2) 
          {
            updateCortinaUIValues();
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
        	updateCortinaUIValues();
          }
      });
      
      setStopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event)  
        {
          endPositionSlider.setValue(timeSlider.getValue()/100);
        }
     });
      
      playPositionSlider.valueProperty().addListener(new ChangeListener<Number>() 
      {
          public void changed(ObservableValue<? extends Number> arg0,
                  Number arg1, Number arg2) 
          {
            playPositionValue.setText(String.format("%.1f", arg2)); //new value
          }
      });
      
      
      return cortinaCreatControls;
    }
    
    private void insertCortina()
    {
      Cortina cortina = new Cortina();
      cortina.start=(int)(startPositionSlider.getValue()*currentTrackDuration.toMillis());
      cortina.stop=(int)(endPositionSlider.getValue()*currentTrackDuration.toMillis());
      if (fadeInCheckBox.isSelected()) cortina.fadein=1; else cortina.fadein=0;
      if (fadeOutCheckBox.isSelected()) cortina.fadeout=1; else cortina.fadeout=0;
      if (delayCheckBox.isSelected()) cortina.delay=1; else cortina.delay=0;
      cortina.hash=currentTrackHash;
      cortina.original_duration=(int)currentTrackDuration.toMillis();
     // String cortinaTimes = " ("+cortinaLength.getText()+") "
     //                          +startPositionValue.getText()+" - "
     //                          +endPositionValue.getText();
      if (currentTrackTitle.length()>60)
      cortina.title=currentTrackTitle.substring(0, 60);
      else cortina.title=currentTrackTitle;
     try {
           int cortinaId=Db.insertCortina(cortina);
           CortinaTable.addTrack(cortinaId);
     } catch (Exception e) { e.printStackTrace(); }
    }
    
    /*
    public void updateUIValues() 
    {
      Platform.runLater(new Runnable() 
      {
        public void run() 
        {
          currentTrackTitleLabel.setText(currentTrackMeta.title);
        }
      });
    }
    */
    
    private void updateCortinaUIValues() 
    {
      if (mode==CORTINA_CREATE)
      {
        cortinaStart = currentTrackDuration.multiply(startPositionSlider.getValue());
        cortinaEnd = currentTrackDuration.multiply(endPositionSlider.getValue());
        startPositionValue.setText(formatTime(cortinaStart)); 
        endPositionValue.setText(formatTime(cortinaEnd));
        cortinaLength = cortinaEnd.subtract(cortinaStart);
      //  Duration remainingCortinaTime=endDuration.subtract(startDuration).subtract(currentTrackTime.subtract(startDuration));
        cortinaLengthLabel.setText(formatTime(cortinaLength));
      }
    }
    
    public void setFeaturesMode(int mode)
    {
      this.mode=mode;
      if (mode==CORTINA_CREATE) 
      {
        if (!advancedControls) showAdvancedControls(true);
      }
        else showAdvancedControls(false);
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
    
    private void stopPlaying()
    {
      mediaPlayer.stop();
      stopButton.setDisable(true);
      playButton.setText(">");
      playing=false;
      timeSlider.setValue(0);
      atEndOfMedia = true;
    }   
    
    public void playPreviousTrack()
    {
      playlist.setPrevious();
    //  playTrack();
    }
    
    public void playTrackDelay(final int delayTime)
    {
      final Timeline timeline = new Timeline();
      timeline.setCycleCount(Timeline.INDEFINITE);
      KeyFrame keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
      {
        int seconds=delayTime;
        public void handle(Event event) 
        {
         seconds--;
         if (seconds<=0) 
         {
           timeline.stop();
     //      playTrack();
         }
         }});
              
        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }
    
    /*
    public void setTrack(String pathHash, int type, CortinaTrack cortinaTrack)
    {
      double start=cortinaTrack.getStartValue();
      double stop = cortinaTrack.getStopValue();
      double tot = cortinaTrack.getOriginal_duration();
      double startFraction = start/tot;
      double stopFraction = stop/tot;
      
      startPositionSlider.setValue(startFraction);
      endPositionSlider.setValue(stopFraction);
      updateCortinaUIValues();
      setTrack(pathHash, type);
    }
    
   
    public void setTrack(String pathHash, int type)
    {
      currentTrackHash=pathHash;
     // System.out.println("Player - currentTrackHash: "+currentTrackHash);
      if (type==CORTINA_CREATE_CLEANUP_TABLE) setFeaturesMode(CORTINA_CREATE);
      currentTrackMeta=Db.getTrackInfo(currentTrackHash);
      int totalTrackTime=currentTrackMeta.duration;
      currentTrackDuration = new Duration(totalTrackTime*1000);
      currentTrackTitle=currentTrackMeta.title;
      updateUIValues();
      if (mode==CORTINA_CREATE) updateCortinaUIValues();
    }
    */
    
    public void playTrack()
    {
      String sourcePath=null;
      cortina=false;
      
      if (currentTrackHash==null) return;
      TrackMeta trackMeta=Db.getTrackInfo(currentTrackHash);
      sourcePath=trackMeta.path;
      currentTrackTitle=trackMeta.title;
      
      playing=true;
      playButton.setText("||");
      stopButton.setDisable(false);


      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
      
      mediaView = new MediaView(mediaPlayer);

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
      double start=0;
      double stop=0;
      int originalDuration=0;
      double startFraction=0;
      double stopFraction=0;
      
      final PlaylistTrack playlistTrack=playlist.getTrack(playlist.getNextTrack());
      
      playlist.setPlayingTrack(playlist.getNextTrack());
      
      if (playlistTrack==null) return;
      
      playlistTrack.baseTreeItem.setPlayingImage(true);
      playlistTrack.playing=true;
      
     // currentTrackMeta=Db.getTrackInfo(playlistTrack.trackHash);
      
      cortina = playlistTrack.cortina;
        
       if (cortina)
       {
          start=playlistTrack.startValue;
          stop = playlistTrack.stopValue;
          originalDuration=playlistTrack.original_duration;
         
          startFraction = start/originalDuration;
          stopFraction = stop/originalDuration;
          
          startPositionSlider.setValue(startFraction);
          endPositionSlider.setValue(stopFraction);
          
         
          
          currentTrackDuration = new Duration(originalDuration*1000);
          currentTrackTitle=playlistTrack.title;
          
          cortinaStart = currentTrackDuration.multiply(startPositionSlider.getValue());
          cortinaEnd = currentTrackDuration.multiply(endPositionSlider.getValue());
          startPositionValue.setText(formatTime(cortinaStart)); 
          endPositionValue.setText(formatTime(cortinaEnd));
          cortinaLength = cortinaEnd.subtract(cortinaStart);
        //  Duration remainingCortinaTime=endDuration.subtract(startDuration).subtract(currentTrackTime.subtract(startDuration));
          cortinaLengthLabel.setText(formatTime(cortinaLength));
        //  updateUIValues();
       
       }
        setCurrentTrackTitle(playlistTrack.title);
        sourcePath=playlistTrack.path;
      
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
      
      if (cortina)
      {
        if (fadeInCheckBox.isSelected()) fadeInTimeline.play();
        if (fadeOutCheckBox.isSelected()) fadeOut=true;
      }
      /*
      volumeSlider.disableProperty().bind(
          Bindings.or(
            Bindings.equal(Timeline.Status.RUNNING, fadeInTimeline.statusProperty()),
            Bindings.equal(Timeline.Status.RUNNING, fadeOutTimeline.statusProperty())
          )
        );
      */
     // int totalTrackTime=currentTrackMeta.duration;
      
     // currentTrackDuration = new Duration(totalTrackTime*1000);
      
     
     // System.out.println("Total Time: "+totalTrackTime);
      
      if (cortina)
      {
     
      
        Duration startTime=currentTrackDuration.multiply(startFraction);
        stopTime=currentTrackDuration.multiply(stopFraction);
      
      
       // System.out.println("Start Time: "+ startTime);
        mediaPlayer.setStartTime(startTime);
        mediaPlayer.setStopTime(stopTime);
      }
      mediaView = new MediaView(mediaPlayer);

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
          atEndOfMedia = true;
          mediaPlayer.stop();
          playlistTrack.baseTreeItem.setPlayingImage(false);
          playlistTrack.playing=false;
          if (playlist.getPlayingTrack()==playlist.getNextTrack()) 
            playlist.setNextTrack(playlist.getNextTrack()+1);
          playPlaylist();
      }
    });
    
    
  }
    
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
         
      currentTrackDuration = new Duration(originalDuration*1000);
      setCurrentTrackTitle(cortinaTrack.getTitle());
          
      cortinaStart = currentTrackDuration.multiply(startPositionSlider.getValue());
      cortinaEnd = currentTrackDuration.multiply(endPositionSlider.getValue());
      startPositionValue.setText(formatTime(cortinaStart)); 
      endPositionValue.setText(formatTime(cortinaEnd));
      cortinaLength = cortinaEnd.subtract(cortinaStart);
        //  Duration remainingCortinaTime=endDuration.subtract(startDuration).subtract(currentTrackTime.subtract(startDuration));
      cortinaLengthLabel.setText(formatTime(cortinaLength));
        //  updateUIValues();
       
      TrackMeta trackMeta = Db.getTrackInfo(cortinaTrack.getPathHash()); // TODO CortinaTrack should already have this
      sourcePath=trackMeta.path;
      System.out.println("Player - trackMeta - path: "+trackMeta.path);
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
    
      mediaView = new MediaView(mediaPlayer);

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
          atEndOfMedia = true;
          mediaPlayer.stop();
         // cortinaTrack.baseTreeItem.setPlayingImage(false);
          return;
      }
    });
    
    
  }
    
    private MediaPlayer createMediaPlayer(String path, boolean autoPlay)
    {
      final String thisPath=path;
      Media  media = new Media(path);
      final MediaPlayer mp = new MediaPlayer(media);
      mp.getTotalDuration();
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
    
    protected void updateValues() 
    {
      //System.out.println("Player: update values - currentTime: "+currentTrackTime);
      if (playTime != null && timeSlider != null && volumeSlider != null) 
      {
         Platform.runLater(new Runnable() 
         {
           public void run() 
           {
             currentTrackDuration = mediaPlayer.getCurrentTime();
             
             if (cortina)
             {
               Duration remainingCortinaTime=cortinaLength.subtract(currentTrackTime.subtract(cortinaStart));
               cortinaLengthLabel.setText(formatTime(remainingCortinaTime));
             }
             playTime.setText(formatTime(currentTrackTime, currentTrackDuration));
             timeSlider.setDisable(currentTrackDuration.isUnknown());
             if (!timeSlider.isDisabled()
                   && currentTrackDuration.greaterThan(Duration.ZERO)
                   && !timeSlider.isValueChanging()) 
             {
                timeSlider.setValue(currentTrackTime.divide(currentTrackDuration.toMillis()).toMillis()* 100.0);
             }
             
             if (fadeOut==true)
             {
               if (currentTrackTime.toSeconds()>=stopTime.subtract(new Duration(5000)).toSeconds())
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
    
    System.out.println("Player - playMode: "+playMode);
    
    if (playMode==PLAYMODE_SINGLE_TRACK)
    {
      skipButton.setDisable(true);
      previousButton.setDisable(true);
    }
  }

  public int getCurrentCortinaId()
  {
    return currentCortinaId;
  }

  public void setCurrentCortinaId(int currentCortinaId)
  {
    this.currentCortinaId = currentCortinaId;
  }
  
  /*
  private void setupListeners() 
  {
    ChangeListener playlistFocusListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        System.out.println("player mode set to PLAYLIST");
        setMode(PLAYLIST);
      }
    };   
    SharedValues.playlistFocus.addListener(playlistFocusListener);
  }
  */
 }
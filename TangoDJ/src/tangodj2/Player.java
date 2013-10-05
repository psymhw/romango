package tangodj2;

import java.io.File;

import tangodj2.cortina.Cortina;
import tangodj2.cortina.CortinaTable;

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
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    //private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private HBox mediaBar;
    private MediaPlayer mediaPlayer;
    final Button playButton;
    final Button stopButton;
    final Button skipButton;
    final Button previousButton;
    private boolean playing=false;
    private Equalizer eq;
    public final static int PLAYLIST_CREATE=1;
    public final static int PLAYLIST=2;
    public final static int EVENT_PLAYLIST=3;
    public final static int CORTINA_CREATE=4;
    Playlist playlist;
    int nextPlaylistTrack=0;
    Tab equalizerTab;
    VBox vbox = new VBox();
    int mode = PLAYLIST_CREATE;
    GridPane gridPane;
    Label startPositionValue=null;
    Label endPositionValue=null;
    Label playPositionValue=null;
    Label cortinaLength=null;
    Label trackTitle = new Label("");
    
   
    private Duration currentTrackDuration = new Duration(0);
    private Slider startPositionSlider=null;
    private Slider endPositionSlider=null;
    private Slider playPositionSlider=null;
    
    
    CheckBox fadeInCheckBox = new CheckBox("Fade In");
    CheckBox fadeOutCheckBox = new CheckBox("Fade Out");
    CheckBox delayCheckBox = new CheckBox("3 Sec delay at end");
    private boolean fadeOut=false;
    double holdVolume=.7;
    Duration stopTime;
    Duration currentTrackTime = new Duration(0);
    String currentTrackHash="";
    String currentTrackTitle="";
    TrackMeta currentTrackMeta;
    final static int PLAYLIST_BUILD_TANGO_TABLE=10;
    final static int PLAYLIST_BUILD_CLEANUP_TABLE=11;
    final static int PLAYLIST_BUILD_CORTINA_TABLE=12;
    final static int CORTINA_CREATE_CLEANUP_TABLE=13;
  
    private static final Duration FADE_DURATION = Duration.seconds(3.0);
    private boolean advancedControls=false;
    
    
   // SimpleStringProperty source = new SimpleStringProperty();
   

    public Player(Playlist playlist, Tab equalizerTab) 
    {
      this.playlist=playlist;
      this.equalizerTab=equalizerTab;
     
      
      trackTitle.setFont(new Font("Arial", 18));
      trackTitle.setStyle("-fx-background-color: #bfc2c7;");
      
     // setupListeners();
      mediaBar = new HBox();
      mediaBar.setSpacing(5);
      mediaBar.setStyle("-fx-background-color: #bfc2c7;");
      mediaBar.setAlignment(Pos.CENTER);
      mediaBar.setPadding(new Insets(5, 10, 5, 10));
      BorderPane.setAlignment(mediaBar, Pos.CENTER);

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
          playTrack();
        }
      });
      
      playButton = new Button(">");
      stopButton = new Button("Stop");
      stopButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          stopPlaying();
        }
      });
      
      stopButton.setDisable(true);

      playButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          
          if (mediaPlayer == null) playTrack(); //playTrackDelay(5);
          else
          {  
            Status status = mediaPlayer.getStatus();
            System.out.println("Player, status: "+status.toString());
           
            if (status == Status.PLAYING) pauseTrack();
            else if (status == Status.PAUSED) resumeTrack();
            else playTrack();
          }
        }
      });
      
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
       
       vbox.getChildren().add(trackTitle);
       vbox.getChildren().add(mediaBar);
       
      // setDefaultTrack();
       
       
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
      trackTitle.setText(trackMeta.title);
      updateUIValues(trackMeta.pathHash);
    }
*/
    public void showAdvancedControls(boolean show)
    {
      
      if (show)
      {
        advancedControls=true;
        setupCortinaControls();
        vbox.getChildren().add(gridPane);
      }
      else 
      {
        advancedControls=false;
        vbox.getChildren().remove(gridPane);
      }
    }
    
    
    
    GridPane setupCortinaControls()
    {
      final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
      final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
      gridPane = new GridPane();
      gridPane.setPadding(new Insets(10, 10, 10, 10));
      gridPane.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
      gridPane.setHgap(15);
      
      Button setStartButton = new Button("*");
      Button setStopButton = new Button("*");
      Button dummyButton = new Button(" ");
      Button saveCortinaButton = new Button("Save Cortina");
      
      saveCortinaButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event)  
        {
          Cortina cortina = new Cortina();
          cortina.start=(int)(startPositionSlider.getValue()*currentTrackDuration.toMillis());
          cortina.stop=(int)(endPositionSlider.getValue()*currentTrackDuration.toMillis());
          if (fadeInCheckBox.isSelected()) cortina.fadein=1; else cortina.fadein=0;
          if (fadeOutCheckBox.isSelected()) cortina.fadeout=1; else cortina.fadeout=0;
          if (delayCheckBox.isSelected()) cortina.delay=1; else cortina.delay=0;
          cortina.hash=currentTrackHash;
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
      cortinaLength=new Label("0:00");
    
      
      gridPane.add(new Text("Start Position"), col[0], row[0]);
      gridPane.add(new Text("End Position"),   col[0], row[1]);
      gridPane.add(new Text("Play Position"),  col[0], row[2]);
      
      
      gridPane.add(startPositionValue,         col[1], row[0]);
      gridPane.add(endPositionValue,           col[1], row[1]);
      gridPane.add(playPositionValue,          col[1], row[2]);
      
      gridPane.add(startPositionSlider,        col[2], row[0]);
      gridPane.add(endPositionSlider,          col[2], row[1]);
      gridPane.add(playPositionSlider,         col[2], row[2]);
      
      gridPane.add(setStartButton,             col[3], row[0]);
      gridPane.add(setStopButton,              col[3], row[1]);
      gridPane.add(dummyButton,                col[3], row[2]);
      
      gridPane.add(fadeInCheckBox,             col[4], row[0]);
      gridPane.add(fadeOutCheckBox,            col[4], row[1]);
      gridPane.add(delayCheckBox,              col[4], row[2]);
      
      gridPane.add(new Text("Cortna Length: "),col[5], row[0]);
      gridPane.add(cortinaLength              ,col[6], row[0]);
      gridPane.add(saveCortinaButton          ,col[7], row[1]);
      
      
      
      
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
      
      
      return gridPane;
    }
    
    public void updateUIValues() 
    {
      Platform.runLater(new Runnable() 
      {
        public void run() 
        {
          trackTitle.setText(currentTrackMeta.title);
        }
      });
    }
    
    private void updateCortinaUIValues() 
    {
      if (mode==CORTINA_CREATE)
      {
        Duration startDuration =currentTrackDuration.multiply(startPositionSlider.getValue());
        Duration endDuration = currentTrackDuration.multiply(endPositionSlider.getValue());
        startPositionValue.setText(formatTime(startDuration)); 
        endPositionValue.setText(formatTime(endDuration));
        cortinaLength.setText(formatTime(endDuration.subtract(startDuration)));
      }
    }
    
    public void setMode(int mode)
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
    }   
    
    public void playPreviousTrack()
    {
      playlist.setPrevious();
      playTrack();
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
           playTrack();
         }
         }});
              
        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }
    
    public void setTrack(String pathHash, int type)
    {
      currentTrackHash=pathHash;
      if (type==CORTINA_CREATE_CLEANUP_TABLE) setMode(CORTINA_CREATE);
      currentTrackMeta=Db.getTrackInfo(currentTrackHash);
      int totalTrackTime=currentTrackMeta.duration;
      currentTrackDuration = new Duration(totalTrackTime*1000);
      currentTrackTitle=currentTrackMeta.title;
      updateUIValues();
      if (mode==CORTINA_CREATE) updateCortinaUIValues();
    }
    
    public void playTrack()
    {
      String sourcePath=null;
      fadeOut=false;
      volumeSlider.setValue(holdVolume);
     
      if (currentTrackHash==null) return;
      //trackMeta = Db.getTrackInfo(currentTrackHash);
      sourcePath=currentTrackMeta.path;
      
      /*
      
      if (mode==PLAYLIST)
      {
        PlaylistTrack playlistTrack=playlist.getNextTrack();
        if (playlistTrack==null) return;
        sourcePath=playlistTrack.path;
        currentTrackHash=playlistTrack.trackHash;
        currentTrackTitle=playlistTrack.title;
       //System.out.println("Playing from playlist: "+playlistTrack.title);
      }
      else 
      {
    	if (mode==PLAYLIST_CREATE)  
    	{
        String trackHash=SharedValues.selectedTangoPathHash.get();
        if (trackHash==null) return;
        trackMeta = Db.getTrackInfo(trackHash);
        sourcePath=trackMeta.path;
        currentTrackHash=trackMeta.pathHash;
        currentTrackTitle=trackMeta.title;
    	}
    	if (mode==CORTINA_CREATE)
    	{
    	     String trackHash=SharedValues.selectedCleanupPathHash.get();
    	        if (trackHash==null) return;
    	        trackMeta = Db.getTrackInfo(trackHash);
    	        sourcePath=trackMeta.path;
    	        currentTrackHash=trackMeta.pathHash;
    	        currentTrackTitle=trackMeta.title;
    	}
      }
      
      if (sourcePath==null)
      {
        System.out.println("Source Path is null");
        return;
      }
      
      */
      
      playing=true;
      playButton.setText("||");
      stopButton.setDisable(false);
     // 
      File file = new File(sourcePath);
      
      mediaPlayer = createMediaPlayer(file.toURI().toString(), true);
      mediaPlayer.setVolume(volumeSlider.getValue());
      
     // mediaPlayer.setVolume(0);
      
      holdVolume = volumeSlider.getValue();
      if (fadeInCheckBox.isSelected()&&(mode==CORTINA_CREATE)) volumeSlider.setValue(0);
      
      final Timeline fadeInTimeline = new Timeline(
        new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), holdVolume)));

   
      
      mediaPlayer.setVolume(volumeSlider.getValue());
      mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
      
      if (fadeInCheckBox.isSelected()&&(mode==CORTINA_CREATE)) fadeInTimeline.play();
      if (fadeOutCheckBox.isSelected()&&(mode==CORTINA_CREATE)) fadeOut=true;
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
      
      if (mode==CORTINA_CREATE)
      {
        double startFraction = startPositionSlider.getValue();
        double endFraction = endPositionSlider.getValue();
      
        Duration startTime=currentTrackDuration.multiply(startFraction);
        stopTime=currentTrackDuration.multiply(endFraction);
      
      
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
          stopRequested = true;
          atEndOfMedia = true;
          mediaPlayer.stop();
          if (mode==PLAYLIST) playTrack();
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
      //System.out.println("Player: update values");
      if (playTime != null && timeSlider != null && volumeSlider != null) 
      {
         Platform.runLater(new Runnable() 
         {
           public void run() 
           {
             currentTrackTime = mediaPlayer.getCurrentTime();
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
package tangodj2;

import java.io.File;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import tangodj.Equalizer;

public class PlayerControls
{
  private MediaPlayer player;
  private double volume=.75;
  private MediaView mediaView=null;
  SimpleStringProperty source;
  private Slider volumeSlider = new Slider(0, 1, 0);
  private Slider progressSlider = new Slider(0,1,0);
  private Label currentTimeLabel;
  private CurrentTimeListener currentTimeListener;
  int counter=1;
  
  Equalizer eq;
   Image player_rew = new Image(getClass().getResourceAsStream("/resources/images/player_rew.png"));
   Image player_stop = new Image(getClass().getResourceAsStream("/resources/images/player_stop.png"));
   Image player_play = new Image(getClass().getResourceAsStream("/resources/images/player_play.png"));
   Image player_pause = new Image(getClass().getResourceAsStream("/resources/images/player_pause.png"));
   Image player_fwd = new Image(getClass().getResourceAsStream("/resources/images/player_fwd.png"));
   HBox hbox = new HBox();
   
   final Button playerRewButton = new Button();
   final Button playerStopButton = new Button();
   final Button playerPlayPauseButton = new Button();
  // Button playerPauseButton = new Button();
   final Button playerFwdButton = new Button();
   
   public PlayerControls(SimpleStringProperty source)
   {
     this.source=source;
     playerRewButton.setGraphic(new ImageView(player_rew));
     playerStopButton.setGraphic(new ImageView(player_stop));
     playerPlayPauseButton.setGraphic(new ImageView(player_play));
    // playerPauseButton.setGraphic(new ImageView(player_pause)); 
     playerFwdButton.setGraphic(new ImageView(player_fwd));
     
     playerRewButton.getStyleClass().add("playerButton");
     playerStopButton.getStyleClass().add("playerButton");
     playerPlayPauseButton.getStyleClass().add("playerButton");
     
     playerPlayPauseButton.setId("paused");
     
    // playerPauseButton.getStyleClass().add("playerButton");
     playerFwdButton.getStyleClass().add("playerButton");
     
     playerStopButton.setDisable(true);
     
     playerPlayPauseButton.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent actionEvent) 
       {
         if ("paused".equals(playerPlayPauseButton.getId()))
         {
           playerPlayPauseButton.setGraphic(new ImageView(player_pause));
           playerPlayPauseButton.setId("playing"); 
           playerStopButton.setDisable(false);
           playTrack();
         }
         else
         {
           playerPlayPauseButton.setGraphic(new ImageView(player_play));
           playerPlayPauseButton.setId("paused"); 
           playerStopButton.setDisable(true);
         }
       }
     });
     
    
     
     playerStopButton.setOnAction(new EventHandler<ActionEvent>() 
         {
           public void handle(ActionEvent actionEvent) 
           {
             if ("playing".equals(playerPlayPauseButton.getId()))
             {
               playerPlayPauseButton.setGraphic(new ImageView(player_play));
               playerPlayPauseButton.setId("paused"); 
               playerStopButton.setDisable(true);
               player.stop();
               volumeSlider.valueProperty().unbind();
             }
           }
         });
     
     
  //
     VBox progressBox = new VBox();
     currentTimeLabel = createLabel("00:00", "mediaText");
     progressBox.getChildren().addAll(progressSlider, currentTimeLabel);
    
     
     hbox.setSpacing(10);
     VBox volumeBox = new VBox();
     volumeBox.getChildren().add(volumeSlider);
     volumeBox.getChildren().add(new Label("Volume"));
     
     hbox.getChildren().addAll(playerRewButton, playerStopButton, playerPlayPauseButton, playerFwdButton, volumeBox, progressBox);
   }
   
  
   
   public HBox get()
   {
     return hbox;
   }
   
   private void playTrack()
   {
    // final MediaPlayer player = createMediaPlayer(playlist.getPlayingTrackPath(), false);
    
     TrackMeta trackMeta = Db.getTrackInfo(source.get());
     File file = new File(trackMeta.path);
     
     player = createMediaPlayer(file.toURI().toString(), false);
     player.setVolume(volumeSlider.getValue());
     player.setOnEndOfMedia(new Runnable() 
     {
         public void run() 
         {
          // if (playlist.isDone()) return;
           //if (playlist.playingIsSelected()) playlist.incrementSelected();
         //  playlist.incrementPlaying();
         //  playTrack();
         }
       });    
      
     mediaView = new MediaView(player);
       //if (eq!=null) vbox.getChildren().remove(3);
        
       
        
       eq = new Equalizer(player);
       TangoDJ2.tabC.setContent(eq.getGridPane());
      // vbox.getChildren().add(eq.getGridPane());
      // progress.setProgress(0);
      // progress2.setProgress(0);
       volumeSlider.valueProperty().bindBidirectional(player.volumeProperty());
       
      currentTimeListener = new CurrentTimeListener(player, currentTimeLabel, progressSlider);
      progressSlider.setOnMouseClicked(bHandler);
       player.currentTimeProperty().addListener(currentTimeListener);
       player.bufferProgressTimeProperty();
      // playlist.setPlaying(true);
       mediaView.getMediaPlayer().play();
     //  setInfoWindow();
   }
  
   EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
     public void handle(MouseEvent event) 
     {
       System.out.println("MOUSE");
       player.stop();
       player.currentTimeProperty().removeListener(currentTimeListener);
       player.setStartTime(new Duration(30000));
       currentTimeListener = new CurrentTimeListener(player, currentTimeLabel, progressSlider);
       player.currentTimeProperty().addListener(currentTimeListener);
       player.play();
     }
   };
   
   ChangeListener cl2 = new ChangeListener() 
   {
     public void changed(ObservableValue observable, Object oldValue, Object newValue) 
     {
       System.out.println(counter+" changed");
       counter++;
     }
   };   
   
   
   
   
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
     }});
     mp.setAutoPlay(autoPlay);
     
     return mp;
   }

   private Slider createSlider(String id) {
     final Slider slider = new Slider(0.0, 1.0, 0.1);
     slider.setId(id);
     slider.setValue(0);
     return slider;
   }
   private Label createLabel(String text, String styleClass) {
     final Label label = new Label(text);
     label.getStyleClass().add(styleClass);
     return label;
   }
}

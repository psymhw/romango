package tangodj2.infoWindow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import tangodj2.Db;
import tangodj2.MyDouble;
import tangodj2.Player;
import tangodj2.Playlist;
import tangodj2.PlaylistTrack;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class InfoWindow2 
{
  BorderPane borderPane = new BorderPane();
  StackPane stackPane = new StackPane();
  ArrayList <ImageView>background = new ArrayList<ImageView>();
  boolean showBorders=false;
  boolean cortina=false;
	
  FontMeta tusj = new FontMeta("FFF Tusj", FontWeight.BOLD);
  FontMeta carousel = new FontMeta("Carousel", FontWeight.BOLD);
  Font cortinaFont = Font.font(carousel.name, carousel.style, 240);
  Font titleFont = Font.font(tusj.name, tusj.style, 100);
  boolean fontsLoaded=false;
  Playlist playlist;
  Stage infoWindow = new Stage();
  Label artistLastNameLabel;
  
  //int counter=0;
  final int LARGER = 0;
  final int SMALLER = 1;
  final int maxNameHeight=350;
  final int minNameHeight=50;
  Scene scene;
  VBox vbox = new VBox();
  ProgressBar tandaProgressBar = new ProgressBar(0);
  PlaylistTrack playlistTrack;
  public final  Image noteImage = new Image(getClass().getResourceAsStream("/resources/images/note.png"));
  
  
  public InfoWindow2(Playlist playlist, URL xxx)
  { 
	  
	  this.playlist=playlist;
	  //infoWindow.initStyle(StageStyle.UNDECORATED);
	  infoWindow.initModality(Modality.NONE);
	  setupBackgrounds();
	  Label test = new Label("TEST");
	  test.setFont(titleFont);
	  borderPane.setPadding(new Insets(5,5,5,5));
	  infoWindow.getIcons().add(noteImage);
	  infoWindow.setTitle("Tango DJ *");
	  infoWindow.setOnCloseRequest(new EventHandler<WindowEvent>() 
	  {
	    public void handle(WindowEvent e)
	    {
	      close();
	      System.out.println("Exit Info Window");
	    }
	  });
	  
	
	 // tandaProgressBar.getStyleClass().add("progress-bar");
	  //maybe can have an image as part of the bar or
	  //use a stackPane with an image behind and some less opacity
	  //progress2.setClip(new ImageView(new Image()));
	  //progress2.setOpacity(.5);
	 // Rectangle r = new Rectangle(1200,800);
   // r.setFill(Color.DARKGRAY);
    
    //root.getChildren().add(r);
    
   // update(playlist, progress2);
	  stackPane.getChildren().add(background.get(0));  // cortina image
	  stackPane.getChildren().add(borderPane);
	  stackPane.getChildren().get(0).setVisible(false);
	  
	  
	  scene = new Scene(stackPane, 1200, 800);
	  
	  final URL stylesheet = getClass().getResource("progress.css");
    scene.getStylesheets().add(stylesheet.toString());

    tandaProgressBar.setMinHeight(100);
    tandaProgressBar.setMinWidth(400);
    tandaProgressBar.getStyleClass().add("red-bar");
    
    update();
    
   
    scene.setFill(Color.BLACK);
    infoWindow.setScene(scene);
    infoWindow.show();
    
    infoWindow.widthProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldVal, Object newVal) 
      {
         int direction=-1;
         if ( ((Number)newVal).doubleValue()> ((Number)oldVal).doubleValue() )
         {
            direction = LARGER;
         } else direction=SMALLER;
         sizeToFitWidth(artistLastNameLabel, ((Number)newVal).doubleValue(), direction, 1); 
      }
    });
    
    makeDraggable(infoWindow, stackPane);
  }
  
  public void updateProgress(double currentTrackTime)
  {
	  double progressVal=(currentTrackTime+playlistTrack.startTimeInTanda)/playlistTrack.tandaInfo.tandaDuration;
	  tandaProgressBar.setProgress(progressVal);
  }
  
  public void close()
  {
    infoWindow.close();
    Player.infoWindow=null;
  }
  
  private void setupBackgrounds()
  {
	  background.add(new ImageView(new Image(InfoWindow2.class.getResourceAsStream("/resources/images/ebabgdag.jpg"))));
	  background.add(new ImageView(new Image(InfoWindow2.class.getResourceAsStream("/resources/images/black.png"))));
  }
  
  public void update()
  {
  	Artist currentArtist;
    Label titleLabel= new Label("--");
  	Label titleLabel2= new Label("--");
  	
  	vbox = new VBox();
  	if (showBorders) vbox.setStyle("-fx-border-style: solid;"
        + "-fx-border-width: 2;"
        + "-fx-border-color: blue");
  	vbox.setAlignment(Pos.TOP_CENTER);
  
  	playlistTrack = playlist.getPlayingPlaylistTrack();
  	
  	if (playlistTrack.cortina) // CORTINA
  	{
  	  stackPane.getChildren().get(0).setVisible(true);
  	  borderPane.setTop(getPane(new Label(""),75));
  	   
  	  vbox.getChildren().add(getPane(getDistantLightLabel("CORTINA", cortinaFont),200));
  	  vbox.getChildren().add(getPane(getDistantLightLabel(playlistTrack.artist, titleFont), 100));
  	  vbox.getChildren().add(getPane(getDistantLightLabel(playlistTrack.title, titleFont), 100));
  	  
      borderPane.setCenter(vbox);
  	
  	  borderPane.setBottom(getShadedPane(getUpNext(), 100));
  	  
  	  return;
  	}
  	else  // TANGO
  	{
  	  stackPane.getChildren().get(0).setVisible(false);
	    currentArtist=Artist.getArtist(playlistTrack.artist);
	    titleLabel = getTitleLabel(playlistTrack.title);
	    artistLastNameLabel =  getDistantLightLabel(currentArtist.lastName, titleFont);
	    
      sizeToFitWidth(artistLastNameLabel, 1150, LARGER, 1);	    
	    Label artistFirstNameLabel =  getDistantLightLabel(currentArtist.firstName, titleFont);
	  	
	    borderPane.setTop(getPane(artistFirstNameLabel, 75));
	    vbox.getChildren().add(getPane(artistLastNameLabel, 200));
	    vbox.getChildren().add(getPane(titleLabel, 100));
	    if (!titleLabel2.getText().equals("--")) vbox.getChildren().add(getPane(titleLabel2, 100));
	   // vbox.getChildren().add(getPane(getDistantLightLabel(playlistTrack.trackInTanda+" of "+playlistTrack.tandaInfo.numberOfTracksInTanda, titleFont), 100));
	  //  vbox.getChildren().add(tandaProgressBar);
	    vbox.getChildren().add(getProgressPane());
	    borderPane.setBottom(getPane(getUpNext(), 100));
	   
	    borderPane.setCenter(vbox);
  	}
  }
  
  private void sizeToFitWidth(final Label label, final double target, final int direction, final int increment)
  {
    final Timeline timeline = new Timeline();
    timeline.setCycleCount(300);  // maximum cycles
    final int numbChars = label.getText().length();
    
    
    
    KeyFrame keyFrame  =  new KeyFrame(Duration.seconds(.05),  new EventHandler() 
    {
      public void handle(Event event) 
      {
        double width=label.getWidth();
        double height=label.getHeight();
        double fontSize = 200;
        
        if (numbChars>10) fontSize=100;
        
        if (direction==LARGER)
        {  
          if (width>=(target-60)) { timeline.stop(); System.out.println("Final font size: "+fontSize); return;  }
          if (height>=maxNameHeight) { timeline.stop(); System.out.println("Final font size: "+fontSize); return; }
          
          fontSize+=increment;
        }
        if (direction==SMALLER)
        {
          if (width<=(target-60)) { timeline.stop(); System.out.println("Final font size: "+fontSize); return; }
          if (height<=minNameHeight) { timeline.stop(); System.out.println("Final font size: "+fontSize); return; }
          fontSize-=increment;
        }
        
        Font titleFont = Font.font(tusj.name, tusj.style, fontSize);
        label.setFont(titleFont);
        
      }
    });
    
    timeline.getKeyFrames().add(keyFrame);
    timeline.play();
    
  }

  
  
  public Pane getPane(Label label, int height)
  {
    
    StackPane stackPane = new StackPane();
     // stackPane.setPrefHeight(height);
      stackPane.setAlignment(Pos.CENTER);  
      stackPane.getChildren().add(label);
      if (showBorders) stackPane.setStyle("-fx-border-style: solid;"
              + "-fx-border-width: 1;"
              + "-fx-border-color: red");
      return stackPane;
  }
  
  public Pane getProgressPane()
  {
    
    StackPane stackPane = new StackPane();
    
     // stackPane.setPrefHeight(height);
      stackPane.setAlignment(Pos.CENTER);  
      stackPane.getChildren().add(tandaProgressBar);
      stackPane.getChildren().add(getDistantLightLabel(playlistTrack.trackInTanda+" of "+playlistTrack.tandaInfo.numberOfTracksInTanda, titleFont));
      if (showBorders) stackPane.setStyle("-fx-border-style: solid;"
              + "-fx-border-width: 1;"
              + "-fx-border-color: red");
      return stackPane;
  }
  
  public Pane getShadedPane(Label label, int height)
  {
    
    StackPane stackPane = new StackPane();
     // stackPane.setPrefHeight(height);
      stackPane.setAlignment(Pos.CENTER);  
      stackPane.getChildren().add(label);
      stackPane.setStyle("-fx-background-color: rgba(255, 255, 0,.40); -fx-border-style: solid;"
              + "-fx-border-width: 1;"
              + "-fx-border-color: red");
      return stackPane;
  }
  
  private Label getUpNext()
  {
	  //System.out.println("InfoWindow - up next: "+playlistTrack.tandaInfo.nextTandaName);
	  return getTitleLabel("Next: "+playlistTrack.tandaInfo.nextTandaName);	  
  }
  
  public Label getTitleLabel(String title)
  {
	  Label trialText = getDistantLightLabel(title, titleFont);
	  Bounds bounds=trialText.getBoundsInLocal();
	
	  if (bounds.getWidth()>1150)
	  {
		  titleFont = Font.font(tusj.name, tusj.style, 60);
		  trialText = getDistantLightLabel(title, titleFont);
	  }
	
	  return trialText;
  }
  
  
  private Label getDistantLightLabel(String inStr, Font f)
  {
    Light.Distant light = new Light.Distant();
    light.setAzimuth(-135.0f);
    light.setElevation(30.0f);

    Lighting l = new Lighting();
    l.setLight(light);
    l.setSurfaceScale(5.0f);

    final Label t = new Label();
    t.setText(inStr);
    t.setTextFill(Color.YELLOW);
    t.setFont(f);
    t.setEffect(l);
    return t;
  }
  
  /** makes a stage draggable using a given node */
  public static void makeDraggable(final Stage stage, final Node byNode) {
    final Delta dragDelta = new Delta();
    byNode.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        // record a delta distance for the drag and drop operation.
        dragDelta.x = stage.getX() - mouseEvent.getScreenX();
        dragDelta.y = stage.getY() - mouseEvent.getScreenY();
        byNode.setCursor(Cursor.MOVE);
      }
    });
    byNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        byNode.setCursor(Cursor.HAND);
      }
    });
    byNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX() + dragDelta.x);
        stage.setY(mouseEvent.getScreenY() + dragDelta.y);
      }
    });
    byNode.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        if (!mouseEvent.isPrimaryButtonDown()) {
          byNode.setCursor(Cursor.HAND);
        }
      }
    });
    byNode.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        if (!mouseEvent.isPrimaryButtonDown()) {
          byNode.setCursor(Cursor.DEFAULT);
        }
      }
    });
  }

  /** records relative x and y co-ordinates. */
  private static class Delta {
    double x, y;
  }
}

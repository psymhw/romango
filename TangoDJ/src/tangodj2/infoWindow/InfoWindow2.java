package tangodj2.infoWindow;

import java.util.ArrayList;

import tangodj2.Playlist;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.util.Duration;

public class InfoWindow2 
{
  Group root = new Group();
  ArrayList <ImageView>background = new ArrayList<ImageView>();
  boolean showBorders=false;
  boolean cortina=false;
	
  FontMeta tusj = new FontMeta("FFF Tusj", FontWeight.BOLD);
  FontMeta carousel = new FontMeta("Carousel", FontWeight.BOLD);
  Font cortinaFont = Font.font(carousel.name, carousel.style, 240);
  Font titleFont = Font.font(tusj.name, tusj.style, 70);
  boolean fontsLoaded=false;
  Playlist playlist;
  Stage infoWindow = new Stage();
  
  int counter=0;
  int fontSize=50;
  
  public InfoWindow2(Playlist playlist, ProgressBar progress2)
  { 
	  
	  this.playlist=playlist;
	  //infoWindow.initStyle(StageStyle.UNDECORATED);
	  infoWindow.initModality(Modality.NONE);
	
	  setupBackgrounds();
	  Label test = new Label("TEST");
	  test.setFont(titleFont);
	
	
	  //maybe can have an image as part of the bar or
	  //use a stackPane with an image behind and some less opacity
	  //progress2.setClip(new ImageView(new Image()));
	  //progress2.setOpacity(.5);
	  Rectangle r = new Rectangle(1200,800);
    r.setFill(Color.DARKGRAY);
    
    root.getChildren().add(r);
    
   // update(playlist, progress2);
    update();
    
    Scene scene = new Scene(root, 1200, 800);
    infoWindow.setScene(scene);
    infoWindow.show();
  }
  
  public void close()
  {
    infoWindow.close();
  }
  
  private void setupBackgrounds()
  {
	  background.add(new ImageView(new Image(InfoWindow2.class.getResourceAsStream("/resources/images/ebabgdag.jpg"))));
	  background.add(new ImageView(new Image(InfoWindow2.class.getResourceAsStream("/resources/images/black.png"))));
  }
  
  public void update()
  {
	  while (root.getChildren().size()>1) { root.getChildren().remove(1); }
  	Artist currentArtist;
    Text titleText= new Text("--");
  	Text titleText2= new Text("--");
  	
  	BorderPane borderPane = new BorderPane();	
    borderPane.setPrefHeight(800);
  	
  	VBox vbox = getVBox();
  	vbox.setAlignment(Pos.TOP_CENTER);
  	vbox.setPrefWidth(1200);
  	if (showBorders) vbox.setStyle("-fx-border-style: solid;"
            + "-fx-border-width: 2;"
            + "-fx-border-color: black");
  	if (playlist.isCortina())
  	{
  	  root.getChildren().add(background.get(0));
  	  borderPane.setTop(getPane(new Text(""),75));
  	   
  	  vbox.getChildren().add(getPane(Artist.getDistantLight("CORTINA", cortinaFont),200));
  	  vbox.getChildren().add(getPane(Artist.getDistantLight(playlist.getPlayingArtist(), titleFont), 100));
  	  vbox.getChildren().add(getPane(Artist.getDistantLight(playlist.getPlayingTitle(), titleFont), 100));
  	  
      borderPane.setCenter(vbox);
  	
  	  borderPane.setBottom(getPane(getUpNext(), 100));
  	  
  	  root.getChildren().add(borderPane);
  	  return;
  	}
  	else
  	{
  	  root.getChildren().add(background.get(1));
	    currentArtist=Artist.getArtist(playlist.getPlayingArtist());
	    titleText = getTitleText(playlist.getPlayingTitle());
	    Label artistLastNameLabel =  currentArtist.getLastNameLabel();
	    
      sizeToFitWidth(artistLastNameLabel, 1150);	    
	    Text artistFirstNameText =  currentArtist.getFirstNameText();
	  	
	    borderPane.setTop(getPane(artistFirstNameText, 75));
	  	
	    vbox.getChildren().add(getPane(artistLastNameLabel, 200));
	    vbox.getChildren().add(getPane(titleText, 100));
	    if (!titleText2.getText().equals("--")) vbox.getChildren().add(getPane(titleText2, 100));
	    vbox.getChildren().add(getPane(Artist.getDistantLight(playlist.getPlayingTandaProgress(), titleFont), 100));
	   // vbox.getChildren().add(getTandaProgress());
	    borderPane.setCenter(vbox);
	    
	    borderPane.setBottom(getPane(getUpNext(), 100));
	  	
	    root.getChildren().add(borderPane);
  	}
  }
  
  private void sizeToFitWidth(final Label label, final int sceneWidth)
  {
    counter=0;
    fontSize=50;
    final Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    KeyFrame keyFrame  =  new KeyFrame(Duration.seconds(.05),  new EventHandler() 
    {
      public void handle(Event event) 
      {
        fontSize+=5;
        Font titleFont = Font.font(tusj.name, tusj.style, fontSize);
        label.setFont(titleFont);
        double width=label.getWidth();
        System.out.println("width: "+width); 
        counter++;
        if (width>=sceneWidth-100) timeline.stop();
        if (counter>=100) timeline.stop();
      }
    });
    
    timeline.getKeyFrames().add(keyFrame);
    timeline.play();
    
  }

  private VBox getVBox()
  {
	  VBox vbox;  
	  vbox = new VBox(); 
	  vbox.setPadding(new Insets(0,0,0,0));
	  // vbox.setVgap(2);
	  // vbox.setHgap(5);
    return vbox;
  }
  
  public Pane getPane(Text text, int height)
  {
	  StackPane stackPane = new StackPane();
      stackPane.setPrefHeight(height);
      stackPane.setAlignment(Pos.CENTER);  
      stackPane.getChildren().add(text);
      if (showBorders) stackPane.setStyle("-fx-border-style: solid;"
              + "-fx-border-width: 1;"
              + "-fx-border-color: red");
      return stackPane;
  }
  
  public Pane getPane(Label label, int height)
  {
    StackPane stackPane = new StackPane();
      stackPane.setPrefHeight(height);
      stackPane.setAlignment(Pos.CENTER);  
      stackPane.getChildren().add(label);
      if (showBorders) stackPane.setStyle("-fx-border-style: solid;"
              + "-fx-border-width: 1;"
              + "-fx-border-color: red");
      return stackPane;
  }
  
  private Text getUpNext()
  {
	  System.out.println("InfoWindow - up next: "+playlist.getNextTandaInfo());
	  return getTitleText("Up Next: "+playlist.getNextTandaInfo());	  
  }
  
  public Text getTitleText(String title)
  {
	Text trialText = Artist.getDistantLight(title, titleFont);
	Bounds bounds=trialText.getBoundsInLocal();
	
	if (bounds.getWidth()>1150)
	{
		titleFont = Font.font(tusj.name, tusj.style, 60);
		
		trialText = Artist.getDistantLight(title, titleFont);
	}
	
	return trialText;
  }
  
}

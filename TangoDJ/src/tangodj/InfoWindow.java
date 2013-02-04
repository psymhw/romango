package tangodj;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import test.Test;

public class InfoWindow 
{
//  public ArrayList<FontMeta> fontMeta = new ArrayList<FontMeta>();
	FontMeta tusj = new FontMeta("FFF Tusj", FontWeight.BOLD);
	FontMeta carousel = new FontMeta("Carousel", FontWeight.BOLD);
	Font cortinaFont = Font.font(carousel.name, carousel.style, 240);
  Font titleFont = Font.font(tusj.name, tusj.style, 70);
  boolean fontsLoaded=false;
  Group root = new Group();
  Random random = new Random();
  Playlist playlist;
  ProgressBar progress2;
  
 
  ArrayList <ImageView>background = new ArrayList<ImageView>();
  
 // public InfoWindow(String artist, String title, String group, boolean test)
 // {
//	 this(artist, title, group); 
 // }
  
  public InfoWindow(Playlist playlist, ProgressBar progress2)
  {
	Stage infoWindow = new Stage();
	//infoWindow.initStyle(StageStyle.UNDECORATED);
    infoWindow.initModality(Modality.NONE);
    
    setupBackgrounds();
    
    Rectangle r = new Rectangle(1200,800);
    r.setFill(Color.DARKGRAY);
    
    root.getChildren().add(r);
    
   // update(artist, title, group);
    update(playlist, progress2);
    
    Scene scene = new Scene(root, 1200, 800);
    infoWindow.setScene(scene);
    infoWindow.show();
  }
  
  private void setupBackgrounds()
  {
	background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/ebabgdag.jpg"))));
	background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/black.png"))));
  }
  
  private GridPane getGridPane()
  {
    GridPane gridPane;  
	gridPane = new GridPane(); 
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(5);
    return gridPane;
  }
  
  private VBox getVBox()
  {
	  VBox vbox;  
	  vbox = new VBox(); 
	  vbox.setPadding(new Insets(10, 10, 10, 10));
	 // vbox.setVgap(2);
	 // vbox.setHgap(5);
    return vbox;
  }
  
  
  
  public void update(Playlist playlist, ProgressBar progress2)
  {
    this.playlist=playlist;
	this.progress2=progress2;

	if  (playlist.getPlayingGrouping().equalsIgnoreCase("PADDING")) return;
	
	while (root.getChildren().size()>1) { root.getChildren().remove(1); }
	
  	Artist currentArtist;
  	Text titleText= new Text("--");
  	Text titleText2= new Text("--");
  	
  	VBox vbox = getVBox();
  	vbox.setAlignment(Pos.TOP_CENTER);
  	vbox.setPrefWidth(1190);
  	 
  	if (playlist.getPlayingGrouping().equalsIgnoreCase("CORTINA"))
  	{
  	   root.getChildren().add(background.get(0));
  	     	   
  	   vbox.getChildren().add(getPane(new Text(""),75));
  	   vbox.getChildren().add(getPane(Artist.getDistantLight("CORTINA", cortinaFont),200));
  	   vbox.getChildren().add(Artist.getDistantLight(playlist.getPlayingArtist(), titleFont));
  	   vbox.getChildren().add(Artist.getDistantLight(playlist.getPlayingTitle(), titleFont));
  	  
  	    root.getChildren().add(vbox);
  	    return;
  	}
  	
  	root.getChildren().add(background.get(1));
    currentArtist=Artist.getArtist(playlist.getPlayingArtist());
    titleText = getTitleText(playlist.getPlayingTitle());
  	   	  
  	Text artistLastNameText =  currentArtist.getLastNameText();
  	Text artistFirstNameText =  currentArtist.getFirstNameText();
  	 
  	
    	 
     //gp.add(artistFirstNameText, 0, 0);
  	vbox.getChildren().add(artistFirstNameText);
    vbox.getChildren().add(artistLastNameText);
    vbox.getChildren().add(titleText);
    if (!titleText2.getText().equals("--")) vbox.getChildren().add(titleText2);
   
    vbox.getChildren().add(getTandaProgress());
    vbox.getChildren().add(getUpNext());
  	
  	root.getChildren().add(vbox);
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
  
  
  
  private HBox getTandaProgress()
  {
	HBox hbox = new HBox();
	//hbox.setPadding(new Insets(3, 3, 3, 3));
	hbox.setSpacing(5);
	Color c;
	int height=75;
	
	
	int numberOfTracks=playlist.getNumberOfTracks();
	int unitSize=125;
	int width=numberOfTracks*unitSize;
	
	hbox.setPrefWidth(width);
	progress2.setPrefWidth(unitSize-6);
	//progress2.setStyle("-fx-accent: blue;");
	
	
	if (playlist.getPlayingGrouping().equalsIgnoreCase("CORTINA"))
	{
		Rectangle r = new Rectangle();
		r.setWidth(500);
		r.setHeight(height);
		r.setFill(null);
		hbox.getChildren().add(r);
		return hbox;
	}

	hbox.getChildren().add(getTitleText(playlist.getPlayingGrouping()+": "));
	
	int currentTandaTrack = playlist.getTandaTrackIndex();
	
	System.out.println("current track: "+currentTandaTrack);
	
	for(int i=0; i<playlist.getNumberOfTracksInPlayingTanda(); i++)
	{
	  if (i==currentTandaTrack) hbox.getChildren().add(getProgressBox(i+1, unitSize, height));
	  else
	  {
		if (i<currentTandaTrack) c=Color.DEEPSKYBLUE;
		else c=Color.LIGHTGREY;
	    hbox.getChildren().add(getBlankProgressBox(i+1, unitSize, height, c));
	  }
	}
	
	return hbox;
  }
  
  public Pane getBlankProgressBox(int number, int width, int height, Color c)
  {
	  StackPane stackPane = new StackPane();
      stackPane.setPrefSize(width, height);
      stackPane.setAlignment(Pos.CENTER);
      Rectangle r = new Rectangle();
	  r.setWidth(width);
		r.setHeight(height);
		r.setFill(c);
		r.setArcHeight(10);
		r.setArcWidth(10);

		
      stackPane.getChildren().add(r);
      
      Text t = new Text(""+number);
      t.setFont(Font.font(tusj.name, tusj.style, 60));
      
      stackPane.getChildren().add(t);
      
      return stackPane;
  }

  
  public Pane getProgressBox(int number, int width, int height)
  {
	  StackPane stackPane = new StackPane();
      stackPane.setPrefSize(width, height);
      stackPane.setAlignment(Pos.CENTER);
      
      progress2.getStyleClass().add("progress-bar");
      stackPane.getChildren().add(progress2);
      
      Text t = new Text(""+number);
      t.setFont(Font.font(tusj.name, tusj.style, 60));
      
      stackPane.getChildren().add(t);
      
      return stackPane;
  }
  
  public Pane getPane(Text text, int height)
  {
	  StackPane stackPane = new StackPane();
      stackPane.setPrefHeight(height);
      stackPane.setAlignment(Pos.CENTER);  
      stackPane.getChildren().add(text);
      return stackPane;
  }

	
  private Text getUpNext()
  {
	  return getTitleText(playlist.getNextTandaInfo());	  
  }
	
}

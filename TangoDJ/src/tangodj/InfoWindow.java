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
  
  
  
  public void update(Playlist playlist, ProgressBar progress2)
  {
	 this.playlist=playlist;
	 this.progress2=progress2;
	 if  (playlist.getPlayingGrouping().equalsIgnoreCase("PADDING")) return;
  	 Artist currentArtist;
  	 Text titleText= new Text("--");
  	 Text titleText2= new Text("--");
  	 
  	 //SongInfo songInfo = new SongInfo(playlist.getPlayingTitle());
  	 
  	if (playlist.getPlayingGrouping().equalsIgnoreCase("CORTINA"))
  	{
  	   currentArtist=Artist.getArtist("cortina");
  	   
  	   titleText = Artist.getDistantLight(playlist.getPlayingArtist(), titleFont);
  	   titleText2 = Artist.getDistantLight(playlist.getPlayingTitle(), titleFont);
  	}
  	else
  	{
      currentArtist=Artist.getArtist(playlist.getPlayingArtist());
      titleText = getTitleText(playlist.getPlayingTitle());
  	}
  	   	  
  	 Text artistLastNameText =  currentArtist.getLastNameText();
  	 Text artistFirstNameText =  currentArtist.getFirstNameText();
  	  
  	 GridPane gp = getGridPane();
  	 gp.setAlignment(Pos.TOP_CENTER);
     gp.setPrefWidth(1190);
    // gp.setGridLinesVisible(true);
    	 
     gp.add(artistFirstNameText, 0, 0);
     gp.add(artistLastNameText, 0, 1);
     gp.add(titleText, 0, 2);
     if (!titleText2.getText().equals("--")) gp.add(titleText2, 0, 3);
     gp.add(getTitleText(playlist.getTandaInfo()), 0, 4);
     gp.add(getTandaProgress(), 0, 5);
     
	 GridPane.setHalignment(artistFirstNameText, HPos.CENTER);
     GridPane.setHalignment(artistLastNameText, HPos.CENTER);
     GridPane.setHalignment(titleText, HPos.CENTER);
     if (!titleText2.getText().equals("--"))  GridPane.setHalignment(titleText2, HPos.CENTER);
     
  	while (root.getChildren().size()>1) { root.getChildren().remove(1); }
  	
  	if (playlist.getPlayingGrouping().equalsIgnoreCase("CORTINA"))
  	{	
  	  root.getChildren().add(background.get(0));
  	}
  	else
  	{
  		root.getChildren().add(background.get(1));
  	}
  	root.getChildren().add(gp);
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
  
  LinearGradient linearGradient_REFLECT
  = LinearGradientBuilder.create()
  .startX(275)
  .startY(50)
  .endX(250)
  .endY(100)
  .proportional(false)
  .cycleMethod(CycleMethod.REFLECT)
  .stops(
      new Stop(0.1f, Color.rgb(255, 0, 255, 0.9)),
      new Stop(1.0f, Color.rgb(0, 255, 0, 1.0)))
  .build();
  
  private HBox getTandaProgress()
  {
	HBox hbox = new HBox();
	//hbox.setPadding(new Insets(3, 3, 3, 3));
	hbox.setSpacing(5);
	int numberOfTracks=playlist.getNumberOfTracks();
	int unitSize=125;
	int width=numberOfTracks*unitSize;
	
	hbox.setPrefWidth(width);
	progress2.setPrefWidth(unitSize-6);
	
	

	
	int currentTandaTrack = playlist.getTandaTrackIndex();
	
	System.out.println("current track: "+currentTandaTrack);
	
	for(int i=0; i<playlist.getNumberOfTracksInPlayingTanda(); i++)
	{
	  if (i==currentTandaTrack) hbox.getChildren().add(progress2);
	  else
	  {
		Rectangle r = new Rectangle();
		r.setWidth(unitSize);
		r.setHeight(100);
		if (i<currentTandaTrack) r.setFill(Color.DEEPSKYBLUE);
		else r.setFill(Color.LIGHTGREY);
	   // r.setOpacity(.3);
	    //r.setStroke(Color.BLACK);
	   // r.setStrokeWidth(3);
	   // r.setStrokeType(StrokeType.INSIDE);
	    //tandaHighlightBox.setVisible(false);
	    hbox.getChildren().add(r);
	  }
	}
	
	return hbox;
  }


	
	
}

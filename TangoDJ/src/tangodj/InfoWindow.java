package tangodj;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
 
  ArrayList <ImageView>background = new ArrayList<ImageView>();
  
  public InfoWindow(String artist, String title, String group, boolean test)
  {
	 this(artist, title, group); 
  }
  
  public InfoWindow(String artist, String title, String group)
  {
    //loadFonts();
	 
	Stage infoWindow = new Stage();
	//infoWindow.initStyle(StageStyle.UNDECORATED);
    infoWindow.initModality(Modality.NONE);
    
    setupBackgrounds();
    
    Rectangle r = new Rectangle(1200,800);
    r.setFill(Color.DARKGRAY);
    
    //root.getChildren().add(r);
    
    update(artist, title, group);
    
    Scene scene = new Scene(root, 1200, 800);
    infoWindow.setScene(scene);
    infoWindow.show();
  }
  
  private void setupBackgrounds()
  {
	background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/ebabgdag.jpg"))));
	background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/black.png"))));
	//background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/space.png"))));
	//background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/BsAs1.jpg"))));
	//background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/nyc1.jpg"))));
	//background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/blueDrop.jpg"))));
	//background.add(new ImageView(new Image(InfoWindow.class.getResourceAsStream("/resources/images/sunset1.jpg"))));
  }
  
  private GridPane getGridPane()
  {
    GridPane gridPane;  
	gridPane = new GridPane(); 
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(5);
   // gridPane.setStyle("-fx-background-color: darkgrey;");
    return gridPane;
  }
  
  
  
  public void update(String artist, String title, String group)
  {
	  
  	 Artist currentArtist;
  	 Text titleText= new Text("--");
  	 SongInfo songInfo = new SongInfo(title);
  	 
  	if (group.equalsIgnoreCase("CORTINA"))
  	{
  	   currentArtist=Artist.getArtist("cortina");
  	   
  	   titleText = Artist.getDistantLight(artist+" - "+title, titleFont);
  	}
  	else
  	{
      currentArtist=Artist.getArtist(artist);
      titleText = songInfo.getTitleText();
  	}
  	 
  	   	  
  	 Text artistLastNameText =  currentArtist.getLastNameText();
  	 Text artistFirstNameText =  currentArtist.getFirstNameText();
   	// Text titleText = songInfo.getTitleText();
  	  
  	 GridPane gp = getGridPane();
  	 gp.setAlignment(Pos.TOP_CENTER);
     gp.setPrefWidth(1190);
     gp.setGridLinesVisible(true);
    	 
     gp.add(artistFirstNameText, 0, 0);
     gp.add(artistLastNameText, 0, 1);
     gp.add(titleText, 0, 2);
     
	 GridPane.setHalignment(artistFirstNameText, HPos.CENTER);
     GridPane.setHalignment(artistLastNameText, HPos.CENTER);
     GridPane.setHalignment(titleText, HPos.CENTER);
     
    // Button b = new Button("Button");
     
  	while (root.getChildren().size()>1) { root.getChildren().remove(1); }
  	
  	if (group.equalsIgnoreCase("CORTINA"))
  	{	
  	  root.getChildren().add(background.get(0));
  //	  root.getChildren().add(b);
  	}
  	else
  	{
  		root.getChildren().add(background.get(1));
  		//root.getChildren().add(background.get(random.nextInt(background.size()-1)+1));
  //		root.getChildren().add(b);
  	 // gp.setStyle("-fx-background-color: darkgrey;");
  	}
  	root.getChildren().add(gp);
  }
  
  
  /*
  
  private Font getSizedFont(String inStr, FontMeta fontMeta)
  {
	Font trialFont;  
	Text trialText;
	Bounds bounds;
	int cycleCount=0;
	String breakReason;
	
	int numberOfChars=inStr.length();
	int fontSize=200;
	if (numberOfChars>8) fontSize=170;
	if (numberOfChars>12) fontSize=140;
	if (numberOfChars>16) fontSize=120;
	
	
	while(true)
	{
	  trialFont = Font.font(fontMeta.name, fontMeta.style, fontSize);
	
	  trialText = new Text(inStr);
	  trialText.setFont(trialFont);
	  bounds=trialText.getBoundsInLocal();
	  cycleCount++;
	  if (cycleCount==20) { breakReason="cycle limit"; break; }
	  if ((int)bounds.getWidth()>MAX_FONT_WIDTH) { breakReason="max width"; break; }

	  if ((int)bounds.getHeight()>MAX_FONT_HEIGHT) { breakReason="max height"; break; }
	  fontSize+=10;
	}
	
	System.out.println("getFont() - str size: "+numberOfChars
			+", fontSize: "+fontSize
			+", width: "+(int)bounds.getWidth()
			+", height: "+(int)bounds.getHeight()
			+", reason: "+breakReason
			+", cycles: "+cycleCount
			);
	
	
	return trialFont;
	
  }
  
  */
/*
private void loadFonts()
  {
	if (fontsLoaded) return;
	Font.loadFont(Test.class.getResource("/resources/fonts/Carousel.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/Anagram.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/Carrington.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/DEFTONE.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/EastMarket.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/england.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/FFF_Tusj.ttf").toExternalForm(), 10  );
	fontsLoaded=true;
  }
*/
/*
  
	private Text getDropShadow(String str, Font f)
	{
		DropShadow ds = new DropShadow();
	    ds.setOffsetY(3.0f);
	    ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
	     
	    Text tx = new Text();
	    tx.setEffect(ds);
	    tx.setCache(true);
	    tx.setFill(Color.RED);
	    tx.setText(str);
	    tx.setFont(f);
	    return tx;
	}
	
	*/



	
	
}

package tangodj;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import test.Test;

public class InfoWindow 
{
//  public ArrayList<FontMeta> fontMeta = new ArrayList<FontMeta>();

  boolean fontsLoaded=false;
  Group root = new Group();
  ImageView curtain;
  ImageView border1;
  
  public InfoWindow(String artist, String title, boolean test)
  {
	 this(artist, title); 
  }
  
  public InfoWindow(String artist, String title)
  {
    loadFonts();
	 
	Stage infoWindow = new Stage();
    infoWindow.initModality(Modality.NONE);
    
    Image curtainImage  = new Image(InfoWindow.class.getResourceAsStream("/resources/images/ebabgdag.jpg"));
    Image border1Image  = new Image(InfoWindow.class.getResourceAsStream("/resources/images/border1.jpg"));
    curtain = new ImageView(curtainImage);
    border1 = new ImageView(border1Image);
    
    Rectangle r = new Rectangle(1200,800);
    r.setFill(Color.DARKGRAY);
    
    root.getChildren().add(r);
    
    update(artist, title);
    
    Scene scene = new Scene(root, 1200, 800);
    infoWindow.setScene(scene);
    infoWindow.show();
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
  
  
  
  public void update(String artist, String title)
  {
  	 Artist currentArtist=Artist.getArtist(artist);
  	 SongInfo songInfo = new SongInfo(title);
  	   	  
  	 Text artistLastNameText =  currentArtist.getLastNameText();
  	 Text artistFirstNameText =  currentArtist.getFirstNameText();
   	 Text titleText = songInfo.getTitleText();
  	  
  	 GridPane gp = getGridPane();
  	 gp.setAlignment(Pos.CENTER);
     gp.setPrefWidth(1190);
    	 
     gp.add(artistFirstNameText, 0, 0);
     gp.add(artistLastNameText, 0, 1);
     gp.add(titleText, 0, 2);
     
	 GridPane.setHalignment(artistFirstNameText, HPos.CENTER);
     GridPane.setHalignment(artistLastNameText, HPos.CENTER);
     GridPane.setHalignment(titleText, HPos.CENTER);
     
     
  	while (root.getChildren().size()>1) { root.getChildren().remove(1); }
  	
  	if (currentArtist.getLastNameText().getText().equals("CORTINA"))
  	{	
  	  root.getChildren().add(curtain);
  	}
  	else
  	{
  		root.getChildren().add(border1);
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

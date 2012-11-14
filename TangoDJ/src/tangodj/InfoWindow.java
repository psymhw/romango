package tangodj;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import test.Test;

public class InfoWindow 
{
 
  Random random = new Random();
  public ArrayList<Font> fontList = new ArrayList<Font>();
  public ArrayList<FontMeta> fontMeta = new ArrayList<FontMeta>();
  Group root = new Group();
  Artist lastArtist;
  int lastFontNumber=0;
  boolean test=false;
  final int MAX_FONT_WIDTH=1100;
  final int MAX_FONT_HEIGHT=500;
 
  
  public InfoWindow(String artist, String title, boolean test)
  {
	 this(artist, title); 
	 this.test=test;
  }
  
  public InfoWindow(String artist, String title)
  {
    loadFonts();
	 
	Stage infoWindow = new Stage();
	Artist currentArtist = massageArtist(artist);
	Text artistText = getRandomEffect(artist);
	 
	lastArtist=currentArtist;
	
	Text trackNameText = new Text(title);
    	
    // TODO
    // infoWindow.initStyle(StageStyle.TRANSPARENT);
    	
    infoWindow.initModality(Modality.NONE);
        
    GridPane gp = getGridPane();
    gp.add(artistText, 0, 0);
    gp.add(trackNameText, 0, 1);
        
    final Rectangle r = new Rectangle(1200, 600);
    r.setFill(Color.DARKGRAY);
        
    root.getChildren().add(r);
    root.getChildren().add(gp);
    Scene scene = new Scene(root, 1200, 600);
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
    gridPane.setStyle("-fx-background-color: darkgrey;");
    return gridPane;
  }
  
  public void update(String artist, String title)
  {
	int effectChoice=random.nextInt(1);
	int fontChoice=0;
	Artist currentArtist = massageArtist(artist);
	if (lastArtist.lastName.equals(currentArtist.lastName))
	  fontChoice=lastFontNumber;
	else
	  fontChoice= random.nextInt(10);
	  
	update(artist, title, fontChoice, effectChoice); 
  }
  
  public void update(String artist, String title, int fontIndex, int effectIndex)
  {
  	Text artistText;
  	Text titleText = new Text(title);
  	
  	
  	Artist currentArtist=massageArtist(artist);
  	  	  	
  	 // if (effectIndex==1)  artistText = getDropShadow(artist , fontList.get(fontIndex));
  	//  else  artistText =  getDistantLight(artist, fontList.get(fontIndex));
  	  
  	
  	  artistText =  getDistantLight(artist, fontList.get(fontIndex));

  	  getFont(currentArtist.lastName, fontMeta.get(fontIndex));
  	  
  	 GridPane gp = getGridPane();
  	 
     GridPane.setHalignment(artistText, HPos.CENTER);
     GridPane.setHalignment(titleText, HPos.CENTER);
     
  	 lastArtist=currentArtist;
  	 lastFontNumber=fontIndex;
  	 
     gp.add(artistText, 0, 0);
     gp.add(new Text(title), 0, 1);
  	
    root.getChildren().remove(1);
  	root.getChildren().add(gp);
  }
  
  private Text getRandomEffect(String txt)
  {
  	int choice=random.nextInt(1);
  	if (choice==1) return getDropShadow(txt, fontList.get(random.nextInt(10)));
  	else return getDistantLight(txt, fontList.get(random.nextInt(10)));
  }
  
  private Font getFont(String inStr, FontMeta fontMeta)
  {
	Font trialFont;  
	Text trialText;
	Bounds bounds;
	
	int numberOfChars=inStr.length();
	int fontSize=200;
	if (numberOfChars>8) fontSize=160;
	if (numberOfChars>12) fontSize=140;
	if (numberOfChars>16) fontSize=120;
	
	trialFont = Font.font(fontMeta.name, fontMeta.style, fontSize);
	
	trialText = new Text(inStr);
	trialText.setFont(trialFont);
	bounds=trialText.getBoundsInLocal();
	
	System.out.println("getFont() - str size: "+numberOfChars
			+" fontSize: "+fontSize
			+" width: "+(int)bounds.getWidth()
			+" height: "+(int)bounds.getHeight()
			);
	
	
	
	return trialFont;
	
  }
  
  private Font getSizedFont(String inStr, FontMeta fontMeta, int fontSize) 
  {
	Font aFont = Font.font(fontMeta.name, fontMeta.style, fontSize);
	return null;
}

private void loadFonts()
  {
    Font.loadFont(Test.class.getResource("/resources/fonts/blackrose.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/Carousel.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/Anagram.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/blackjack.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/Carrington.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/DEFTONE.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/EastMarket.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/england.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/FFF_Tusj.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/PermanentMarker.ttf").toExternalForm(), 10  );
	Font.loadFont(Test.class.getResource("/resources/fonts/SEASRN.ttf").toExternalForm(), 10  );

	int fontSize=160;
	fontList.add(Font.font("Black Rose", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("Carousel", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("Anagram", FontWeight.NORMAL, fontSize-20));
	fontList.add(Font.font("BlackJack", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("Carrington", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("Deftone Stylus", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("EastMarket", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("England Hand DB", FontWeight.BOLD, fontSize));
	fontList.add(Font.font("FFF Tusj", FontWeight.BOLD, fontSize-10));
	fontList.add(Font.font("Permanent Marker", FontWeight.NORMAL, fontSize));
	fontList.add(Font.font("SeasideResortNF", FontWeight.NORMAL, fontSize));
	    
	fontMeta.add(new FontMeta("Black Rose", FontWeight.BOLD));
	fontMeta.add(new FontMeta("Carousel", FontWeight.BOLD));
	fontMeta.add(new FontMeta("Anagram", FontWeight.NORMAL));
	fontMeta.add(new FontMeta("BlackJack", FontWeight.BOLD));
	fontMeta.add(new FontMeta("Carrington", FontWeight.BOLD));
	fontMeta.add(new FontMeta("Deftone Stylus", FontWeight.BOLD));
	fontMeta.add(new FontMeta("EastMarket", FontWeight.BOLD));
	fontMeta.add(new FontMeta("England Hand DB", FontWeight.BOLD));
	fontMeta.add(new FontMeta("FFF Tusj", FontWeight.BOLD));
	fontMeta.add(new FontMeta("Permanent Marker", FontWeight.NORMAL));
	fontMeta.add(new FontMeta("SeasideResortNF", FontWeight.NORMAL));
  }
  
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
	
	private Text getDistantLight(String str, Font f)
	{
	  Light.Distant light = new Light.Distant();
	  light.setAzimuth(-135.0f);
	  light.setElevation(30.0f);

	  Lighting l = new Lighting();
	  l.setLight(light);
	  l.setSurfaceScale(5.0f);

	  final Text t = new Text();
	  t.setText(str);
	  t.setFill(Color.RED);
	  t.setFont(f);
	      
	  //  t.setTextOrigin(VPos.TOP);

	  t.setEffect(l);
	
	  return t;
	}

	public Artist massageArtist(String inStr)
	{
	  if (inStr.toLowerCase().contains("biagi"))       return new Artist("Rudolfo", "Biagi");
	  if (inStr.toLowerCase().contains("calo"))        return new Artist("Miguel", "Calo");
	  if (inStr.toLowerCase().contains("canaro"))      return new Artist("Francisco", "Canaro");
	  if (inStr.toLowerCase().contains("rodriguez"))   return new Artist("Enrique", "Rodriguez");
	  if (inStr.toLowerCase().contains("d'arienzo"))   return new Artist("Juan", "D'Arienzo");
	  if (inStr.toLowerCase().contains("de angelise")) return new Artist("Alfredo", "De Angelise");
	  if (inStr.toLowerCase().contains("di sarli"))    return new Artist("Caslos", "Di Sarli");
	  
	  
	  return new Artist("", inStr);
	}
	
}

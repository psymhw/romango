package tangodj;

import java.util.ArrayList;
import java.util.Random;

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
  String lastArtist;
  int lastFontNumber=0;
  boolean test=false;
 
  
  public InfoWindow(String artist, String title, boolean test)
  {
	 this(artist, title); 
	 this.test=test;
  }
  
  public InfoWindow(String artist, String title)
  {
	 loadFonts();
	 
	 
	 artist=massageArtist(artist);
	 Text artistText = getRandomEffect(artist);
	 
	 lastArtist=artist;
	
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
	  if (lastArtist.equals(artist))
	     fontChoice=lastFontNumber;
	  else
		  fontChoice= random.nextInt(10);
	  
	 update(artist, title, fontChoice, effectChoice); 
  }
  
  public void update(String artist, String title, int fontIndex, int effectIndex)
  {
  	Text artistText;
  	Text titleText = new Text(title);
  	
  	artist=massageArtist(artist);
  	  	  	
  	  if (effectIndex==1)  artistText = getDropShadow(artist , fontList.get(fontIndex));
  	  else  artistText =  getDistantLight(artist, fontList.get(fontIndex));
  	
  	 GridPane gp = getGridPane();
  	 
     GridPane.setHalignment(artistText, HPos.CENTER);
     GridPane.setHalignment(titleText, HPos.CENTER);
     
  	 lastArtist=artist;
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

	public String massageArtist(String inStr)
	{
	  if (inStr.toLowerCase().contains("biagi")) return "Rudolfo Biagi";
	  if (inStr.toLowerCase().contains("calo"))        return "Miguel Calo";
	  if (inStr.toLowerCase().contains("canaro"))      return "Francisco Canaro";
	  if (inStr.toLowerCase().contains("rodriguez"))   return "Enrique Rodriguez";
	  if (inStr.toLowerCase().contains("d'arienzo"))   return "Juan D'Arienzo";
	  if (inStr.toLowerCase().contains("de angelise")) return "Alfredo De Angelise";
	  if (inStr.toLowerCase().contains("di sarli"))    return "Caslos Di Sarli";
	  
	  
	  return inStr;
	}
	
}

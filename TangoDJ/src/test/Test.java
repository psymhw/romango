package test;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test  extends Application  
{


	public void start(Stage stage) throws Exception 
	{
		Group root = new Group();
				
	    Circle circle = new Circle();
	    circle.setCenterX(30);
	    circle.setCenterY(30);
	    circle.setRadius(10.0f);
	    //circle.setVisible(false);
	    
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
	    
	    int fontSize=60;
	    Font blackRose = Font.font("Black Rose", FontWeight.BOLD, fontSize);
	    Font carousel = Font.font("Carousel", FontWeight.BOLD, fontSize);
	    Font anagram = Font.font("Anagram", FontWeight.NORMAL, fontSize);
	    Font blackjack = Font.font("BlackJack", FontWeight.BOLD, fontSize);
	    Font carrington = Font.font("Carrington", FontWeight.BOLD, fontSize);
	    Font deftone = Font.font("Deftone Stylus", FontWeight.BOLD, fontSize);
	    Font eastMarket = Font.font("EastMarket", FontWeight.BOLD, fontSize);
	    Font england = Font.font("England Hand DB", FontWeight.BOLD, fontSize);
	    Font tusj = Font.font("FFF Tusj", FontWeight.BOLD, fontSize);
	    Font marker = Font.font("Permanent Marker", FontWeight.NORMAL, fontSize);
	    Font seaside = Font.font("SeasideResortNF", FontWeight.NORMAL, fontSize);
	    
	   
	    int xPos=10;
	    int yPos=50;
	    
	    Text tx = getDropShadow("Black Rose", blackRose, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Black Rose", blackRose, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
	    
	    //================================================
	    
	    tx = getDropShadow("Carousel", carousel, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Carousel", carousel, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
	    
	    //================================================
	    
	    tx = getDropShadow("Anagram", anagram, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Anagram", anagram, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);

	    //================================================
	    
	    tx = getDropShadow("BlackJack", blackjack, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("BlackJack", blackjack, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
   
	    //================================================
	    
	    tx = getDropShadow("Carrington", carrington, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Carrington", carrington, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
    
	    //================================================
	    
	    tx = getDropShadow("Deftone", deftone, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Deftone", deftone, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
    
		xPos=400;
		yPos=50;
	    
	    //================================================
	    
	    tx = getDropShadow("East Market", eastMarket, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("East Market", eastMarket, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);

	    //================================================
	    
	    tx = getDropShadow("England", england, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("England", england, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);

	    //================================================
	    
	    tx = getDropShadow("Tusji", tusj, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Tusji", tusj, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);

	    //================================================
	    
	    tx = getDropShadow("Marker", marker, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Marker", marker, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);

	    //================================================
	    
	    tx = getDropShadow("Seaside", seaside, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
		
	    tx = getDistantLight("Seaside", seaside, xPos, yPos); 
	    yPos+=(fontSize+10);
	    root.getChildren().add(tx);
	    
	   

       
        Scene scene = new Scene(root, 800, 850);
        scene.setFill(Color.LIGHTGRAY);
		stage.setScene(scene);
		stage.show();
		
	
		   
	}
	
	private Text getDropShadow(String str, Font f, int xPos, int yPos)
	{
		DropShadow ds = new DropShadow();
	    ds.setOffsetY(3.0f);
	    ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
	     
	    Text tx = new Text();
	    tx.setEffect(ds);
	    tx.setCache(true);
	    tx.setX(xPos);
	    tx.setY(yPos);
	    tx.setFill(Color.RED);
	    tx.setText(str);
	    tx.setFont(f);
	    return tx;
	}
	
	private Text getDistantLight(String str, Font f, int xPos, int yPos)
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
	        t.setX(xPos);
	        t.setY(yPos);
	      //  t.setTextOrigin(VPos.TOP);

	        t.setEffect(l);
	
	        return t;
	}
	
	
	 public static void main(final String[] arguments)  
	   {  
	      Application.launch(arguments);  
	   }


}

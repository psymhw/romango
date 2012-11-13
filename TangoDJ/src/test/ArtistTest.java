package test;

import java.util.ArrayList;

import tangodj.InfoWindow;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArtistTest extends Application
{
	 private InfoWindow infoWindow;
	
	  ArrayList<String> testArtists = new ArrayList<String>();
	//  ArrayList<Button> buttonList = new ArrayList<Button>();
	  int testArtistIndex=0;
	  int testArtistSize=0;
	  int testFontIndex=0;
	  int testEffectIndex=0;
	    
	@Override
	public void start(Stage stage) throws Exception 
	{
		Group root = new Group();
		// root.getChildren().add(next);
		
		 testArtists.add("biagi");
		 testArtists.add("calo");
		 testArtists.add("canaro");
		 testArtists.add("rodriguez");
		 testArtists.add("d'arienzo");
		 testArtists.add("de angelise");
		 testArtists.add("di sarli");
		 				 
		
		 
		 
		 GridPane gp = new GridPane();
		 gp.setHgap(3);
		    gp.setVgap(3);
		    gp.setPadding(new Insets(10));
		 infoWindow = new InfoWindow("Hello World", "Some Title", true);
		 		 		 
		 for(int i=0; i<infoWindow.fontList.size(); i++)
		 {
		   gp.add(getFontButton(i), 0, i);
		 }
		 
		 for(int j=0; j<testArtists.size(); j++)
		 {
		   gp.add(getArtistButton(j), 1, j);
		 }
		 
		 Button effectButton1 = new Button("Drop Shadow");
		 effectButton1.setStyle("-fx-font-size:28;");
		 effectButton1.setOnAction(new EventHandler<ActionEvent>() {
		      @Override
		      public void handle(ActionEvent event) {
		        testEffectIndex=1;
		        update();
		      }
		    });
		 
		 Button effectButton2 = new Button("Distant Light");
		 effectButton2.setStyle("-fx-font-size:28;");
		 effectButton2.setOnAction(new EventHandler<ActionEvent>() {
		      @Override
		      public void handle(ActionEvent event) {
		        testEffectIndex=0;
		        update();
		      }
		    });
		 
		 gp.add(effectButton1, 2, 0);
		 gp.add(effectButton2, 2, 1);
		
		 
		 root.getChildren().add(gp);
		
		 Scene scene = new Scene(root, 900, 800);
	        scene.setFill(Color.LIGHTGRAY);
			stage.setScene(scene);
			stage.show();
			
	}
	
	 private Button getFontButton(int fontNumber)
	 {
		Font f = infoWindow.fontList.get(fontNumber);
		
		final String fontName= f.getName();
		final String fontFamily=f.getFamily();
		Button b = new Button(fontName);
		b.setFont(f);
		b.setStyle("-fx-font-family:"+fontFamily+"; -fx-font-size:32;");
		
		final int fn = fontNumber;
		b.setOnAction(new EventHandler<ActionEvent>() {
		      @Override
		      public void handle(ActionEvent event) {
		        testFontIndex=fn;
		        update();
		      }
		    });
		
		return b;
	 }
	    
	 private Button getArtistButton(int artistNumber)
	 {
		final String artistName= infoWindow.massageArtist(testArtists.get(artistNumber));
		Button b = new Button(artistName);
		b.setStyle("-fx-font-size:28;");
		final int an = artistNumber;
		b.setOnAction(new EventHandler<ActionEvent>() {
		      @Override
		      public void handle(ActionEvent event) {
		        testArtistIndex=an;
		        update();
		      }
		    });
		
		return b;
	 }
	    
	 void update()
	 {
       String massagedArtist=infoWindow.massageArtist(testArtists.get(testArtistIndex));
       infoWindow.update(massagedArtist, "some title)", testFontIndex, testEffectIndex);
	 }
	 

	 public static void main(final String[] arguments)  
	   {  
	      Application.launch(arguments);  
	      
	     
	   }
	
}

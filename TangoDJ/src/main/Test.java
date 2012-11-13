package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
	    
	    DropShadow ds = new DropShadow();
	    ds.setOffsetY(3.0f);
	    ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
	     
	    Text t = new Text();
	    t.setEffect(ds);
	    t.setCache(true);
	    t.setX(10.0f);
	    t.setY(100.0f);
	    t.setFill(Color.RED);
	    t.setText("Rodriguez");
	    t.setFont(Font.font("Serif", FontWeight.BOLD, 42));
	  
	   root.getChildren().add(circle);
	 
	   root.getChildren().add(t);
	   
        Scene scene = new Scene(root, 400, 300);
        scene.setFill(Color.LIGHTGRAY);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
	
	 public static void main(final String[] arguments)  
	   {  
	      Application.launch(arguments);  
	   }


}

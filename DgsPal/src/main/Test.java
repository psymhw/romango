package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Test  extends Application  
{


	public void start(Stage stage) throws Exception 
	{
		Group root = new Group();
		Group stoneGroup = new Group();
		Group moveNumbersGroup = new Group();
		int x=30, y=30;
		String src="/resources";
		Image white_stone_image = new Image(Stone.class.getResourceAsStream(src+"/images/w.gif"));
		
		
		int textLength1=0;
		int textLength2=0;
		int textLength3=0;
		
		Label label1 = new Label("4");
		Label label2 = new Label("22");
		Label label3 = new Label("123");
		label1.setFont(Font.font("Serif", 15));
		label1.setTextFill(Color.BLACK);
	    label2.setFont(Font.font("Serif", 15));
	    label2.setTextFill(Color.BLACK);
	    label3.setFont(Font.font("Serif", 15));
	    label3.setTextFill(Color.BLACK);
		
		textLength1=label1.getText().length();
		textLength2=label2.getText().length();
		textLength3=label3.getText().length();
		
	    ImageView iv1 = new ImageView(white_stone_image);
	    ImageView iv2 = new ImageView(white_stone_image);
	    ImageView iv3 = new ImageView(white_stone_image);
	    
	    iv1.setX(x);
	    iv1.setY(y);
	    label1.setLayoutX(x+13);
	    label1.setLayoutY(y+9);
	    
	    x=70; y=70;
	    iv2.setX(x);
	    iv2.setY(y);
	    label2.setLayoutX(x+9);
	    label2.setLayoutY(y+9);

	    x=110; y=110;
	    iv3.setX(x);
	    iv3.setY(y);
	    label3.setLayoutX(x+5);
	    label3.setLayoutY(y+9);

	    
	    stoneGroup.getChildren().add(iv1);
	    stoneGroup.getChildren().add(iv2);
	    stoneGroup.getChildren().add(iv3);
	    
	    moveNumbersGroup.getChildren().add(label1);
	    moveNumbersGroup.getChildren().add(label2);
	    moveNumbersGroup.getChildren().add(label3);
	    
	    root.getChildren().add(stoneGroup);
	    root.getChildren().add(moveNumbersGroup);
	    
	    moveNumbersGroup.setVisible(false);
	    
	    
        Scene scene = new Scene(root, 200, 150);
        scene.setFill(Color.LIGHTGRAY);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
	
	 public static void main(final String[] arguments)  
	   {  
	      Application.launch(arguments);  
	   }


}

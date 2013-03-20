package test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test  extends Application  
{
  public void start(Stage stage) throws Exception 
  {
    Group root = new Group();
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

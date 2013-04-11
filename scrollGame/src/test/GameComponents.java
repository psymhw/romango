package test;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameComponents extends GameWorld
{

	public GameComponents(int fps, String title) {
		super(fps, title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(Stage primaryStage) 
	{
	  primaryStage.setTitle(getWindowTitle());
	 
	  // Create the scene
	  setSceneNodes(new Group());
	  setGameSurface(new Scene(getSceneNodes(), 800, 600));
      getGameSurface().setFill(Color.BLACK);
      primaryStage.setScene(getGameSurface());
      // Setup Game input
     // setupInput(primaryStage);
     // Sliding clouds = getClouds();
     // getSpriteManager().addSprites(clouds);
     // getSceneNodes().getChildren().add(new SlidingBackground(800,50));
      BoxSprite box = new BoxSprite();
      
      getSpriteManager().addSprites(box);
      getSceneNodes().getChildren().add(box.getImage());
       
      

	}
	
	private Sliding getClouds() {
		Cloud[] clouds = new Cloud[]{new Cloud(100, 100), new Cloud(150, 20), new Cloud(220, 150), new Cloud(260, 200), new Cloud(310, 40), new Cloud(390, 150), new Cloud(450, 30), new Cloud(550, 100)};
        Sliding cloudSlider = new Sliding(clouds, 800, 100);
		return cloudSlider;
	}


}

package test;

//import tutorial.Sprite;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameComponents extends GameWorld
{
	BoxSprite box;
	Image garages;
	Background front;
	Background front2;
	int lastFront=0;
	boolean addFlag=false;
	int GAME_WIDTH=800;
	int GAME_HEIGHT=600;
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
	  setGameSurface(new Scene(getSceneNodes(), GAME_WIDTH, GAME_HEIGHT));
      getGameSurface().setFill(Color.BLACK);
      primaryStage.setScene(getGameSurface());
      // Setup Game input
      setupInput(primaryStage);
     // Sliding clouds = getClouds();
     // getSpriteManager().addSprites(clouds);
     // getSceneNodes().getChildren().add(new SlidingBackground(800,50));
      
      
      // only automaticly moving sprites added to spriteManager
      Image garages = new Image(GameComponents.class.getResourceAsStream("/resources/images/garages.jpg"));
      front = new Background(garages, 2);
      front2 = new Background(garages, 2);
      front2.getImageView().setTranslateX(GAME_WIDTH);
      
      front.getImageView().setTranslateY(GAME_HEIGHT-193);
      front2.getImageView().setTranslateY(GAME_HEIGHT-193);
      
      
      getSpriteManager().addSprites(front);
      getSpriteManager().addSprites(front2);
      getSceneNodes().getChildren().add(front.getImageView());
      getSceneNodes().getChildren().add(front2.getImageView());
      
      box = new BoxSprite();
      getSceneNodes().getChildren().add(box.getImage());
      
     // Boy boy = new Boy();
     // getSpriteManager().addSprites(boy);
      
	}
	
	private Sliding getClouds() {
		Cloud[] clouds = new Cloud[]{new Cloud(100, 100), new Cloud(150, 20), new Cloud(220, 150), new Cloud(260, 200), new Cloud(310, 40), new Cloud(390, 150), new Cloud(450, 30), new Cloud(550, 100)};
        Sliding cloudSlider = new Sliding(clouds, 800, 100);
		return cloudSlider;
	}

	 /**
     * Each sprite will update it's velocity and bounce off wall borders.
     *
     * @param sprite - An atomic particle (a sphere).
     */
    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object
        
    	sprite.update();
        if (sprite instanceof Background)
        {
          int trailingX=(int)((Background) sprite).getImageView().getTranslateX()+GAME_WIDTH;
          if (trailingX<=0)
          {
            if (lastFront==0)
            {
            	front.getImageView().setTranslateX(trailingX+GAME_WIDTH);
            	lastFront=1;
            }
            else
            {
            	front2.getImageView().setTranslateX(trailingX+GAME_WIDTH);
            	lastFront=0;
            }
      
                  
          
          }
        	
        }
    }
    
    private void setupInput(Stage primaryStage) {
	 
	EventHandler keypress = new EventHandler() {
            public void handle(Event me) 
            { 
            	box.update();
            }

            	
    };
    primaryStage.getScene().setOnKeyPressed(keypress);
    
    }

}

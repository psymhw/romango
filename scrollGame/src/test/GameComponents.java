package test;

//import tutorial.Sprite;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameComponents extends GameWorld
{
	BoxSprite box;
	Image garages;
	Background front;
	Background front2;
	BoySprite boy;
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
      
     
      
     // box = new BoxSprite();
     // getSceneNodes().getChildren().add(box.getImage());
      
      boy = new BoySprite();
      getSpriteManager().addSprites(boy);
      getSceneNodes().getChildren().add(boy.getImageView());
      
      addLineOfCoins();
	}
	
	private void addLineOfCoins()
	{
	  for(int i=0; i<5; i++)
	  {
		Coin coin = new Coin(850+(i*20), 450);
		getSpriteManager().addSprites(coin);
		getSceneNodes().getChildren().add(coin.getCoin());  
	  }
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
    
    private void setupInput(Stage primaryStage) 
    {
	  EventHandler<KeyEvent> keypress = new EventHandler<KeyEvent>() 
	  {
        public void handle(KeyEvent event) 
        { 
          if (event.getCode()==KeyCode.RIGHT) 
          {
        	boy.makeRun();
        	front.setRate(3);
        	front2.setRate(3);
          }
          if (event.getCode()==KeyCode.UP) 
          {
        	boy.up=true;
          }
          if (event.getCode()==KeyCode.ESCAPE) { System.exit(0); }
        }
        
        
    };
    
    EventHandler<KeyEvent> keyrelease = new EventHandler<KeyEvent>() 
    {
      public void handle(KeyEvent event) 
      { 
        if (event.getCode()==KeyCode.RIGHT) 
    	{
    	  boy.makeWalk();
      	  front.setRate(2);
      	  front2.setRate(2);
    	}
        
        if (event.getCode()==KeyCode.UP) 
    	{
          boy.suspendFall=false;
          boy.up=false;
    	}
      }

    	        
    	            	
    	    };
    primaryStage.getScene().setOnKeyPressed(keypress);
    primaryStage.getScene().setOnKeyReleased(keyrelease);
    
    }
    
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) 
    {
    	//System.out.println("handle collisions");
      if (spriteA != spriteB) 
      {
    	    
    	 if (spriteA.collide(spriteB)) 
    	{
    		// if ("coin".equals(spriteA.type)) System.out.println("A coin collision");
        	// if ("coin".equals(spriteB.type)) System.out.println("B coin collision");
        
    		
          if (spriteA instanceof Coin)
          {
        	System.out.println("A is Coin");  
          }
          if (spriteB instanceof Coin)
          {
        	System.out.println("B is Coin");  
          }
    		
    		return true;  	
        }
      }
      return false;
    }

}

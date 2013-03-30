package test;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Boy extends ImageView
{
	   private static final Image FORWARD_IMAGE 
	   = new Image(SideScroller.class.getResourceAsStream("/resources/images/newboy2.png"));
	  
	   private static final Image REVERSE_IMAGE 
	   = new Image(SideScroller.class.getResourceAsStream("/resources/images/newboy2rev.png"));
	  
	    private static final int COLUMNS  =   2;
	    private static final int COUNT    =  2;
	    private static final int OFFSET_X =  0;
	    private static final int OFFSET_Y =  0;
	    private static final int WIDTH    = 33;
	    private static final int HEIGHT   = 42;
	    int FORWARD = 0;
	    int REVERSE = 1;
	    
	    private int direction=0;
	    private int walkDuration=1000;
	    private int runDuration=400;
	    private boolean running=false;
	    
	    final SpriteAnimation animation;

	public Boy ()
	{
		this.setImage(FORWARD_IMAGE);
        this.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
         animation = new SpriteAnimation(
                this,
                Duration.millis(1000),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        this.setX(50);
        this.setY(340);
	}
	
	public void setDirection(int direct)
	{
	  if (direct==FORWARD) 
	  {
		if (direction==REVERSE)
		{
			setImage(FORWARD_IMAGE);
			direction=FORWARD;
		}
	  }
	  else
	  {
		  if (direction==FORWARD)
			{
				setImage(REVERSE_IMAGE);
				direction=REVERSE;
			}
		  
	  }
	}
	
	public void makeRun()
	{
	  if (!running)
	  {
		animation.setRate(3);  
	    //animation.stop();
	    //animation.setDuration(runDuration);
	    //animation.play();
	    running=true;
	  }
	}
	
	public void makeWalk()
	{
	  if (running)
	  {
		  animation.setRate(1);  
		//animation.stop();
	    //animation.setDuration(walkDuration);
	    //animation.play();
	    running=false;
	  }
	}
	
}

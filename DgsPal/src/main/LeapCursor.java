package main;

import com.leapmotion.leap.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LeapCursor extends Group
{
  public float lastZ=0;
  public boolean moveFlag=false;
  public boolean swipeFlag=false;
  public float moveX=0;
  public float moveY=0;
  public boolean coordinateFreeze=false;
  private ImageView imageView;
  private ImageView imageView_green;
  private ImageView imageView_red;
  final int imageOffset=8;
  final int baseX=350;
  final int baseY=800;
  private boolean active = false;
  static LeapListener listener;
  static Controller controller;
  public SimpleIntegerProperty action= new SimpleIntegerProperty(0);
  private long inactivityTime =0;
  
  private Move move;
	
  public LeapCursor()
  {
	String src="/resources";
	Image black_ghost_image = new Image(Stone.class.getResourceAsStream(src+"/images/b_ghost.png")); 
	Image green_ghost = new Image(Stone.class.getResourceAsStream(src+"/images/green_ghost_overlay.png")); 
	Image red_ghost = new Image(Stone.class.getResourceAsStream(src+"/images/red_ghost_overlay.png")); 
	imageView = new ImageView(black_ghost_image);
	imageView_green = new ImageView(green_ghost);
	imageView_red = new ImageView(red_ghost);
	final int imageOffset=8;
	
	 listener = new LeapListener();
	 controller = new Controller();
	 controller.addListener(listener);
	  
	imageView.setX(imageOffset);
	imageView.setY(imageOffset);
	
	imageView.setVisible(false);
	
	imageView_green.setX(imageOffset);
	imageView_green.setY(imageOffset);
	imageView_green.setOpacity(0);
	
	imageView_red.setX(imageOffset);
	imageView_red.setY(imageOffset);
	imageView_red.setOpacity(0);
	
	
	
	this.getChildren().add(imageView);
	this.getChildren().add(imageView_green);
	this.getChildren().add(imageView_red);
	
	inactivityTime=System.currentTimeMillis();
	
	Timeline timeline = new Timeline();
	timeline.setCycleCount(Timeline.INDEFINITE);
	
	
	KeyFrame keyFrame= new KeyFrame(Duration.millis(5), new EventHandler()
	{
	  public void handle(Event event) 
	  {
		 int swipeAction = listener.getSwipeAction();
		 
		 if (!swipeFlag)
		 {	 
		   if (swipeAction!=GoClient.RESET)
		   {
		     action.set(swipeAction);
		     swipeFlag=true;
		     return;
		   }
		 }
			 
	     MyFinger myFinger = listener.getFinger();
	     if (myFinger!=null)
	     {
	       if (myFinger.z<0)
	       {	  
	    	 if (!coordinateFreeze)
	    	 {
	    	   savePosition(myFinger.x,  myFinger.y);
	    	 }
	    	 
	    	 
	    	 imageView_green.setOpacity(0);
	    	 if ((myFinger.z>-30)&&(myFinger.z<0))
	    	 {
	    		 imageView_red.setOpacity(-(myFinger.z/30)); 
	    	 }
	    	 
	    	 if (!moveFlag)
	    	 {	  
	    	   if (myFinger.z<(-30)) 
	    	   {
	    	   	 System.out.println("MOVE: "+myFinger.z);
	    	     makeMove();
	    	   }
	         }
	       }
	       else
	       {
	    	 setPosition(myFinger.x, myFinger.y);
	    	 if ((myFinger.z<30)&&(myFinger.z>0))
	    	 {
	    	  imageView_red.setOpacity(0); 	 
	    	   imageView_green.setOpacity(1-(myFinger.z/30));
	    	 }
	       }
	       lastZ=myFinger.z;
	     }
	     else
	     { 
	       if (active)
	       {
	         long currentTime = System.currentTimeMillis();
	         if (currentTime>inactivityTime+1000) fade();
	       }
	     }
	   }
	 });
		 
	 timeline.getKeyFrames().add(keyFrame);
	 timeline.play();
  }

  /*
  public void setPos(double x, double y)
  {
	  imageView.setX(x+imageOffset);
	  imageView.setY(y+imageOffset);
  }
  */
  public void savePosition(float x, float y)
  {
	 moveX=x;
	 moveY=y;
	 coordinateFreeze=true;
  }
  
  public void setPosition(float x, float y)
  {
	//  imageView_green.setOpacity(0);
	//  imageView_red.setOpacity(0);
	inactivityTime=System.currentTimeMillis();
	moveFlag=false;  
	coordinateFreeze=false;
	if (action.get()!=GoClient.RESET) resetAction();
	if (!active) { active=true; imageView.setVisible(true); }
	String sgfPos=getSgfPosition(x, y);
	//System.out.println("SGF pos: "+sgfPos);
	imageView.setX(Move.calcSceneX(sgfPos)+imageOffset);
	imageView.setY(Move.calcSceneY(sgfPos)+imageOffset);
	imageView_green.setX(Move.calcSceneX(sgfPos)+imageOffset);
	imageView_green.setY(Move.calcSceneY(sgfPos)+imageOffset);
	imageView_red.setX(Move.calcSceneX(sgfPos)+imageOffset);
	imageView_red.setY(Move.calcSceneY(sgfPos)+imageOffset);
	
  }
  
  private String getSgfPosition(float x, float y)
  {
	String sgfPos=Move.getSgfPosition(baseX+(2*x), baseY-(2*y)); 
	return sgfPos;
  }
  
  public String getSgfPositionForMove()
  {
	 return getSgfPosition(moveX,moveY); 
  }
  
  public void makeMove()
  {
	  moveFlag=true; 
	  move = new Move(getSgfPositionForMove(), -1); // color not assigned here
	  fade();
      action.set(GoClient.MAKE_MOVE);
  }
  
  private void fade()
  {
	  FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), imageView);
      fadeTransition.setFromValue(0.0);
      fadeTransition.setToValue(1.0);
      fadeTransition.play();
      imageView.setVisible(false);
      active = false;
      swipeFlag=false;
  }
  
  public Move getMove(int color)
  {
	move.setColor(color); 
	return move;
  }
  
  public void resetAction()
  {
	action.set(GoClient.RESET);
  }
}

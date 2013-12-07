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
  private ImageView cursorView;
  private ImageView greenCross;
  private ImageView redCross;
  private ImageView yellowCross;
  final int imageOffset=7;
  final int baseX=350;
  final int baseY=800;
  private boolean active = false;
  static LeapListener listener;
  static Controller controller;
  public SimpleIntegerProperty action= new SimpleIntegerProperty(0);
  private long inactivityTime =0;
  String src="/resources";
  Image b_cursor = new Image(Stone.class.getResourceAsStream(src+"/images/b.gif")); 
  Image w_cursor = new Image(Stone.class.getResourceAsStream(src+"/images/w.gif")); 
  
  private Move move;
	
  public LeapCursor()
  {
	
	Image red_cross = new Image(Stone.class.getResourceAsStream(src+"/images/red_cross.png")); 
	Image green_cross = new Image(Stone.class.getResourceAsStream(src+"/images/green_cross.png")); 
	Image yellow_cross = new Image(Stone.class.getResourceAsStream(src+"/images/yellow_cross.png")); 
	
	redCross = new ImageView(red_cross);
	greenCross = new ImageView(green_cross);
	yellowCross = new ImageView(yellow_cross);
	
	cursorView = new ImageView(b_cursor);
	final int imageOffset=8;
	
	 listener = new LeapListener();
	 controller = new Controller();
	 controller.addListener(listener);
	  
	setPositions(imageOffset, imageOffset);
	
	cursorView.setOpacity(0);
	redCross.setOpacity(0);
	greenCross.setOpacity(0);
	yellowCross.setOpacity(0);
	
	
	this.getChildren().add(cursorView);
	this.getChildren().add(yellowCross);
	this.getChildren().add(redCross);
	this.getChildren().add(greenCross);
	
	
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
	    	 
	    	 
	    	 if ((myFinger.z>-30)&&(myFinger.z<0))
	    	 {
	    	   greenCross.setOpacity(0);
	    	   redCross.setOpacity(-(myFinger.z/30));
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
             if (moveFlag==false)
             {
               if ((myFinger.z<30)&&(myFinger.z>0))
	    	   {
            	 redCross.setOpacity(0);  
            	 greenCross.setOpacity(1-(myFinger.z/30));
	    	   }
             }
	       }
	       lastZ=myFinger.z;
	     }
	     else
	     { 
	       if (active)
	       {
	         long currentTime = System.currentTimeMillis();
	         if (currentTime>inactivityTime+3000) fade();
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
	inactivityTime=System.currentTimeMillis();
	
	moveFlag=false;  
	coordinateFreeze=false;
	if (action.get()!=GoClient.RESET) resetAction();
	//if (!active) { active=true; cursorView.setOpacity(1); yellowCross.setOpacity(1); }
	 active=true; cursorView.setOpacity(1); 
	 yellowCross.setOpacity(1);
	String sgfPos=getSgfPosition(x, y);
	setPositions(Move.calcSceneX(sgfPos)+imageOffset, Move.calcSceneY(sgfPos)+imageOffset);
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
  
  public void fade()
  {
	  redCross.setOpacity(0);
      greenCross.setOpacity(0);
      //yellowCross.setOpacity(0);
      
	  FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), cursorView);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      
      FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(500), yellowCross);
      fadeTransition2.setFromValue(1);
      fadeTransition2.setToValue(0);
      
      fadeTransition.play();
      fadeTransition2.play();
      
     // imageView.setVisible(false);
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
	//fade();
  }
  
  public void setMoveColor(int moveColor)
  {
	if (moveColor==GoClient.BLACK) cursorView.setImage(b_cursor);
	else { 
		
		cursorView.setImage(w_cursor);
	
	}
  }
  
  private void setPositions(float x, float y)
  {
	cursorView.setX(x);
	cursorView.setY(y);
	redCross.setX(x);
	redCross.setY(y);
	greenCross.setX(x);
	greenCross.setY(y);
	yellowCross.setX(x);
	yellowCross.setY(y);
  }
}

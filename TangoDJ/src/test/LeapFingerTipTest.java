package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class LeapFingerTipTest  extends Application 
{
  static FingerTipListener listener;
  static Controller controller;
  static int width=950;
  static int height = 550;
  
  Group circleGroup = new Group();
  int clearDelay=0;
	
  public static void main(String[] args) 
  {
	listener = new FingerTipListener();
	controller = new Controller();
	controller.addListener(listener);
	launch(args);
  }

  public void start(Stage stage) throws Exception 
  {
	Group root = new Group();
	root.getChildren().add(circleGroup);
	Scene scene = new Scene(root, 950, 550, Color.WHITE);
	  
	stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
	{
	  public void handle(WindowEvent event) 
	  {
		controller.removeListener(listener);
		System.out.println("listener stopped");
	  }
	});
	      
	Timeline timeline = new Timeline();
	timeline.setCycleCount(Timeline.INDEFINITE);
	/*
	 * The timeline is only used as a timer. No double buffered frames or anything. Just updating circles
	 * on a single screen. create, move, resize and remove circles
	 */
	KeyFrame keyFrame= new KeyFrame(Duration.millis(5), new EventHandler()
    {
      public void handle(Event event) 
      {
    	MyFinger[] myFingerArray = listener.getFingerArray();
    	for (int i=0; i<10; i++) 
    	{
    	  if (myFingerArray[i]!=null) updateCircle(myFingerArray[i]);
    	}
    	 
    	clearDelay++;
    	/*
    	 * Clearing old circles too often seems to cause trouble. So only once every 10 frames.
    	 */
    	if (clearDelay>=10)
    	{
    	  clearOldCircles(myFingerArray);
    	  clearDelay=0;
    	}
      }
    });
	 
	timeline.getKeyFrames().add(keyFrame);
    timeline.play();
     
	stage.setScene(scene);
	stage.show();
  }
	
  private void updateCircle(MyFinger myFinger)
  {
	Circle circle;	
	boolean circleFound=false;
	Iterator it = circleGroup.getChildren().iterator();	
	
	
	while(it.hasNext())
	{
	  circle = (Circle)it.next();
	  if (circle.getId().equals(myFinger.id))
	  {
		System.out.println("update circle "+myFinger.frameId+" - "+myFinger.id);
		circle.setFill(Color.RED);
		circle.setTranslateX((width/2)+(2*myFinger.x));
	   	circle.setTranslateY(height-(2*myFinger.y)); 
    	circle.setRadius(50-(myFinger.z+250)/10);
    	circleFound=true;
    	break;
	  }
    }
	   
	// if circle not found, add a new circle
	if (circleFound==false)
	{
      System.out.println("new circle "+myFinger.frameId+" - "+myFinger.id);
	  Circle c2 = new Circle();
	  /*
	   * Set the id of the circle to the same value as the leap finger so I can find it later.
	   */
	  c2.setId(myFinger.id);
	  c2.setFill(Color.RED);
	  c2.setTranslateX((width/2)+(2*myFinger.x));
	  c2.setTranslateY(height-(2*myFinger.y)); 
	  c2.setRadius(50-(myFinger.z+250)/10);
	  circleGroup.getChildren().add(c2);
	}
  }
	
  private void clearOldCircles(MyFinger[] myFingerArray)
  {
	boolean[] toRemove = new boolean[10];
	for(int i=0; i<10; i++) toRemove[i]=false;

	Circle circle;
    int count=0;
	Iterator it = circleGroup.getChildren().iterator();	
	  
	boolean removeResult=false;
	
	/*
	 * create a list of circles that are no longer present in the MyFinger array
	 */
	while(it.hasNext())
	{
	  circle = (Circle)it.next();	 
	  if (!circleIdExistsInFingers(circle, myFingerArray))
	  {
		toRemove[count]=true;
		count++;
	  }
    }
	 
	// now remove those circles
	for(int j=0; j<10; j++)
	{
	  try 
	  { 
		 if (toRemove[j]) circleGroup.getChildren().remove(j);
		 } catch(Exception e) { System.out.println("can't find circle to remove");}
	  }
	}

	private boolean circleIdExistsInFingers(Circle circle, MyFinger[] myFingerArray) 
	{
	  boolean rc=false;	
	  
	  for(int i=0; i<10; i++)
	  {
		if (myFingerArray[i]==null) return false;
		if (circle.getId().equals(myFingerArray[i].id)) return true;
	  }
	  return false;
	}

}

package main;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

class LeapListener extends Listener 
{
  private MyFinger myFinger=null;
  private int swipeAction=0;
	
  public void onInit(Controller controller)  { System.out.println("Initialized");  }

  public void onDisconnect(Controller controller) { System.out.println("Disconnected");  }
  
  public void onConnect(Controller controller) 
  { 
	System.out.println("Connected");
    controller.enableGesture(Gesture.Type.TYPE_SWIPE); 
    controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
  }

  public void onExit(Controller controller) { System.out.println("Exited"); }

  public void onFrame(Controller controller) 
  {
    Frame frame = controller.frame();
    FingerList fingers = frame.fingers();
    if (!fingers.empty()) 
    {
    	Finger finger = fingers.frontmost();
    	Vector pos = finger.tipPosition();
    	myFinger = new MyFinger(pos.getX(), pos.getY(), pos.getZ(), ""+finger.id(), ""+frame.id());
    } 
    else myFinger=null;
    
    GestureList gestures = frame.gestures();
    for (int i = 0; i < gestures.count(); i++) {
        Gesture gesture = gestures.get(i);
        if (gesture.type()==Gesture.Type.TYPE_SWIPE)
        {
          SwipeGesture swipe = new SwipeGesture(gesture);
          
          if (swipeAction==GoClient.RESET)
          {	  
            if (swipe.direction().getX()>.7) swipeAction=GoClient.SWIPE_RIGHT;
            if (swipe.direction().getX()<-.7) swipeAction=GoClient.SWIPE_LEFT;
          }
          /*
          System.out.println("Swipe id: " + swipe.id()
                   + ", " + swipe.state()
                   + ", position: " + swipe.position()
                   + ", direction X: " + swipe.direction().getX()
                   + ", direction Y: " + swipe.direction().getY()
                   + ", direction Z: " + swipe.direction().getZ()
                    + ", frame ID: " + frame.id()
                   + ", speed: " + swipe.speed());
          */
        }
        if (gesture.type()==Gesture.Type.TYPE_CIRCLE)
        {
        	 CircleGesture circle = new CircleGesture(gesture);
             
             String clockwiseness;
             if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) 
             {
                // Clockwise if angle is less than 90 degrees
                clockwiseness = "clockwise";
             } else { clockwiseness = "counterclockwise";  }
             
             if (swipeAction==GoClient.RESET)
             {	 
            	if ("counterclockwise".equals(clockwiseness)) 
            	{
            		if (circle.radius()>50) swipeAction=GoClient.CIRCLE_COUNTERCLOCKWISE;
            	 //System.out.println("CIRCLE radius "+circle.radius());
            	}
            	
            	// else swipeAction=GoClient.CIRCLE_COUNTERCLOCKWISE;
             }
             
        }
    }
  }
    
  
  public int getSwipeAction()
  {
	int sa=swipeAction;
	swipeAction=GoClient.RESET;
	return sa;
  }
  
  public MyFinger getFinger()
  {
    return myFinger;	
  }
}




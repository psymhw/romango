package test;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

class FingerTipListener extends Listener 
{
  private MyFinger[] myFingerArray = new MyFinger[10];
	
  public void onInit(Controller controller)  { System.out.println("Initialized");  }

  public void onDisconnect(Controller controller) { System.out.println("Disconnected");  }

  public void onExit(Controller controller) { System.out.println("Exited"); }

  public void onFrame(Controller controller) 
  {
	clearMyFingerArray();  
    Frame frame = controller.frame();
    FingerList fingers = frame.fingers();
    if (!fingers.empty()) 
    {
      int i=0;
      for (Finger finger : fingers) 
      {
        if (finger.isValid())
        {
          if (finger.isFinger())
          {
        	/*
        	 * For simplicity, I only want the fingertip position information which I limit to 10 possible fingers
        	 * and put into an array of MyFingers 
        	 */
        	Vector pos = finger.tipPosition();
        	myFingerArray[i] = new MyFinger(pos.getX(), pos.getY(), pos.getZ(), ""+finger.id(), ""+frame.id());
        	i++;
          }	
        }
        if (i>10) break;
      }
    } 
  }
    
  private void clearMyFingerArray()
  {
   	for(int j=0; j<10; j++) myFingerArray[j]=null;  
  }
    
  public MyFinger[] getFingerArray()
  {
    return myFingerArray;	
  }
}




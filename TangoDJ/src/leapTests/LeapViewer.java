package leapTests;


//LeapViewer.java
//Andrew Davison, July 2013, ad@fivedots.coe.psu.ac.th

/* Display GUI information on the Leap controller's current frame, 
one hand, and any gestures
(https://www.leapmotion.com/)

My LeapListener class is closely based on the
the SampleListener example in the Leap Motion SDK
(\LeapSDK\samples\Sample.java), but with a GUI interface
*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;



public class LeapViewer extends JFrame
{
private JTextField frameID_TF, timeHands_TF, frameHands_TF, frameFingers_TF, 
                  frameTools_TF, frameGestures_TF,
                  handFingers_TF, handTips_TF, handRadius_TF,
                  handPalm_TF, handPalmIB_TF, 
                  handPitch_TF, handRoll_TF, handYaw_TF, handTouchTF,
                  circleDir_TF, circleID_TF, circleProgess_TF, 
                  circleRadius_TF, circleAngle_TF, circleState_TF,
                  swipeID_TF, swipePosn_TF, swipeDir_TF, 
                  swipeState_TF, swipeSpeed_TF,
                  tapID_TF, tapPosn_TF, tapDir_TF, 
                  tapState_TF, tapFingerID_TF,
                  keyTapID_TF, keyTapPosn_TF, keyTapDir_TF, 
                  keyTapState_TF, keyTapFingerID_TF;

private JCheckBox circle_CB, swipe_CB, tap_CB, keyTap_CB;

private LeapListener listener;
private Controller controller;


public LeapViewer()
{
 super("Leap Controller Viewer");

 buildGUI();

 // Create listener and controller
 listener = new LeapListener(this);
 controller = new Controller();
 controller.addListener(listener);

 addWindowListener( new WindowAdapter() {
   public void windowClosing(WindowEvent e)
   { controller.removeListener(listener);    // Remove the sample listener when done
     System.exit(0);
   }
 });

 pack();
 setResizable(false);
 setLocationRelativeTo(null);  // center the window 
 setVisible(true);

 try {
   Thread.sleep(5000);
 }
 catch(InterruptedException e) {}

 if (!controller.isConnected()) {
   System.out.println("Cannot connect to Leap");
   System.exit(1);
 }
} // end of LeapViewer()



private void buildGUI()
{
	Container c = getContentPane();
	// use BoxLayout: align components vertically
	c.setLayout( new BoxLayout(c, BoxLayout.Y_AXIS) );   

 // ------------------------- Frame Info -------------------

 JPanel framePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 framePanel.setBorder( BorderFactory.createTitledBorder("Frame Info") );

 frameID_TF = new JTextField(6);
 labelText(framePanel, "ID", frameID_TF);

 timeHands_TF = new JTextField(10);
 labelText(framePanel, "Timestamp", timeHands_TF);

 frameHands_TF = new JTextField(2);
 labelText(framePanel, "No. Hands", frameHands_TF);

 frameFingers_TF = new JTextField(2);
 labelText(framePanel, "No. Fingers", frameFingers_TF);

 frameTools_TF = new JTextField(2);
 labelText(framePanel, "No. Tools", frameTools_TF);
       // a tool is longer, thinner, and straighter than a finger

 frameGestures_TF = new JTextField(2);
 labelText(framePanel, "No. Gestures", frameGestures_TF);

 c.add(framePanel);


 // ------------------------- Hand Info -------------------

 JPanel handPanel = new JPanel();
 handPanel.setLayout( new BoxLayout(handPanel, BoxLayout.Y_AXIS) );
 handPanel.setBorder( BorderFactory.createTitledBorder("Hand Info") );

 JPanel hPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 handPanel.add(hPanel);

 handFingers_TF = new JTextField(5);
 labelText(hPanel, "No. Fingers", handFingers_TF);

 handTips_TF = new JTextField(10);
 labelText(hPanel, "Avg. Tip Pos", handTips_TF);

 handPalm_TF = new JTextField(10);
 labelText(hPanel, "Palm Pos", handPalm_TF);
    // center of the palm measured in millimeters from the Leap origin

 handPalmIB_TF = new JTextField(10);
 labelText(hPanel, "Box Palm Pos", handPalmIB_TF);
    // center of the palm measured in millimeters from the interaction box origin


 JPanel hand2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 handPanel.add(hand2Panel);

 // 3 axes for hand rotation
 handPitch_TF = new JTextField(3);
 labelText(hand2Panel, "Pitch (around x-axis)", handPitch_TF);

 handYaw_TF = new JTextField(3);
 labelText(hand2Panel, "Yaw (around y-axis)", handYaw_TF);

 handRoll_TF = new JTextField(3);   
 labelText(hand2Panel, "Roll (around z-axis)", handRoll_TF);

 handRadius_TF = new JTextField(5);
 labelText(hand2Panel, "Radius", handRadius_TF);
     /* The curvature of the sphere decreases as the fingers 
        are curled, defining a sphere with a smaller radius */

 handTouchTF = new JTextField(3);   
 labelText(hand2Panel, "Touching", handTouchTF);

 c.add(handPanel);


 // ------------------------- Gestures Info -------------------

 JPanel gesturesPanel = new JPanel();
	gesturesPanel.setLayout( new BoxLayout(gesturesPanel, BoxLayout.Y_AXIS) );  
 gesturesPanel.setBorder( BorderFactory.createTitledBorder("Gestures Info") );

 c.add(gesturesPanel);


 // --------- circle gesture ------------------

 JPanel circlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 gesturesPanel.add(circlePanel);

 circle_CB = new JCheckBox("Circle");
 circlePanel.add(circle_CB);

 circleID_TF = new JTextField(5);
 labelText(circlePanel, "ID", circleID_TF);

 circleDir_TF = new JTextField(5);
 labelText(circlePanel, "Dir", circleDir_TF); 
        // clockwise or counter-clockwise

 circleProgess_TF = new JTextField(4);
 labelText(circlePanel, "No. Circuits", circleProgess_TF);
    // progress == number of times finger tip has gone round the circle
 circleRadius_TF = new JTextField(4);
 labelText(circlePanel, "Radius", circleRadius_TF);
        // radius of the circle in mm

 circleAngle_TF = new JTextField(3);
 labelText(circlePanel, "Angle", circleAngle_TF);
       // angle swept since last frame (in degrees)

 circleState_TF = new JTextField(9);
 labelText(circlePanel, "State", circleState_TF);
        // start, stop, or update


 // --------- swipe gesture ------------------

 JPanel swipePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 gesturesPanel.add(swipePanel);

 swipe_CB = new JCheckBox("Swipe");
 swipePanel.add(swipe_CB);

 swipeID_TF = new JTextField(5);
 labelText(swipePanel, "ID", swipeID_TF);

 swipePosn_TF = new JTextField(10);
 labelText(swipePanel, "Pos", swipePosn_TF);

 swipeDir_TF = new JTextField(10);
 labelText(swipePanel, "Dir", swipeDir_TF);
        // a unit vector parallel to the swipe motion

 swipeSpeed_TF = new JTextField(4);
 labelText(swipePanel, "Speed", swipeSpeed_TF);   // in mm/sec

 swipeState_TF = new JTextField(9);
 labelText(swipePanel, "State", swipeState_TF);
        // start, stop, or update

 // --------- screen tap gesture ------------------

 JPanel tapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 gesturesPanel.add(tapPanel);

 tap_CB = new JCheckBox(" Screen Tap");
 tapPanel.add(tap_CB);

 tapID_TF = new JTextField(5);
 labelText(tapPanel, "ID", tapID_TF);

 tapPosn_TF = new JTextField(10);
 labelText(tapPanel, "Pos", tapPosn_TF);

 tapDir_TF = new JTextField(10);
 labelText(tapPanel, "Dir", tapDir_TF);
        // a unit vector parallel to the tap motion
        // should be along the z-axis

 tapState_TF = new JTextField(9);
 labelText(tapPanel, "State", tapState_TF);
        // start, stop, or update

 tapFingerID_TF = new JTextField(5);
 labelText(tapPanel, "Finger ID", tapFingerID_TF);

 // --------- key tap gesture ------------------

 JPanel keyTapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 gesturesPanel.add(keyTapPanel);

 keyTap_CB = new JCheckBox("Key Tap");
 keyTapPanel.add(keyTap_CB);

 keyTapID_TF = new JTextField(5);
 labelText(keyTapPanel, "ID", keyTapID_TF);

 keyTapPosn_TF = new JTextField(10);
 labelText(keyTapPanel, "Pos", keyTapPosn_TF);

 keyTapDir_TF = new JTextField(10);
 labelText(keyTapPanel, "Dir", keyTapDir_TF);
        // a unit vector parallel to the tap motion
        // should be along the y-axis

 keyTapState_TF = new JTextField(9);
 labelText(keyTapPanel, "State", keyTapState_TF);
        // start, stop, or update

 keyTapFingerID_TF = new JTextField(5);
 labelText(keyTapPanel, "Finger ID", keyTapFingerID_TF);
}  // end of buildGUI()



private void labelText(JPanel p, String label, JTextField tf)
{ p.add( new JLabel(label + ":"));
 p.add(tf);
}


// ---------------- update the fields ---------------

public void clear()
// clear all the textfields
{
 frameID_TF.setText(""); 
 timeHands_TF.setText(""); frameHands_TF.setText("");
 frameFingers_TF.setText(""); frameTools_TF.setText("");
 frameGestures_TF.setText("");

 handFingers_TF.setText(""); handTips_TF.setText("");
 handRadius_TF.setText(""); 
 handPalm_TF.setText(""); handPalmIB_TF.setText("");
 handPitch_TF.setText(""); handRoll_TF.setText("");
 handYaw_TF.setText(""); handTouchTF.setText("");

 circle_CB.setSelected(false);
 circleDir_TF.setText(""); circleID_TF.setText("");
 circleProgess_TF.setText(""); circleRadius_TF.setText("");
 circleAngle_TF.setText(""); circleState_TF.setText("");

 swipe_CB.setSelected(false);
 swipeID_TF.setText(""); swipePosn_TF.setText("");
 swipeDir_TF.setText(""); swipeState_TF.setText("");
 swipeSpeed_TF.setText("");

 tap_CB.setSelected(false);
 tapID_TF.setText(""); tapPosn_TF.setText("");
 tapDir_TF.setText(""); tapState_TF.setText("");
 tapFingerID_TF.setText("");

 keyTap_CB.setSelected(false);
 keyTapID_TF.setText(""); keyTapPosn_TF.setText("");
 keyTapDir_TF.setText(""); keyTapState_TF.setText("");
 keyTapFingerID_TF.setText("");
}  // end of clear()



public void setFrameInfo(long id, long timestamp, int hCount, int fCount,
                        int tCount, int gCount)
{
 frameID_TF.setText(""+id); timeHands_TF.setText(""+timestamp);
 frameHands_TF.setText(""+hCount);
 frameFingers_TF.setText(""+fCount); frameTools_TF.setText(""+tCount);
 frameGestures_TF.setText(""+gCount);
}  // end of setFrameInfo();


public void setHandInfo(int fCount, Vector tipsPos, float sphRadius,
              Vector palmPos, Vector palmPosIB,
              int pitch, int roll, int yaw, boolean isTouching)
{
 handFingers_TF.setText(""+fCount); handTips_TF.setText(""+tipsPos);
 handRadius_TF.setText(""+sphRadius); 
 handPalm_TF.setText(""+palmPos); handPalmIB_TF.setText(""+palmPosIB);
 handPitch_TF.setText(""+pitch); handRoll_TF.setText(""+roll);
 handYaw_TF.setText(""+yaw);
 String touching = (isTouching) ? "yes" : "";
 handTouchTF.setText(touching);
}  // end of setHandInfo();


public void setCircleInfo(long id, State state, boolean isClockwise, float progress, 
                               float radius, int angle)
{
 circle_CB.setSelected(true);
 circleID_TF.setText(""+id); 
 String dir = (isClockwise) ? "CW" : "CCW";
 circleDir_TF.setText(dir); 
 circleProgess_TF.setText(""+progress); circleRadius_TF.setText(""+radius);
 circleAngle_TF.setText(""+angle); circleState_TF.setText(""+state);
}  // end of setCircleInfo();


public void setSwipeInfo(long id, State state, Vector posn, Vector dir, float speed)
{
 swipe_CB.setSelected(true);
 swipeID_TF.setText(""+id); swipePosn_TF.setText(""+posn);
 swipeDir_TF.setText(""+dir); swipeState_TF.setText(""+state);
 swipeSpeed_TF.setText(""+speed);
}  // end of setSwipeInfo();


public void setTapInfo(long id, State state, Vector posn, Vector dir, int fID)
{
 tap_CB.setSelected(true);
 tapID_TF.setText(""+id); tapPosn_TF.setText(""+posn);
 tapDir_TF.setText(""+dir); tapState_TF.setText(""+state);
 tapFingerID_TF.setText(""+fID);
 System.out.println("Screen tap(" + fID + "): " + dir + " / " + state);
     // print as well since GUI appearance is so brief
}  // end of setTapInfo();


public void setKeyTapInfo(long id, State state, Vector posn, Vector dir, int fID)
{
 keyTap_CB.setSelected(true);
 keyTapID_TF.setText(""+id); keyTapPosn_TF.setText(""+posn);
 keyTapDir_TF.setText(""+dir); keyTapState_TF.setText(""+state);
 keyTapFingerID_TF.setText(""+fID);
 System.out.println("Key tap(" + fID + "): " + dir + " / " + state);
     // print as well since GUI appearance is so brief
}  // end of setKeyTapInfo();


// ----------------------------------------

public static void main(String[] args) 
{ new LeapViewer(); }

} // end of LeapViewer class



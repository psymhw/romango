package test;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SlidingBackground extends ImageView
{
  ImageView thisImageView=this;
  private static final Image GARAGES 
  = new Image(SideScroller.class.getResourceAsStream("/resources/images/garages2.jpg"));
  Timeline anim = new Timeline();
  
  public SlidingBackground(final int width, int duration)
  {
	this.setImage(GARAGES);  
	   anim.setCycleCount(Timeline.INDEFINITE);
	    @SuppressWarnings("rawtypes")
	    
		EventHandler onFinished = new EventHandler() {
	                public void handle(Event t) 
	                {
	                    thisImageView.setTranslateX(thisImageView.getTranslateX() - 1.0);
	                    if(thisImageView.getLayoutX() + thisImageView.getTranslateX() + thisImageView.getBoundsInLocal().getWidth() <= 0) 
	                    {
	                    	thisImageView.setTranslateX(width - thisImageView.getLayoutX());
	                  
	                }}};
	    KeyValue keyValueX = new KeyValue(thisImageView.rotateProperty(),0);
	    KeyFrame keyFrame = new KeyFrame(new Duration(duration), onFinished , keyValueX); //, keyValueY);
	    anim.getKeyFrames().add(keyFrame);
	    anim.play();
  }
}

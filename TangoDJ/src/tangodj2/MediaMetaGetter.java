package tangodj2;

import java.io.File;
import java.util.Iterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaMetaGetter 
{
  Media media=null;
  MapChangeListener metaChangeListener;
  MediaPlayer mp;
  TrackDb trackDb;
  private boolean titleNeeded=true;
  private boolean artistNeeded=true;
  private boolean genreNeeded=true;
  private boolean albumNeeded=true;
  private boolean durationNeeded=true;
  private Timeline timeline;
  private int seconds=0;
  ChangeListener timeChangeListener;
  boolean useMetaChangeListener=false;
	
  public MediaMetaGetter(TrackDb trackDb)
  {
	this.trackDb=trackDb;
	setWhatIsNeeded();
	  
	try 
	{
	  File file = new File(trackDb.path);
	  if (!file.exists()) { System.out.println("File NOT Found: "+trackDb.path); return; }
		
	  media = new Media(file.toURI().toString());
	  if (media==null) { System.out.println("Media is null"); return;	}
		
	} catch (Exception exx) { System.out.println("Trouble w media;"); return; }
	  
	  timeChangeListener = new ChangeListener() 
	  {
	    public void changed(ObservableValue observable, Object oldValue, Object newValue) 
		{
		  Duration duration = media.durationProperty().get(); 
		  setDuration((int)duration.toMillis());
		  durationNeeded=false;
		  complete();
		 // checkForCompletion();
		}
    };
	
	metaChangeListener = new MapChangeListener<String, Object>() 
	{
	  public void onChanged(Change<? extends String, ? extends Object> ch) 
	  {
		if (ch.wasAdded()) 
		{
		  handleMetadata(ch.getKey(), ch.getValueAdded());
		}
	  }
	};
						
	mp = new MediaPlayer(media);
	
	if (titleNeeded) { useMetaChangeListener=true; media.getMetadata().addListener(metaChangeListener); }
	else trackDb.metaComplete=true;
	
	media.durationProperty().addListener(timeChangeListener);
	 
	timeline = new Timeline();
	timeline.setCycleCount(Timeline.INDEFINITE);
	KeyFrame keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
	{
	   public void handle(Event event) 
	   {
		 seconds++;
		 if (seconds>=10) 
		 {
		   timeout();
		 }
	   }});
		      
	  timeline.getKeyFrames().add(keyFrame);
	  timeline.playFromStart();
  }
  
  private void setDuration(int millis)
  {
	  trackDb.duration=millis;
  }
  
  private void timeout()
  {
	  System.out.println("MediaMetaGetter timeout: "+trackDb.path); 
      complete();	  
  }
  
  private void complete()
  {
	 timeline.stop();
	 if (useMetaChangeListener) media.getMetadata().removeListener(metaChangeListener);
	 media.durationProperty().removeListener(timeChangeListener);
	 mp=null;
	 media=null;
	 trackDb.metaComplete=true;
	 timeline=null;
  }
  
	private void handleMetadata(String key, Object value) 
	{
	  if (key.equals("album")) { if (albumNeeded) { albumNeeded=false; trackDb.album=value.toString(); }}
	  if (key.equals("title")) { if (titleNeeded) { titleNeeded=false; trackDb.title=value.toString(); }}
	  if (key.equals("genre")) { if (genreNeeded) { genreNeeded=false; trackDb.genre=value.toString(); }}
	  if (key.equals("artist")) { if (artistNeeded) { artistNeeded=false; trackDb.artist=value.toString(); }}
	
	  if((titleNeeded==false)
	   &&(albumNeeded==false)
	   &&(artistNeeded==false)
	   &&(durationNeeded==false)
	   &&(genreNeeded==false)) complete();  // got everything, kill it.
	}
	
	private void setWhatIsNeeded()
	{
	  if (isSet(trackDb.title))  titleNeeded=false;
	  if (isSet(trackDb.artist)) artistNeeded=false;
	  if (isSet(trackDb.genre))  genreNeeded=false;
	  if (isSet(trackDb.album))  albumNeeded=false;
	}
	
	private boolean isSet(String inStr)
	{
	   if (inStr==null) return false;
	   if (inStr.length()==0) return false;
	   return true;
	}
}

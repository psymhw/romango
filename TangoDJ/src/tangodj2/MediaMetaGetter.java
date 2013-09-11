package tangodj2;

import java.io.File;
import java.util.Iterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
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
  TrackMeta trackMeta;
  private boolean titleNeeded=true;
  private boolean artistNeeded=true;
  private boolean genreNeeded=true;
  private boolean albumNeeded=true;
  private Timeline timeline;
  private int seconds=0;
	
  public MediaMetaGetter(TrackMeta trackMeta)
  {
	this.trackMeta=trackMeta;
	setWhatIsNeeded();
	  
	try 
	{
	  File file = new File(trackMeta.path);
	  if (!file.exists()) { System.out.println("File NOT Found"); return; }
		
	  media = new Media(file.toURI().toString());
	  if (media==null) { System.out.println("Media is null"); return;	}
		
	} catch (Exception exx) { System.out.println("Trouble w media;"); return; }
	  
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
	media.getMetadata().addListener(metaChangeListener);
	
	 
	 timeline = new Timeline();
	  timeline.setCycleCount(Timeline.INDEFINITE);
	  KeyFrame keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
	     {
		       public void handle(Event event) 
		       {
		         seconds++;
		         if (seconds>=3) 
		         {
		          // if not done in 3 sec, kill it	 
		           complete();	 
		         }
		       }});
		      
	  timeline.getKeyFrames().add(keyFrame);
	  timeline.playFromStart();
  }
  private void complete()
  {
	 timeline.stop();
	 media.getMetadata().removeListener(metaChangeListener);
	 mp=null;
	 media=null;
	 trackMeta.metaComplete=true;
	 timeline=null;
	 SharedValues.loadMonitor.set(SharedValues.loadMonitor.get()-1);
  }
  
	private void handleMetadata(String key, Object value) 
	{
	  if (key.equals("album")) { if (albumNeeded) { albumNeeded=false; trackMeta.album=value.toString(); }}
	  if (key.equals("title")) { if (titleNeeded) { titleNeeded=false; trackMeta.title=value.toString(); }}
	  if (key.equals("genre")) { if (genreNeeded) { genreNeeded=false; trackMeta.genre=value.toString(); }}
	  if (key.equals("artist")) { if (artistNeeded) { artistNeeded=false; trackMeta.artist=value.toString(); }}
	
	  if((titleNeeded==false)
	   &&(albumNeeded==false)
	   &&(artistNeeded==false)
	   &&(genreNeeded==false)) complete();  // got everything, kill it.
	}
	
	private void setWhatIsNeeded()
	{
	  if (isSet(trackMeta.title))  titleNeeded=false;
	  if (isSet(trackMeta.artist)) artistNeeded=false;
	  if (isSet(trackMeta.genre))  genreNeeded=false;
	  if (isSet(trackMeta.album))  albumNeeded=false;
	}
	
	private boolean isSet(String inStr)
	{
	   if (inStr==null) return false;
	   if (inStr.length()==0) return false;
	   return true;
	}
}

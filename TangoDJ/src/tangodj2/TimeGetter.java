package tangodj2;

import java.io.File;
import java.util.Iterator;
import java.util.List;

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

/*
 * deprecated see MediaMetaGetter
 */
public class TimeGetter 
{
	Media media=null;
	ChangeListener timeChangeListener;
	MapChangeListener metaChangeListener;
	MediaPlayer mp;
	boolean changed=false;
	String str="NOT SET";
	static int counter=0;
	static List<TrackMeta> trackInfo;
	private String pathHash;
	private boolean titleNeeded=true;
	private boolean artistNeeded=true;
	private boolean genreNeeded=true;
	private boolean albumNeeded=true;
	private boolean timeNeeded=true;
	static int seconds = 0;
	Timeline timeline;
	TrackMeta trackMeta;
		
	public TimeGetter(TrackMeta trackMeta)
	{
	  counter++;
	  this.trackMeta=trackMeta;
	  
	 // setWhatIsNeeded();
	  
	  try 
	  {
		File file = new File(trackMeta.path);
		 if (!file.exists()) 
		 {
			 System.out.println("File NOT Found");
			 return;
		 }
		media = new Media(file.toURI().toString());
		if (media==null)
		{
			 System.out.println("Media is null");
			 return;
		}
	//	System.out.println("Track: "+path);
	  } catch (Exception exx) { System.out.println("Trouble w media;"); return; }
		
	  
	  
	  timeChangeListener = new ChangeListener() 
	  {
	    public void changed(ObservableValue observable, Object oldValue, Object newValue) 
		{
		  Duration duration = media.durationProperty().get(); 
		  
		  setDuration((int)duration.toSeconds());
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
	//  media.getMetadata().addListener(metaChangeListener);
	  media.durationProperty().addListener(timeChangeListener);
	 
	 /*
	  timeline = new Timeline();
	  timeline.setCycleCount(Timeline.INDEFINITE);
	  KeyFrame keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
	     {
		       public void handle(Event event) 
		       {
		         seconds++;
		         if (seconds>=3) 
		         {
		           checkForCompletion();	 
		           
		         }
		       }});
		      
	  timeline.getKeyFrames().add(keyFrame);
	  timeline.playFromStart();
	  
	  */
	}
	
	private void checkForCompletion()
	{
	//  if ((timeNeeded==false)
	//	&&(titleNeeded==false)
	//	&&(albumNeeded==false)
	//	&&(artistNeeded==false)
	//	&&(genreNeeded==false))
	//if ((timeNeeded==false)&&(titleNeeded==false))
	//  {
		if (media!=null)
		{
	      media.durationProperty().removeListener(timeChangeListener);
	      media.getMetadata().removeListener(metaChangeListener);
	      mp=null;
	      media=null;
		}
	//  }
	}
	
	private void complete()
	{
	      media.durationProperty().removeListener(timeChangeListener);
	     // media.getMetadata().removeListener(metaChangeListener);
	      mp=null;
	      media=null;
	}
	
	private void setDuration(int seconds)
	{
		trackMeta.duration=seconds;
		/*
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   trackMeta.duration=seconds; 
		   timeNeeded=false;
		   complete();
		   System.out.println("duration set");
		   return;
		  // System.out.println(" setting: "+trackMeta.title+" to "+trackMeta.duration);
		  // trackInfo.set(counter, trackMeta);
		 }
	   }
	   */
	}
	
	private void setAlbum(String album)
	{
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   trackMeta.album=album; 
		   System.out.println("album set");
		   albumNeeded=false;
		   return;
		 }
	   }
	}
	
	private void setTitle(String title)
	{
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   trackMeta.title=title; 
		   System.out.println("title set");
		   titleNeeded=false;
		   return;
		 }
	   }
	}
	
	private void setGenre(String genre)
	{
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   trackMeta.genre=genre; 
		   System.out.println("genre set");
		   genreNeeded=false;
		   return;
		 }
	   }
	}
	
	private void setArtist(String artist)
	{
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   trackMeta.artist=artist; 
		   System.out.println("artist set");
		   artistNeeded=false;
		   return;
		 }
	   }
	}
	
	
	private void setWhatIsNeeded()
	{
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   int counter=0;
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   if (isSet(trackMeta.title))  titleNeeded=false;
		   if (isSet(trackMeta.artist)) artistNeeded=false;
		   if (isSet(trackMeta.genre))  genreNeeded=false;
		   if (isSet(trackMeta.album))  albumNeeded=false;
		   
		   return;
		 }
		 counter++;
	   }
	}
	
	private boolean isSet(String inStr)
	{
	   if (inStr==null) return false;
	   if (inStr.length()==0) return false;
	   return true;
	}
	
	static String formatIntoHHMMSS(double secsIn)
	{

	int hours = (int)secsIn / 3600,
	remainder = (int)secsIn % 3600,
	minutes = remainder / 60,
	seconds = remainder % 60;

	return ( (hours < 10 ? "0" : "") + hours
	+ ":" + (minutes < 10 ? "0" : "") + minutes
	+ ":" + (seconds< 10 ? "0" : "") + seconds );

	}
	
	private void handleMetadata(String key, Object value) 
	{
	  if (key.equals("album")) { if (albumNeeded) setAlbum(value.toString()); }
	  if (key.equals("title")) { if (titleNeeded) setTitle(value.toString()); }
	  if (key.equals("genre")) { if (genreNeeded) setGenre(value.toString()); }
	  if (key.equals("artist")) { if (artistNeeded) setArtist(value.toString()); }
	    
	  }
}

package tangodj2;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class TimeGetter 
{
	Media media=null;
	ChangeListener cl2;
	MediaPlayer mp;
	boolean changed=false;
	String str="NOT SET";
	static int counter=0;
	static List<TrackMeta> trackInfo;
	private String pathHash;
		
	public TimeGetter(String path, List<TrackMeta> trackInfo, String pathHash)
	{
	  counter++;
	  this.trackInfo=trackInfo;
	  this.pathHash=pathHash;
	  try 
	  {
		File file = new File(path);
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
		
	  
	  
	  cl2 = new ChangeListener() 
	  {
	    public void changed(ObservableValue observable, Object oldValue, Object newValue) 
		{
		  Duration duration = media.durationProperty().get(); 
		  setDuration((int)duration.toSeconds());
		  str=formatIntoHHMMSS(duration.toSeconds());
		 // System.out.println("Duration: "+str);
		  SharedValues.loadMonitor.set(SharedValues.loadMonitor.get()-1);
		  media.durationProperty().removeListener(cl2);
		  mp=null;
		  changed=true;
		}
      };
			
	  mp = new MediaPlayer(media);
	  media.durationProperty().addListener(cl2);
		
	}
	
	private void setDuration(int seconds)
	{
	   TrackMeta trackMeta;
	   Iterator<TrackMeta> it = trackInfo.iterator();
	   int counter=0;
	   while(it.hasNext())
	   {
		 trackMeta=it.next();
		 if (trackMeta.pathHash.equals(pathHash))
		 {
		   trackMeta.duration=seconds; 
		   return;
		  // System.out.println(" setting: "+trackMeta.title+" to "+trackMeta.duration);
		  // trackInfo.set(counter, trackMeta);
		 }
		 counter++;
	   }
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
}

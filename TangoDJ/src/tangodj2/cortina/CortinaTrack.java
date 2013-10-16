package tangodj2.cortina;

import tangodj2.TrackMeta;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;

public class CortinaTrack
{
  private final SimpleStringProperty title;
  private final SimpleStringProperty start;
  private final SimpleStringProperty stop;
  private final SimpleStringProperty length;
  private int id;
  private int fadein;
  private int fadeout;
  private int delay;
  private int original_duration;
  private int startValue;
  private int stopValue;
  private String pathHash;
  private TrackMeta trackMeta = null;
  
  public CortinaTrack(int id, int start, int stop, int fadein, int fadeout, int delay, int original_duration, String title, String pathHash)
  {
    this.title = new SimpleStringProperty(title);
    this.start = new SimpleStringProperty(formatTime(new Duration(start)));
    this.stop = new SimpleStringProperty(formatTime(new Duration(stop)));
    this.length = new SimpleStringProperty(formatTime(new Duration(stop-start)));
    this.id=id;
    this.fadein=fadein;
    this.fadeout=fadeout;
    this.delay=delay;
    this.pathHash=pathHash;
    this.startValue=start;
    this.stopValue=stop;
    this.original_duration=original_duration;
  }
  
  private static String formatTime(Duration duration) 
  {
    if (duration.greaterThan(Duration.ZERO)) 
    {
      int intDuration = (int) Math.floor(duration.toSeconds());
      int durationHours = intDuration / (60 * 60);
      if (durationHours > 0) { intDuration -= durationHours * 60 * 60; }
      int durationMinutes = intDuration / 60;
      int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
      if (durationHours > 0) 
      {
        return String.format("%d:%02d:%02d", 
                      durationHours, durationMinutes, durationSeconds);
       } 
       else 
       {
         return String.format("%02d:%02d",
                     durationMinutes,
                      durationSeconds);
       }
     }
    else return "00:00";
    
     
   }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public int getFadein()
  {
    return fadein;
  }

  public void setFadein(int fadein)
  {
    this.fadein = fadein;
  }

  public int getFadeout()
  {
    return fadeout;
  }

  public void setFadeout(int fadeout)
  {
    this.fadeout = fadeout;
  }

  public int getDelay()
  {
    return delay;
  }

  public void setDelay(int delay)
  {
    this.delay = delay;
  }

  public String getTitle()
  {
    return title.get();
  }

  public void setTitle(String title)
  {
    this.title.set(title);
  }

  public String getStart()
  {
    return start.get();
  }
  
  public void setStart(String start)
  {
    this.start.set(start);
  }

  public String getStop()
  {
    return stop.get();
  }

  public String getLength()
  {
    return length.get();
  }

  public String getPathHash()
  {
    return pathHash;
  }

  public void setPathHash(String pathHash)
  {
    this.pathHash = pathHash;
  }

  public int getStartValue()
  {
    return startValue;
  }

  public int getStopValue()
  {
    return stopValue;
  }

  public int getOriginal_duration()
  {
    return original_duration;
  }
  
  public TrackMeta getTrackMeta()
  {
    return trackMeta;
  }

  public void setTrackMeta(TrackMeta trackMeta)
  {
    this.trackMeta = trackMeta;
  }
}

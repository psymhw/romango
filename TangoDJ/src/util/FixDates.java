package util;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj2.Db;
import tangodj2.TrackDb;
import tangodj2.tango.TangoTrack;

public class FixDates
{

  /**
   * @param args
   */
  
  public FixDates()
  {
    try
    {
      Db.databaseConnect(false);
    } catch (Exception e) { e.printStackTrace();  }
    
    ArrayList<TangoTrack> tangoTracks = Db.loadTangoTracks(null);
    int size=tangoTracks.size();
    System.out.println("Number of tracks: "+tangoTracks.size());
    
    Iterator<TangoTrack> it = tangoTracks.iterator();
    
    TangoTrack tt;
    String tt_year;
    String farng_year="";
    int count=0;
    
    for(int i=0; i<size; i++)
    {
      //tt=it.next();
      tt=tangoTracks.get(i);
     // System.out.println(count+ ") name: "+tangoTracks.get(i).getTitle());
      
      tt_year=tt.getTrack_year();
      
      if ((tt_year.length()>0)&&(tt_year.length()<4))
      {    
        File file = new File(tt.getPath().get());
        if (file.exists())
        {  
         // System.out.println(count+ ") file: "+file.getName());
          farng_year=getFarngYear(file).replace("ÿþ","");
          System.out.println(count+ ") Farng year: "+farng_year+" Db year: "+tt_year);
        }
        else System.out.println(count+ ") no file ");
      }
      else System.out.println(count+ ") ok ");
      count++;
    }
    
    try
    {
      Db.databaseDisconnect();
      System.out.println("Disconnect");
    } catch (SQLException e) { e.printStackTrace(); }
    
  }
  
  public static void main(String[] args)
  {
    new FixDates();

  }
  
  
  private String getFarngYear(File file) 
  {
    MP3File mp3 = null;
    AbstractID3v2 tag;
    //File file = path.toFile();
    String track_year="";
    
    try { mp3 = new MP3File(file); } catch (Exception e) 
    { 
      System.out.println("Could not create MP3File class: "+file.getName());
      return null;  
    }
    
    try { tag = mp3.getID3v2Tag();  } catch (Exception e2) 
    { 
      System.out.println("Could not get ID3v2 tag: "+file.getName());
      return null;  
    }
    
    if (tag==null)
    {
      System.out.println("tag is null: "+file.getName());
      return null;  
    }
      
    track_year=tag.getYearReleased();;
    return track_year;
  }

}

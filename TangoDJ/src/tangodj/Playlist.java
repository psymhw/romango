package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

public class Playlist 
{
	private ArrayList<Tanda> tandas = new ArrayList<Tanda>();
	private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
	public int playingTrack=-1;
	public int selectedTrack=0;

	public void addTanda(Tanda tanda)
	{
	  tandas.add(tanda);
	  Iterator<TrackRow> it = tanda.getTrackRows().iterator();
	  while(it.hasNext())
	  {
		trackRows.add(it.next());
	  }
	}
	
	public ArrayList<Tanda> getTandas()
	{
	  return tandas;	
	}
	
	public ArrayList<TrackRow> getTrackRows()
	{
	   return trackRows;
	}
	
	public void setPlayingIndicator()
	{
	  trackRows.get(playingTrack).setNowPlayingIndicatorBall();
	}
	
	public void setNotPlayingIndicator()
	{
	  trackRows.get(playingTrack).setNotPlayingIndicatorBall();
	}
	
	public void setSelectedIndicator()
	{
		trackRows.get(selectedTrack).setSelectedIndicatorBall();
	}
	
	public void setNotSelectedIndicator()
	{
		trackRows.get(selectedTrack).setNotSelectedIndicatorBall();
	}
	public void resetSelectedIndicator()
	{
		trackRows.get(0).setNotSelectedIndicatorBall();
	}
	
	
	public String getPlayingTrackPath()
	{
		return trackRows.get(playingTrack).path;
	}
	
	public TrackRow getTrack(int index)
	{
	  return trackRows.get(index); 
	}
	
	public TrackRow getPlayingTrack()
	{
	  return trackRows.get(playingTrack); 
	}
	
	public boolean isDone()
	{
		if ((playingTrack+1)>=trackRows.size()) return true;
		return false;
	}
	
	public void setPlayingTrackToSelected()
	{
		playingTrack=selectedTrack;
	}
	
	public boolean playingIsSelected()
	{
		if (playingTrack==selectedTrack) return true;
		return false;
	}
	
	public void incrementSelected()
    {
      setNotSelectedIndicator();
      selectedTrack++;
      setSelectedIndicator();
    }
	
	 public void incrementPlaying()
	 {
	   setNotPlayingIndicator();
	      
	   if (playingIsSelected()) 
	   {
	     incrementSelected();
	     playingTrack++;
	   }
	   else 
	   {
	     playingTrack=selectedTrack;
	   }
     }
	    
	 public String getDragText(int index)
	 {
		TrackRow trackRow = trackRows.get(index);
		int tandaNumber = trackRow.getTandaNumber();
		Tanda tanda = tandas.get(tandaNumber);
		return tanda.artist.lastName+" - "+tanda.group;
	 }
 }

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
	
	public void setSelectedIndicator(int index)
	{
		trackRows.get(index).setSelectedIndicatorBall();
	}
	
	public void setNotSelectedIndicator(int index)
	{
		trackRows.get(index).setNotSelectedIndicatorBall();
	}
	
	public String getTrackPath(int index)
	{
		return trackRows.get(index).path;
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
	
	
 }

package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

public class Playlist 
{
	private ArrayList<Tanda> tandas = new ArrayList<Tanda>();
	private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
	public int playingTrack=-1;
	public int selectedTrack=0;
	private int tandaTrackNumber=0;
	boolean newTanda = true;
	
	
	public void addTrackRow(TrackRow trackRow)
	{
	   
	   if (newTanda)
	   {
		 tandas.add(new Tanda(trackRow.artistName, trackRow.groupingName));
		 newTanda=false;
	   }
	   Tanda tanda = tandas.get(tandas.size()-1);
	   trackRow.setTandaInfo(tandas.size()-1, tandaTrackNumber);
	   tandaTrackNumber++;
	   tanda.addTrackRow(trackRow);
	  // trackRows.add(trackRow);
	   
	   if ("cortina".equalsIgnoreCase(trackRow.groupingName))
	   {
		 newTanda=true;
		 tandaTrackNumber=0;
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
		System.out.println("Track title: "+trackRow.title+" "+trackRow.getTandaNumber());
		Tanda tanda = tandas.get(tandaNumber);
		return tanda.artist.lastName+" - "+tanda.group;
	 }
	 
	 public void setTrackRows()
	 {
		 trackRows.clear();
		 Tanda tanda;
		 ArrayList<TrackRow> trs;
		 TrackRow tr;
		 
		 int tandaNumber=0;
		 int tandaTrackNumber=0;
		  
		  Iterator<Tanda> it = tandas.iterator();
		  while(it.hasNext())
		  {
			tanda = it.next();
			trs=tanda.getTrackRows();
			Iterator<TrackRow> itx = trs.iterator();
			while(itx.hasNext())
			{
			  tr = itx.next();
			  tr.setTandaInfo(tandaNumber, tandaTrackNumber);
			  trackRows.add(tr);
			  tandaTrackNumber++;
			}
			
			tandaNumber++;
		  }  
	 }
	 
	 public void reorder(int startIndex, int finishIndex)
	 {
		 System.out.println("start: "+startIndex+", finish: "+finishIndex); 
	 }
 }

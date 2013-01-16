package tangodj;

import java.util.ArrayList;

public class Tanda 
{
  Artist artist;
  String group;
  private int tracksInTanda=0;
  private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
  
  public Tanda(String artistStr, String group)
  {
	this.artist = Artist.getArtist(artistStr);
	this.group=group; 
  }
  
  public void addTrackRow(TrackRow t)
  {
	if (t.groupingName.equalsIgnoreCase("vals")||
			t.groupingName.equalsIgnoreCase("tango")||
			t.groupingName.equalsIgnoreCase("milonga")||
			t.groupingName.equalsIgnoreCase("alternative")) tracksInTanda++;
	trackRows.add(t);  
  }

  public ArrayList<TrackRow> getTrackRows() 
  {
	return trackRows;
  }
  
  public int tracksInTanda()
  {
	return tracksInTanda;
  }
}

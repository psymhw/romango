package tangodj;

import java.util.ArrayList;

public class Tanda 
{
  Artist artist;
  String group;
  private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
  
  public Tanda(String artistStr, String group)
  {
	this.artist = Artist.getArtist(artistStr);
	this.group=group; 
  }
  
  public void addTrackRow(TrackRow t)
  {
	trackRows.add(t);  
  }

public ArrayList<TrackRow> getTrackRows() {
	return trackRows;
}
}

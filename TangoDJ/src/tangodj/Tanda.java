package tangodj;

import java.util.ArrayList;

public class Tanda 
{
  Artist artist;
  String group;
  private int tracksInTanda=0;
  private double position;
  private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
  
  public Tanda(String artistStr, String group)
  {
	this.artist = Artist.getArtist(artistStr);
	this.group=group; 
  }
  
  public void addTrackRow(TrackRow t)
  {
	if (t.getGrouping().equalsIgnoreCase("vals")||
			t.getGrouping().equalsIgnoreCase("tango")||
			t.getGrouping().equalsIgnoreCase("milonga")||
			t.getGrouping().equalsIgnoreCase("alternative")) tracksInTanda++;
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

public double getPosition() {
	return position;
}

public void setPosition(double position) {
	this.position = position;
}
}

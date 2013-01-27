package tangodj;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tanda 
{
  Artist artist;
  String group;
  private int tracksInTanda=0;
  private double position;
  private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
  private double height=0;
  Rectangle tandaHighlightBox;
  
  public Tanda(String artistStr, String group)
  {
	this.artist = Artist.getArtist(artistStr);
	this.group=group; 
	tandaHighlightBox = new Rectangle(525, height);
    tandaHighlightBox.setX(50);
    tandaHighlightBox.setY(0);
    tandaHighlightBox.setFill(Color.RED);
    tandaHighlightBox.setOpacity(.3);
    tandaHighlightBox.setVisible(false);
  }
  
  public void addTrackRow(TrackRow t)
  {
	if   (t.getGrouping()!=null)
	if (t.getGrouping().equalsIgnoreCase("vals")||
			t.getGrouping().equalsIgnoreCase("tango")||
			t.getGrouping().equalsIgnoreCase("milonga")||
			t.getGrouping().equalsIgnoreCase("alternative")) tracksInTanda++;
	trackRows.add(t);  
	height+=22.188;
	tandaHighlightBox.setHeight(height);
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

public double getHeight() {
	return height;
}
}

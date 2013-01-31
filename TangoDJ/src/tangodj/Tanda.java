package tangodj;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

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
	tandaHighlightBox = new Rectangle(550, height);
    tandaHighlightBox.setX(15);
    tandaHighlightBox.setY(0);
    //tandaHighlightBox.setFill(Color.HOTPINK);
    tandaHighlightBox.setFill(linearGradient_REFLECT);
    tandaHighlightBox.setOpacity(.3);
    tandaHighlightBox.setStroke(Color.BLACK);
    tandaHighlightBox.setStrokeWidth(3);
    tandaHighlightBox.setStrokeType(StrokeType.INSIDE);
    tandaHighlightBox.setVisible(false);
  }
  
  LinearGradient linearGradient_REFLECT
  = LinearGradientBuilder.create()
  .startX(275)
  .startY(50)
  .endX(250)
  .endY(100)
  .proportional(false)
  .cycleMethod(CycleMethod.REFLECT)
  .stops(
      new Stop(0.1f, Color.rgb(255, 0, 255, 0.9)),
      new Stop(1.0f, Color.rgb(0, 255, 0, 1.0)))
  .build();
  
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

public void setPosition(double position) 
{
	this.position = position;
	this.tandaHighlightBox.setY(position);
}

public double getHeight() {
	return height;
}

public Rectangle getTandaHighlightBox() {
	return tandaHighlightBox;
}

public void highlight(boolean choice)
{
	tandaHighlightBox.setVisible(choice);
}

public int getTracksInTanda() {
	return tracksInTanda;
}
}

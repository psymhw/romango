package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TandaDragAnimation extends Group
{
    Text dragText = new Text("HELLO");
    public int dragStartIndex=0;
    public int dragFinishIndex=0;
    public long startDragTime=0;
    public GridPane gp = new GridPane();
    private Tanda tanda;
 
  	public TandaDragAnimation()
  	{
      dragText.setFill(Color.RED);
	  dragText.setOpacity(.75);
	  dragText.setFont(new Font("Cambria", 18));
	  this.getChildren().add(dragText);
	  
	  
  	}

  	public void setTanda(Tanda tanda)
  	{
  		gp = new GridPane();
  		gp.setPadding(new Insets(10, 10, 10, 10));
  	  gp.setVgap(0);
  	  gp.setHgap(0);
  	  
  	  this.tanda=tanda;
  	  ArrayList<TrackRow> trs;
	  TrackRow tr=null;
	  TrackRow trt;
	  int row=0;
  	trs=tanda.getTrackRows();
	Iterator<TrackRow> itx = trs.iterator();
	while(itx.hasNext())
	{
	  trt = itx.next();
	  tr = new TrackRow(trt.trackLabel.getText(), trt.artistName, trt.path, trt.grouping.getText(), 20,  trt.trackNumber);
	  gp.add(tr.indicator, 0, row);
	  gp.add(tr.trackLabel, 1, row);
	  gp.add(tr.grouping, 2, row);
	  gp.add(tr.artist, 3, row);
	  gp.add(tr.name, 4, row);
	  row++;
	}
	
	this.getChildren().add(gp);
  	}
}

package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TandaDragAnimation extends Group
{
    //public int dragStartIndex=0;
    //public int dragFinishIndex=0;
    public long startDragTime=0;
    public GridPane gp = new GridPane();
    private Tanda tanda;
    double startPosition=0;
    //Rectangle destMarker;
    double[] tandaPositions;
    Image pointHand;
    private ImageView destMarker = new ImageView();
    private int destTandaIndex=0;
    private int startTandaIndex=0;
    ScrollPane scrollPane;
    double scrollWindow;
 

  	public TandaDragAnimation(Tanda tanda, int startTandaIndex, double startPosition, double[] tandaPositions, ScrollPane scrollPane, double scrollWindow)
  	{
  	  this.tanda=tanda;
  	  this.scrollPane=scrollPane;
  	  this.scrollWindow=scrollWindow;
  	  this.startPosition=startPosition;
  	  this.destTandaIndex=startTandaIndex;
  	  this.tandaPositions=tandaPositions;
  	  this.startTandaIndex=startTandaIndex;
  	  pointHand = new Image(TangoDJ.class.getResourceAsStream("/resources/images/point_hand.png"));
  	  destMarker.setImage(pointHand);
  	  
  	  gp = new GridPane();
	  gp.setPadding(new Insets(10, 10, 10, 10));
	  gp.setVgap(0);
	  gp.setHgap(0);
	  gp.setOpacity(.7);
  	  ArrayList<TrackRow> trs;
	  TrackRow tr=null;
	  TrackRow trt;
	  int row=0;
  	  trs=tanda.getTrackRows();
	  Iterator<TrackRow> itx = trs.iterator();
	  while(itx.hasNext())
	  {
	    trt = itx.next();
	    tr = trt.clone(trt);
	    gp.add(tr.getIndicator(), 0, row);
	    gp.add(tr.getTrackNumberLabel(), 1, row);
	    gp.add(tr.getGroupingLabel(), 2, row);
	    gp.add(tr.getArtistLabel(), 3, row);
	    gp.add(tr.getTrackTitleLabel(), 4, row);
	    row++;
	}
	
	this.getChildren().clear();
	gp.setLayoutX(0);
	gp.setLayoutY(tanda.getPosition());
	
	double tandaHeight=row*22.188;
	double tandaWidth=525;
	Rectangle voidBox = new Rectangle(tandaWidth, tandaHeight);
	voidBox.setX(50);
	voidBox.setY(tanda.getPosition()+8);
	voidBox.setFill(Color.LIGHTGRAY);
	voidBox.setOpacity(.8);
	
	//destMarker = new Rectangle(75, 8);
	//destMarker.setFill(Color.YELLOW);
	//destMarker.setStrokeWidth(1);
	//destMarker.setStroke(Color.RED);
	destMarker.setX(0);
	destMarker.setY(tanda.getPosition());
	
	this.getChildren().add(voidBox);
	this.getChildren().add(destMarker);
	this.getChildren().add(gp);
	/*
	  for(int i=0; i<tandaPositions.length; i++)
	  {
		System.out.println(i+") tandapos: "+tandaPositions[i]);
	  }  
	*/
	//System.out.println("panable: "+scrollPane.isPannable());
  	}
  
  	public void move(double newPosition)
  	{
  	  double viewport =scrollPane.getVvalue()*scrollWindow;
  	  double gridPosition=tanda.getPosition()-(startPosition-newPosition)-viewport;
   	  double destPos=findDestPosition(gridPosition);
   	  //System.out.println("scroll value: "+scrollPane.getVvalue()+" dest pos: "+destPos+" view: "+viewport);
  
   	  if (destPos-viewport<10)
  	 {
  	   double vpos = scrollPane.getVvalue();
  	   if (vpos>0)
  	   {
  		 vpos=vpos-.1;
  		 if (vpos<0) vpos=0;
  		 scrollPane.setVvalue(vpos);
  	   }
  	 }
  	 
   	 if (destPos!=-1) destMarker.setY(destPos);
  	 gp.setLayoutY(gridPosition);	
  	}

	private double findDestPosition(double gridPosition) 
	{
		double returnPos=0;
	  for(int i=0; i<tandaPositions.length; i++)
	  {
		//System.out.println("gridPosition: "+i+") "+gridPosition+" tandapos: "+tandaPositions[i]);
	    if (gridPosition>tandaPositions[i]-40) 
		{  
		  destTandaIndex=i;	
		  returnPos=tandaPositions[i];
		}
	  }  
      return returnPos;
	}

	public int getDestTandaIndex() {
		return destTandaIndex;
	}

	public int getStartTandaIndex() {
		return startTandaIndex;
	}

	public void setStartTandaIndex(int startTandaIndex) {
		this.startTandaIndex = startTandaIndex;
	}
}

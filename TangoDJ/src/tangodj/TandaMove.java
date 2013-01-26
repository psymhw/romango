package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TandaMove extends Group
{
    double[] tandaPositions;
    Image pointHand;
    private ImageView destMarker = new ImageView();
    private int destTandaIndex=0;
    private int startTandaIndex=0;
    ScrollPane scrollPane;
    private int fontSize=18;
 

  	public TandaMove( int startTandaIndex,  double[] tandaPositions, ScrollPane scrollPane)
  	{
  	  this.scrollPane=scrollPane;
  	  this.destTandaIndex=startTandaIndex;
  	  this.tandaPositions=tandaPositions;
  	  this.startTandaIndex=startTandaIndex;
  	  pointHand = new Image(TangoDJ.class.getResourceAsStream("/resources/images/point_hand.png"));
  	  destMarker.setImage(pointHand);
  	  
  	  
	
	this.getChildren().clear();
	//gridPane.setLayoutX(0);
	//gridPane.setLayoutY(tanda.getPosition());
	
	
	destMarker.setX(0);
	destMarker.setY(tandaPositions[startTandaIndex]);
	
	this.getChildren().add(destMarker);
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
  	  
   	  destMarker.setY(newPosition);
   	
  	 //gridPane.setLayoutY(gridPosition);	
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
	
	private Label textLabel(String text, int width, String cssBkgColor)
	{
	   Label label = new Label(" "+text);
	   label.setPrefWidth(width);
	   label.getStyleClass().add(cssBkgColor);
	   label.setFont(new Font("Cambria", fontSize));
	  // label.setStyle("tangoBkg");
	   return label;
	}
}

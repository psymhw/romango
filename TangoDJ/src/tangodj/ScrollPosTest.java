package tangodj;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScrollPosTest extends Group
{
  private ImageView destMarker = new ImageView();
  Image pointHand;
	
  public ScrollPosTest(double[] tandaPositions)
  {
	 pointHand = new Image(TangoDJ.class.getResourceAsStream("/resources/images/point_hand.png"));
	 for(int i=0; i<tandaPositions.length; i++)
	 {
		 destMarker= new ImageView(pointHand);
		 destMarker.setX(0);
		 destMarker.setY(tandaPositions[i]);
		this.getChildren().add(destMarker);
	 }
	  
  }
}

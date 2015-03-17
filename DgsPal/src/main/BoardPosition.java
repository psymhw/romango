package main;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import javafx.scene.text.Font;

public class BoardPosition extends Group
{
	private static boolean initiallized=false;
	float x=0;
	float y=0;
	private String sgfPosition;
	
	
	   
	private static Image grid_ul_corner_image;
	private static Image grid_ur_corner_image;
	private static Image grid_ll_corner_image;
	private static Image grid_lr_corner_image;
	   
	private static Image grid_top_image;
	private static Image grid_left_image;
	private static Image grid_right_image;
	private static Image grid_bottom_image;
	private static Image grid_hoshi_image;
	 
	private static Image grid_cross_image;
	
	public final static int TOP =       1;
	public final static int LEFT =      2;
	public final static int RIGHT =     3;
	public final static int BOTTOM =    4;
	
	public final static int UL_CORNER = 5;
	public final static int UR_CORNER = 6;
	public final static int LL_CORNER = 7;
	public final static int LR_CORNER = 8;
	
	public final static int FILL =      9;
	public final static int HOSHI =    10;
	
	ImageView background=null;
	ImageView grid=null;
	private Label sgfLabel = new Label();
	private Label positionLabel = new Label();
	
	Circle circle = new Circle();
	private Ellipse hoshi;
	private int position=0;
	int[] hoshiPositions = new int[]{60, 66, 72, 174, 180, 186, 288, 294, 300};
	
	public BoardPosition(String sgfPosition, int type, float x, float y)
	{
	  this.x=x;
	  this.y=y;
	  this.sgfPosition=sgfPosition;
	  
	  calculatePosition();
	  
	  if (!initiallized) 
	  { 
		importImages(); 
		initiallized=true; 
		//System.out.println("intiallizing grid");
	  }
	  
      sgfLabel.setFont(Font.font("Serif", 12));
      sgfLabel.setText(sgfPosition);
      sgfLabel.setLayoutX(x+5);
      sgfLabel.setLayoutY(y+5);
      
      positionLabel.setFont(Font.font("Serif", 12));
      positionLabel.setText(""+position);
      positionLabel.setLayoutX(x+5);
      positionLabel.setLayoutY(y+5);
      
	    
	    circle.setCenterX(x+17);
	    circle.setCenterY(y+17);
	    circle.setRadius(10.0f);
	    circle.setFill(Color.BLUEVIOLET);
	    circle.setVisible(false);
	    
	    hoshi = EllipseBuilder.create()
	             .centerX(x+18)
	             .centerY(y+17)
	             .radiusX(2)
	             .radiusY(2)
	             .strokeWidth(1)
	             .stroke(Color.BLACK)
	             .fill(Color.BLACK)
	             .build();
	  
	  switch (type)
	  {
	    case TOP:
	        grid = getImageView(grid_top_image,x,y);
	        break;
	    case LEFT:
	    	grid = getImageView(grid_left_image,x,y);
	    	break;
	    case RIGHT:
	    	grid = getImageView(grid_right_image,x,y);
	    	break;
	    case BOTTOM:
	    	grid = getImageView(grid_bottom_image,x,y);
	    	break;
	    case UL_CORNER:
	    	grid = getImageView(grid_ul_corner_image,x,y);
	    	break;
	    case UR_CORNER:
	    	grid = getImageView(grid_ur_corner_image,x,y);
	    	break;
	    case LL_CORNER:
	    	grid = getImageView(grid_ll_corner_image,x,y);
	    	break;
	    case LR_CORNER:
	    	grid = getImageView(grid_lr_corner_image,x,y);
	    	break;
	    case FILL:
	    	grid = getImageView(grid_cross_image,x,y);
	    	break;
	    case HOSHI:
	    	grid = getImageView(grid_hoshi_image,x,y);
	    	break;
	  }
	  
	  this.getChildren().add(grid);
	  if (isHoshi()) this.getChildren().add(hoshi);
	//  this.getChildren().add(sgfLabel);
	  this.getChildren().add(circle);
	 // this.getChildren().add(positionLabel);
	
	}
	
	private boolean isHoshi() 
	{
		for(int i=0; i<9; i++)
		{
		  if (hoshiPositions[i]==position) return true;
		}
		return false;
	}

	private void calculatePosition() 
	{
	  char xChar = sgfPosition.charAt(0);
	  char yChar = sgfPosition.charAt(1);
	  
	  int x = xChar-97;
	  int y = yChar-97;
	  
	  position=(x*19)+y;
	  
	}

	public void toggleCircle()
	{
	  if (circle.isVisible()) circle.setVisible(false);
	  else circle.setVisible(true);
	}
	
	private void importImages()
	{
	  String src="/resources";  
	  		 
	  grid_ul_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/ul.gif"));
	  grid_ur_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/ur.gif"));
	  grid_ll_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/dl.gif"));
	  grid_lr_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/dr.gif"));
		 
	  grid_top_image = new Image(GoClient.class.getResourceAsStream(src+"/images/u.gif"));
	  grid_left_image = new Image(GoClient.class.getResourceAsStream(src+"/images/el.gif"));
	  grid_right_image = new Image(GoClient.class.getResourceAsStream(src+"/images/er.gif"));
	  grid_bottom_image = new Image(GoClient.class.getResourceAsStream(src+"/images/d.gif"));
		 
	  grid_cross_image = new Image(GoClient.class.getResourceAsStream(src+"/images/e.gif"));
	  grid_hoshi_image = new Image(GoClient.class.getResourceAsStream(src+"/images/h.gif"));
		 
	}
	
	private ImageView getImageView(Image image, float x, float y)
	{
	  ImageView imageView = new ImageView(image);
	  imageView.setX(x);
	  imageView.setY(y);
	  return imageView;
	}

	public String getSgfPosition() {
		return sgfPosition;
	}

	public void setSgfPosition(String sgfPosition) {
		this.sgfPosition = sgfPosition;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Ellipse getHoshi() {
		return hoshi;
	}

}

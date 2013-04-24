package test;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public  class BoxSprite extends Sprite
{
	Rectangle r = new Rectangle(100,100);
	int counter=0;

	public BoxSprite()
	{
	  r.setFill(Color.RED);
	  r.setTranslateX(700);
	  r.setTranslateY(0);
	  vX=-5;
	}
	public void update() 
	{
	  r.setTranslateX(r.getTranslateX()-5);
	}
  
	public Rectangle getImage()
	{
		return r;
	}
}

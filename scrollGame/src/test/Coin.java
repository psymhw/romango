package test;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Coin extends Sprite
{ 

  Circle circle = new Circle(10);
  int rate=2;
  int initialX=0;
 
  
  public Coin(int x, int y)
  {
	circle.setTranslateX(x);
    circle.setTranslateY(y);
    initialX=x;
    circle.setVisible(true);
    circle.setFill(Color.GOLD);
    type="coin";
  }
  
  public void update() 
  {
	int x = (int)circle.getTranslateX();
	if (x<-20) circle.setTranslateX(initialX);
	else
	circle.setTranslateX(x-rate);
	
	
  }

public int getRate() {
	return rate;
}

public void setRate(int rate) {
	this.rate = rate;
}

public Circle getCoin() {
	return circle;
}

public boolean collide(Sprite other) 
{
	if (other instanceof BoySprite) return true;
    return false;
}

}

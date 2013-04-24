package test;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Background extends Sprite
{
	private ImageView imageView;
	private int rate;
	
	public Background(Image image, int rate)
	{
	  imageView = new ImageView(image);
	  this.rate=rate;
	}
	
	public void update() 
	{
	  imageView.setTranslateX(imageView.getTranslateX()-rate);	
		
	}

	public ImageView getImageView() {
		return imageView;
	}

}

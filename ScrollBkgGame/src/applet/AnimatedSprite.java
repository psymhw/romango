/*****************************************************
* Beginning Java Game Programming, 2nd Edition
* by Jonathan S. Harbour
* AnimatedSprite class
*****************************************************/
package applet;
import java.awt.*;
import java.applet.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.net.*;

public class AnimatedSprite extends Sprite {
    //this image holds the large tiled bitmap
    private ImageEntity animImage;
    //temp image passed to parent draw method
    BufferedImage tempImage;
    Graphics2D tempSurface;
    //custom properties
    private int currFrame, totFrames;
    private int animDir;
    private boolean clearBuffer=false;
    private int colorCode=0;
    private boolean shortLived=false;
    private boolean firstRun = true;
    

	public boolean isShortLived() {
		return shortLived;
	}


	public void setShortLived(boolean shortLived) {
		this.shortLived = shortLived;
	}


	public int getColorCode() {
		return colorCode;
	}


	public void setColorCode(int colorCode) {
		this.colorCode = colorCode;
	}


	public void setClearBuffer(boolean clearBuffer) {
		this.clearBuffer = clearBuffer;
	}

	private int frCount, frDelay;
    public void setFrCount(int frCount) {
		this.frCount = frCount;
	}


	public int getFrCount() {
		return frCount;
	}

	private int frWidth, frHeight;
    private int cols;
    private boolean animationOn=true;
    
    public boolean isAnimationOn() {
		return animationOn;
	}


	public void setAnimationOn(boolean animationOn) {
		this.animationOn = animationOn;
	}

	private boolean jumped;
     public boolean isJumped() {
		return jumped;
	}
     
    
	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	//  private Crossing crossing;
    private int associatedCrossing=-1;  
    public int getAssociatedCrossing() {
		return associatedCrossing;
	}

	public void setAssociatedCrossing(int associatedCrossing) {
		this.associatedCrossing = associatedCrossing;
	}

	
    
  

//	public Crossing getCrossing() {
//		return crossing;
//	}

//	public void setCrossing(Crossing crossing) {
//		this.crossing = crossing;
//	}

	public AnimatedSprite(Applet applet, Graphics2D g2d) {
        super(applet, g2d);
        animImage = new ImageEntity(applet);
        currFrame = 0;
        totFrames = 0;
        animDir = 1;
        frCount = 0;
        frDelay = 0;
        frWidth = 0;
        frHeight = 0;
        cols = 0;
    }

    public void load(String filename, int columns, int rows,
        int width, int height)
    {
        //load the tiled animation bitmap
        animImage.load(filename);
        setColumns(columns);
        setTotalFrames(columns * rows);
        setFrameWidth(width);
        setFrameHeight(height);

        //frame image is passed to parent class for drawing
        tempImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_ARGB);
        tempSurface = tempImage.createGraphics();
        super.setImage(tempImage);
    }
    
    public void load(ImageEntity imageEntity, int columns, int rows, int width, int height)
        {
            //load the tiled animation bitmap
          //  animImage.load(filename);
    	    animImage=imageEntity;
            setColumns(columns);
            setTotalFrames(columns * rows);
            setFrameWidth(width);
            setFrameHeight(height);

            //frame image is passed to parent class for drawing
            tempImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
            tempSurface = tempImage.createGraphics();
            super.setImage(tempImage);
        }

    public int currentFrame() { return currFrame; }
    public void setCurrentFrame(int frame) { currFrame = frame; }

    public int frameWidth() { return frWidth; }
    public void setFrameWidth(int width) { frWidth = width; }

    public int frameHeight() { return frHeight; }
    public void setFrameHeight(int height) { frHeight = height; }

    public int totalFrames() { return totFrames; }
    public void setTotalFrames(int total) { totFrames = total; }

    public int animationDirection() { return animDir; }
    public void setAnimationDirection(int dir) { animDir = dir; }

    public int frameDelay() { return frDelay; }
    public void setFrameDelay(int delay) { frDelay = delay; }

    public int columns() { return cols; }
    public void setColumns(int num) { cols = num; }

    public Image getAnimImage() { return animImage.getImage(); }
    public void setAnimImage(Image image) { animImage.setImage(image); }

    public void updateAnimation() 
    {
      frCount += 1;
     
      if (shortLived)
      {
        if (currFrame==totFrames-1) firstRun=false;
        if (currFrame==0)
        {
      	  if (firstRun==false) setAlive(false);  
        }
      }
      if (frCount > frDelay) 
      {
        frCount = 0;
        if (!animationOn) return;
        //update the animation frame
        currFrame += animDir;
        if (currFrame > totFrames - 1) 
        {
          currFrame = 0;
        }
        else if (currFrame < 0) 
        {
          currFrame = totFrames - 1;
        }
      }
    }

    public void updateFrame() {
        if (totFrames > 0) {
            //calculate the current frame's X and Y position
            int frameX = (currentFrame() % columns()) * frameWidth();
            int frameY = (currentFrame() / columns()) * frameHeight();
          
            /*
            if (clearBuffer) // for transparent images 
            { 	
              tempImage = new BufferedImage(frameWidth(), frameHeight(), BufferedImage.TYPE_INT_ARGB);
              tempSurface = tempImage.createGraphics();
            }
            else
            {
              if (tempImage == null) 
              {
                tempImage = new BufferedImage(frameWidth(), frameHeight(), BufferedImage.TYPE_INT_ARGB);
                tempSurface = tempImage.createGraphics();
              }
            }
            */
            
            if (tempImage == null) 
            {
              tempImage = new BufferedImage(frameWidth(), frameHeight(), BufferedImage.TYPE_INT_ARGB);
            
              tempSurface = tempImage.createGraphics();
            }
   
            //copy the frame onto the temp image
            if (animImage.getImage() != null) 
            {
              if (clearBuffer)
              {	  
                // clear to transparent
                Composite composite=tempSurface.getComposite(); // get the current setting
                tempSurface.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f)); // clear the buffer
                Rectangle2D.Double rect = new Rectangle2D.Double(0,0,frameWidth(),frameHeight()); 
                tempSurface.fill(rect);
                tempSurface.setComposite(composite); // restore setting
              }
              tempSurface.drawImage(animImage.getImage(), 0, 0, frameWidth() - 1,
              frameHeight() - 1, frameX, frameY,
              frameX + frameWidth(),
              frameY + frameHeight(), applet());
            }
            //pass the temp image on to the parent class and draw it
            super.setImage(tempImage);
        }
    }

}


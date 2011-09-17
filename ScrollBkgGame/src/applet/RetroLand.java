package applet;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class RetroLand extends Game
{
  private static final long serialVersionUID = -7251591393889798045L;
  static int FRAMERATE = 60;
  static int SCREENWIDTH = 800; 
  static int SCREENHEIGHT = 600;
  boolean keyLeft, keyRight, keyUp, keyFire, keyB, keyC, keyShield, spaceBar, keyShift, keyF1, keyF2, keyF3, keyF7, keyF11;
  boolean showBounds = false;
  boolean collisionTesting = true;
   
  final int MENU_LEVEL=0;
  
  int level=MENU_LEVEL;
  
  final int SPRITE_KID=1;
  final int STATE_NORMAL=1;
  
  ImageEntity block_1;
  ImageEntity block_2;
  ImageEntity block_3;
  ImageEntity forwardKidImage;
  
  
  int block_1_Offset=0;
  int block_2_Offset=500;
  int block_3_Offset=1000;
  
  int scrollRate=2;
  
  
  int block_1_Pos=block_1_Offset;
  int block_2_Pos=block_2_Offset;
  int block_3_Pos=block_3_Offset;
  
  int blockSize=500;
  
  public RetroLand() { super(FRAMERATE, SCREENWIDTH, SCREENHEIGHT); }
  
  
  
  void gameStartup() 
  {
	 initSounds();
	 initScreens();
	 initSprites();
	 level=MENU_LEVEL;
	 setLevel();
	 this.requestFocus();
	 sprites().add(createKid(forwardKidImage));
  }
  
  public void checkInput() 
  {
	  if (keyRight) 
      {
		block_1_Pos-=scrollRate; 
		block_2_Pos-=scrollRate; 
		block_3_Pos-=scrollRate; 
		if ((block_1_Pos+blockSize)<0) block_1_Pos=998;
		if ((block_2_Pos+blockSize)<0) block_2_Pos=998;
		if ((block_3_Pos+blockSize)<0) block_3_Pos=998;
		//System.out.println("block 1 pos: "+block_1_Pos);
      }
  }
  
   private void setLevel() 
   {
   }

   private void initSprites() 
   {
	   forwardKidImage= new ImageEntity(this);
	   forwardKidImage.load("newboy2.png");
   }

   private void initScreens() 
   {
	 block_1 = new ImageEntity(this);
	 block_1.load("redBlock.png");
	 
	 block_2 = new ImageEntity(this);
	 block_2.load("greenBlock.png");
	 
	 block_3 = new ImageEntity(this);
	 block_3.load("blueBlock.png");
	 	
   }

   private void initSounds() 
   {
   }


   void gameRefreshScreen() 
   {
	 Graphics2D g2d = graphics();
	
	 if (((block_1_Pos+blockSize)>0)&&(block_1_Pos<SCREENWIDTH))
	 {	 
	   g2d.drawImage(block_1.getImage(),block_1_Pos,0,this);
	 } 
	 
	 if (((block_2_Pos+blockSize)>0)&&(block_2_Pos<SCREENWIDTH))
	 {	 
	   g2d.drawImage(block_2.getImage(),block_2_Pos,0,this);
	 } 
	 
	 if (((block_3_Pos+blockSize)>0)&&(block_3_Pos<SCREENWIDTH))
	 {	 
	   g2d.drawImage(block_3.getImage(),block_3_Pos,0,this);
	 } 
   }
   
  
   void gameTimedUpdate() 
   {
	 checkInput();
   }
   
   @Override
   void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2) 
   {
	// TODO Auto-generated method stub
   }
   
   @Override
   void spriteDraw(AnimatedSprite sprite) 
   {
	// TODO Auto-generated method stub
   }
   
   
   
   @Override
   void spriteUpdate(AnimatedSprite sprite) 
   {
	// TODO Auto-generated method stub
   }
   
   void gameKeyDown(int keyCode) 
   {
 	  switch(keyCode) 
 	  {
       case KeyEvent.VK_LEFT:
           keyLeft = true;
           break;
       case KeyEvent.VK_RIGHT:
           keyRight = true;
           break;
       case KeyEvent.VK_UP:
           keyUp = true;
           break;
       case KeyEvent.VK_CONTROL:
           keyFire = true;
           break;
       case KeyEvent.VK_B:
           showBounds = !showBounds;
           break;
       case KeyEvent.VK_C:
           //toggle collision testing
           collisionTesting = !collisionTesting;
           break;
           
       case KeyEvent.VK_ENTER:
     	 // System.out.println("ENTER PRESSED");
          
           break;
           
       case KeyEvent.VK_SHIFT:
     	  keyShift=true;
     	  break;
     	  
       case KeyEvent.VK_F1:
     	  keyF1=true;
           break;
       case KeyEvent.VK_F2:
     	  keyF2=true;
           break;
       case KeyEvent.VK_F3:
     	  keyF3=true;
           break;
       case KeyEvent.VK_F7:
     	  keyF7=true;
     	  break;
       case KeyEvent.VK_F11:
     	  keyF11=true;
     	  break;
     	  
       case KeyEvent.VK_ESCAPE:
   
            //  level=MENU_LEVEL;
            //  setLevel();
         
           break;
 	  }
   }
   
   void gameKeyUp(int keyCode) 
   {
	     switch(keyCode) {
	      case KeyEvent.VK_LEFT:
	          keyLeft = false;
	          break;
	      case KeyEvent.VK_RIGHT:
	          keyRight = false;
	          break;
	      case KeyEvent.VK_UP:
	          keyUp = false;
	          break;
	      case KeyEvent.VK_CONTROL:
	          keyFire = false;
	          break;
	      case KeyEvent.VK_SHIFT:
	    	  keyShift=false;
	          break;
	      case KeyEvent.VK_F1:
	    	  keyF1=false;
	          break;
	      case KeyEvent.VK_F2:
	    	  keyF2=false;
	          break;
	      case KeyEvent.VK_F3:
	    	  keyF3=false;
	          break;
	      case KeyEvent.VK_F7:
	    	  keyF7=false;
	          break;
	      case KeyEvent.VK_F11:
	    	  keyF11=false;
	          break;
	      }
       }
    
     void gameMouseMove() {}
     
     void gameMouseUp()   {}
     
     void gameMouseDown() {}
     
     void gameShutdown()  {}
     
     void spriteDying(AnimatedSprite sprite) {}
     
     public AnimatedSprite createKid(ImageEntity imageEntity) 
     {
       AnimatedSprite akid = new AnimatedSprite(this, graphics());
       akid.setAlive(true);
       akid.setStopped(true);
       akid.setSpriteType(SPRITE_KID);
       
       akid.setState(STATE_NORMAL);
      
       int y=500;
         
       akid.setSidewalk(0);
         
       int x = 350; // left
       akid.setPosition(new Point2D(x, y));

       akid.setDirection(90);
       akid.setFaceAngle(0);
       akid.setRotationRate(0);
         
         //set velocity based on movement direction
       akid.setVelocity(new Point2D(0, 0));
   	akid.setAnimImage(imageEntity.getImage());
   	akid.setTotalFrames(2);
   	akid.setColumns(2);
   	akid.setFrameWidth(33);
   	akid.setFrameHeight(42);
   	akid.setFrameDelay(10);
   	akid.setClearBuffer(true);
         
       return akid;
     }
     
}

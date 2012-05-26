package main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Stone extends ImageView
{
  private int stoneColor;
  //private BoardPosition boardPosition;
  private boolean alive=true;
  private int captureMoveNumber;
  private static Image black_stone_image=null;
  private static Image white_stone_image=null;
  private static Image black_move_image=null;
  private static Image white_move_image=null;
  private static Image b1=null;
  private static Image b2=null;
  private static Image b3=null;
  private static Image b4=null;
  private static Image b5=null;
  private static Image b6=null;
  private static Image b7=null;
  private static Image b8=null;
  private static Image b9=null;
  private static Image b10=null;
  
  private static Image w1=null;
  private static Image w2=null;
  private static Image w3=null;
  private static Image w4=null;
  private static Image w5=null;
  private static Image w6=null;
  private static Image w7=null;
  private static Image w8=null;
  private static Image w9=null;
  private static Image w10=null;
  
  public int x;
  public int y;
  private String sgfPosition;
  private double sceneX=0;
  private double sceneY=0;
  private Move move;
  
  int style=0;
  
  public boolean forget=false;
  
  public Stone(Move move, int style)
  {
	this.move=move;  
    this.x=move.x;
    this.y=move.y;
    sgfPosition = move.getSgfPosition();
    sceneX = calcSceneX();
    sceneY = calcSceneY();
    this.style=style;
    this.stoneColor=move.color;
    createStone();
  }
  
  /*
  
    public Stone(int color, String sgfPosition, int style)
    {
      this.sgfPosition=sgfPosition;
      x=calcX();
      y=calcY();
      sceneX = calcSceneX();
      sceneY = calcSceneY();
      this.style=style;
       this.stoneColor=color;
       createStone(); 
    }
    
    public Stone(int color, int x, int y, int style)
    {
      this.x=x;
      this.y=y;
      sgfPosition = calcSgfPosition();
      sceneX = calcSceneX();
      sceneY = calcSceneY();
      this.style=style;
      this.stoneColor=color;
      createStone();
    }
    
    public Stone(int stoneColor, double sceneX, double sceneY, int style)
    {
      double xxx = sceneX+23;
      double yyy = sceneY+23;
	  
      int xx = (int)Math.round(xxx/35)-1;
      int yy = (int)Math.round(yyy/35)-1;
	  
      if ((xx>=0)&&(xx<20)&&(yy>=0)&&(yy<20))
      {
	this.x=xx;
	this.y=yy;
      }
      sgfPosition = calcSgfPosition();
      this.sceneX = calcSceneX();
      this.sceneY = calcSceneY();
      this.stoneColor = stoneColor;
      this.style=style;
      
      createStone();
  
        
    }
    
    */
  /*
    public Stone(int stoneColor, BoardPosition boardPosition, boolean moveImage)
    {
      if (black_stone_image==null)  black_stone_image = new Image(Stone.class.getResourceAsStream("/images/b.gif")); 
      if (white_stone_image==null)  white_stone_image = new Image(Stone.class.getResourceAsStream("/images/w.gif"));
      if (black_move_image==null)   black_move_image = new Image(Stone.class.getResourceAsStream("/images/bm.gif"));
      if (white_move_image==null)   white_move_image = new Image(Stone.class.getResourceAsStream("/images/wm.gif"));

       this.boardPosition=boardPosition;
       this.stoneColor=stoneColor;
       if (moveImage)
       {
         if (stoneColor==GoClient.WHITE) setImage(white_move_image);
         else setImage(black_move_image);
       }
       else
       {
         if (stoneColor==GoClient.WHITE) setImage(white_stone_image);
         else setImage(black_stone_image);
       }    
       setX(boardPosition.getSceneX());
       setY(boardPosition.getSceneY());
    }
    */
    void createStone()
    {
      if (black_stone_image==null)  black_stone_image = new Image(Stone.class.getResourceAsStream("/images/b.gif")); 
      if (white_stone_image==null)  white_stone_image = new Image(Stone.class.getResourceAsStream("/images/w.gif"));
      if (black_move_image==null)   black_move_image = new Image(Stone.class.getResourceAsStream("/images/bm.gif"));
      if (white_move_image==null)   white_move_image = new Image(Stone.class.getResourceAsStream("/images/wm.gif"));
      
      if (b1==null) b1 = new Image(Stone.class.getResourceAsStream("/images/b1.gif"));
      if (b2==null) b2 = new Image(Stone.class.getResourceAsStream("/images/b2.gif"));
      if (b3==null) b3 = new Image(Stone.class.getResourceAsStream("/images/b3.gif"));
      if (b4==null) b4 = new Image(Stone.class.getResourceAsStream("/images/b4.gif"));
      if (b5==null) b5 = new Image(Stone.class.getResourceAsStream("/images/b5.gif"));
      if (b6==null) b6 = new Image(Stone.class.getResourceAsStream("/images/b6.gif"));
      if (b7==null) b7 = new Image(Stone.class.getResourceAsStream("/images/b7.gif"));
      if (b8==null) b8 = new Image(Stone.class.getResourceAsStream("/images/b8.gif"));
      if (b9==null) b9 = new Image(Stone.class.getResourceAsStream("/images/b9.gif"));
      if (b10==null) b10 = new Image(Stone.class.getResourceAsStream("/images/b10.gif"));
      
      if (w1==null) w1 = new Image(Stone.class.getResourceAsStream("/images/w1.gif"));
      if (w2==null) w2 = new Image(Stone.class.getResourceAsStream("/images/w2.gif"));
      if (w3==null) w3 = new Image(Stone.class.getResourceAsStream("/images/w3.gif"));
      if (w4==null) w4 = new Image(Stone.class.getResourceAsStream("/images/w4.gif"));
      if (w5==null) w5 = new Image(Stone.class.getResourceAsStream("/images/w5.gif"));
      if (w6==null) w6 = new Image(Stone.class.getResourceAsStream("/images/w6.gif"));
      if (w7==null) w7 = new Image(Stone.class.getResourceAsStream("/images/w7.gif"));
      if (w8==null) w8 = new Image(Stone.class.getResourceAsStream("/images/w8.gif"));
      if (w9==null) w9 = new Image(Stone.class.getResourceAsStream("/images/w9.gif"));
      if (w10==null) w10 = new Image(Stone.class.getResourceAsStream("/images/w10.gif"));
       
      
      if (stoneColor==GoClient.WHITE)
      {
    	switch(style)
    	{
    	case 0:
    		setImage(white_stone_image);
    		break;
    	case 1:
    		setImage(w1);
    		break;
    	case 2:
    		setImage(w2);
    		break;
    	case 3:
    		setImage(w3);
    		break;
    	case 4:
    		setImage(w4);
    		break;
    	case 5:
    		setImage(w5);
    		break;
    	case 6:
    		setImage(w6);
    		break;
    	case 7:
    		setImage(w7);
    		break;
    	case 8:
    		setImage(w8);
    		break;
    	case 9:
    		setImage(w9);
    		break;
    	case 10:
    		setImage(w10);
    		break;
        default:
        	setImage(white_stone_image);
    	}
      }
      else
      {
    	  switch(style)
      	{
      	case 0:
      		setImage(black_stone_image);
      		break;
      	case 1:
      		setImage(b1);
      		break;
      	case 2:
      		setImage(b2);
      		break;
      	case 3:
      		setImage(b3);
      		break;
      	case 4:
      		setImage(b4);
      		break;
      	case 5:
      		setImage(b5);
      		break;
      	case 6:
      		setImage(b6);
      		break;
      	case 7:
      		setImage(b7);
      		break;
      	case 8:
      		setImage(b8);
      		break;
      	case 9:
      		setImage(b9);
      		break;
      	case 10:
      		setImage(b10);
      		break;
          default:
          	setImage(black_stone_image);
      	}
      }
      
      /*
      
      if (style==GoClient.STYLE_LAST_MOVE)
      {
        if (stoneColor==GoClient.WHITE) setImage(white_move_image);
        else setImage(black_move_image);
      }
      else
      {
        if (stoneColor==GoClient.WHITE) setImage(white_stone_image);
        else setImage(black_stone_image);
      }    
      */
      
      setX(sceneX);
      setY(sceneY);   
    }
    
    void setMoveImage()
    {
        if (stoneColor==GoClient.WHITE) setImage(white_move_image);
       else setImage(black_move_image);
        
    }
    
    void setRegularImage()
    {
        if (stoneColor==GoClient.WHITE) setImage(white_stone_image);
       else setImage(black_stone_image);
        
    }
    
        public int getCaptureMoveNumber()
        { return captureMoveNumber; }
        
        public void setCaptureMoveNumber(int captureMoveNumber)
        { this.captureMoveNumber=captureMoveNumber; }
        
    
	public int getStoneColor() {
		return stoneColor;
	}
	public void setStoneColor(int stoneColor) {
		this.stoneColor = stoneColor;
	}
        /*
	public BoardPosition getBoardPosition() {
		return boardPosition;
	}
	public void setBoardPosition(BoardPosition boardPosition) {
		this.boardPosition = boardPosition;
	}
       
        */
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
        
        public double getSceneX() {
	return sceneX;
}

        public void setSceneX(int sceneX) {
                this.sceneX = sceneX;
        }

        public double getSceneY() {
                return sceneY;
        }

        public void setSceneY(int sceneY) {
                this.sceneY = sceneY;
        }
public int calcSceneX()
  {
	char xChar = sgfPosition.charAt(0);
  	int xInt = xChar;
	return (xInt-97)*35;
  }
  
  public int calcSceneY()
  {
	char yChar = sgfPosition.charAt(1);
  	int yInt = yChar;
	return (yInt-97)*35;
  }
  
  
    public int calcX()
    {
            char xxx = sgfPosition.charAt(0);
            return xxx-97;
    }

    public int calcY()
    {
            char yyy = sgfPosition.charAt(1);
            return yyy-97;
    }

    public String calcSgfPosition()
    {
            String pos = new String();
            char xx = (char)(x+97); 
            char yy = (char)(y+97);

            pos=""+xx+""+yy;
            return pos;
    }

    public String getSgfPosition() {
            return sgfPosition;
    }

    public void setSgfPosition(String sgfPosition) {
            this.sgfPosition = sgfPosition;
    }
  

}

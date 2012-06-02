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
 
  private static Image bx=null;
  private static Image bcheck=null;
  
 
  
  private static Image wx=null;
  private static Image wcheck=null;
  
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
      
     
      if (bx==null) bx = new Image(Stone.class.getResourceAsStream("/images/bx.gif"));
      if (bcheck==null) bcheck = new Image(Stone.class.getResourceAsStream("/images/bcheck.gif"));
      
          
      if (wx==null) wx = new Image(Stone.class.getResourceAsStream("/images/wx.gif"));
      if (wcheck==null) wcheck = new Image(Stone.class.getResourceAsStream("/images/wcheck.gif"));
       
      
      if (stoneColor==GoClient.WHITE)
      {
    	switch(style)
    	{
    	case 0:
    		setImage(white_stone_image);
    		break;
    	case 1:
    		setImage(wcheck);
    		break;
    	
        default:
        	setImage(wx);
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
      		setImage(bcheck);
      		break;
      	      	
          default:
          	setImage(bx);
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

package main;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Stone extends Group
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
  private ImageView stoneImageView = new ImageView();
  private Label stoneNumberLabel = new Label();
  private Label subsetStoneNumberLabel = new Label();
  private int moveNumber=0;
  
  int style=0;
  
  public boolean forget=false;
  final static char[] xLabels = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't'};
  
  
  public Stone(Move move)
  {
	this.move=move;  
    this.x=move.x;
    this.y=move.y;
    sgfPosition = move.getSgfPosition();
    sceneX = calcSceneX();
    sceneY = calcSceneY();
    move.sceneX=sceneX;
    move.sceneY=sceneY;
    
   
    this.stoneColor=move.color;
    moveNumber=move.getMoveNumber();
    setupStoneNumberLabel();
    setupSubsetStoneNumberLabel();
    createStone();
    
    Tooltip t = new Tooltip(move.getMoveNumber()+" - "+xLabels[x]+"-"+(19-y)+"    ");
    Tooltip.install(stoneImageView, t);
    this.getChildren().add(stoneImageView);
    this.getChildren().add(stoneNumberLabel);
    this.getChildren().add(subsetStoneNumberLabel);
  }
  
  
    private void setupStoneNumberLabel() 
    {
      stoneNumberLabel.setFont(Font.font("Serif", 15));
      stoneNumberLabel.setText(""+moveNumber);
    	
      int numberLength=(""+moveNumber).length();
      if (numberLength==3) stoneNumberLabel.setLayoutX(move.sceneX+5);
      else if (numberLength==2) stoneNumberLabel.setLayoutX(move.sceneX+9);
      else  stoneNumberLabel.setLayoutX(move.sceneX+13);
    	   
      stoneNumberLabel.setLayoutY(move.sceneY+9);
    	   
      if (move.color==GoClient.WHITE) stoneNumberLabel.setTextFill(Color.BLACK);
      else stoneNumberLabel.setTextFill(Color.WHITE);
      stoneNumberLabel.setVisible(false);
    }
    
    private void setupSubsetStoneNumberLabel() 
    {
      subsetStoneNumberLabel.setFont(Font.font("Serif", 15));
         	   
      if (move.color==GoClient.WHITE) subsetStoneNumberLabel.setTextFill(Color.BLACK);
      else subsetStoneNumberLabel.setTextFill(Color.WHITE);
      
      subsetStoneNumberLabel.setLayoutX(move.sceneX+13);
      subsetStoneNumberLabel.setLayoutY(move.sceneY+9);
      
      subsetStoneNumberLabel.setVisible(false);
    }


	void createStone()
    {
      String src="/resources";
      if (black_stone_image==null)  black_stone_image = new Image(Stone.class.getResourceAsStream(src+"/images/b.gif")); 
      if (white_stone_image==null)  white_stone_image = new Image(Stone.class.getResourceAsStream(src+"/images/w.gif"));
      if (black_move_image==null)   black_move_image = new Image(Stone.class.getResourceAsStream(src+"/images/bm.gif"));
      if (white_move_image==null)   white_move_image = new Image(Stone.class.getResourceAsStream(src+"/images/wm.gif"));
      
     
      if (bx==null) bx = new Image(Stone.class.getResourceAsStream(src+"/images/bx.gif"));
      if (bcheck==null) bcheck = new Image(Stone.class.getResourceAsStream(src+"/images/bcheck.gif"));
      if (wx==null) wx = new Image(Stone.class.getResourceAsStream(src+"/images/wx.gif"));
      if (wcheck==null) wcheck = new Image(Stone.class.getResourceAsStream(src+"/images/wcheck.gif"));
       
      
      if (stoneColor==GoClient.WHITE)
      {
    	  stoneImageView.setImage(white_stone_image);
      }
      else
      {
    	  stoneImageView.setImage(black_stone_image);
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
      
      stoneImageView.setX(sceneX);
      stoneImageView.setY(sceneY);   
      
    }
    
    void setMoveImage()
    {
        if (stoneColor==GoClient.WHITE) stoneImageView.setImage(white_move_image);
       else stoneImageView.setImage(black_move_image);
        
    }
    
    void setMarkImage()
    {
        if (stoneColor==GoClient.WHITE) stoneImageView.setImage(wx);
       else stoneImageView.setImage(bx);
        
    }
    
    void setCheckImage()
    {
        if (stoneColor==GoClient.WHITE) stoneImageView.setImage(wcheck);
       else stoneImageView.setImage(bcheck);
        
    }
    
    void setRegularImage()
    {
        if (stoneColor==GoClient.WHITE) stoneImageView.setImage(white_stone_image);
       else stoneImageView.setImage(black_stone_image);
        
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


	public Move getMove() {
		return move;
	}


	public void setMove(Move move) {
		this.move = move;
	}


	public Label getStoneNumberLabel() {
		return stoneNumberLabel;
	}


	

	public Label getSubsetStoneNumberLabel() {
		return subsetStoneNumberLabel;
	}


	public int getMoveNumber() {
		return moveNumber;
	}


	
  

}

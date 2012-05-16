package main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Stone extends ImageView
{
  private int stoneColor;
  private BoardPosition boardPosition;
  private boolean alive=true;
  
    public Stone(Image stone_image, int stoneColor, BoardPosition boardPosition)
    {
       this.boardPosition=boardPosition;
       this.stoneColor=stoneColor;
       setImage(stone_image);	
       setX(boardPosition.getSceneX());
       setY(boardPosition.getSceneY());
    }
    
	public int getStoneColor() {
		return stoneColor;
	}
	public void setStoneColor(int stoneColor) {
		this.stoneColor = stoneColor;
	}
	public BoardPosition getBoardPosition() {
		return boardPosition;
	}
	public void setBoardPosition(BoardPosition boardPosition) {
		this.boardPosition = boardPosition;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}

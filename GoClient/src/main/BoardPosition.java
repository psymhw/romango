package main;

public class BoardPosition 
{
  public int x;
  public int y;
  private String sgfPosition;
  private double sceneX=0;
  private double sceneY=0;
  
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

public BoardPosition(double sceneX, double sceneY)
{
  //this.sceneX=sceneX;
  //this.sceneY=sceneY;
	
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
	  
}


public BoardPosition(int x, int y)
  {
	 this.x=x;
	 this.y=y;
	 sgfPosition = calcSgfPosition();
	 sceneX = calcSceneX();
	 sceneY = calcSceneY();
  }
  
  public BoardPosition(String sgfPosition)
  {
	 this.sgfPosition=sgfPosition;
	 x=calcX();
	 y=calcY();
	 sceneX = calcSceneX();
	 sceneY = calcSceneY();
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

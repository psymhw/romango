/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author rick
 */
public class Move 
{
  public int x;
  public int y;
  private boolean pass=false;
  private boolean resign=false;
  private int moveNumber=0;
  
  public int color;
  public String sgfPosition;
  public double sceneX;
  public double  sceneY;
  final static char[] xLabels = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't'};
  
  
  public Move(double sceneX, double sceneY, int color)
  {
	this.color=color;
   
	
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
    this.color = color;
  }
  
  
  public Move(String sgfPosition, int color)
  {
	this.sgfPosition=sgfPosition;
	this.color=color;
	
	this.x=calcX();
	this.y=calcY();
  }
  
  
  public Move(int color)
  {
	//this.color=color;
	this("'pass'" +
			"", color); //pass
	pass=true;
	
  }
  
 
  public String calcSgfPosition()
  {
    String pos = new String();
    char xx = (char)(x+97); 
    char yy = (char)(y+97);
    pos=""+xx+""+yy;
    return pos;
  }
  
  private static String calcSgfPosition(int x, int y)
  {
    String pos = new String();
    char xx = (char)(x+97); 
    char yy = (char)(y+97);
    pos=""+xx+""+yy;
    return pos;
  }
  
  public String getSgfPosition()
  {
    return sgfPosition;
  }
  
  public static String getSgfPosition(double sceneX, double sceneY)
  {
	  double xxx = sceneX+23;
	    double yyy = sceneY+23;
	    int x=0;
	    int y=0;
	  int xx = (int)Math.round(xxx/35)-1;
	    int yy = (int)Math.round(yyy/35)-1;
		  
	    if ((xx>=0)&&(xx<20)&&(yy>=0)&&(yy<20))
	    {
		x=xx;
		y=yy;
	    }
    return calcSgfPosition(x, y);
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
  
  public String getColorStr()
  {
	if (color==1) return "B";
	else return "W";
  }
  
  public int getColor()
  {
	return color;
  }
  
  public SimplePosition getSimplePosition()
  {
	 return new SimplePosition(x,y); 
  }
  
  public String getBoardPosition()
  {
	  if (pass) return "";
	  return xLabels[x]+"-"+(19-y);
  }
  
  public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public boolean isResign() {
		return resign;
	}

	public void setResign(boolean resign) {
		this.resign = resign;
	}


	public int getMoveNumber() {
		return moveNumber;
	}


	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

  
  
}

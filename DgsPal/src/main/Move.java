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
  
  /*
  public Move(int x, int y, int color)
  {
    this.x=x;
    this.y=y;
    this.color=color;
    sgfPosition=calcSgfPosition();
  }
    */
  public Move(String sgfPosition, int color)
  {
	this.sgfPosition=sgfPosition;
	this.color=color;
	
	this.x=calcX();
	this.y=calcY();
  }
 
  public String calcSgfPosition()
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
  
  public String getColor()
  {
	if (color==1) return "B";
	else return "W";
  }
  
  public SimplePosition getSimplePosition()
  {
	 return new SimplePosition(x,y); 
  }
  
  public String getBoardPosition()
  {
	  return xLabels[x]+"-"+(19-y);
  }
  
  
  
}

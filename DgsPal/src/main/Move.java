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
  
  public Move(int x, int y, int color)
  {
    this.x=x;
    this.y=y;
    this.color=color;
  }
    
  public String getSgfPosition()
  {
          String pos = new String();
          char xx = (char)(x+97); 
          char yy = (char)(y+97);

          pos=""+xx+""+yy;
          return pos;
  }
}

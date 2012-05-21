/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;

public class Neighbors 
{
   public int groupSize=0;
   public int liberties=0;
   public List<SimplePosition> groupPositions = new ArrayList<>();
   public List<SimplePosition> libertyPositions = new ArrayList<>();
   public SimplePosition initialPosition;
   public int color=0;
    
    Neighbors (int x, int y, int color) 
    {
        initialPosition=new SimplePosition(x, y);
        this.color=color;  
        groupSize=1;
        groupPositions.add(initialPosition);
    }
    
     Neighbors (int color) 
    {
        initialPosition=new SimplePosition(-1, -1);
        this.color=color;  
        groupSize=1;
        groupPositions.add(initialPosition);
    }
    
    void accumulate(Neighbors n)
    {
      if (n!=null)
      {
        groupSize+=n.groupSize;
        liberties+=n.liberties;
        groupPositions.addAll(n.groupPositions);
        libertyPositions.addAll(n.libertyPositions);
      }
    }
 
}



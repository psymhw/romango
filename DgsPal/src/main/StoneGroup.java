package main;

import java.util.ArrayList;
import java.util.List;

public class StoneGroup 
{
   public int groupSize=0;
   public int liberties=0;
   public List<SimplePosition> groupPositions = new ArrayList<>();
   public List<SimplePosition> libertyPositions = new ArrayList<>();
   public SimplePosition initialPosition;
   public int color=0;
   
   final static int GROUP_MEMBER=33;
   final static int LIBERTY=43;
   
   final static int START=0;
   final static int NORTH=1;
   final static int SOUTH=2;
   final static int EAST=3;
   final static int WEST=4;
   
   final static int OPEN = 0;
   final static int BLACK = 1;
   final static int WHITE = 2;
   
   public int[][] groupMap = new int[19][19];
   public int[][] moveMap;
   
   int x, y;
    
    StoneGroup (int x, int y, int[][] moveMap) 
    {
      this.moveMap=moveMap;
      this.x=x;
      this.y=y;
       initialPosition=new SimplePosition(x, y);
       this.color=moveMap[x][y];  
       groupSize=1;
       groupPositions.add(initialPosition);
       clearGroupMap();
     //  groupMap[x][y]=GROUP_MEMBER;
       createGroup();
    }
    
    
    private void clearGroupMap() 
    {
      for(int i=0; i<19; i++)
      {
        for(int j=0; j<19; j++)
        {
  	      groupMap[i][j]=GoClient.OPEN;
        }
      }
    }
    
    private void createGroup()
    {
       countLiberties();
    }
    
    private void countLiberties()
    {
       countLiberties(this.x, this.y,  START);
    }
   
    
    private void countLiberties(int x, int y,  int noCheck) 
    {
      if (groupMap[x][y]==GROUP_MEMBER) return;
       
       if (moveMap[x][y]==color) groupMap[x][y]=GROUP_MEMBER; 
       groupPositions.add(new SimplePosition(x,y));
               
       if (noCheck!=NORTH) directionCheck(x,y, NORTH); 
       if (noCheck!=SOUTH) directionCheck(x,y, SOUTH);
       if (noCheck!=WEST)  directionCheck(x,y, WEST);
       if (noCheck!=EAST)  directionCheck(x,y, EAST);
    }
    
    void directionCheck(int x, int y, int direction)
    {
     
      int checkX=x, checkY=y;
      int oppositeDirection=0;
      
     // System.out.println("direction: "+direction);
      if (direction==NORTH) { if (y==0) return;  checkY=y-1; oppositeDirection=SOUTH; }
      if (direction==SOUTH) { if (y==18) return; checkY=y+1; oppositeDirection=NORTH; }
      if (direction==EAST)  { if (x==18) return; checkX=x+1; oppositeDirection=WEST;  }
      if (direction==WEST)  { if (x==0) return;  checkX=x-1; oppositeDirection=EAST;  }
      
      
      
      if (moveMap[checkX][checkY]==OPEN) 
      {
    	 // System.out.println(checkX+"-"+checkY+" is open");
        if (groupMap[checkX][checkY]!=LIBERTY)
        {    
          groupMap[checkX][checkY]=LIBERTY;   
          liberties++;
          libertyPositions.add(new SimplePosition(checkX,checkY));
        }
      }
      else if (moveMap[checkX][checkY]==color) 
      {
        countLiberties(checkX, checkY, oppositeDirection);
      }
    }

    
    
   
    
    
 
}


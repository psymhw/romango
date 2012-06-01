package main;

public class BoardMap 
{
  private  int[][] moveMap = new int[19][19];
  
  public BoardMap(int[][] moveMap)
  {
	  for(int j=0; j<19; j++)
		{
		  for(int i=0; i<19; i++)
		  {
			this.moveMap[i][j]=moveMap[i][j];
		  }
		}
  }
  
  public boolean equals(int[][] moveMap)
  {
	boolean identical=true;  
	  for(int j=0; j<19; j++)
		{
		  for(int i=0; i<19; i++)
		  {
			if (identical==false) break;
			if (this.moveMap[i][j]!=moveMap[i][j])
			{
			  identical=false;
			  break;
			}
		  }
		}
	  return identical;
  }
}

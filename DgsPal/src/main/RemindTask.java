package main;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.TimerTask;

class RemindTask extends TimerTask 
{
	SimpleIntegerProperty timerCount;
	int count=0;
	public RemindTask(SimpleIntegerProperty timerCount)
	{
	  this.timerCount=timerCount;	
	}
    public void run() 
    {
      System.out.println("Time's up! "+count);
      timerCount.set(count++);
     // count=0;
    }
}
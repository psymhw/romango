package main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextArea;
import java.util.TimerTask;

class RemindTask extends TimerTask 
{
	SimpleIntegerProperty i;
	int count=0;
	public RemindTask(SimpleIntegerProperty i)
	{
	  this.i=i;	
	}
    public void run() 
    {
      System.out.println("Time's up! "+count);
      i.set(count++);
      
    }
}
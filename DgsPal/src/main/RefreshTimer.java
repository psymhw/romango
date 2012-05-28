package main;

import java.util.Timer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class RefreshTimer 
{
	Timer timer;
	IntegerProperty timerCount;
	//int min=60000;
	int min=6000;
	int level=0;
	long interval[] = new long[]{         min/2,        2*min,      5*min,        10*min,       30*min,   60*min};
	String[] timeStr = new String[]{"30 seconds", "2 minutes", "5 minutes", "10 minutes", "30 minutes", "1 hour"};
	
	public RefreshTimer(Timer timer, IntegerProperty timerCount)
	{
	   this.timer=timer;
	   this.timerCount=timerCount;
	}
	
	public void start()
	{
	  timer = new Timer();
	  timer.schedule(new RemindTask((SimpleIntegerProperty) timerCount), interval[level], interval[level]);
	}
	
	public void stop()
	{
	  timer.cancel();
	  timer.purge();
	  level=0;
	}
	
	public void levelUp()
	{
	  level++;
	  if (level>5) level=5;
	  timer.cancel();
	  timer.purge();
	  
	  timer = new Timer();
	  timer.schedule(new RemindTask((SimpleIntegerProperty) timerCount), interval[level], interval[level]);
	  System.out.println("level up");
	}
	
	public String getLevel()
	{
	  return timeStr[level];
	}
	
	
}

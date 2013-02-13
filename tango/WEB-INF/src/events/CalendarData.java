package events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import db.EventDb;

public class CalendarData 
{
  String firstMonth;
  String lastMonth;
  String prevMonth;
  String nextMonth;
  String strMonth;
  
  public String getStrMonth() {
	return strMonth;
}
public void setStrMonth(String strMonth) {
	this.strMonth = strMonth;
}
ArrayList events;
  ArrayList thisMonthSpecial;
  
public String getFirstMonth() {
	return firstMonth;
}
public void setFirstMonth(String firstMonth) {
	this.firstMonth = firstMonth;
}
public String getLastMonth() {
	return lastMonth;
}
public void setLastMonth(String lastMonth) {
	this.lastMonth = lastMonth;
}
public ArrayList getEvents() {
	return events;
}
public void setEvents(ArrayList events) {
	this.events = events;
}
public String getPrevMonth() {
	return prevMonth;
}
public void setPrevMonth(String prevMonth) {
	this.prevMonth = prevMonth;
}
public String getNextMonth() {
	return nextMonth;
}
public void setNextMonth(String nextMonth) {
	this.nextMonth = nextMonth;
}
public ArrayList getThisMonthSpecial() {
	return thisMonthSpecial;
}
public void setThisMonthSpecial(ArrayList thisMonthSpecial) {
	this.thisMonthSpecial = thisMonthSpecial;
}



}

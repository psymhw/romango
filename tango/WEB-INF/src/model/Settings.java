package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.struts.util.LabelValueBean;

public class Settings 
{
	public ArrayList <LabelValueBean>eventTypes = new ArrayList<LabelValueBean>();
	public ArrayList <LabelValueBean>areas = new ArrayList<LabelValueBean>();
	
	public static Hashtable<String, String> eventTypesHash = new Hashtable<String, String>();
	public static Hashtable<String, String> areasHash = new Hashtable<String, String>();
	private ArrayList<String> eventType = new ArrayList<String>();
	public static int MILONGA = 2;
	public static int SPECIAL_EVENT = 1;
	public static int PRACTICA = 4;
	public static int CLASS = 5;
	public static int EUGENE = 1;
		
	public Settings()
	{
	  eventType.add("error");
	  eventType.add("Special Event");
	  eventType.add("Regular Milonga");
	  eventType.add("----");
	  eventType.add("Regular Practica");
	  eventType.add("Classes");
		
	  LabelValueBean test = new LabelValueBean( "Special Event", "1");
	   int tint= Integer.parseInt(test.getValue());
	  eventTypes.add(new LabelValueBean( "Special Event", "1"));
	  eventTypes.add(new LabelValueBean( "Regular Milonga", "2"));
	 // eventTypes.add(new LabelValueBean( "----", "3"));
	  eventTypes.add(new LabelValueBean( "Regular Practica", "4"));
	  eventTypes.add(new LabelValueBean( "Classes", "5"));
	  
	  eventTypesHash.put("1", "Special Event" );
	  eventTypesHash.put("2", "Regular Milonga");
	  eventTypesHash.put("3", "----");
	  eventTypesHash.put("4", "Regular Practica");
	  eventTypesHash.put("5", "Classes");
	  
	  
	  areas.add(new LabelValueBean( "Eugene", "1"));
	  areas.add(new LabelValueBean( "Portland", "2"));
	  areas.add(new LabelValueBean( "Oregon", "3"));
	  areas.add(new LabelValueBean( "USA", "4"));
	  areas.add(new LabelValueBean( "International", "5"));
	  
	  areasHash.put("1", "Eugene");
	  areasHash.put("2", "Portland");
	  areasHash.put("3", "Oregon");
	  areasHash.put("4", "USA");
	  areasHash.put("5", "International");
	  
	}
	
	public ArrayList getEventTypes()
	{
		return eventTypes;
	}
	  
}

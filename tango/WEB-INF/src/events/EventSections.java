package events;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.struts.util.LabelValueBean;

import model.Settings;

import db.EventDb;

public class EventSections 
{
  
  private ArrayList  sections = new ArrayList();
  
  public EventSections()
  {
	Settings settings = new Settings();
	ArrayList eventTypes = settings.getEventTypes();
	Iterator it = eventTypes.iterator();
	int counter=0;
	//System.out.println("EventSections - size: "+eventTypes.size());
	
	for(int i=0; i<10; i++)
	{
		//System.out.println("EventSections - size: "+counter++);
	  sections.add(new ArrayList()); // add an array for each section, including the first, which is a dummy
	}
	//sections.add(new ArrayList()); // add one more for the non-eugene events
	
  }
  
  public void addEvent(EventDb edb)
  {
	 ArrayList singleSection = (ArrayList)sections.get(edb.getType_id()); // get it out
	 singleSection.add(edb);
	 sections.set(edb.getType_id(), singleSection);  //put it back
  }
  
  public ArrayList getSection(int section)
  {
	  return (ArrayList)sections.get(section);
  }
}

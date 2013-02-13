package events;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Security;
import model.Settings;
import model.WebErrorOutput;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.ConnectionManagerImpl;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.util.ObjectModification;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import db.ArticleDb;
import db.EventDb;
import db.MemberDb;

public class EventsAction extends Action
{
	String contextPath="";
	//String singleColor="#E6E6FA";
	String singleColor="#FFE4E1";
	//String classColor="#E0FFFF";
	//String practicaColor="#FFFACD";
	String specialColor="#DDA0DD";
	//String milongaColor="#EE82EE";
	
	String classColor=singleColor;
	String practicaColor=singleColor;
	//String specialColor=singleColor;
	String milongaColor=singleColor;
	static long brokerOpens=0;
	static long brokerCloses=0;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
    { 
      ActionForward list = mapping.findForward("list");   
      ActionForward show = mapping.findForward("show");  
      ActionForward edit = mapping.findForward("edit");
      ActionForward map = mapping.findForward("map");
      ActionForward calendar = mapping.findForward("calendar");
      ActionForward about = mapping.findForward("about");
      ActionForward accessError = mapping.findForward("accessError");
      ActionForward myEvents = mapping.findForward("myEvents");
      ActionForward testConnections = mapping.findForward("testConnections");
      ActionForward unsupportedForward = mapping.findForward("unsupportedForward");
      ActionRedirect actionRedirect=null;
      
      String mode = (String)request.getParameter("mode");
	  //System.out.println("Events - mode: "+mode);
	  String id = (String)request.getParameter("id");
	  if (mode==null) mode="list";
	  Settings settings = new Settings();
	 // server=request.getServerName();
	 // if (server==null) server="remote";
	  
	  String error="";
	  contextPath=request.getContextPath();
	  
	  if ("testConnections".equals(mode))
	  {
		  request.setAttribute("brokerOpens", ""+brokerOpens);
		  request.setAttribute("brokerCloses", ""+brokerCloses);
		  return testConnections;
	  }
	       
	  if ("calendar".equals(mode))
      {
		calendar(request);  
		//PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		//broker.close();
		
		return calendar;
      }
	  
	  if ("list".equals(mode))
      {
		listEvents(request, response);  
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		brokerOpens++;
		//((ConnectionManagerImpl)broker.serviceConnectionManager()).getUnderlyingConnectionFactory().releaseAllResources();
		request.setAttribute("Articles", getArticles(broker, false));
		broker.close();
		brokerCloses++;
		return list;
      }
	  
	  HttpSession session = request.getSession();
	  MemberDb userData = (MemberDb)session.getAttribute("UserData");
	  
	  if  (Security.isOff)
	     if (userData==null) 
	     {
	        userData= new MemberDb();
	        userData.setId(4);  // Security off user
	        userData.setAdmin(0);
	     }
	 	 
	  
	  if ("show".equals(mode))
	  {
	    showEvent(id, request, response);
		return show;
	  }
	  
	  if ("edit".equals(mode))
	  {
		  //TODO check security
		  int tint =0;
		  EventDb edb=null;
  		 try { tint=Integer.parseInt(id); } catch(Exception e) {}
		 
		 
		 try {
			    //edb = EventDb.getEvent(tint, (String)null);
			    edb=getEvent(tint);
			    edb.setMode("update");
			    setEditPrivleges(edb, edb.getMember_id(), request);
			    //edb.outEvent();
		 } catch (Exception e) {  e.printStackTrace(); }
		 
		 setEditPrivleges(edb, edb.getMember_id(), request);
	     request.setAttribute("EventDb", edb);
		 return edit;
	  }
	
	  if ("map".equals(mode))
	  {
		  System.out.println("EventsAction - mode: "+mode);
		  int tint =0;
		  EventDb edb=null;
  		 try { tint=Integer.parseInt(id); } catch(Exception e) {}
		 
		 
		 try {
			   // edb = EventDb.getEvent(tint, (String)null);
			    edb=getEvent(tint);
		 } catch (Exception e) {  e.printStackTrace(); }
		 
	     request.setAttribute("EventDb", edb);
		 return map;
	  }
	  
	
	  // security
	  
	 
	  boolean auth=true;
	  if (userData==null) { auth=false; error="<br>* UserData not found: user not logged in"; }
	  
	  if  (Security.isOff) auth=true;
	 
	  if (!auth)
	  {
		if ("new".equals(mode))
		{
			 String message = "You must be logged in to create a new event";
			 request.setAttribute("Message", message);
			 return about;
		}
		
		System.out.println("Event Access Error: "+error);
		request.setAttribute("ErrorMessage", error);
		return accessError;
	  }
	   
	  if ("myevents".equals(mode))
	    {
			ArrayList events = null;
			try
			{
			  String sql="";
			  if (userData.getAdmin()==1) sql = "select * from event";
			  else
				  sql =  "select * from event where member_id= "+userData.getId();
			  events = EventDb.getEvents((String)null, sql);
			} catch (Exception e)
			{  e.printStackTrace();	}
			request.setAttribute("MyEvents", events);
			return myEvents;
	    }
	  
	    if ("activate".equals(mode))
	    {
	      try
	      {
	        EventDb.activate(id,true, (String)null);
	        showEvent(id, request, response);
	      } catch (Exception e) {  e.printStackTrace(); }
	   		 return show;
	    }
	    
	    if ("deactivate".equals(mode))
	    {
	      try {
	          EventDb.activate(id,false, (String)null);
	          showEvent(id, request, response);
	      } catch (Exception e) {  e.printStackTrace(); }
		 return show;
	    }
	   
	    if ("delete".equals(mode))
	    {
	      try {
	          EventDb.delete(id, (String)null);
	          listEvents(request, response);
	      } catch (Exception e) {  e.printStackTrace(); }
		 return list;
	    }
	 
	  
	  if ("new".equals(mode))
      {
		EventDb edb = new EventDb();  
		edb.setMode("insert");
		edb.setMember_id(userData.getId());
		int type_id=0;
		try { type_id=Integer.parseInt(request.getParameter("type_id")); } catch (Exception e) {}
	    int location_id=0;
	    try { location_id=Integer.parseInt(request.getParameter("location_id")); } catch (Exception e) {}
	    
		edb.setType_id(type_id);
		edb.setLocation_id(location_id);
		edb.setDay_of_week("na");
		String eventTypeName = settings.eventTypesHash.get(""+type_id);
		//System.out.println("Events - eventTypeName: "+eventTypeName);
		Boolean editDates = new Boolean(true);
		if ("Special Event".equals(eventTypeName)||"Class Series".equals(eventTypeName))
		  editDates = new Boolean(true);
		else
		  editDates = new Boolean(false);
			
		request.setAttribute("EditDates", editDates);
		
		request.setAttribute("EventDb", edb);
		
		
		return edit;
      }
	 
	  if ("insert".equals(mode))
	  {
		 EventDb edb = new EventDb(request);
		 edb.setMember_id(userData.getId());
		 edb.setActive(1); // initially visible
		 java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
		 if (edb.getStart_date()==null) edb.setStart_date(today);
		 if (edb.getExpire_date()==null) edb.setStart_date(today);
		// edb.outEvent();
		 int lastEventId=0;
		 try {
			 PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			 brokerOpens++;
			 broker.beginTransaction();
			 broker.store(edb, ObjectModification.INSERT);
			 broker.commitTransaction();
			 broker.close();
			 brokerCloses++;
	    	 lastEventId=EventDb.getLastMemberEventId(userData.getId(), null);
		 } catch (Exception e) {  e.printStackTrace(); }
		// showEvent(""+lastEventId, request, response);
		 actionRedirect = new ActionRedirect(mapping.findForward("showRedirect"));
		 actionRedirect.addParameter("mode", "show");
		 actionRedirect.addParameter("id", lastEventId);
		 return actionRedirect;
	  }
	 
	  if ("update".equals(mode))
	  {
		 EventDb edb = new EventDb(request);
		 PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		 brokerOpens++;
		 broker.beginTransaction();
		 broker.store(edb, ObjectModification.UPDATE);
		 broker.commitTransaction();
		 broker.close();
		 brokerCloses++;
	     request.setAttribute("EventDb", edb);
	     setEditPrivleges(edb, edb.getMember_id(), request);
	     actionRedirect = new ActionRedirect(mapping.findForward("showRedirect"));
		 actionRedirect.addParameter("mode", "show");
		 actionRedirect.addParameter("id", edb.getId());
		 return actionRedirect;	  
	   }

      return unsupportedForward;
    }
	
	private Collection getArticles(PersistenceBroker broker, boolean admin) 
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("active", 1);
		QueryByCriteria query = new QueryByCriteria(ArticleDb.class, criteria);
		query.addOrderByDescending("article_date");
		Collection articles=broker.getCollectionByQuery(query);
		return articles;
	}
	
	private void listEvents(HttpServletRequest request, HttpServletResponse response) 
	{
	  EventSections eventSections = new EventSections();
	  ArrayList events = null;
	  try
	  {
		events = getEvents();
		
		Iterator it = events.iterator();
		EventDb edb = null;
		while(it.hasNext())
		{
		  edb = (EventDb)it.next();
		  if (edb.getLocation_id()==1) // Eugene
		  {
			if (edb.getExpire_date()==null)
				eventSections.addEvent(edb); 
			else
			{	
			   if (edb.getExpire_date().after(new java.util.Date())) eventSections.addEvent(edb);
			 //  else System.out.println("Events - "+edb.getTitle()+" is expired");
			}
		  }
		}
		
		request.setAttribute("EventSections", eventSections);
		} catch (Exception e)
		{  new WebErrorOutput(e,response);  return;	}
	}
	
	private void calendar(HttpServletRequest request) 
	{
	  SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
	  String month=request.getParameter("month");
	  if (month==null) month = df.format(new Date());
	 // System.out.println("NEW month: "+month);
	  String strYear = month.substring(0,4);
	  String strMonth = month.substring(4);
	  int year=0;
	  try { year=Integer.parseInt(strYear); } catch(Exception e) {  }
	  int imonth=0;
	  try { imonth=Integer.parseInt(strMonth); } catch(Exception e) {  }
	 // System.out.println("year: "+year);
	//  System.out.println("month: "+imonth);
	  
	  Calendar calendar = Calendar.getInstance();
	  calendar.set(year, imonth-1, 1);
	  
	  CalendarData calendarData = new CalendarData();
	  calendarData.setStrMonth(getStrMonth(imonth)+" "+year);
	  ArrayList eventArray = new ArrayList();
	  ArrayList thisMonthSpecialArray = new ArrayList();
	  ArrayList events = null;
	  try
	  {
		events = getEvents();
		
		Iterator it = events.iterator();
		EventDb edb = null;
		while(it.hasNext())
		{
		  edb = (EventDb)it.next();
		  
		  if (edb.getLocation_id()==Settings.EUGENE) 
		  {
			if (edb.getType_id()==Settings.SPECIAL_EVENT)
			{
			  eventArray.addAll(specialEvent(edb,  year, imonth));
			  if (eventMonth(edb, imonth, year)) // is this event in the month to be shown?
			  {
				 // System.out.println("SE found");
				  thisMonthSpecialArray.add(edb); 
			  }
			}
			
		    if ((edb.getType_id()==Settings.PRACTICA)
		     || (edb.getType_id()==Settings.MILONGA)
		     || (edb.getType_id()==Settings.CLASS&&edb.getDrop_in()==1)
		  )                                  eventArray.addAll(repeatingEvent(edb, year, imonth));
		   
		   if (edb.getType_id()==Settings.CLASS&&edb.getDrop_in()!=0) 
			                                 eventArray.addAll(classEvent(edb, year, imonth));
		  }
		}
		
		calendarData.setFirstMonth(month);
		calendar.add(Calendar.MONTH, -1);
		calendarData.setPrevMonth(df.format(calendar.getTime()));
		calendar.add(Calendar.MONTH, 2);
		calendarData.setNextMonth(df.format(calendar.getTime()));

		calendarData.setLastMonth(df.format(calendar.getTime()));
		calendarData.setEvents(eventArray);
		calendarData.setThisMonthSpecial(thisMonthSpecialArray);
		request.setAttribute("CalendarData", calendarData);
		} catch (Exception e)
		{  e.printStackTrace();	}
	}

	private String getStrMonth(int imonth) 
	{
		if (imonth==1) return "January";
		if (imonth==2) return "February";
		if (imonth==3) return "March";
		if (imonth==4) return "April";
		if (imonth==5) return "May";
		if (imonth==6) return "June";
		if (imonth==7) return "July";
		if (imonth==8) return "August";
		if (imonth==9) return "September";
		if (imonth==10) return "October";
		if (imonth==11) return "November";
		if (imonth==12) return "December";
		return "Error";
	}

	public  ArrayList repeatingEvent(EventDb edb, int year, int month)
	{
      boolean debug=false;
      if (edb.getId()==8) debug=false;
	  SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
	  String day="";
	  
	  SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
      ArrayList localList= new ArrayList();		
	  
      Calendar calendar = Calendar.getInstance();
      
      calendar.set(year, month-1,  1);
      int currentMonth=calendar.get(Calendar.MONTH);
      int currentYear=calendar.get(Calendar.YEAR);
      
      if (debug) System.out.println("currentMonth = "+currentMonth);
      if (debug) System.out.println("currentYear = "+currentYear);
      if (debug) System.out.println("this event DOW = "+edb.getDay_of_week());
      
	  Date d = null;
	
	  if (debug) System.out.println(edb.getTitle());
	  if (debug) System.out.println("dayOfWeek = "+edb.getDay_of_week());
	  while(true)
	  {
		if (calendar.get(Calendar.MONTH)!=currentMonth) break;
		d=calendar.getTime();
		if (debug) System.out.println("date = "+d);
		day=dayFormat.format(d);
	    if (debug) System.out.println("day = "+day);
		if (day.equalsIgnoreCase(edb.getDay_of_week()))
		{
			if (debug) System.out.println("*** DAY FOUND! ***");
			if (isValidWeek(calendar.get(Calendar.WEEK_OF_MONTH), edb))
			{
			  StringBuffer eventStr = new StringBuffer();
			  eventStr.append("DefineEvent(");
			  String dateStr = df.format(d);
			  eventStr.append(dateStr+",");
			  eventStr.append("\""+edb.getShort_title()+"\",");
			  eventStr.append("\""+contextPath+"/events.do?mode=show&id="+edb.getId()+"\", ");
			  if (edb.getType_id()==Settings.MILONGA) eventStr.append("\""+contextPath+"/images/milonga_icon2.gif\", 25, 35, \""+milongaColor+"\")");
			  if (edb.getType_id()==Settings.PRACTICA) eventStr.append("\""+contextPath+"/images/practica_icon.gif\", 25, 35, \""+practicaColor+"\")");
			  if (edb.getType_id()==Settings.CLASS) eventStr.append("\""+contextPath+"/images/class_icon.gif\", 25, 35, \""+classColor+"\")");
			  if (debug)  System.out.println(eventStr);
			  localList.add(eventStr.toString());
			}
		}
		
		
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		
	  }
	  
	  
	  return localList;
	}
	
	private ArrayList getEventDays(EventDb edb)
	{
	  SimpleDateFormat dfx = new SimpleDateFormat("yyyyMMdd");		
	  ArrayList eventDays = new ArrayList();
	  
	  if (edb.getStart_date()==null) 
	  {
		  System.out.println("EventsAction - startDays is null");
		  return eventDays;
	  }
	  eventDays.add(edb.getStart_date());
	  
	  if (edb.getExpire_date()==null) return eventDays;
	  if (edb.getExpire_date().before(edb.getStart_date())) return eventDays;
	  
	  
	  Calendar calendar = Calendar.getInstance();
	  calendar.setTime(edb.getStart_date());
	  int failsafe=0;
	  
	  String strCalendarDate="";
	  String strExpireDate=dfx.format(edb.getExpire_date());
	  java.sql.Date calendarDate=null;
	  calendarDate=new java.sql.Date(calendar.getTime().getTime());
	  strCalendarDate= dfx.format(calendarDate);
	  if (strExpireDate.equals(strCalendarDate)) return eventDays;
	  
	  while(true)
	  {
		  calendar.add(Calendar.DAY_OF_MONTH, 1);
		  calendarDate=new java.sql.Date(calendar.getTime().getTime());
		  strCalendarDate= dfx.format(calendarDate);
		  if (strExpireDate.equals(strCalendarDate)) break;
          failsafe++;
          if (failsafe==35) break;
          eventDays.add(calendarDate);
	  }
	  
	  return eventDays;
	}
	
	private boolean eventMonth(EventDb edb, int month, int year)
	{
		boolean debug=false;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		String strThisMonth=""+year;
		strThisMonth=strThisMonth+month;
		
		ArrayList eventDays = getEventDays(edb);
		
		Iterator it = eventDays.iterator();
		java.sql.Date eventDate = null;
		String strEventDate="";
		while(it.hasNext())
		{
		  eventDate=(java.sql.Date)it.next();
		  strEventDate = df.format(eventDate);
		  if (strEventDate.equals(strThisMonth)) return true;
		}
		
	
		return false;
	}
	
	private boolean eventDay(EventDb edb, Date thisDate, int eventSequence)
	{
		boolean debug=false;
		//if (edb.getId()==56) debug=true;
		
		Date startDate=edb.getStart_date();
		Date expireDate=edb.getExpire_date();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		Calendar calendar=  Calendar.getInstance();
		calendar.setTime(startDate);
		String strTestDate="";
		String strExpireDate = df.format(expireDate);
		
		String strThisDate=df.format(thisDate);
		
		int failsafe=0;
		while(true)
		{
		  failsafe++;
		  if (failsafe==35) break;
		  
		  strTestDate=df.format(calendar.getTime());
		  if (strTestDate.equals(strExpireDate))
		  {
			 if (debug)  System.out.println("eventDay returning false. strTestDate: "+strTestDate+" strThisDate: "+strThisDate); 
			 return false;
		  }
		  if (strTestDate.equals(strThisDate)) return true;
		  calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		if (debug)  System.out.println("eventDay returning false 2. sequence: "+eventSequence+" thisDate: "+strThisDate+" strTestDate: "+strTestDate); 
		return false;
	}
	
	
	public  ArrayList specialEvent(EventDb edb, int year, int month)
	{
      boolean debug=true;
	  SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
	  String day="";
	  Date d = null;
	  
	  SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
      ArrayList localList= new ArrayList();		
      int failsafeCounter=0;
      
      ArrayList eventDays = getEventDays(edb);
      
      Iterator it = eventDays.iterator();
	  java.sql.Date eventDate = null;
	  String strEventDate="";
	  while(it.hasNext())
	  {
		 eventDate=(java.sql.Date)it.next();
		 //eventSequence++;
		  StringBuffer eventStr = new StringBuffer();
		  eventStr.append("DefineEvent(");
		
			//day=dayFormat.format(d);
		  String dateStr = df.format(eventDate);
		  eventStr.append(dateStr+",");
		  eventStr.append("\""+edb.getShort_title()+"\",");
		  eventStr.append("\""+contextPath+"/events.do?mode=show&id="+edb.getId()+"\", ");
		  if (edb.getType_id()==Settings.CLASS) eventStr.append("\""+contextPath+"/images/class_icon.gif\", 25, 35, \""+classColor+"\")");
		  else eventStr.append("\""+contextPath+"/images/special_icon2.gif\", 30, 41, \""+specialColor+"\")");
		  localList.add(eventStr.toString());
		}
   /*   
      Calendar calendar = Calendar.getInstance();
      
      
      calendar.set(year, month-1,  1);
     // if (!eventMonth(edb, month, year)) return localList;
      
      
      
      int eventSequence=0;
      
	  while(true)
	  { 
		  failsafeCounter++;
		  if (failsafeCounter==35) break;
		  
		  d=calendar.getTime();
		  if (!eventDay(edb, d, eventSequence))
		    { calendar.add(Calendar.DAY_OF_MONTH, 1); continue; }
			
		      eventSequence++;
			  StringBuffer eventStr = new StringBuffer();
			  eventStr.append("DefineEvent(");
			
				day=dayFormat.format(d);
			  String dateStr = df.format(d);
			  eventStr.append(dateStr+",");
			  eventStr.append("\""+edb.getShort_title()+"\",");
			  eventStr.append("\""+contextPath+"/events.do?mode=show&id="+edb.getId()+"\", ");
			  if (edb.getType_id()==Settings.CLASS) eventStr.append("\""+contextPath+"/images/class_icon.gif\", 25, 35, \""+classColor+"\")");
			  else eventStr.append("\""+contextPath+"/images/special_icon2.gif\", 30, 41, \""+specialColor+"\")");
			  localList.add(eventStr.toString());
			  calendar.add(Calendar.DAY_OF_MONTH, 1);
	  }
	*/
	  return localList;
	}
	
	public  ArrayList classEvent(EventDb edb, int year, int month) // a non drop-in class
	{
      boolean debug=false;
	  SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
	  String day="";
	  Date d = null;
	  
	  SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
      ArrayList localList= new ArrayList();		
      int failsafeCounter=0;
      
      if (!eventMonth(edb, month, year)) return localList;
      
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(edb.getStart_date());
      
		 // if (!calendar.getTime().before(edb.getExpire_date())) return null;
	  
			  StringBuffer eventStr = new StringBuffer();
			  eventStr.append("DefineEvent(");
			  d=calendar.getTime();
				if (debug) System.out.println("date = "+d);
				day=dayFormat.format(d);
			  String dateStr = df.format(d);
			  eventStr.append(dateStr+",");
			  eventStr.append("\""+edb.getShort_title()+"\",");
			  eventStr.append("\""+contextPath+"/events.do?mode=show&id="+edb.getId()+"\", ");
			  eventStr.append("\""+contextPath+"/images/class_icon.gif\", 25, 31, \""+classColor+"\")");
			  // System.out.println(eventStr);
			  localList.add(eventStr.toString());
		
	
	  return localList;
	}




	private boolean isValidWeek(int weekInMonth, EventDb edb) 
	{
		//System.out.println("week in month: "+weekInMonth);
		//System.out.println("first: "+edb.getFirst()+" second: "+edb.getSecond()+" third: "+edb.getThird()+" fourth: "+edb.getFourth()+" fifth: "+edb.getFifth());
		//if (true) return true;
	  if (edb.getAll_x()==1) return true;
	  if ((edb.getFirst()==1)&&(weekInMonth==1)) return true;
	  if ((edb.getSecond()==1)&&(weekInMonth==2)) return true;
	  if ((edb.getThird()==1)&&(weekInMonth==3)) return true;
	  if ((edb.getFourth()==1)&&(weekInMonth==4)) return true;
	  if ((edb.getFifth()==1)&&(weekInMonth==5)) return true;
	  return false;
	}

	private void showEvent(String id, HttpServletRequest request,HttpServletResponse response) 
	{
	  	int tint =0;
		  EventDb edb=null;
		  try { tint=Integer.parseInt(id); } catch(Exception e) {}
		  try {   
			 // edb = EventDb.getEvent(tint, (String)null);
			  edb=getEvent(tint);
		 
	  	
		 setEditPrivleges(edb, edb.getMember_id(), request);
	     request.setAttribute("EventDb", edb);
		  } catch (Exception e) {  e.printStackTrace(); return; }
	}

	
	private EventDb getEvent(int id)
	{
      Settings settings = new Settings();
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	  brokerOpens++;
	  EventDb edb=null;
	  Criteria criteria = new Criteria();
	  criteria.addEqualTo("id", id);
	  QueryByCriteria query = new QueryByCriteria(EventDb.class, criteria);
	  edb=(EventDb)broker.getObjectByQuery(query);
	  if (edb!=null);
	     {
	       MemberDb mdb = getMember(broker, edb.getMember_id());
	       if (mdb!=null)
	       {	   
	         edb.setOwner(mdb.getFirst_name()+" "+mdb.getLast_name());
	         edb.setOwner_username(mdb.getUsername());
	         edb.setLocation(settings.areasHash.get(""+edb.getLocation_id()));
	       }
	       edb.setStatus("visible");
		   if (edb.getActive()==0) edb.setStatus("not visible");
		   if (edb.getExpire_date()!=null)
		   if (edb.getExpire_date().before(new java.util.Date())) edb.setStatus("past expiration date");
		   try {
	       edb.setPhotoPresent(isEventPhotoPresent(edb.getId()));
		   } catch(Exception e) { e.printStackTrace();}
	     }
	  broker.close();
	  brokerCloses++;
	  return edb;
	}
	
	private ArrayList getEvents()
	{
		ArrayList events = new ArrayList();
      Settings settings = new Settings();
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	  brokerOpens++;
	  EventDb edb=null;
	  Criteria criteria = new Criteria();
	  criteria.addEqualTo("active", 1);
	  QueryByCriteria query = new QueryByCriteria(EventDb.class, criteria);
	  query.addOrderByAscending("start_date");
	  Collection edbs=(Collection)broker.getCollectionByQuery(query);
	  Iterator it = edbs.iterator();
	  while(it.hasNext())
	  {	 
		  edb = (EventDb)it.next();
	      MemberDb mdb = getMember(broker, edb.getMember_id());
	      if (mdb!=null)
	      {	   
	         edb.setOwner(mdb.getFirst_name()+" "+mdb.getLast_name());
	         edb.setOwner_username(mdb.getUsername());
	         edb.setLocation(settings.areasHash.get(""+edb.getLocation_id()));
	       }
	       edb.setStatus("visible");
		   if (edb.getActive()==0) edb.setStatus("not visible");
		   if (edb.getExpire_date()!=null)
		   if (edb.getExpire_date().before(new java.util.Date())) edb.setStatus("past expiration date");
		   try {
		       edb.setPhotoPresent(isEventPhotoPresent(edb.getId()));
			   } catch(Exception e) { e.printStackTrace();}
		   events.add(edb);
	   }
	    
	 
	  broker.close();
	  brokerCloses++;
	  
	  return events;
	}
	
	public boolean isEventPhotoPresent(int id) throws Exception
	  {
		  Connection con = null;
		  Blob b = null;
		  String db="jdbc:mysql://localhost:3306/etango?autoReconnect=true"; 
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  con = DriverManager.getConnection(db, "rroman", "smegmafish7A");
		  String selectSql ="select photo from event where id = "+id;
		  Statement stmt = con.createStatement(); 
		  ResultSet rs = stmt.executeQuery(selectSql);	
		  
		  if (rs.next()) b = rs.getBlob("photo");
		  rs.close();
		  stmt.close();
		  con.close();
		  if (b==null) return false; 
		  else return true;
	  }
	
	private MemberDb getMember(PersistenceBroker broker, int id)
	{
	  MemberDb mdb=null;
	  Criteria criteria = new Criteria();
	  criteria.addEqualTo("id", id);
	  QueryByCriteria query = new QueryByCriteria(MemberDb.class, criteria);
	  mdb=(MemberDb)broker.getObjectByQuery(query);
	  return mdb;
	}
	
	  private void setEditPrivleges(EventDb edb, int member_id, HttpServletRequest request) 
	  {
		

		Boolean editAllowed=getEventEditAllowed(edb.getMember_id(), request);
		request.setAttribute("EventEditAllowed", editAllowed); 
		Boolean editDates = new Boolean(true);
		String eventTypeName = Settings.eventTypesHash.get(""+edb.getType_id());
		if ((edb.getType_id()==Settings.SPECIAL_EVENT)||(edb.getType_id()==Settings.CLASS))
				  editDates = new Boolean(true);
				else
				  editDates = new Boolean(false);
					
		request.setAttribute("EditDates", editDates);
	  }

	private Boolean getEventEditAllowed(int member_id, HttpServletRequest request) {
		//Boolean test = new Boolean(false);
		//test.
		HttpSession session = request.getSession();
		MemberDb userData = (MemberDb)session.getAttribute("UserData");
		int admin=0;
		int uid=0;
		if (userData!=null)
		{
		  uid=userData.getId();
		  admin=userData.getAdmin();
		}
		if (Security.isOff) new Boolean(true);
		if (admin==1) return new Boolean(true);
		if (uid==member_id) return new Boolean(true);
		return new Boolean(false);
	}
}


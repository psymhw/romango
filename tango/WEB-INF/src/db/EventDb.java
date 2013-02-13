package db;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import model.Settings;

public class EventDb 
{
  private String mode;
  private int id=0;
  private int member_id=0;
  private int location_id=0;
  private int type_id=0;
  private String day_of_week="";
  private int regular;
  private Date start_date;
  private Date last_date;
  private Date entry_date;
  private Date expire_date;
  private String description="";
  private boolean photoPresent;
  private String caption;
  private String photo_type;
  private String title="";
  private String venue_name="";
  private String venue_csz="";
  private String venue_addr="";
  private int active=0;
  private String owner;
  private String location;
  private String owner_username;
  private String status;
  private int first;
  private int second;
  private int third;
 private int fourth;
 private int fifth;
 private int all_x;
 private Blob photo;
 private int drop_in;
 private String short_title;
  
  
  public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getOwner_username() {
	return owner_username;
}

public void setOwner_username(String owner_username) {
	this.owner_username = owner_username;
}

public String getVenue_name() {
	return venue_name;
}

public void setVenue_name(String venue_name) {
	this.venue_name = venue_name;
}

public String getVenue_csz() {
	return venue_csz;
}

public void setVenue_csz(String venue_csz) {
	this.venue_csz = venue_csz;
}

public int getActive() {
	return active;
}

public void setActive(int active) {
	this.active = active;
}


  
  SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  
  
  public EventDb() {}
  
  public EventDb(HttpServletRequest request)
  {
	String strDate;
	java.util.Date jDate;
	java.sql.Date sDate;
	if ("insert".equals(request.getParameter("mode"))) id=0;
	else { try { id=Integer.parseInt(request.getParameter("id")); } catch (Exception e) {} }
	
	title=getSqlString((String)request.getParameter("title"));
	description=getSqlString((String)request.getParameter("description"));
	day_of_week=(String)request.getParameter("day_of_week");
	try { member_id=Integer.parseInt(request.getParameter("member_id")); } catch (Exception e) {}
	try { location_id=Integer.parseInt(request.getParameter("location_id")); } catch (Exception e) {}
	try { type_id=Integer.parseInt(request.getParameter("type_id")); } catch (Exception e) {}
	try { 
		 strDate =(String)(request.getParameter("start_date"));
		// System.out.println(strDate);
		 jDate=df.parse(strDate);
		 sDate = new java.sql.Date(jDate.getTime());
	  	 start_date=sDate;
	} catch (Exception e) {}
		try { 
		 strDate =(String)(request.getParameter("expire_date"));
		 //System.out.println(strDate);
		 jDate=df.parse(strDate);
		 sDate = new java.sql.Date(jDate.getTime());
	  	 expire_date=sDate;
	} catch (Exception e) {}
	jDate = new java.util.Date();
	sDate = new java.sql.Date(jDate.getTime());
	entry_date=(sDate);
	venue_name=getSqlString((String)request.getParameter("venue_name"));
	venue_addr=getSqlString((String)request.getParameter("venue_addr"));
	venue_csz=getSqlString((String)request.getParameter("venue_csz"));
	try { active=Integer.parseInt(request.getParameter("active")); } catch (Exception e) {}
	try { all_x=Integer.parseInt(request.getParameter("all_x")); } catch (Exception e) {}
	try { first=Integer.parseInt(request.getParameter("first")); } catch (Exception e) {}
	try { second=Integer.parseInt(request.getParameter("second")); } catch (Exception e) {}
	try { third=Integer.parseInt(request.getParameter("third")); } catch (Exception e) {}
	try { fourth=Integer.parseInt(request.getParameter("fourth")); } catch (Exception e) {}
	try { fifth=Integer.parseInt(request.getParameter("fifth")); } catch (Exception e) {}
	try { drop_in=Integer.parseInt(request.getParameter("drop_in")); } catch (Exception e) {}
    short_title=request.getParameter("short_title");
  }
  public EventDb(ResultSet rs) throws Exception
  {
	 id=rs.getInt("id"); 
	 member_id=rs.getInt("member_id");  
	 type_id=rs.getInt("type_id"); 
	 location_id=rs.getInt("location_id"); 
	 day_of_week = rs.getString("day_of_week");
	 title = rs.getString("title");
	 description = rs.getString("description");
	 caption = rs.getString("caption");
	 photo_type = rs.getString("photo_type");
	 start_date = rs.getDate("start_date");
	 
	 expire_date = rs.getDate("expire_date");
	 entry_date = rs.getDate("entry_date");
	 Blob b = rs.getBlob("photo");
	 if (b==null) photoPresent=false; else photoPresent=true;
	 venue_name = rs.getString("venue_name");
	 venue_addr = rs.getString("venue_addr");
	 venue_csz = rs.getString("venue_csz");
	 active=rs.getInt("active"); 
   }
  
  public static void setPhoto(byte[] imgBytes, String runLocation, int id, String caption, String photoType) throws Exception
  {
	  MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	  Connection db = mysqlConnection.connect();
	  caption=getSqlString(caption);
	  String sql ="update event set caption='"+caption+"', photo_type='"+photoType+"', photo = ? where id="+id;
	  PreparedStatement stmt = db.prepareStatement(sql);
	    ByteArrayInputStream inStream = new ByteArrayInputStream(imgBytes);
	   
	    stmt.setBinaryStream(1,inStream,inStream.available());
	    inStream.close();
	    stmt.executeUpdate();
	    mysqlConnection.close();
  }
  
  
 /* 
  public static ArrayList getEvents(String runLocation) throws Exception
	{
	  String selectSql ="select * from event where active = 1 order by location_id, type_id, start_date";
	  return getEvents(runLocation, selectSql);
	}
*/
  
  public static ArrayList getEvents(String runLocation, String selectSql) throws Exception
	{
	  EventDb edb = null;
	  ArrayList events = new ArrayList();
	  MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	  Connection db = mysqlConnection.connect();
	  Statement stmt = db.createStatement(); 
	  
	  
		 
	  ResultSet rs = stmt.executeQuery(selectSql);	
	  if (rs != null) 
	  { 
		 while (rs.next()) 
		 { 
		   edb = new EventDb(rs);
		   if (edb!=null);
		     {
		       MemberDb mdb = MemberDb.getMember(edb.getMember_id(), db);
		       if (mdb!=null)
		       {	   
		         edb.setOwner(mdb.getFirst_name()+" "+mdb.getLast_name());
		         edb.setOwner_username(mdb.getUsername());
		         edb.setLocation(Settings.areasHash.get(""+edb.getLocation_id()));
		       }
		     }
		     edb.setStatus("visible");
		     if (edb.getActive()==0) edb.setStatus("not visible");
		     if (edb.getExpire_date()!=null)
		       if (edb.getExpire_date().before(new java.util.Date())) edb.setStatus("past expiration date");
		  // mdb.outMember();
		   events.add(edb);
		 } 
	  }
	  rs.close();
	  stmt.close();
	  
	  mysqlConnection.close();   
	  return events;
	}
  
  /*
  public void insert(String runLocation)  throws Exception
  {
	MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	Connection db = mysqlConnection.connect();
		
	StringBuffer insertSql=new StringBuffer();
	
	insertSql.append("insert into event (member_id, location_id, type_id, day_of_week, title, description, start_date, expire_date, entry_date, venue_name, venue_addr, venue_csz, active) values(");
	insertSql.append(member_id);
	insertSql.append(", ");
	insertSql.append(location_id);
	insertSql.append(", ");
	insertSql.append(type_id);
	insertSql.append(", '");
	insertSql.append(day_of_week);
	insertSql.append("', '");
	insertSql.append(title);
	insertSql.append("', '");
	insertSql.append(description);
	insertSql.append("', ");
	insertSql.append(getDateSql(start_date));
	insertSql.append(", ");
	insertSql.append(getDateSql(expire_date));
	insertSql.append(", ");
	insertSql.append(getDateSql(entry_date));
	insertSql.append(", '");
	insertSql.append(venue_name);
	insertSql.append("', '");
	insertSql.append(venue_addr);
	insertSql.append("', '");
	insertSql.append(venue_csz);
	insertSql.append("', ");
	insertSql.append(active);
	insertSql.append(")");
	//System.out.println("EventDb - insertSql: "+insertSql);
	 Statement stmt=db.createStatement();
	 int rc=stmt.executeUpdate(insertSql.toString());
	 stmt.close();
	 mysqlConnection.close();
  }
  
  public void update(String runLocation)  throws Exception
  {
	MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	Connection db = mysqlConnection.connect();
		
	StringBuffer insertSql=new StringBuffer();
	
	insertSql.append("update event set member_id=");
	insertSql.append(member_id);
	insertSql.append(", ");
	insertSql.append(" location_id=");
    insertSql.append(location_id);
    insertSql.append(", ");
	insertSql.append(" type_id=");
    insertSql.append(type_id);
    insertSql.append(", ");
	insertSql.append(" day_of_week='");
    insertSql.append(day_of_week);
    insertSql.append("', ");
	insertSql.append(" title='");
    insertSql.append(title);
    insertSql.append("', ");
	insertSql.append(" description='");
    insertSql.append(description);
    insertSql.append("', ");
	insertSql.append(" start_date=");
    insertSql.append(getDateSql(start_date));
   
    insertSql.append(", ");
	insertSql.append(" expire_date=");
    insertSql.append(getDateSql(expire_date));
    insertSql.append(", ");
	insertSql.append(" venue_name='");
    insertSql.append(venue_name);
    insertSql.append("', ");
    insertSql.append(" venue_addr='");
    insertSql.append(venue_addr);
    insertSql.append("', ");
	insertSql.append(" venue_csz='");
    insertSql.append(venue_csz);
    insertSql.append("', ");
	insertSql.append(" active=");
    insertSql.append(active);
	insertSql.append(" where id="+id);

	 //System.out.println(" Event update sql: "+insertSql);
	 Statement stmt=db.createStatement();
	 int rc=stmt.executeUpdate(insertSql.toString());
	 stmt.close();
	 mysqlConnection.close();
  }
  */
  public static void activate(String id, boolean turnOn, String runLocation)  throws Exception
  {
	MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	Connection db = mysqlConnection.connect();
		
	StringBuffer insertSql=new StringBuffer();
	
	if (turnOn)
	  insertSql.append("update event set active=1");
	else
		insertSql.append("update event set active=0");
	
	insertSql.append(" where id="+id);

	//System.out.println(" Event activate sql: "+insertSql);
	 Statement stmt=db.createStatement();
	 int rc=stmt.executeUpdate(insertSql.toString());
	// System.out.println(" Event activate result: "+rc);
	 stmt.close();
	 mysqlConnection.close();
  }
  public static void delete(String id, String runLocation)  throws Exception
  {
	MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	Connection db = mysqlConnection.connect();
		
	StringBuffer sql=new StringBuffer();
	
	sql.append("delete from event");
	
	sql.append(" where id="+id);

	 Statement stmt=db.createStatement();
	 int rc=stmt.executeUpdate(sql.toString());
	 stmt.close();
	 mysqlConnection.close();
  }
  
  private String getDateSql(Date d) 
  {
	if (d==null) return "null";
	return "'"+d.toString()+"'";
}

public static int getLastMemberEventId(int memb_id, String runLocation) throws Exception
  {
	 int id=0;
	 MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
		Connection db = mysqlConnection.connect();
     Statement stmt = db.createStatement();    
     ResultSet rs = stmt.executeQuery("select max(id) as currval from event where member_id ="
                 +memb_id);
     if (rs != null) { if (rs.next()) { id=rs.getInt("currval"); } }
      rs.close();
      stmt.close();
	 return id;
  }
  
/*
  public static EventDb getEvent(int id, String runLocation) throws Exception
  {
	  Settings settings = new Settings();
	 EventDb edb= null;
	 MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	 Connection db = mysqlConnection.connect();
     edb=getEvent(id, db);
     
     if (edb!=null);
     {
       MemberDb mdb = MemberDb.getMember(edb.getMember_id(), db);
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
 
     }
     
     mysqlConnection.close(); 
	 return edb;
  }
  
  public static EventDb getEvent(int id, Connection db) throws Exception
  {
	 EventDb edb= null;
	 
     Statement stmt = db.createStatement();    
     ResultSet rs = stmt.executeQuery("select * from event where id ="
                 +id);
     if (rs != null) 
     {
    	 if (rs.next()) 
		 { 
    	   edb = new EventDb(rs); 
		 }
      }
      rs.close();
      stmt.close();
     
	 return edb;
  }
  */
  
  public void outEvent()
  {
	System.out.println("EventDb record:");
	System.out.println("mode: "+mode);
	System.out.println("id: "+id);
	System.out.println("member_id: "+member_id);
	System.out.println("location_id: "+location_id);
	System.out.println("type_id: "+type_id);
	System.out.println("day_of_week: "+day_of_week);
	System.out.println("start_date: "+getStart_date_formatted());
	System.out.println("last_date: "+getLast_date_formatted());
	System.out.println("expire_date: "+getExpire_date_formatted());
	System.out.println("entry_date: "+entry_date);
	System.out.println("title: "+title);
	System.out.println("description: "+description);
	System.out.println("venue_name: "+venue_name);
	System.out.println("venue_addr: "+venue_name);
	System.out.println("venue_csz: "+venue_csz);
	System.out.println("active: "+active);
  }
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getMember_id() {
	return member_id;
}
public void setMember_id(int member_id) {
	this.member_id = member_id;
}
public int getLocation_id() {
	return location_id;
}
public void setLocation_id(int location_id) {
	this.location_id = location_id;
}
public int getType_id() {
	return type_id;
}
public void setType_id(int type_id) {
	this.type_id = type_id;
}
public String getDay_of_week() {
	return day_of_week;
}
public void setDay_of_week(String day_of_week) {
	this.day_of_week = day_of_week;
}
public int getRegular() {
	return regular;
}
public void setRegular(int regular) {
	this.regular = regular;
}
public Date getStart_date() {
	
	return start_date;
}
public String getStart_date_formatted() {
	if (start_date==null) return "";
	else return df.format(start_date);
}
public void setStart_date(Date start_date) {
	this.start_date = start_date;
}
public Date getLast_date() {
	return last_date;
}
public String getLast_date_formatted() {
	if (last_date==null) return "";
	else return df.format(last_date);
}
public void setLast_date(Date last_date) {
	this.last_date = last_date;
}
public Date getEntry_date() {
	return entry_date;
}
public void setEntry_date(Date entry_date) {
	this.entry_date = entry_date;
}
public Date getExpire_date() {
	return expire_date;
}
public String getExpire_date_formatted() {
	if (expire_date==null) return "";
	else return df.format(expire_date);
}
public void setExpire_date(Date expire_date) {
	this.expire_date = expire_date;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public boolean isPhotoPresent() {
	return photoPresent;
}
public void setPhotoPresent(boolean photoPresent) {
	this.photoPresent = photoPresent;
}
public String getCaption() {
	return caption;
}
public void setCaption(String caption) {
	this.caption = caption;
}

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getMode() {
	return mode;
}
public void setMode(String mode) {
	this.mode = mode;
}



public String getLocation() {
	return location;
}

public void setLocation(String location) {
	this.location = location;
}

public String getOwner() {
	return owner;
}

public void setOwner(String owner) {
	this.owner = owner;
}

public String getPhoto_type() {
	return photo_type;
}

public void setPhoto_type(String photo_type) {
	this.photo_type = photo_type;
}

public String getFullDay_of_week()
{
	if (day_of_week==null) return null;
	if ("mon".equals(day_of_week)) return "Mondays";
	if ("tue".equals(day_of_week)) return "Tuesdays";
	if ("wed".equals(day_of_week)) return "Wenesdays";
			
	if ("thu".equals(day_of_week)) return "Thursdays";
	if ("fri".equals(day_of_week)) return "Fridays";
	if ("sat".equals(day_of_week)) return "Saturdays";
	if ("sun".equals(day_of_week)) return "Sundays";
	if ("na".equals(day_of_week)) return "Not Applicable";
	return "unknown";
}

public String getVenue_addr() {
	return venue_addr;
}

public void setVenue_addr(String venue_addr) {
	this.venue_addr = venue_addr;
}
private static String getSqlString(String inStr)
{
	if (inStr==null) return null;
	return inStr.replace("'", "\'");
}

public int getFirst() {
	return first;
}

public void setFirst(int first) {
	this.first = first;
}

public int getSecond() {
	return second;
}

public void setSecond(int second) {
	this.second = second;
}

public int getThird() {
	return third;
}

public void setThird(int third) {
	this.third = third;
}

public int getFourth() {
	return fourth;
}

public void setFourth(int fourth) {
	this.fourth = fourth;
}

public int getFifth() {
	return fifth;
}

public void setFifth(int fifth) {
	this.fifth = fifth;
}

public int getAll_x() {
	return all_x;
}

public void setAll_x(int all_x) {
	this.all_x = all_x;
}

public SimpleDateFormat getDf() {
	return df;
}

public void setDf(SimpleDateFormat df) {
	this.df = df;
}

public Blob getPhoto() {
	return photo;
}

public void setPhoto(Blob photo) {
	this.photo = photo;
}

public int getDrop_in() {
	return drop_in;
}

public void setDrop_in(int drop_in) {
	this.drop_in = drop_in;
}

public String getShort_title() {
	if (short_title==null) 
	{
		int len = title.length();
		return title.substring(0, len);
	}
	if ("".equals(short_title)) 
	{	
		int len = title.length();
		return title.substring(0, len);
	}
	return short_title;
}

public void setShort_title(String short_title) {
	if (short_title.length()<45)
	this.short_title = short_title;
	else this.short_title = short_title.substring(0,44);
}


  
}

package db;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.struts.util.LabelValueBean;

import model.Settings;

public class MemberDb 
{
  private int id=0;
  private String last_name="";
  private String first_name="";
  private String email="";
  private int location_id=0;
  private int instructor=0;
  private String password="";
  private int host=0;
  private String description="";
  private String mode;
  private int admin=0;
  private int active=0;
  private String username="";
  private String caption="";
  private boolean photoPresent;
  private String photoType;
  
  public MemberDb() {}
  
  public static void setPhoto(byte[] imgBytes, String runLocation, int id, String caption, String photoType) throws Exception
  {
	  MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	  Connection db = mysqlConnection.connect();
	  caption=getSqlString(caption);
	  String sql ="update member set caption='"+caption+"', photoType='"+photoType+"', photo = ? where id="+id;
	  PreparedStatement stmt = db.prepareStatement(sql);
	    ByteArrayInputStream inStream = new ByteArrayInputStream(imgBytes);
	   
	    stmt.setBinaryStream(1,inStream,inStream.available());
	    inStream.close();
	    stmt.executeUpdate();
	    mysqlConnection.close();
  }
  
  public static MemberDb getMember(String username, String runLocation) throws Exception
  {
	  MemberDb mdb = null;
	  MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	  Connection db = mysqlConnection.connect();
	  Statement stmt = db.createStatement(); 
	  String selectString ="select * from member where username='"+username+"'";
	  
		 
	  ResultSet rs = stmt.executeQuery(selectString);	
	  if (rs != null) 
	  { 
		 if (rs.next()) 
		 { 
		   mdb = new MemberDb(rs);
		  //mdb.outMember();
		  
		 } 
	  }
	  rs.close();
	  stmt.close();
	  
	  mysqlConnection.close();   
	  return mdb;
  }
  
  public static MemberDb getMember(int id, Connection db) throws Exception
  {
	MemberDb mdb = null;
	Statement stmt = db.createStatement(); 
	String selectString ="select * from member where id="+id;
		 
	ResultSet rs = stmt.executeQuery(selectString);	
	if (rs != null) { if (rs.next()) mdb = new MemberDb(rs); }
	rs.close();
	stmt.close();
	return mdb;
  }
  
  public static MemberDb getMember(int id, String runLocation) throws Exception
  {
	MemberDb mdb = null;
	MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	Connection db = mysqlConnection.connect();
	mdb=getMember(id, db);
	mysqlConnection.close();   
	return mdb;
  }
  
  public MemberDb(HttpServletRequest request)
  {
	if ("insert".equals(request.getParameter("mode"))) id=0;
	else 
	{
	  try { id=Integer.parseInt(request.getParameter("id")); } catch (Exception e) {}
	}
	username=request.getParameter("username");
	last_name=request.getParameter("last_name");
	first_name=request.getParameter("first_name");
	email=request.getParameter("email");
	try { location_id=Integer.parseInt(request.getParameter("location_id")); } catch (Exception e) {}
	password=request.getParameter("password");
	description=getSqlString(request.getParameter("description"));
	
	try { host=Integer.parseInt(request.getParameter("host")); } catch (Exception e) {}
	try { admin=Integer.parseInt(request.getParameter("admin")); } catch (Exception e) {}
	try { instructor=Integer.parseInt(request.getParameter("instructor")); } catch (Exception e) {}
	try { active=Integer.parseInt(request.getParameter("active")); } catch (Exception e) {}
  }
	public MemberDb(ResultSet rs) throws Exception
	{
	  id=rs.getInt("id"); 
	  username = rs.getString("username");
	  first_name = rs.getString("first_name");
	  last_name = rs.getString("last_name");
	  email = rs.getString("email");
	  location_id=rs.getInt("location_id");
	  host=rs.getInt("host");
	  admin=rs.getInt("admin");
	  active=rs.getInt("active");
	  instructor=rs.getInt("instructor");
	  password = rs.getString("password");
	  description = rs.getString("description");
	  caption = rs.getString("caption");
	  Blob b = rs.getBlob("photo");
	  if (b==null) photoPresent=false; else photoPresent=true;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	public String getInstructorChecked() { if (instructor==1) return "checked";
		return "";
	}
	public void setInstructor(int instructor) {
		this.instructor = instructor;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHostChecked() { if (host==1) return "checked";
		return "";
	}
	public void setHost(int host) {
		this.host = host;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getAdminChecked() { if (admin==1) return "checked";
	else return "";
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public int getInstructor() {
		return instructor;
	}
	public int getHost() {
		return host;
	}
	public int getAdmin() {
		return admin;
	}
	
	public void insert(String runLocation) throws Exception
	{
		MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
		Connection db = mysqlConnection.connect();
		
		StringBuffer insertSql=new StringBuffer();
	    insertSql.append("insert into member (username, first_name, last_name, email, password, location_id, admin, host, instructor, description, active) values('");
	    insertSql.append(username);
	    insertSql.append("', '");
	    insertSql.append(first_name);
	    insertSql.append("', '");
	    insertSql.append(last_name);
	    insertSql.append("', '");
	    insertSql.append(email);
	    insertSql.append("', '");
	    insertSql.append(password);
	    insertSql.append("', ");
	    insertSql.append(location_id);
	    insertSql.append(", ");
	    insertSql.append(admin);
	    insertSql.append(", ");
	    insertSql.append(host);
	    insertSql.append(", ");
	    insertSql.append(instructor);
	    insertSql.append(", '");
	    insertSql.append(description);
	    insertSql.append("', ");
	    insertSql.append(active);
	    insertSql.append(")");
	    Statement stmt=db.createStatement();
	    int rc=stmt.executeUpdate(insertSql.toString());
	    stmt.close();
	    mysqlConnection.close();
	}
	
	public void update(String runLocation) throws Exception
	{
		MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
		Connection db = mysqlConnection.connect();
		
		StringBuffer insertSql=new StringBuffer();
	    insertSql.append("update member set ");
	    insertSql.append("username='");
	    insertSql.append(username);
	    insertSql.append("', ");
	    insertSql.append("first_name='");
	    insertSql.append(first_name);
	    insertSql.append("', ");
	    insertSql.append("last_name='");
	    insertSql.append(last_name);
	    insertSql.append("', ");
	    insertSql.append("email='");
	    insertSql.append(email);
	    insertSql.append("', ");
	    insertSql.append("password='");
	    insertSql.append(password);
	    insertSql.append("', ");
	    insertSql.append("description='");
	    insertSql.append(description);
	    insertSql.append("', ");
	    insertSql.append("admin=");
	    insertSql.append(admin);
	    insertSql.append(", ");
	    insertSql.append("host=");
	    insertSql.append(host);
	    insertSql.append(", ");
	    insertSql.append("instructor=");
	    insertSql.append(instructor);
	    insertSql.append(", ");
	    insertSql.append("caption='");
	    insertSql.append(caption);
	    insertSql.append("', ");
	    insertSql.append("active=");
	    insertSql.append(active);
	    insertSql.append(", ");
	    insertSql.append("location_id=");
	    insertSql.append(location_id);
	    insertSql.append(" where id="+id);
	    
	   // System.out.println("MemberDb - update SQL: "+insertSql.toString());
	    Statement stmt=db.createStatement();
	    int rc=stmt.executeUpdate(insertSql.toString());
	    stmt.close();
	    mysqlConnection.close();
	}
	
	public static ArrayList getMembers(String whereClause, String runLocation) throws Exception
	{
	  MemberDb mdb = null;
	  ArrayList members = new ArrayList();
	  MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	  Connection db = mysqlConnection.connect();
	  Statement stmt = db.createStatement(); 
	  String selectString ="select * from member";
	  if (whereClause!=null)
	  {
	  	if (whereClause.length()>0)
	   	{
	      selectString+=" where "+whereClause;
	   	}
	  }
		 
	  ResultSet rs = stmt.executeQuery(selectString);	
	  if (rs != null) 
	  { 
		 while (rs.next()) 
		 { 
		   mdb = new MemberDb(rs);
		  // mdb.outMember();
		   members.add(mdb);
		 } 
	  }
	  rs.close();
	  stmt.close();
	  
	  mysqlConnection.close();   
	  return members;
	}
	
	public void outMember()
	{
	  System.out.println("id: "+id);
	  System.out.println("username: "+username);
  	  System.out.println("first_name: "+first_name);
  	  System.out.println("last_name: "+last_name);
  	  System.out.println("email: "+email);
  	  System.out.println("location_id: "+location_id);
  	  System.out.println("password: "+password);
  	  System.out.println("description: "+description);
  	  System.out.println("admin: "+admin);
  	  System.out.println("host: "+host);
  	  System.out.println("active: "+active);
  	  System.out.println("instructor: "+instructor);
	}

	public int getActive() {
		return active;
	}
	
	public String getActiveChecked() { if (active==1) return "checked";
		return "";
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getLocationText()
	{
		String StrId=""+location_id;
		Settings settings = new Settings();
		ArrayList areas = settings.areas;
		java.util.Iterator it = areas.iterator();
		LabelValueBean lvb=null;
		while(it.hasNext())
		{
		  lvb=(LabelValueBean)it.next();
		  if (lvb.getValue().equals(StrId)) return lvb.getLabel();
		}
		return "Not Found";
		
		
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public boolean isPhotoPresent() {
		return photoPresent;
	}

	public void setPhotoPresent(boolean photoPresent) {
		this.photoPresent = photoPresent;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}
	
	private static String getSqlString(String inStr)
	{
		if (inStr==null) return null;
		return inStr.replace("'", "\\'");
	}
}

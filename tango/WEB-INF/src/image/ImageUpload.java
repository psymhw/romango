package image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Security;
import model.Settings;
import model.WebErrorOutput;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import db.EventDb;
import db.MemberDb;

public class ImageUpload extends HttpServlet 
{
	public void service(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException
    {
		 try {
			  String message="";
			 
	  String server=request.getServerName();
	  message+="Server: "+server;
	  if (server==null) server="remote";
	  List fileItemsList=null;
	  String mode = "";
	  String strId = "";
	  String caption = "";
	  String filename="";
	

	  DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
	  message+="DiskFileItemFactory created<br>";
	  File repositoryPath = new File("/tmp");
	  message+="Temp dir set<br>";
	  
	  diskFileItemFactory.setRepository(repositoryPath);
	  message+="Temp dir set<br>";
	  ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
	  message+="Servlet file upload creaed<br>";
	  fileItemsList = servletFileUpload.parseRequest(request);
	  message+="fileItemList created<br>";
  
	  Iterator it = fileItemsList.iterator();
	  int count=1;
	  byte[] imgBytes=null;
	  while (it.hasNext())
	  {
	    FileItem fileItem = (FileItem)it.next();
  	    if (fileItem.isFormField())
  	    {
		  if ("mode".equals(fileItem.getFieldName())) mode=fileItem.getString();
		  if ("caption".equals(fileItem.getFieldName())) caption=fileItem.getString();
		  if ("id".equals(fileItem.getFieldName())) strId=fileItem.getString();
		  message+="form field found<br>";
  	    }
	    else
	    {
		  imgBytes =fileItem.get();
		  message+="Image bytes found<br>";
		  if (imgBytes==null)   message+="imgBytes is null<br>";
		  else   message+="imgBytes size = "+imgBytes.length+"<br>";
		  filename=fileItem.getName();
	    }
	  }
  	    int id = 0;
  	    try {id=Integer.parseInt(strId); } catch (Exception e){}
  	    HttpSession session = request.getSession();
	    String error="";
	    MemberDb userData = (MemberDb)session.getAttribute("UserData");
	    
	  
	          error=authorizationCheck(userData, mode, id, server);
	  
	    boolean auth=true;
	    if (error!=null) auth=false;
	    
	    error=validImageCheck(imgBytes, filename, error);
	    boolean validImage=true;
	    if (error!=null) validImage=false;
	      
	    message+="Auth: "+auth+"<br>";
	    message+="validImage: "+validImage+"<br>";
	    if (auth&&validImage)
	    {
		
		    String photoType=filename.substring(filename.indexOf('.')+1);
		    if ("adminMember".equals(mode)||"instructor".equals(mode)) MemberDb.setPhoto(imgBytes, server, id, caption, photoType);
		    if ("event".equals(mode)) EventDb.setPhoto(imgBytes, server, id, caption, photoType);
		    message+="photo set<br>";
		    
		
		  String nextJSP = "";
		 
		  if ("adminMember".equals(mode)) nextJSP = "/admin/members.do?mode=show&id="+id;
		  if ("instructor".equals(mode)) nextJSP = "/instructors.do?mode=list";
		  if ("event".equals(mode)) nextJSP = "/events.do?mode=show&id="+id;
		  RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		  dispatcher.forward(request,response);
		  return;
	      }
	      else
    	  {
    		response.setContentType("text/html");
    	    ServletOutputStream out = response.getOutputStream();
    	    response.setContentType("text/html");
    	    out.println("Access Denied"+error);
    	    return;
    	 }
		  }
		 catch (Exception e) 
		    {
		      response.setContentType("text/html");
	    	  ServletOutputStream out = response.getOutputStream();

	    	  response.setContentType("text/html");
	    	  new WebErrorOutput(e, out);
	    	  return;
		    }
    }

	private String validImageCheck(byte[] imgBytes, String filename, String error) 
	{
	  String validError="";
		if (imgBytes==null) validError="<br>image not found";
	  
		if (imgBytes.length>1000000) validError+="<br>Picture too large. Limit 1MB";
		
		if (imgBytes.length<1) validError+="<br>Picture too large. Limit 1MB";
		
		if (filename==null)
			validError+="<br>invalid filename";
		else
		{	
    	filename=filename.toLowerCase();
    	if (!filename.endsWith(".jpg")) 
    		if (!filename.endsWith(".jpeg")) 
    		   validError+="<br>Sorry, only JPG files allowed";
		}
    	
        if (error==null)
        {
        	if (validError.length()>0) error=validError;
        }
        else error+=validError;
		return error;
	}

	private String authorizationCheck(MemberDb userData, String mode, int id, String server) throws Exception
	{	
	  if (Security.isOff) return null;
	  String error="";
	  if (userData==null) return "not logged in";
	  if ("adminMember".equals(mode)||"instructor".equals(mode))
	  {
		if (userData.getAdmin()!=1) // not an admin
		  if (userData.getId()!=id)  error+="<br>not an admin or authorized user to record to be changed";
	  }
	  if ("event".equals(mode))
	  {
		 //EventDb edb = EventDb.getEvent(id, server); 
		 EventDb edb = getEvent(id);
		 if (edb.getMember_id()!=userData.getId()) 
		   if (userData.getAdmin()!=1)
			 error+="<br>you are not an admin or owner  of this event";
	  }
	  if (error.length()==0) return null;
	  return error;
	}
	
	private EventDb getEvent(int id)
	{
      Settings settings = new Settings();
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
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
	 
	     }
	  broker.close();
	  return edb;
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
}
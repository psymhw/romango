package serv;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.ActionForward;

import db.PasswordDb;



public class DbImageReader extends HttpServlet 
{
 	private static final long serialVersionUID = 1L;
 
	ServletOutputStream out;
	boolean debug=false;
	
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
	    
	    HttpSession session = request.getSession();
	    String user = (String)session.getAttribute("user");
	    if (user==null) return;
	    //if (!getUser().equals(user)) { return; } // whay am I doing this???

     try
     {
       String id = request.getParameter("id");
       if (id==null) id="1";
			 new DbImageReaderMain(id, request, response); 
			
     } catch (Exception e) { e.printStackTrace();  }
   }
  String getUser()
  {
	  PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
	  Criteria criteria = new Criteria();
      criteria.addEqualTo("id", 1);
      QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
      PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
      broker.close();
      return pdb.getUser();
  }
   
	 
}

class DbImageReaderMain
{
	HttpServletRequest request;
	HttpServletResponse response;
	private String mode;
	ServletOutputStream out;
	boolean debug=false;
	
	 
	public DbImageReaderMain (String id, HttpServletRequest request, HttpServletResponse response) 
	{
		this.request=request;
		this.response=response;
		mode = request.getParameter("mode");
		if (mode==null) mode = "list";
		response.setContentType("text/html");
    try 
    { 
      out = response.getOutputStream(); 
      sendUnchangedFile(id);
		// out.println("<font size=2>Test</font>");
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	 
  void sendUnchangedFile(String id) throws Exception
   {
     try 
     {
       PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
       
       Connection db = null;
       //db = broker.serviceConnectionManager().getConnection();
       db = connect();
       String sql = "select image from image_ref where id = '"+id+"'";
       ResultSet rs = db.createStatement().executeQuery(sql);
       // if record found process blob
       if (rs.next()) {
         // get blob
         Blob image = rs.getBlob("image");
          // setup the streams to process blob
         InputStream input = image.getBinaryStream();
         ByteArrayOutputStream output = new ByteArrayOutputStream();
         // set read buffer size
         byte[] rb = new byte[1024];
         int ch = 0;
          // process blob
         while ((ch=input.read(rb)) != -1) {
           output.write(rb, 0, ch);
         }
         // transfer to byte buffer
         byte[] b = output.toByteArray();
         input.close();
         output.close();
      writeImageToStream(b);
        db.close();
       }
      
       
     } catch (Exception e) 
      { System.out.println("DbImageReader - sendUnchangedCachedFile - writeImageToStream interupted?"); }
     return;
   }
  
  private void writeImageToStream(byte[] imgBytes)
  {
    response.reset();
    response.setHeader("Content-Disposition", "inline; filename=test.jpg");
    response.setContentType("image/jpeg");
    DataOutput dataOutput = new DataOutputStream(out);
      
      response.setContentLength(imgBytes.length);
     long bytesOut=0;
     try
     {
      for(int i = 0; i < imgBytes.length; i++)
      { dataOutput.writeByte(imgBytes[i]); bytesOut++;}
        
     } catch (IOException e)  
     {  
       System.out.println("DbImageReader - writeImageToStream ClientAbort"); 
      //e.printStackTrace();
     }
     response.setContentType("text/html");
   }
  
  public Connection connect() throws Exception
  {  
	  Connection con = null;
	  String db="jdbc:mysql://localhost:3306/rickdb?autoReconnect=true";
	//  if ("localhost".equals(runLocation)) db="jdbc:mysql://localhost/etango";
	//  else db="jdbc:mysql://etango.db.4163272.hostedresource.com/etango";
    
		   Class.forName("com.mysql.jdbc.Driver").newInstance();
		//    con = DriverManager.getConnection(db, "etango", "tangX123");
		   con = DriverManager.getConnection(db, "rroman", "smegmafish7A");
		    return con;
		 
       
	  }
  
  
	
}


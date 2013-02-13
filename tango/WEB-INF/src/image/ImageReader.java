package image;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.WebErrorOutput;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import db.MysqlConnection;

public class ImageReader extends HttpServlet  
{
	private static final long serialVersionUID = 8957898213854288631L;

	public void service(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException
    {
      //System.out.println("ImageReader");
	  String server=request.getServerName();
	  if (server==null) server="remote";
	  HttpSession session = request.getSession();
	  
	  String mode = (String)request.getParameter("mode");
	  String id = (String)request.getParameter("id");
	  response.setContentType("text/html");
	  ServletOutputStream out = response.getOutputStream();
	  try
	  {
	    sendUnchangedFile(mode, id, response, out, server);
	  } catch (Exception e) {
		  new WebErrorOutput(e, out);
  	      return;
	  }
	  
    }
	
	void sendUnchangedFile(String mode, String id, HttpServletResponse response, ServletOutputStream out, String runLocation) throws Exception
	   {
	     
	       MysqlConnection mysqlConnection = new MysqlConnection(runLocation);
	       
	       Connection db = mysqlConnection.connect();
	      
	       String sql = "select photo from "+mode+" where id = '"+id+"'";
	       ResultSet rs = db.createStatement().executeQuery(sql);
	       // if record found process blob
	       if (rs.next()) {
	         // get blob
	         Blob image = rs.getBlob("photo");
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
	      writeImageToStream(b, response, out);
	       }
	       mysqlConnection.close();
	       
	     
	     return;
	   }
	  
	  private void writeImageToStream(byte[] imgBytes, HttpServletResponse response, ServletOutputStream out)
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
		
}

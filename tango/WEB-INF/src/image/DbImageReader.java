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

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;



public class DbImageReader extends HttpServlet 
{
 	private static final long serialVersionUID = 1L;
 
	ServletOutputStream out;
	boolean debug=false;
	
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
	 
     try
     {
       String id = request.getParameter("id");
       if (id==null) id="1";
			 new DbImageReaderMain(id, request, response); 
			
     } catch (Exception e) { e.printStackTrace();  }
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
       db = broker.serviceConnectionManager().getConnection();
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
       }
       broker.close();
       
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
	
}


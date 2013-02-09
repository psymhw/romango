package serv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;



public class Test extends HttpServlet 
{
 	private static final long serialVersionUID = 1L;
  private HttpServletRequest request;
	private HttpServletResponse response;
	private String mode = "";
	ServletOutputStream out;
	boolean debug=false;
	
  public void service(HttpServletRequest request, HttpServletResponse response) 
         throws IOException, ServletException
  {
	 
     try
     {
			 new CacheCheckMain(request, response); 
			
     } catch (Exception e) { e.printStackTrace();  }
   }
  
   
	 
}

class CacheCheckMain
{
	HttpServletRequest request;
	HttpServletResponse response;
	private String mode;
	ServletOutputStream out;
	boolean debug=false;
	String presizeRoot = "/archive/presized_images/archive/collection/";
	String archiveRoot = "/archive/mff_archive/archive/collection/";
	
	 
	public CacheCheckMain (HttpServletRequest request, HttpServletResponse response) 
	{
		this.request=request;
		this.response=response;
		mode = request.getParameter("mode");
		if (mode==null) mode = "list";
		response.setContentType("text/html");
    try 
    { 
      out = response.getOutputStream(); 
      sendUnchangedFile("c:\\temp\\test.jpg");
		// out.println("<font size=2>Test</font>");
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	 
  void sendUnchangedFile(String cachedFilePath) throws Exception
   {
     String extension;
     extension= ".jpg";
     
     byte[] imgBytes=null;
     
     try 
     {
       /*
       File f = new File(cachedFilePath);
       FileInputStream fis = new FileInputStream(f);
       imgBytes = new byte[(int)f.length()];
       fis.read(imgBytes);
       fis.close();
       PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
       
       Connection db = null;
       db = broker.serviceConnectionManager().getConnection();
       String sql = "insert into image_ref (description, image) values('test image',?)";
       PreparedStatement stmt = db.prepareStatement(sql);
       ByteArrayInputStream inStream = new ByteArrayInputStream(imgBytes);
       stmt.setBinaryStream(1,inStream,inStream.available());
//     execute statement
       stmt.executeUpdate();
       
       broker.close();
       */
       
       if (debug) System.out.println("PageImage - using cached file: "+cachedFilePath);
     } catch (Exception e) 
    { e.printStackTrace(); }
     
     try 
     {
       //writeImageToStream(imgBytes);
       PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
       
       Connection db = null;
       db = broker.serviceConnectionManager().getConnection();
       String sql = "select image from image_ref where id = '2'";
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
      { System.out.println("PageImage - sendUnchangedCachedFile - writeImageToStream interupted?"); }
     return;
   }
  
  private void writeImageToStream(byte[] imgBytes)
   {
     //if (debug6) System.out.println("PageImage - writeImageToStream "+fileRoot+"."+ext); 
      
     
     
     response.reset();
    response.setHeader("Content-Disposition", "inline; filename=test.jpg");
    response.setContentType("image/jpeg");
    DataOutput dataOutput = new DataOutputStream(out);
      
      response.setContentLength(imgBytes.length);
     //if (debug6) System.out.println("PageImage - writeImageToStream file size:"+imgBytes.length); 
     long bytesOut=0;
     try
     {
      for(int i = 0; i < imgBytes.length; i++)
      { dataOutput.writeByte(imgBytes[i]); bytesOut++;}
      // if (debug6) System.out.println("PageImage - writeImageToStream bytes written:"+bytesOut); 
        
     } catch (IOException e)  
     {  
       System.out.println("PageImage - writeImageToStream ClientAbort"); 
      //e.printStackTrace();
     }
     response.setContentType("text/html");
   }
	
}


package servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfServlet extends HttpServlet
{
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	response.setContentType("application/pdf");
	System.out.println("Pdf Servlet *");
	try
	{
      Document document = new Document();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, response.getOutputStream());
      document.open();
      document.add(new Paragraph("Hello Bruno"));
      document.add(new Paragraph(new Date().toString()));
      document.close();
      
   // setting some response headers
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control",
          "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      // setting the content type
      response.setContentType("application/pdf");
      // the contentlength
      response.setContentLength(baos.size());
      // write ByteArrayOutputStream to the ServletOutputStream
      OutputStream os = response.getOutputStream();
      baos.writeTo(os);
      os.flush();
      os.close();
      
      System.out.println("Pdf Servlet");
	} catch(Exception de) {
		de.printStackTrace();
		//throw new IOException(de.getMessage()); 
		}
	
  }
}

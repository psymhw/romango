package servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import services.Services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import data.Product;

public class PdfServlet extends HttpServlet
{
  BaseFont bf =null;
  Font font = null;
  Font titlePageRegFont;
  Font bold;
  protected Services services = new Services();
  HttpServletRequest request;
 
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String mode=request.getParameter("mode");
    String item=request.getParameter("item");
    this.request=request;
    Product p = (Product)request.getAttribute("Product");
    
    if (p==null) System.out.println("product is null");
    
    System.out.println("mode: "+mode);
	try
	{
	  setFonts();
      Document document = new Document(PageSize.LETTER);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
      pdfWriter.setPdfVersion(PdfWriter.VERSION_1_7);
      
      document.open();
      if ("PIS".equals(mode))
      {
        writePIS(p, document);
      }
      else
      {
        document.add(new Paragraph("Hello Bruno", titlePageRegFont));
        document.add(new Paragraph(new Date().toString(), font));
      }
      document.close();
     // pdfWriter.close();
      
   // setting some response headers
      response.setHeader("Content-Disposition", "inline; filename=hello.pdf");
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setContentType("application/pdf");
      response.setHeader("Pragma", "public");
     
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
  
  private void writePIS(Product product,  Document document) throws Exception
  {
    document.add(new Paragraph("Product Information Sheet", titlePageRegFont));
    document.add(new Paragraph("For Product: "+product.item, font));
    document.add(new Paragraph(product.product_name, font));
    
  }

  private void setFonts() throws Exception 
  {
    bf = BaseFont.createFont(
         BaseFont.TIMES_ROMAN,
         BaseFont.CP1252,
         BaseFont.EMBEDDED);
        // System.out.println(bf.getClass().getName());
        // bf.setColor(new Color(0xFF, 0xFF, 0xFF));
   // if (debug) System.out.println("PDFServlet: basefont created");
    font = new Font(bf, 10, Font.NORMAL);
    
    titlePageRegFont = new Font(font);
    titlePageRegFont.setSize(15);
      bold = new Font(titlePageRegFont);
    bold.setStyle(Font.BOLD);
}
  
  
  
}

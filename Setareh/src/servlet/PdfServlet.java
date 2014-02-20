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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import data.Product;

public class PdfServlet extends HttpServlet
{
  BaseFont bf =null;
  Font font = null;
  Font titlePageRegFont;
  Font bold;
  
  Font regularFont;
  Font boldRegularFont;
  Font smallFont;
  Font logoFont;
  Font titleFont;
  
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
      
      document.add(getTestTable());
      document.close();
     // pdfWriter.close();
      
   // setting some response headers
      response.setHeader("Content-Disposition", "inline; filename=Setareh_"+p.getItem()+".pdf");
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
	document.add(new Paragraph("Setareh Biotech, LLC", titlePageRegFont));
    document.add(new Paragraph("Product Information Sheet", titlePageRegFont));
    document.add(new Paragraph("For Product: "+product.item, font));
    document.add(new Paragraph(product.product_name, font));
    
  }

  
  PdfPTable getTestTable()
  {
	PdfPTable table = new PdfPTable(3);
    PdfPCell cell= new PdfPCell(new Phrase("Cell w colspan 3"));
	cell.setColspan(3);
	table.addCell(cell);
	  
	cell= new PdfPCell(new Phrase("Cell w colspan 2"));
	cell.setColspan(2);
	table.addCell(cell);
	
	table.addCell("row 1, col 1");
	table.addCell("row 1, col 2");
	table.addCell("row 2, col 1");
	table.addCell("row 2, col 2");
	return table;
  }
  
  private void setFonts() throws Exception 
  {
    bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.EMBEDDED);
    // bf.setColor(new Color(0xFF, 0xFF, 0xFF));
    font = new Font(bf, 10, Font.NORMAL);
    
    regularFont=new Font(bf);
    regularFont.setSize(12);
    
    titlePageRegFont = new Font(font);
    titlePageRegFont.setSize(25);
      bold = new Font(titlePageRegFont);
    bold.setStyle(Font.BOLD);
}
  
}

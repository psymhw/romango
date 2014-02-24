package servlet;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import data.Product;

public class PdfServlet extends HttpServlet
{
  BaseFont bf =null;
 
  
  Font regularFont;
  Font regularBoldFont;
  Font smallFont;
  Font logoFont;
  Font titleFont;
  Font largeFont;
  Font tinyFont;
  
  protected Services services = new Services();
  HttpServletRequest request;
 
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String mode=request.getParameter("mode");
    String item=request.getParameter("item");
    this.request=request;
    Product p = (Product)request.getAttribute("Product");
   // XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
   // XMLWorker werw;
    if (p==null) System.out.println("product is null");
    
   // System.out.println("mode: "+mode);
	try
	{
	  setFonts();
      Document document = new Document(PageSize.LETTER);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
      pdfWriter.setPdfVersion(PdfWriter.VERSION_1_7);
      pdfWriter.setInitialLeading(16);
      
      
      
      document.open();
      if ("PIS".equals(mode))
      {
        writePIS(p, document);
      }
      else
      {
        document.add(new Paragraph("Hello Bruno", titleFont));
        document.add(new Paragraph(new Date().toString(), regularFont));
      }
      
      Reader reader = new StringReader("<table border=\"1\"><tr><td>Hello</td><td> C<sub>1</sub><sub>6</sub>H<sub>1</sub><sub>4</sub>N<sub>2</sub>O<sub>4</sub></td></tr></table>");
      XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, reader);
      
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
      
     // System.out.println("Pdf Servlet");
	} catch(Exception de) {
		de.printStackTrace();
		//throw new IOException(de.getMessage()); 
		}
	
	
	
  }
  
  private void writePIS(Product product,  Document document) throws Exception
  {
	  document.add(new Paragraph("Setareh Biotech, LLC", titleFont));
	  document.add(new Paragraph("Tel : 866-883-6992 / 541-515-6560, Fax : 541-844-1835 orders@setarehbiotech.com", tinyFont));
	
    document.add(new Paragraph("Product Information Sheet", largeFont));
    document.add(Chunk.NEWLINE);
    
    PdfPTable table = new PdfPTable(2);
    table.setLockedWidth(true);
    table.setTotalWidth(550);
    table.setWidths(new int[]{2,8});
    Paragraph p = new Paragraph("Product#: "+product.item, regularFont);
    table.addCell(p);
    p = new Paragraph(product.product_name, smallFont);
    table.addCell(p);
    
    PdfPTable table2 = new PdfPTable(2);
    table2.setLockedWidth(true);
    table2.setTotalWidth(300);
    table2.setWidths(new int[]{2,8});
    Paragraph p2 = new Paragraph("Product#: "+product.item, regularFont);
    table2.addCell(p2);
    p2 = new Paragraph(product.product_name, smallFont);
    table2.addCell(p2);
   // document.add(new Paragraph("For Product: "+product.item, regularFont));
  //  document.add(new Paragraph(product.product_name, smallFont));
    document.add(table);
    document.add(table2);
    document.add(getTestTable());
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
    
    titleFont=new Font(bf);
    largeFont=new Font(bf);
    regularFont=new Font(bf);
    regularBoldFont=new Font(bf);
    smallFont=new Font(bf);
    tinyFont=new Font(bf);
    
    titleFont.setSize(25);
    largeFont.setSize(18);
    regularFont.setSize(12);
    regularBoldFont.setSize(12);
    smallFont.setSize(8);
    tinyFont.setSize(5);
    
    regularBoldFont.setStyle(Font.BOLD);
    
    largeFont.setColor(BaseColor.BLUE);
 }
  
}

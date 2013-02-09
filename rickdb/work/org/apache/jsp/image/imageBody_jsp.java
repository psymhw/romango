package org.apache.jsp.image;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Collection;
import java.util.Iterator;
import db.ImageRefDb;

public final class imageBody_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/WEB-INF/struts-tiles.tld");
    _jspx_dependants.add("/WEB-INF/struts-html.tld");
  }

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

Collection imageList = (Collection)request.getAttribute("ImageList");
ImageRefDb irdb=null;
boolean odd = true;
if (imageList==null)
{
out.println("no images");
return;
}
if (imageList.size()==0)
{
out.println("no images");
return;
}

out.println("<table border='1'>");
 

Iterator it = imageList.iterator();
String imageLayout=(String)request.getAttribute("ImageLayout");

if (imageLayout==null) imageLayout="sideBar";
int max_columns=2;

if ("home".equals(imageLayout)) max_columns=4;

int column_counter=0;
while(it.hasNext())
{
  if (column_counter==0) out.println("<tr>");
  
  //if (odd) { out.println("<tr><td valign='top'>"); }
  //else { out.println("<td valign='top'>"); }
  
  irdb = (ImageRefDb)it.next();
  if (irdb.getDescription()==null) irdb.setDescription("");
  if (irdb.getNotes()==null) irdb.setNotes("");
 
  if ("sideBar".equals(imageLayout)) 
  out.println("<td valign='top' width='205'><a href='imageView.do?id="+irdb.getId()+"'><img src=\"DbImageReader?id="+irdb.getId()
      +"\" alt=\"Image\"  width=\"200\"/></a><br>"
      +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
      +"</td>");
  if ("home".equals(imageLayout)) 
  {
    long cust_id=irdb.getCust_id();
    long note_id=irdb.getNote_id();
    
    if (cust_id>0)
    out.println("<td valign='top'  width='205'><a href='addressEdit.do?mode=show&id="+cust_id+"'><img src=\"DbImageReader?id="+irdb.getId()
        +"\" alt=\"Image\"  width=\"200\"/></a><br>"
        +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
        +"</td>");
    else if (note_id>0)
      out.println("<td valign='top'  width='205'><a href='notes.do?mode=edit&id="+note_id+"'><img src=\"DbImageReader?id="+irdb.getId()
          +"\" alt=\"Image\"  width=\"200\"/></a><br>"
          +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
          +"</td>");
    else
      out.println("<td valign='top' width='205'><img src=\"DbImageReader?id="+irdb.getId()
          +"\" alt=\"Image\"  width=\"200\"/><br>"
          +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
          +"</td>");

   
  }
  //if (odd) { out.println("</td>"); }
  //else { out.println("</td></tr>"); }
   column_counter++;
   if (column_counter==max_columns) 
   {
     out.println("</tr>");
     column_counter=0;
   }
  
  //odd=!odd;
}

if (column_counter<max_columns)  out.println("</tr>");
out.println("</table>");

      out.write("\r\n");
      out.write("\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

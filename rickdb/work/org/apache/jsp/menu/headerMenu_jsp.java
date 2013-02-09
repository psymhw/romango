package org.apache.jsp.menu;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class headerMenu_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

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

      out.write('\r');
      out.write('\n');

String currentMenuItem = (String)request.getAttribute("CurrentMenuItem");
if (currentMenuItem==null) currentMenuItem="none";

      out.write("\r\n");
      out.write("\r\n");
      out.write("<h1>RickDb</h1>\r\n");
      out.write("&nbsp;&nbsp;\r\n");
      out.write("\r\n");
 if (!"home".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write(" <a href=\"home.do\">home</a> \r\n");

}
else
{

      out.write("\r\n");
      out.write(" home\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("| \r\n");
      out.write("\r\n");
 if (!"address".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write(" <a href=\"addressList.do\">address book</a> \r\n");

}
else
{

      out.write("\r\n");
      out.write(" address book\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"transaction master".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"transHome.do\">transaction master</a> \r\n");

}
else
{

      out.write("\r\n");
      out.write("| transaction master\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"tracks".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"tracks.do\">tracks</a> \r\n");

}
else
{

      out.write("\r\n");
      out.write("| tracks\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"feet".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"feet.do\">feet</a> \r\n");

}
else
{

      out.write("\r\n");
      out.write("| feet\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
 if (!"admin".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"admin.do?mode=show\">admin</a>\r\n");

}
else
{

      out.write("\r\n");
      out.write("| admin\r\n");
 } 
      out.write(" | budget \r\n");
      out.write("\r\n");
 if (!"mileage".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"mileage.do?mode=list\">mileage</a>\r\n");

}
else
{

      out.write("\r\n");
      out.write("| mileage\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"notes".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"notes.do?mode=list\">notes</a>\r\n");

}
else
{

      out.write("\r\n");
      out.write("| notes\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"bookmarks".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"notes.do?mode=list&filter=bookmarks\">bookmarks</a>\r\n");

}
else
{

      out.write("\r\n");
      out.write("| bookmarks\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"orders".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"order.do\">orders</a>\r\n");

}
else
{

      out.write("\r\n");
      out.write("| orders\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
 if (!"logout".equals(currentMenuItem))
{ 
      out.write("\r\n");
      out.write("| <a href=\"logout.do\">logout</a>\r\n");

}
else
{

      out.write("\r\n");
      out.write("| logout\r\n");
 } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<br>");
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

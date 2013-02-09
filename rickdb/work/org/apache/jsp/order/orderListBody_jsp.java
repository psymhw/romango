package org.apache.jsp.order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Collection;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import db.OrdersDb;

public final class orderListBody_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(3);
    _jspx_dependants.add("/WEB-INF/struts-bean.tld");
    _jspx_dependants.add("/WEB-INF/struts-html.tld");
    _jspx_dependants.add("/WEB-INF/struts-logic.tld");
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
Collection orderList = (Collection)request.getAttribute("orderList");
if (orderList==null) { out.println("orderListBody: orderList is null"); return; }
String bgcolor="#D8BFD8";
boolean highlight=true;
String highlightColor=bgcolor;

Iterator it = orderList.iterator();

      out.write("\r\n");
      out.write("<h1>Orders</h1>\r\n");
      out.write("\r\n");
      out.write("<a href='order.do?filter=all'>All Orders</a>\r\n");
      out.write("&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href='order.do?filter=complete'>Completed Orders</a>\r\n");
      out.write("&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href='order.do?filter=incomplete'>Pending Orders</a>\r\n");
      out.write("<br><br>\r\n");
      out.write("\r\n");
      out.write("<table border=\"0\">\r\n");
      out.write("<tr>\r\n");
      out.write("<td valign=\"bottom\"><b>Order#</b></td>\r\n");
      out.write("<td valign=\"bottom\"><b>Status</b></td>\r\n");
      out.write("<td valign=\"bottom\"><b>Customer</b></td>\r\n");
      out.write("<td valign=\"bottom\"><b>Ordered</b></td>\r\n");
      out.write("<td valign=\"bottom\"><b>Estimated<br>Delivery</b></td>\r\n");
      out.write("<td valign=\"bottom\"><b>Price</b></td>\r\n");
      out.write("<td valign=\"bottom\"><b>Status</b></td>\r\n");
      out.write("</tr>\r\n");
 
while(it.hasNext())
{
  OrdersDb odb = (OrdersDb)it.next();
  if (highlight) out.println("<tr bgcolor='"+highlightColor+"'>"); else out.println("<tr bgcolor='white'>");
  out.println("<td><a href='order.do?mode=orderView&id="+odb.getId()+"'>"+odb.getId()+"</a></td>");
  if (odb.getComplete()==1) out.println("<td><font color='green'>Complete</font></td>"); else out.println("<td><font color='red'>Pending</a></td>");
  out.println("<td><a href='addressEdit.do?mode=show&id="+odb.getCust_id()+"'>"+odb.getCustomer_name()+"</a></td>");
  out.println("<td>"+df.format(odb.getOrder_date())+"</td>");
  out.println("<td>"+df.format(odb.getEst_delivery())+"</td>");
  out.println("<td>"+odb.getPrice()+"</td>");
  out.println("<td>"+odb.getStatus()+"</td>");
  out.println("</tr>");
  highlight=!highlight;
}

      out.write('\r');
      out.write('\n');
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

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="db.OrdersDb" %>

<%
SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
Collection orderList = (Collection)request.getAttribute("orderList");
if (orderList==null) { out.println("orderListBody: orderList is null"); return; }
String bgcolor="#D8BFD8";
boolean highlight=true;
String highlightColor=bgcolor;

Iterator it = orderList.iterator();
%>
<h1>Orders</h1>

<a href='order.do?filter=all'>All Orders</a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href='order.do?filter=complete'>Completed Orders</a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href='order.do?filter=incomplete'>Pending Orders</a>
<br><br>

<table border="0">
<tr>
<td valign="bottom"><b>Order#</b></td>
<td valign="bottom"><b>Status</b></td>
<td valign="bottom"><b>Customer</b></td>
<td valign="bottom"><b>Ordered</b></td>
<td valign="bottom"><b>Estimated<br>Delivery</b></td>
<td valign="bottom"><b>Price</b></td>
<td valign="bottom"><b>Status</b></td>
</tr>
<% 
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
%>

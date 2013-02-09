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
OrdersDb odb = (OrdersDb)request.getAttribute("OrderHeader");
if (odb==null) { out.println("orderViewBody: order is null"); return; }

//out.println("orderViewBody - order: "+odb.getId());

pageContext.setAttribute("odb", odb);

%>
<h1>Order</h1>

<a href="order.do?mode=edit&id=<bean:write name="odb" property="id"/>">edit</a>
|
<a href="orderStore.do?mode=delete&id=<bean:write name="odb" property="id"/>">delete</a>
<br><br>
<table border="1">
<tr>
<td align="right"><b>Order#</b></td>
<td><bean:write name="odb" property="id"/></td>
</tr>

<tr>
<td align="right"><b>Customer Name</b></td>
<td><bean:write name="odb" property="customer_name"/></td>
</tr>

<tr>
<td align="right"><b>Order Date</b></td>
<td><bean:write name="odb" property="order_date" format="dd-MMM-yyyy"/></td>
</tr>

<tr>
<td align="right"><b>Estimated Delivery</b></td>
<td><bean:write name="odb" property="est_delivery" format="dd-MMM-yyyy"/></td>
</tr>

<tr>
<td align="right"><b>Completion Status</b></td>
<td><bean:write name="odb" property="complete" /></td>
</tr>

<tr>
<td align="right"><b>Notes</b></td>
<td><bean:write name="odb" property="notes"/></td>
</tr>

<tr>
<td align="right"><b>Status</b></td>
<td><bean:write name="odb" property="status"/></td>
</tr>
</table>
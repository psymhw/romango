<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="db.OrdersDb" %>
<%@ page import="trans.TransData" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="db.NotesDb" %>

<%
SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
DecimalFormat nf = new DecimalFormat("#,###");
OrdersDb odb = (OrdersDb)request.getAttribute("OrderHeader");
if (odb==null) { out.println("orderViewBody: order is null"); return; }
ArrayList orderLines = (ArrayList)request.getAttribute("OrderLines");

Collection noteList = (Collection)request.getAttribute("NoteList");
String status="";
if (odb.getComplete()==1) status="Complete"; else status="Incomplete";

//out.println("orderViewBody - order: "+odb.getId());

pageContext.setAttribute("odb", odb);
%>
<h1>Order</h1>

<a href="order.do?mode=edit&id=<bean:write name="odb" property="id"/>">edit</a>
|
<a href="orderStore.do?mode=delete&id=<bean:write name="odb" property="id"/>">delete</a>
<br><br>
<table border="0">
<tr>
<td align="right"><b>Order#</b></td>
<td><bean:write name="odb" property="id"/></td>
</tr>

<tr>
<td align="right"><b>Customer Name</b></td>
<td><a href="addressEdit.do?mode=show&id=<bean:write name="odb" property="cust_id"/>"><bean:write name="odb" property="customer_name"/></a></td>
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
<td align="right"><b>Price</b></td>
<td><bean:write name="odb" property="price" format="###.00"/></td>
</tr>

<tr>
<td align="right"><b>Completion Status</b></td>
<td><%=status %></td>
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

<%
if (orderLines!=null)
	if (orderLines.size()>0)
{
	double debitTotal=0, creditTotal=0, balanceDue=0;
	%>
	<br><br>
	<table border="0">
  <tr>
   <td><b>id</b></td>
    <td><b>date</b></td>
    <td><b>description</b></td>
    <td width='50px'  align='right'><b>debit</b></td>
    <td width='50px'  align='right'><b>credit</b></td>
</tr>    
   
  <%
    out.println("<tr>");
	out.println("<td align='right' colspan='5'><hr></td>");
	out.println("</tr>");
	out.println("<tr>");
	TransData td;
	Iterator it = orderLines.iterator();
	while(it.hasNext())
	{
		td=(TransData)it.next();
		debitTotal+=td.getDebit();
		creditTotal+=td.getCredit();
		balanceDue=debitTotal-creditTotal;
		out.println("<tr>");
		out.println("<td><a href=\"transEdit.do?mode=edit&transId="+td.getTransId()+"\">"+td.getTransId()+"</a></td>");
		  out.println("<td>"+td.getTransDate()+"</td>");
		  out.println("<td>"+td.getDescription()+"</td>");
		  if (td.getDebit()<1) out.println("<td>&nbsp;</td>"); else out.println("<td align='right'>"+nf.format(td.getDebit())+"</td>");
		  if (td.getCredit()<1) out.println("<td>&nbsp;</td>"); else  out.println("<td align='right'>"+nf.format(td.getCredit())+"</td>");
		out.println("</tr>");
	}
	out.println("<tr>");
	out.println("<td align='right' colspan='5'><hr></td>");
	out.println("</tr>");
	out.println("<tr>");
	
	out.println("<tr>");
	out.println("<td colspan='3' align='right'><b>Totals: </b></td>");
	  if (debitTotal<1) out.println("<td>&nbsp;</td>"); else out.println("<td align='right'>"+nf.format(debitTotal)+"</td>");
	  if (creditTotal<1) out.println("<td>&nbsp;</td>"); else  out.println("<td align='right'>"+nf.format(creditTotal)+"</td>");
	out.println("</tr>");

	out.println("<tr>");
	out.println("<td align='right' colspan='5'><hr></td>");
	out.println("</tr>");
	out.println("<tr>");
	
	out.println("<td align='right' colspan='4'><b>Balance Due: </b></td>");
	 out.println("<td align='right'>"+nf.format(balanceDue)+"</td>");
	out.println("</tr>");
}
else
{ out.println("<br>no order lines"); }
%>
</table>
<a href="transEdit.do?mode=newTrans&trackId=6&cust_id=<bean:write name="odb" property="cust_id"/>&order_id=<bean:write name="odb" property="id"/>"/>new order line</a>
<br/>

<%
NotesDb ndb;
if (noteList.size()>0) 
{
  Iterator it = noteList.iterator();
  out.println("<H2>Notes</H2>");
  out.println("<table border=0>");
  out.println("<tr>");
  out.println("<td nowrap><b>Note ID</b></td>");
 
  out.println("<td><b>Date</b></td>");
  out.println("<td><b>Summary</b></td>");
  out.println("<td><b>Body</b></td>");
   out.println("</tr>");
 // SimpleDateFormat df = new SimpleDateFormat("d-MMM-yyyy");
  while(it.hasNext())
  {
    ndb=(NotesDb)it.next();
    //System.out.println("addressShowBody.jsp - NoteId: "+ndb.getId());
    pageContext.setAttribute("ndb", ndb);
    out.println("<tr>");
    out.println("<td valign='top' nowrap><a href=\"notes.do?mode=edit&id="
            +ndb.getId()+"\">"+ndb.getId()+"</a></td>");
    
    out.println("<td valign='top'>"+df.format(ndb.getNote_date())+"</td>");
    out.println("<td valign='top' nowrap>"+ndb.getSummary()+"</td>");
    
    out.println("<td>"+ndb.getNote()+"</td>");
    out.println("</tr>");
  }
  out.println("</table>");
}
 %>
 
 <a href="notes.do?mode=new&track=6&cust_id=<bean:write name="odb" property="cust_id"/>&order_id=<bean:write name="odb" property="id"/>"/>new note</a>
<br/>

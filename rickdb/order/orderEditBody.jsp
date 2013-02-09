<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="order.OrderForm" %>

<%
OrderForm of = (OrderForm)request.getAttribute("OrderForm");
pageContext.setAttribute("of", of);
%>

<html:form action="/orderStore.do">
<html:hidden name="of" property="mode" />
<html:hidden name="of" property="id" />
<html:hidden name="of" property="cust_id" />

<table cellspacing="0" cellpadding="2" border="0" style="margin-left:-2px"><tr>
   <tr> 
      <td align="right"><b>Mode: </b> </td>
      <td><bean:write name="of" property="mode" /></td>
   </tr>
<tr> 
      <td align="right"><b>Customer: </b> </td>
      <td><bean:write name="of" property="customer_name" /></td>
   </tr>
<tr> 
      <td align="right"><b>Order Date: </b> </td>
      <td><html:text  name="of" property="order_date" size="11" maxlength="11"/> (<i>yyyy-mm-dd</i>)
      </td>
   </tr>
   <tr> 
      <td align="right"><b>Est. Delivery Date: </b> </td>
      <td><html:text  name="of" property="est_delivery" size="11" maxlength="11"/> (<i>yyyy-mm-dd</i>)
      </td>
   </tr>
   <tr> 
      <td align="right"><b>Price: </b> </td>
     <td><html:text  name="of" property="price" size="11" maxlength="11"/> 
      </td>
   </tr>
   <tr> 
      <td align="right"><b>Completed: </b> </td>
      <td><html:checkbox  name="of" property="complete" />
      </td>
   </tr>
   <tr> 
      <td valign="top"  align="right"><b>Notes:</b> <td><html:textarea  name="of"  property="notes" rows="10" cols="80" /></td>
</tr>

<tr> 
      <td align="right"><b>Status: </b> </td>
      <td><html:text name="of" property="status" size="100" maxlength="200"/></td>
   </tr>
</table>      
<html:submit/>
</html:form>
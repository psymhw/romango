<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="address.AddressForm" %>
<%@ page import="db.NotesDb" %>

<%
AddressForm af = (AddressForm)request.getAttribute("AddressForm");
Collection noteList = (Collection)request.getAttribute("NoteList");

pageContext.setAttribute("af", af);

%>
<h2><bean:write name="af" property="firstname"/> <bean:write name="af" property="lastname"/></h2>
&nbsp;&nbsp;<a href="addressEdit.do?mode=edit&id=<bean:write name="af" property="id"/>">edit</a>
 | <a href="imageAdd.do?mode=addImage&type=cust&id=<bean:write name="af" property="id"/>">add image</a>
 | <a href="order.do?mode=newOrder&type=cust&id=<bean:write name="af" property="id"/>">new order</a>
 
<logic:equal name="af" property="feet" value="true">
 | <a href="feetEdit.do?mode=new&cust_id=<bean:write name="af" property="id"/>">add feet</a>
 
</logic:equal> 
<br><br>
<table border="1" >
  <tr>
    <th align="right">ID: </th>
    <td>
    <bean:write name="af" property="id"/></td>
  </tr>
  <tr>
    <th align="right">Categories: </th>
     <td>
       <logic:equal name="af" property="personal" value="true"> -personal</logic:equal>
       <logic:equal name="af" property="business" value="true"> -business</logic:equal>
       <logic:equal name="af" property="provider" value="true"> -provider</logic:equal>
       <logic:equal name="af" property="customer" value="true"> -customer</logic:equal>
       <logic:equal name="af" property="family" value="true"> -family</logic:equal>
       <logic:equal name="af" property="feet" value="true"> -feet</logic:equal>
     </td>
  </tr>
  <tr>
    <th align="right" >Company: </th>
    <td width="400px"><bean:write name="af" property="company"/></td>
  </tr>
  
  <tr>
    <th align="right">Street1: </th>
    <td><bean:write name="af" property="street1"/></td>
  </tr>
  <logic:notEmpty name="af" property="street2">
  <tr>
    <th align="right">Street2: </th>
    <td><bean:write name="af" property="street2"/></td>
  </tr>
  </logic:notEmpty>
  <tr>
    <th align="right">City, State Zip: </th>
    <td><bean:write name="af" property="csz"/></td>
  </tr>
  <tr>
    <th align="right">E-Mail: </th>
      <td><a href='mailto:<bean:write name="af" property="email"/>'><bean:write name="af" property="email"/></a></td>
  </tr>
  <tr>
    <th align="right">Home Phone: </th>
    <td><bean:write name="af" property="home_phone"/></td>
  </tr>
  <tr>
    <th align="right">Work Phone: </th>
    <td><bean:write name="af" property="work_phone"/></td>
  </tr>
  <tr>
    <th align="right">Cell Phone: </th>
    <td><bean:write name="af" property="cell_phone"/></td>
  </tr>
  <tr>
    <th align="right">Web Site: </th>
    <td><a href='<bean:write name="af" property="website"/>'><bean:write name="af" property="website"/></a></td>
  </tr>
  <tr>
    <th align="right">Notes: </th>
    <td><bean:write name="af" property="notes" filter="false"/></td>
  </tr>
</table>

<%
NotesDb ndb;
if (noteList.size()>0) 
{
  Iterator it = noteList.iterator();
  out.println("<br><b>Notes</b><br>");
  out.println("<table border=0>");
  out.println("<tr>");
  out.println("<td nowrap><b>Note ID</b></td>");
  out.println("<td nowrap><b>Order ID</b></td>");
  out.println("<td><b>Date</b></td>");
  out.println("<td><b>Summary</b></td>");
  out.println("<td><b>Body</b></td>");
   out.println("</tr>");
  SimpleDateFormat df = new SimpleDateFormat("d-MMM-yyyy");
  while(it.hasNext())
  {
    ndb=(NotesDb)it.next();
    //System.out.println("addressShowBody.jsp - NoteId: "+ndb.getId());
    pageContext.setAttribute("ndb", ndb);
    out.println("<tr>");
    out.println("<td valign='top' nowrap><a href=\"notes.do?mode=edit&id="
            +ndb.getId()+"\">"+ndb.getId()+"</a></td>");
    out.println("<td valign='top'><a href=\"order.do?mode=orderView&id="+ndb.getOrder_id()+"\">"+ndb.getOrder_id()+"</a></td>");
    out.println("<td valign='top'>"+df.format(ndb.getNote_date())+"</td>");
    out.println("<td valign='top' nowrap>"+ndb.getSummary()+"</td>");
    
    out.println("<td>"+ndb.getNote()+"</td>");
    out.println("</tr>");
  }
  out.println("</table>");
}
 %>


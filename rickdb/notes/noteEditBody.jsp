<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="notes.NotesForm" %>
<%@ page import="trans.Lookups" %>

<%
Lookups lookups = new Lookups();
pageContext.setAttribute("tracks", lookups.tracks);
String customerName = (String)request.getAttribute("CustomerName");
if (customerName==null) customerName="";

NotesForm nf = (NotesForm)request.getAttribute("NotesForm");
if (nf==null)
{
  out.println("NotesForm is null");
  return;
}
pageContext.setAttribute("nf", nf);
if ("update".equals(nf.getMode()))
{
%>
<a href="imageAdd.do?mode=addImage&type=note&id=<bean:write name="nf" property="id"/>">add image</a>
<% } %>
<html:form action="/notes.do">
<html:hidden name="nf" property="mode" />
<table>
<tr>
  <td align="right"><b>Summary: </b></td>
  <td><html:text name="nf" property="summary" size="45" maxlength="45"/></td>
  </tr>
     <tr>
      <td align="right"><b>Track: </b> </td>
      <td><html:select name="nf" property="track">
       <html:options collection="tracks" property="value" labelProperty="label"/> 
       </html:select></td>
   </tr>
   </tr>
     <tr>
      <td align="right"><b>Address ID: </b> </td>
      <td><html:text name="nf" property="cust_id" size="4" maxlength="4"/> <%=customerName %></td>
      
   </tr>
   <tr>
      <td align="right"><b>Order ID: </b> </td>
      <td><html:text name="nf" property="order_id" size="4" maxlength="4"/> <%=customerName %></td>
      
   </tr>
   <td align="right"><b>Date: </b> </td>
<td><html:text  name="nf" property="note_date" size="10" maxlength="10"/> (<i>yyyy-mm-dd</i>)
</td>
   <tr>
    <th align="right" valign="top">Note: </th>
    <td><html:textarea name="nf" property="note" cols="75" rows="10"/></td>
  </tr>
  <tr>
  <td align="right"><b>Website: </b></td>
  <td><html:text name="nf" property="website" size="45" maxlength="200"/></td>
  </tr>
  <tr>
  <td align="right"><b>Priority: </b></td>
  <td><html:text name="nf" property="priority" size="2" maxlength="2"/></td>
  </tr>
  <tr>
  
  <tr>
    
    <td colspan="2"><html:submit /></td>
  </tr>
</table>
</html:form>
<br>
<br>
<%
if ("update".equals(nf.getMode()))
{
  out.println("<a href='notes.do?mode=delete&id="+nf.getId()+"'>delete</a>");
}

%>

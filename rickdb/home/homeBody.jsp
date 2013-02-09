<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.AddressDb" %>
<%@ page import="db.NotesDb" %>
<%@ page import="trans.Lookups" %>
<%@ page import="java.util.Hashtable" %>

<h1>Rick DB Home</h1>

<b>Recently used Addresses</b><br>

<%
Collection addressList = (Collection)request.getAttribute("AddressList");
Iterator it = addressList.iterator();
AddressDb adb;

%>
 <table border="1" bgcolor='#F5F5DC'>
  <tr>
    <th>ID</th>
  <th nowrap>First Name</th>
 <th nowrap>Last Name</th>
  <th nowrap>Company</th>

    <th>E-Mail</th>
    <th>Phone</th>
    <th>Web Site</th>
  </tr>

<%


while(it.hasNext())
{
  adb=(AddressDb)it.next();
  
  pageContext.setAttribute("adb", adb);
 
  %>
  
  <tr>
    <td><a href="addressEdit.do?mode=show&id=<bean:write name="adb" property="id"/>">
    <bean:write name="adb" property="id"/></a></td>
    
      <td><bean:write name="adb" property="firstname"/></td>
      <td><bean:write name="adb" property="lastname"/></td>
      <td><bean:write name="adb" property="company"/></td>
  
    <td><a href='mailto:<bean:write name="adb" property="email"/>'><bean:write name="adb" property="email"/></a></td>
    <td>
      <logic:notEmpty name="adb" property="home_phone">
        home: <bean:write name="adb" property="home_phone"/>
      </logic:notEmpty>
      <logic:notEmpty name="adb" property="work_phone">
        work: <bean:write name="adb" property="work_phone"/> 
      </logic:notEmpty>
      <logic:notEmpty name="adb" property="cell_phone">  
        cell: <bean:write name="adb" property="cell_phone"/>
      </logic:notEmpty>
    </td>
 <td>
 <logic:notEmpty name="adb" property="website">
 <a href='<bean:write name="adb" property="website"/>'><bean:write name="adb" property="website"/></a>
 </logic:notEmpty>&nbsp;
 </td>
  </tr>
 
  
  <%
}
%>
</table>
<br>
<b>Recent Notes</b><br>
<% 
Collection notesList = (Collection)request.getAttribute("NotesList");
Iterator itx = notesList.iterator();
NotesDb ndb=null;
out.println("<table border=1  bgcolor='#FFE4C4'>");
Hashtable<String, String> trackHash;
trackHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("TrackHash");

String note="";
while(itx.hasNext())
{
  out.println("<tr>");
  ndb=(NotesDb)itx.next();
  
  if (ndb.getNote().length()>100) note=ndb.getNote().substring(0,100)+" ...";
  else note=ndb.getNote();
  out.println("<td><a href='notes.do?mode=edit&id="+ndb.getId()+"'>"+ndb.getId()+"</a></td>");
  out.println("<td>"+trackHash.get(""+ndb.getTrack())+"</td>");
  out.println("<td><b>"+ndb.getSummary()+"</b></td>");
  out.println("<td>"+note+"</td>");
  
  
  if (ndb.getWebsite()!=null)
  out.println("<td><a href='"+ndb.getWebsite()+"'>"+ndb.getWebsite()+"</a></td>");
  else
    out.println("<td>&nbsp;</td>");
  out.println("</tr>");
  
}

out.println("</table>");

%>
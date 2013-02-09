<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.MileageDb" %>
<%@ page import="trans.Lookups" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="mileage.MileageSelectForm" %>

 <a href="mileage.do?mode=new">new mileage</a>
<br><br>
<%
Lookups lookups = new Lookups();
pageContext.setAttribute("tracks", lookups.tracks);
int total=0;

MileageSelectForm nsf = (MileageSelectForm)request.getAttribute("MileageSelectForm");
pageContext.setAttribute("nsf", nsf);


Hashtable<String, String> trackHash;
trackHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("TrackHash");
%>
<html:form action="/mileage.do">
<html:hidden name="nsf" property="mode" />
<table>
    <tr>
      <td align="right" ><b>Track: </b> </td>
      <td><html:select name="nsf" property="track" onchange="form.submit()">
       <html:options collection="tracks" property="value" labelProperty="label"/> 
       </html:select></td>
   </tr>
</table>
</html:form>
<br>
<% 
//Lookups lookups = new Lookups();
Collection mileageList = (Collection)request.getAttribute("MileageList");
Iterator it = mileageList.iterator();
MileageDb mdb=null;
out.println("<table border=1  bgcolor='#FFF4C4'>");
SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

while(it.hasNext())
{
  out.println("<tr>");
  mdb=(MileageDb)it.next();
  total+=mdb.getMiles();
  out.println("<td><a href='/rickdb/mileage.do?mode=edit&id="+mdb.getId()+"'>"+mdb.getId()+"</a></td>");
  java.util.Date tdate = new java.util.Date(mdb.getMileage_date().getTime());
  out.println("<td>"+df.format(tdate)+"</td>");
  out.println("<td>"+trackHash.get(""+mdb.getTrack())+"</td>");
  out.println("<td><b>"+mdb.getDescription()+"</b></td>");
  out.println("<td align='right'><b>"+mdb.getStart_mile()+"</b></td>");
  out.println("<td align='right'><b>"+mdb.getEnd_mile()+"</b></td>");

  out.println("<td align='right'>"+mdb.getMiles()+"</td>");
  out.println("</tr>");
  
}
out.println("<tr>");
out.println("<td colspan=5 align=right><b>Total:</b> </td>");
out.println("<td align=right>"+total+"</td>");
out.println("</tr>");
out.println("</table>");

%>
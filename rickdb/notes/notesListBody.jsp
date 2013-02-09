<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.NotesDb" %>
<%@ page import="trans.Lookups" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="notes.NotesSelectForm" %>

 
<%
Lookups lookups = new Lookups();
pageContext.setAttribute("tracks", lookups.tracks);

NotesSelectForm nsf = (NotesSelectForm)request.getAttribute("NotesSelectForm");
pageContext.setAttribute("nsf", nsf);


Hashtable<String, String> trackHash;
trackHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("TrackHash");
%>
<a href="notes.do?mode=new&track=<%=nsf.getTrack()%>">new note</a>
<br><br>
<html:form action="/notes.do">
<html:hidden name="nsf" property="mode" />
<html:hidden name="nsf" property="filter" />
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
Collection notesList = (Collection)request.getAttribute("NotesList");
Iterator it = notesList.iterator();
NotesDb ndb=null;
out.println("<table border=1  bgcolor='#FFE4C4'>");


while(it.hasNext())
{
  out.println("<tr>");
  ndb=(NotesDb)it.next();

  out.println("<td><a href='notes.do?mode=edit&id="+ndb.getId()+"'>"+ndb.getId()+"</a></td>");
  out.println("<td valign='top'>"+trackHash.get(""+ndb.getTrack())+"</td>");
  out.println("<td valign='top' nowrap><b>"+ndb.getSummary()+"</b></td>");
  
  out.println("<td valign='top'>"+ndb.getNote()+"</td>");
  
  if (ndb.getWebsite()!=null)
  out.println("<td valign='top'><a href='"+ndb.getWebsite()+"'>"+ndb.getWebsite()+"</a></td>");
  else   out.println("<td valign='top'>&nbsp;</td>");
  out.println("</tr>");
  
}

out.println("</table>");

%>
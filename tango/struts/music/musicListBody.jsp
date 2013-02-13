<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.ArticleDb" %>
<%@ page import="db.MemberDb" %>

<%
MemberDb userData = (MemberDb)session.getAttribute("UserData");
boolean admin=false;
if (userData!=null)
	if (userData.getAdmin()==1)
		admin=true;

/* ADMIN LOGIC OVERRIDE */
//admin=true;
Collection articles = (Collection)request.getAttribute("Articles");
if (articles==null) {	out.println("Articles is null");	return; }
Iterator it = articles.iterator();
%>
<h1>Music Articles List</h1>
<br>
<% if (admin) { %>
<a href="<%=request.getContextPath()%>/music.do?mode=new">New Article</a><br><br>
<%} %>
<table border="0">
<tr>
  <td><b>Date</b></td>
  <td><b>Author</b></td>
  <td><b>Title</b></td>
  <td><b>Summary</b></td>
</tr>
<%
ArticleDb adb=null;
while(it.hasNext())
{
	adb=(ArticleDb)it.next();
	pageContext.setAttribute("adb", adb);
	%>
	  <tr>
	     <td nowrap valign="top"><bean:write name="adb" property="strDate" /></td>
	     <td nowrap valign="top"><bean:write name="adb" property="author" /></td>
	     <td nowrap valign="top"><a href="<%=request.getContextPath()%>/music.do?mode=show&id=<%=adb.getId()%>" ><bean:write name="adb" property="title" /></a></td>
	     <td><bean:write name="adb" property="summary" filter="false"/></td>
	  </tr>
	<% 
	
}
%>
</table>

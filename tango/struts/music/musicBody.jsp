<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.ArticleDb" %>
<h3 align="center">&nbsp;La Musica del Tango</h3>

<p>				
Articles about the rich body of tango music.
</p>
To contribute - Jim Thurmond <a href="mailto:jimt@efn.org">jimt@efn.org</a> or
Pam Joffe <a href="mailto:pamelajoffe@yahoo.com">pamelajoffe@yahoo.com</a>
<br><br>
<p>
<a href="<%=request.getContextPath()%>/links.do?category=Music%20and%20Dancing">Recommended Sites</a>
</p> 	
<%
Collection articles = (Collection)request.getAttribute("Articles");
if (articles==null) {	out.println("Articles is null");	return; }
Iterator it = articles.iterator();
%>
<br>
<%
ArticleDb adb=null;
while(it.hasNext())
{
	adb=(ArticleDb)it.next();
	pageContext.setAttribute("adb", adb);
	%>
	 <table>
  <tr>
     <td><a href="<%=request.getContextPath()%>/music.do?mode=show&id=<%=adb.getId()%>" ><font size="+1"><bean:write name="adb" property="title"/></font></a></td>
  </tr>
  <tr>
     <td>by <bean:write name="adb" property="author"/> - <bean:write name="adb" property="strDate"/></td>
  </tr>
   <tr>
     <td><bean:write name="adb" property="body" filter="false"/></td>
  </tr>
</table>
	<% 
	
} %>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="db.ArticleDb" %>
<%@ page import="db.MemberDb" %>
<%
ArticleDb adb = (ArticleDb)request.getAttribute("ArticleDb");
if (adb==null) { out.println("ArticleDb is null"); return; }
pageContext.setAttribute("adb", adb);
MemberDb userData = (MemberDb)session.getAttribute("UserData");
boolean admin=false;
if (userData!=null)
	if (userData.getAdmin()==1)
		admin=true;

/* ADMIN LOGIC OVERRIDE */
//admin=true;
%>
<h1>Music Article</h1>
<br>
<% if (admin) {%>
<a href="<%=request.getContextPath()%>/music.do?mode=edit&id=<%=adb.getId()%>">edit</a><br><br>
<% } %>
<table>
  <tr>
    <td align="right"><b>Title: </b></td>
    <td><font size="+1"><bean:write name="adb" property="title"/></font></td>
  </tr>
  <tr>
    <td align="right"><b>By: </b></td>
    <td><bean:write name="adb" property="author"/> - <bean:write name="adb" property="strDate"/></td>
  </tr>
  <tr>
    <td align="right" valign="top"><b>Summary: </b></td>
    <td><bean:write name="adb" property="summary" filter="false"/></td>
  </tr>
  <tr>
    <td align="right" valign="top"><b>Article: </b></td>
    <td><bean:write name="adb" property="body" filter="false"/></td>
  </tr>
</table>

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page import="db.MediaDb" %>
<%@ page import="media.CategoryForm" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="db.MemberDb" %>
<%
             MemberDb userData = (MemberDb)session.getAttribute("UserData");
             String user = (String)session.getAttribute("user");

Collection media = (Collection)request.getAttribute("Media");
CategoryForm cf = (CategoryForm)request.getAttribute("CategoryForm");
String pageWidget=(String)request.getAttribute("PageWidget");
if (pageWidget==null) pageWidget="Page: n/a";
pageContext.setAttribute("cf", cf);
if (media==null) { out.println("mediaListBody.jsp: media is null"); return;}
Iterator it = media.iterator();
MediaDb mdb = null;
boolean newRow=true;
ServletContext context = request.getSession().getServletContext();
ArrayList categories = (ArrayList)context.getAttribute("Categories");
pageContext.setAttribute("cats", categories);


boolean admin=false;
if (userData!=null)
	if (userData.getAdmin()==1) admin=true;
	if (admin)	out.println("<a href=\""+request.getContextPath()+"/mediaEdit.do?mode=new\">new</a><br><br>");

%>
<h1>Media</h1><br>
<html:form action="/media">
<b>Category: </b><html:select name="cf" property="selectedValue" onchange="submit()">
        <html:options collection="cats" property="value" labelProperty="label"/>
</html:select>
</html:form>

Page: <%=pageWidget%>
<table width = "100%" border="0">
<% 
while(it.hasNext())
{
	mdb = (MediaDb)it.next();
	pageContext.setAttribute("mdb", mdb);
	if (newRow) out.println("<tr>");
	%>
	  <td width="50%" align="center" valign="top">
    <table width="100%" cellspacing="0" cellpadding="0">
      <tr><td bgcolor="#CCCCFF"><b>Category: </b><bean:write name ="mdb" property="category"  /><hr></td></tr>
      <tr><td align="center" valign="top"><br><bean:write name ="mdb" property="embed" filter="false" /></td></tr>
      <tr><td align="left" bgcolor="#FFFFE0"><b><bean:write name ="mdb" property="title"  /></b></td></tr>
      
      <tr><td bgcolor="#FFFFE0"><bean:write name ="mdb" property="description" filter="false"/></td></tr>
    </table>
    <% 
    if (userData!=null) if (userData.getAdmin()==1)
     { %>
    <a href="<%=request.getContextPath()%>/mediaEdit.do?mode=show&id=<%=mdb.getId()%>">show</a>
    <%} %>
	<% 
	out.println("</td>");
	if (!newRow) out.println("</tr>");
	newRow=!newRow;
}

%>
</table>

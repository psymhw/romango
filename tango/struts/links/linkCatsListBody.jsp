<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@ page import="db.LinkCatsDb" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<h1>Link Categories</h1><br>
<table border="1">
<tr>
<td><b>Sequence</b></td>
<td><b>Category</b></td>
<td><b>action</b></td>
</tr>
<%
Collection links = (Collection)request.getAttribute("Links");
LinkCatsDb lcdb = (LinkCatsDb)request.getAttribute("LinkCatsDb");
pageContext.setAttribute("lcdb", lcdb);
if (links==null) 
{	
	out.println("links is null<br>");
}
else
{
	if (links.size()==0) out.println("no links found<br>");
  else
  {
  	LinkCatsDb lcdbx=null;
  	Iterator it = links.iterator();
  	while(it.hasNext())
  	{
  		out.println("<tr>");
	  	lcdbx= (LinkCatsDb)it.next();
	  	out.println("<td>"+lcdbx.getSeq()+"</td><td>"+lcdbx.getCategory()+"</td>");
		
	  	out.println("<td><a href='"+request.getContextPath()+"/linkcats.do?mode=delete&id="+lcdbx.getId()+"'>delete</a></td>");
	  	out.println("</tr>");
	  }
  }
}


%>
</table>
<br>
<html:form action="linkcats">
<html:hidden name="lcdb" property="mode" />
<html:hidden name="lcdb" property="id" />
<table border="1">
  <tr>
    <td align="right"><b>Sequence: </b></td>
    <td><html:text name="lcdb" property="seq" /></td>
  </tr>
  <tr>
    <td align="right"><b>Category: </b></td>
    <td><html:text name="lcdb" property="category" /></td>
  </tr>
</table>
<html:submit/>
</html:form>

